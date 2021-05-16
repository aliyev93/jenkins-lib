import hudson.model.*
import PodTemplates

def call() {
    node ("fortify-node") {
        stage("SCM Checkuot"){
            git branch: "${GIT_BRANCH}",
            credentialsId: 'gidcredID',
            url: "${GIT_URL}"
        }
        stage('Fortify Scan') {
            withEnv(['JAVA_HOME=/usr/lib/jvm/java-11/','PATH+EXTRA=/opt/Fortify/Fortify_SCA_and_Apps_20.1.2/bin/']){
                sh "printenv"
                sh "chmod +x gradlew"
                sh "sourceanalyzer -b TestGradle -clean"
                sh "sourceanalyzer -jdk 11 -verbose -b TestGradle ./gradlew clean build"
                sh "sourceanalyzer -b TestGradle -scan -format fpr -f TestGradle.fpr"
                fortifyUpload appName: "TestGradle", appVersion: "v0.0.1", resultsFile: "TestGradle.fpr"
            }
        } 
    }
}
//'JAVA_HOME=/usr/lib/jvm/java-11/',