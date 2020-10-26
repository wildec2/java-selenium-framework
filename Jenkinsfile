pipeline {

    agent any

    stages{
        stage ('SCM checkout'){
            steps{
                git "https://github.com/wildec2/pwa-responsive-web-java-selenium-framework.git"
            }
        }
        stage ('Bring up grid'){
            steps{
                sh "docker-compose up -d"
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
        stage ('Bring down grid'){
            steps{
                sh "docker-compose down"
            }
        }
 	}
}