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
                sh "docker-compose -f docker-compose-v3.yml up --scale chrome=2 --scale firefox=2 --scale edge=2 --exit-code-from selenium-tests"
            }
        }
 	}

 	post{
 	    always{
 	        sh "docker-compose down"
 	        step(publishHTML target: [
                                     allowMissing: false,
                                     alwaysLinkToLastBuild: true,
                                     keepAll: true,
                                     reportDir: "./build/reports/tests/runTestSuite",
                                     reportFiles: 'index.html',
                                     reportName: 'HTML Report'])
 	        //delete image created for application under test "sh docker image rm my/app"
 	        sh "docker image rm selenium/tests"
 	    }
 	}
}