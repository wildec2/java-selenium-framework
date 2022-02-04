pipeline{

    agent any

    options{
        buildDiscarder(logRotator(numToKeepStr: '8'))
    }

    stages{
        stage ('Clone project'){
            steps{
                script{
                    currentBuild.displayName = "#${BUILD_NUMBER} [${GIT_BRANCH}]"
                }
                cleanWs()
                checkout scm
            }
        }
        stage ('Set up environment and run tests'){
            steps{
                sh "docker-compose up --exit-code-from selenium-tests"
            }
        }
 	}

 	post{
 	    always{
 	        sh "docker-compose down"
 	        step([$class: 'Publisher', reportFilenamePattern: '**/testng-results.xml'])
 	        //delete image created for application under test "sh docker image rm my/app"
 	        sh "docker image rm selenium/tests"
 	    }
 	}
}