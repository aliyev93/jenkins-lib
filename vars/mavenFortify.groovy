import hudson.model.*
import PodTemplates

def call() {
    node ("fortify-node") {
        stage("SCM Checkuot"){
            git branch: "${GIT_BRANCH}",
            credentialsId: "${GIT_CRED}",
            url: "${GIT_URL}"
        }
        //stage ('Fortify Clean') {
        //    //fortifyClean buildID: "TestMaven"
        //    sh "sourceanalyzer -b TestMaven -clean"
        //}
        stage('Fortify Scan') {
            withEnv(['M2_HOME=/opt/maven/apache-maven-3.6.3/','PATH+EXTRA=/opt/maven/apache-maven-3.6.3/bin:/opt/Fortify/Fortify_SCA_and_Apps_20.1.2/bin/']){
                sh "sourceanalyzer -b TestMaven -clean"
               // fortifyTranslate buildID: "TestMaven",
               //projectScanType: fortifyMaven3(mavenOptions: "-B -DskipTests clean package")
                sh "sourceanalyzer -b TestMaven mvn -B -DskipTests clean package"
                sh "sourceanalyzer -b TestMaven -scan -format fpr -f TestMaven.fpr"
                fortifyUpload appName: "TestMaven", appVersion: "v0.0.1", resultsFile: "TestMaven.fpr"
            }
        }  
        //stage('Fortify Scan') {
        //    //fortifyScan buildID: "TestMaven", resultsFile: "TestMaven.fpr", addOptions: "-format fpr", logFile: "/opt/fortifylog/asdf.log", debug: true
        //    sh "sourceanalyzer -b TestMaven -scan -format fpr -f TestMaven.fpr"
        //}
        //stage(" Fortify Upload"){
        //    fortifyUpload appName: "TestMaven", appVersion: "v0.0.1", resultsFile: "TestMaven.fpr"
        //    //sh "fortifyclient -url https://fortify.kapitalbank.az/ssc -authtoken ed7f4725-4e13-4e7d-98a8-ed9c543d8d19 uploadFPR -file TestMaven.fpr -project my-app -version 1.0-SNAPSHOT"
        //}
    }
}