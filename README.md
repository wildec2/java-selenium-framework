# Overview

This is a java and selenium based framework for web UI automation testing on PWAs or 
responsive web apps. The framework caters for desktop(multiple browsers), mobile and tablet.
Device mode, offered by Chrome(dev tools), is used 
when running mobile or tablet tests. Using device mode this allows the frontend to be 
rendered server side which can be essential for PWA testing. Included is a selenium grid
setup via docker-compose to allow for remote headless running of tests. The build tool is gradle and 
the testng test automation framework is used for specifying, arranging and running the tests. 
Docker is used to spin up the grid. There's also a Jenkins set-up included to get the tests running in a pipeline.

## Setup

This will work straight out of the box once your java setup is ok.
There's a gradle wrapper included so you don't have to install gradle. Just  use gradlew(or ./gradlew) rather than gradle when running commands. 
Clone the repo, open in IntelliJ, build and run the tests.

To fire up selenium grid and run the tests inside of a container:
```
docker-compose up -d
```

http://localhost:4444/grid/console

To bring down our containers:
```
docker-compose down
```

## Run Tests
You can run your tests using intelliJ straight away. 
Or you can run the testng suites via the gradle tasks.
```
gradle clean runTestSuite
```
If you want to run on selenium grid:
```
gradle clean runTestSuite -Dheadless=true  -Dgrid=true
```


# How it Works

Several things are tied together to allow us to run tests on different environments/simulated environments. Each is outlined here and how they work together to create the conditions under which the tests are ran.

## Selenium
There are two dependencies in the build file to allow us to automate the browser. 

The required Selenium Libraries for interacting with a browser:
```
org.seleniumhq.selenium:selenium-api
```
And webdrivermanager for managing drivers which are required by Selenium:
```
io.github.bonigarcia:webdrivermanager
```
With these combined you can launch and perform actions on specied browsers.

DriverSupplier.java specifies the functions for setting up drivers for local visual or remote headless running. 

For desktop there are funtions for both Chrome and Firefox. Depending on whether it's a remote run or not varying ChromeOptions and FirefoxOptions are added. 

For mobile or tablet tests ChromeDriver is used. An experimental option is set for options not exposed by the api so that when the browser is launched with chrome dev tools mobile emulation enabled.
```
chromeOptions.setExperimentalOption("mobileEmulation", singletonMap("deviceName", "iPad"));
```
And for Remote runs a RemoteWebDriver is used with the corresponding driver options for remote runs.

The BeforeMethod in TestBase.java then sets up the driver before every test using the TestNG parameter specified for each test.

## TestNG
TestNg is the testing framework used for running, multithreading, parameterising etc. the tests. The TestNG dependency in the build file adds all the libraries needed.
```
org.testng:testng
```
Every test will use the TestNG @BeforeMethod and @AfterMethod annotations for the driver setup and teardown as the test classes themselves are child classes of TestBase.java. Each test has a valid device type parameter which is used by the setUpEnvBeforeTest method to set up the device conditions.
```
@Test(groups = "mobile", description = "sample test - mobile")
@Test(groups = "tablet", description = "sample test - tablet")
@Test(groups = "desktop", description = "sample test - desktop")
```
The suites are then arranged and ran using testng xml files. The browser on which to run is specified here which is passed to the before method as a parameter.
```
<parameter name="Browser" value="firefox"/>
```
By default the browser is set to Chrome.
```
@Parameters("Browser")
    public void setUpEnvBeforeTest(@Optional("chrome") String browser, Method method) {
```
So this setup only allows us to run mobile of tablet tests on Chrome using the device mode feature. So the chrome suite runs all the tests(mobile, desktop and tablet) making use of Chrome device mode but firefoxsuite.xml excludes the mobile and tablet groups. 
```
<groups>
       <run>
           <exclude name="mobile" />
           <exclude name="tablet" />
       </run>
</groups>
```
Multithreading is also specified in these xml files for 4 concurrent runs of methods. These can be updated to handle classes or packages or whatever is needed for concurrency. As we are using methods we need to ensure WebDriver instances are thread safe.
The DriverFactory class and how the Webdriver instances are created in TestBase handle this for us.

## Page Object Model
The Page Object Model design pattern is followed where WebElements and the actions to be performed on them are mapped in classes representing a page.
Page Factory is also used to find and initialise elements. The @FindBy annotation is used for locating elements.
```
@FindBy(css = "someClass")
private WebElement heading;
```
The elements are initialised with:
```
PageFactory.initElements(new AppiumFieldDecorator(driver, ofSeconds(20)), this);
```
Dependency:
```
ru.yandex.qatools.htmlelements:htmlelements-all
```
Page Factory is not required here for creating page objects. You can use By or String to as element locators. Whatever you fancy. 

## Selenium Grid
Selenium Grid is used for running the tests on remote machines. This can be used for parallel running of tests to reduce the run time of suites 
and can make available many platforms where the tests can be run. The hub accepts requests to run tests and manages threads while the nodes, where the browsers live, receive the requests from the hub and execute them on the browser. 
This set up contains a hub with two nodes. One node has X Chrome instances and the other has X Firefox instances. When the testngxml suite is ran the tests will be distributed by the hub to node with the correct browser. As our 
testng xml specifies parallel classes one class will be run on one browser instance at a time. 

