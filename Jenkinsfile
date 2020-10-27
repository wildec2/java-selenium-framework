def network='jenkins-${BUILD_NUMBER}'
def seleniumHub='selenium-hub-${BUILD_NUMBER}'
def chrome='chrome-${BUILD_NUMBER}'
def firefox='firefox-${BUILD_NUMBER}'
def containertest='conatinertest-${BUILD_NUMBER}'

pipeline{

    agent any

    stages{
        stage ('SCM checkout'){
            steps{
                git "https://github.com/wildec2/pwa-responsive-web-java-selenium-framework.git"
            }
        }
        stage('Bringing Up Selenium Grid') {
                 steps{
                    sh "docker-compose up -d"
                 }
        }
        stage ('Build'){
            steps{
                sh "gradle clean build"
            }
        }
        stage ('Run tests'){
            steps{
                sh "gradle clean runTestSuite -Dheadless=true -Dgrid=true"
            }
        }
        stage("Publish HTML Report"){
            steps{
                echo "publish report steps here"
            }
        }
 	}
 	post{
 	    always{
 	        sh "docker-compose down"
 	    }
 	}
}