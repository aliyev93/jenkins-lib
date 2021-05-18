import hudson.model.*
import PodTemplates
import sonarUtils



def call() {
    if (!binding.hasVariable('MAVEN_VERSION')) {
        env.MAVEN_VERSION="3.6.3-jdk-8-slim"
    }
    env.DOCKER_REPO = "http://nexus.kapitalbank.az/bankrepo"

    slaveTemplates = new PodTemplates()
    slaveTemplates.mavenTemplate() {
        parallel(
            "Stage 1": {
                stage ("Maven build"){
                    node ("maven") {
                        sh "printenv"
                        git branch: "${GIT_BRANCH}",
                            credentialsId: "${GIT_CRED}",
                            url: "${GIT_URL}"
                        container ("maven"){
                            sh "mvn ${MAVEN_OPTION}"
                        }
                        dir('target') {
                        stash name: 'JAR', includes: '*.jar'
                        }
                    }   
                }
            },
            "Stage 2": {
                if (FORTIFY_ENABLED) {
                   mavenFortify()
                } else {
                    echo " Fortify Stage sciped "
                }
                
            },
            

            "Stage 3": {
                if (SONAR_ENABLED) {
                    sonarUtils()
                } else {
                    echo "SonarScan Stage scipped"
                }
                
            }
        )
    }

    slaveTemplates.kanikoTemplate {
        stage("Kaniko Build"){
            node("kaniko"){
                git branch: "${GIT_BRANCH}",
                    credentialsId: "${GIT_CRED}",
                    url: "${GIT_URL}"
                unstash name: 'JAR'
                container(name: "kaniko", shell: "/busybox/sh"){
                    sh """
                    #!/busybox/sh
                    ls -la 
                    pwd
                    /kaniko/executor --context $PWD --insecure --skip-tls-verify --destination "${env.DDOCKER_REPO}/${env.PROJECT_NAME}/${APP_NAME}:${BUILD_ID}" --destination "${env.DDOCKER_REPO}/${env.PROJECT_NAME}/${APP_NAME}":latest
                    """
                }

            }
        }

    }     
}