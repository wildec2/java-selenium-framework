 node{
    stage ('SCM checkout'){
        git "https://github.com/wildec2/pwa-responsive-web-java-selenium-framework.git"
    }
 	stage ('Build'){
        sh "gradle clean build"
 	}
 	stage ('Run Tests') {
 	    sh "gradle clean runTestSuite -Dheadless=true -Dgrid=true"
 	}
}