import hudson.model.*
import PodTemplates

def call() {
    slaveTemplates = new PodTemplates()
    slaveTemplates.gradleTemplate {
        stage ("Gradle build"){
            node ("gradle") {
                sh "printenv"
                git branch: "${GIT_BRANCH}",
                    credentialsId: '2cb4fd4b-8f7f-46a3-93c7-a4f92ce7c0d3',
                    url: "${GIT_URL}"
                container ("gradle"){
                    sh 'echo $JAVA_HOME'
                    sh "java -version"
                    sh "chmod +x ./gradlew && ./gradlew clean build"
                }
                sh "ls -la build/libs"
                dir("build/libs"){
                    stash name: "JAR", includes:"*.*ar"
                }

            }   
        }
    }

    slaveTemplates.kanikoTemplate {
        stage("Kaniko Build"){
            node("kaniko"){
                git branch: "${GIT_BRANCH}",
                    credentialsId: '2cb4fd4b-8f7f-46a3-93c7-a4f92ce7c0d3',
                    url: "${GIT_URL}"
                unstash name: 'JAR'
                container(name: "kaniko", shell: "/busybox/sh"){
                    sh '''
                    #!/busybox/sh
                    ls -la 
                    pwd
                    /kaniko/executor --context $PWD --insecure --skip-tls-verify --destination nexus.kblab.local:8089/v1/repositories/tstdevops/images/gradleapp:$BUILD_NUMBER --destination nexus.kblab.local:8089/v1/repositories/tstdevops/images/gradleapp:latest
                    '''
                }

            }
        }

    }     
}