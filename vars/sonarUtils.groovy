import hudson.model.*
import PodTemplates

def call(){
    slaveTemplates = new PodTemplates()
    slaveTemplates.mavenSonarTemplate() {
        stage ("Sonar Scan"){
            node ("maven-sonar") {
                sh "printenv"
                git branch: "${GIT_BRANCH}",
                    credentialsId: '2cb4fd4b-8f7f-46a3-93c7-a4f92ce7c0d3',
                    url: "${GIT_URL}"
                //def scannerHome = tool 'SonarScanner 4.0';
                withSonarQubeEnv('BankSonar') { // You can override the credential to be used
                     container ("maven-sonar"){
                        script { 
                            sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.7.0.1746:sonar" + 
                            " -Dsonar.qualitygate.wait"
                        }
                    }   
                }    
            }

        }
        stage("Quality Gate"){
            sleep 10
            timeout(time: 1, unit: 'MINUTES') {
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        }
    }
}