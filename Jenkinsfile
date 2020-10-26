 node{
    stage ('SCM checkout'){
        git "https://github.com/wildec2/pwa-responsive-web-java-selenium-framework.git"
    }
 	stage ('Build'){
        sh "gradlew clean build"
 	}
 	stage ('Run Tests') {
 	    sh "gradlew clean runTestSuite -Dheadless=true -Dgrid=true"
 	}
}