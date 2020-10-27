pipeline {

    def network='jenkins-${BUILD_NUMBER}'
    def seleniumHub='selenium-hub-${BUILD_NUMBER}'
    def chrome='chrome-${BUILD_NUMBER}'
    def firefox='firefox-${BUILD_NUMBER}'
    def containertest='conatinertest-${BUILD_NUMBER}'

    agent any

    stages{
        stage ('SCM checkout'){
            steps{
                git "https://github.com/wildec2/pwa-responsive-web-java-selenium-framework.git"
            }
        }
        stage('Setting Up Selenium Grid') {
                 steps{
                    sh "docker network create ${network}"
                    sh "docker run -d -p 4444:4444 --name ${seleniumHub} --network ${network} selenium/hub"
                    sh "docker run -d -e HUB_PORT_4444_TCP_ADDR=${seleniumHub} -e HUB_PORT_4444_TCP_PORT=4444 --network ${network} --name ${chrome} selenium/node-chrome"
                    sh "docker run -d -e HUB_PORT_4444_TCP_ADDR=${seleniumHub} -e HUB_PORT_4444_TCP_PORT=4444 --network ${network} --name ${firefox} selenium/node-firefox"
                 }
        }
        stage ('Build'){
            steps{
                sh "gradlew clean build"
            }
        }
        stage ('Run tests') {
            steps{
                sh "gradlew clean runTestSuite -Dheadless=true -Dgrid=true"
            }
        }
        stage('Tearing Down Selenium Grid') {
                  steps {
                     sh "docker rm -vf ${chrome}"
                     sh "docker rm -vf ${firefox}"
                     sh "docker rm -vf ${seleniumHub}"
                     sh "docker network rm ${network}"
                  }
        }
 	}
}