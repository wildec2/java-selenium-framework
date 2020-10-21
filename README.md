# Overview

This is a java and selenium based framework for web UI automation testing on PWAs or 
responsive web apps. The framework caters for desktop(multiple browsers), mobile and tablet.
Here we tap into device mode offered by Chrome(dev tools) 
when running mobile or tablet tests. Using device mode this allows the frontend to be 
rendered server side which can be essential for PWA testing. Included is a selenium grid
setup via docker-compose to allow for remote headless running of tests. Built with gradle and 
we use the testng test automation framework for running tests. 

## Setup

This will work straight out of the box.
Clone the repo, open in IntelliJ, build and run the tests.

To fire up selenium grid:

```
docker-compose up -d
```

http://localhost:4444/grid/console

To bring down selenium grid:
```
docker-compose down
```

## Run Tests
You can run your tests using intelliJ straight away. 
Or you can run the testng suites via the the gradle tasks.
```
gradle runChromeSuite
gradle runFirefoxSuite
```
If you want run on selenium grid:
```
gradle runChromeSuite -Dheadless=true  -Ddocker=true
gradle runFirefoxSuite -Dheadless=true  -Ddocker=true
```


## How it Works
Something useful about something.

Followed by something useful about something else.
