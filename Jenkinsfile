pipeline{

    agent any

    options{
        buildDiscarder(logRotator(numToKeepStr: '8'))
    }

    stages{
        stage ('Clone Project'){
            steps{
                script{
                    currentBuild.displayName = "#${BUILD_NUMBER} [${GIT_BRANCH}]"
                }
                cleanWs()
                checkout scm
            }
        }
        stage('Create Selenium Grid') {
                 steps{
                    sh "docker-compose up -d"
                 }
        }
        stage ('Run Tests'){
            steps{
                sh "./gradlew clean runTestSuite -Dheadless=true -Dgrid=true"
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