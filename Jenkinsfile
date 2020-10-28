def network='jenkins-${BUILD_NUMBER}'
def seleniumHub='selenium-hub-${BUILD_NUMBER}'
def chrome='chrome-${BUILD_NUMBER}'
def firefox='firefox-${BUILD_NUMBER}'
def containertest='conatinertest-${BUILD_NUMBER}'

pipeline{

    agent any

    options{
        buildDiscarder(logRotator(numToKeepStr: '8'))
    }

    stages{
        stage ('SCM checkout'){
            steps{
                script{
                    currentBuild.displayName = "#${BUILD_NUMBER} [${GIT_BRANCH}]"
                }
                cleanWs()
                checkout scm
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
 	}

 	post{
 	    always{
 	        sh "docker-compose down"
 	        step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
 	    }
 	}
}