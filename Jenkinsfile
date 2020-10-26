pipeline {

    agent any

    stages{
        stage ('SCM checkout'){
            git "https://github.com/wildec2/pwa-responsive-web-java-selenium-framework.git"
        }
        stage ('Bring up grid'){
            sh "docker-compose up -d"
        }
        stage ('Build'){
            sh "gradlew clean build"
        }
        stage ('Run tests') {
            sh "gradlew clean runTestSuite -Dheadless=true -Dgrid=true"
        }
        stage ('Bring down grid'){
            sh "docker-compose down"
        }
 	}
}