## Docker Compose
Docker compose is used to, bring up a branch environment(although currently commented out), bring up(and down) the grid and nodes and run our tests.

To fire up selenium grid and run tests:
```
docker-compose up --exit-code-from selenium-tests
```

'--exit-code-from selenium-tests' exits the container when the selenium-tests services finished.

To remove the containers and network:
```
docker-compose down
```
This will remove the container and images automatically.

See docker-compose.yml.

services(containers): contains the images and their configurations.

image: The image to pull down that will be used toi spin up the container. 

ports: Published ports with host:container format.

volumes: mounts the host path to service with host:container format.

depends_on: a depencency to be met before spinning up a continer. So the nodes won't be spun up until the hub is ready.

NODE_MAX_INSTANCES: defines how many instances of same version of browser can run over the Remote System.

Hub:
```
hub:
    image: selenium/hub
    ports:
      - 4444:4444
    environment:
      GRID_MAX_SESSION: 8
```

Node:
```
firefox:
    image: selenium/node-firefox
    volumes:
      - /dev/shm:/dev/shm
    depends_on:
      - hub
    environment:
      HUB_HOST: hub
      NODE_MAX_INSTANCES: 4
      NODE_MAX_SESSION: 4
```
You could specify the ports here for each node.
```
ports:
      - "9002:5900"
```
And tell your testng.xml to run the tests here
```
<parameter name="Port" value="9002" />
```
Running Tests:
```
selenium-tests:
    build: .
    image: selenium/tests
    depends_on:
      - hub
      - chrome
      - firefox
    command: "gradle runTestSuite --warning-mode=all -Dbase_url=https://www.discoverireland.ie/ -Dheadless=true -Dgrid=true -DhubUrl=http://hub:4444/wd/hub"
    volumes:
      - ./:/java-selenium-framework
      - /dev/shm:/dev/shm
```
This will run our tests using the image with the name Dockerfile in the current directory. We've named it 'selenium/tests' so we can delete the image later. We've made it dependent on the hub and nodes.

The Dockerfile gives us everything we need to run gradle builds inside a container so not installation is needed. And the command is the same as we would use on our host machine to run our tests via gradle.

Finally we mount so the results are visible on the host machine.

```
#  myApp:
#    build: ../
#    image: my/app
#    ports:
#      - "3000:3000"
```

We also have a commented out service. If these tests were living in a project repository inside their own directory then this service would create the app if there were a Dockerfile in the project root and expose it on port 3000.

This allows us to test branch environments by updating  -Dbase_url in the gradle command.

## Gradle
Gradle is used as the build tool. The dependencies can be seen above.

Additionally contained are java and lombok plugins:
```
plugins {
    id 'java'
    id 'io.freefair.lombok'
}
```

And the tasks to run the tests:
```
task runTestSuite(type: Test) {
    scanForTestClasses = false
    useTestNG() {
        useDefaultListeners = true
        testLogging.showStandardStreams = true
        suites 'testngsuites/chromesuite.xml',
                'testngsuites/firefoxsuite.xml'
        options {
            systemProperties(System.getProperties())
            systemProperties.remove("java.endorsed.dirs")
            useDefaultListeners = true
        }
    }
```
To run the testng suites via the gradle tasks.
```
gradle clean runTestSuite
```
To run the testng suites via the gradle tasks on selenium grid:
```
gradle clean runTestSuite -Dheadless=true  -Dgrid=true
```

## Jenkins
The Jenkinsfile in the root of the project contains most of what you need to run the tests in a jenkins pipeline. Jenkins will have to be configured to run docker/docker-compose commands. Add the List Git Branches Parameter and TestNG Results Plugin for selecting branches and publishing test results.

The Pipeline consists of 3 stages and 2 post actions:

Stage - Clone Project:
Preparation stage where we receive updates from our repository. Here the build is given a name to display and the code is checked out.
```
stage ('Clone Project'){
       steps{
              script{
                     currentBuild.displayName = "#${BUILD_NUMBER} [${GIT_BRANCH}]"
              }
              cleanWs()
              checkout scm
       }
}
```
Stage - Create Selenium Grid: 
This stage runs the docker-compose up command and brings up, potentially a branch environment, the grid and nodes and runs our tests exiting when the build is complete.
```
stage ('Set up environment and run tests'){
    steps{
        sh "docker-compose up --exit-code-from selenium-tests"
    }
}
```

Post Actions - 
There are two action which always run even if an earlier step in the pipeline fails.
1. Stop and delete the created containers and network.
2. Publish the testNG test results report created when the tests completed.
3. Delete the created images.
```
post{
    always{
        sh "docker-compose down"
 	    step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
 	    //delete image created for application under test "sh docker image rm cypress/someAppImageName"
 	    sh "docker image rm selenium/tests"
 	}
}
```
