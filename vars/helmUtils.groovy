import groovy.text.* 
import java.io.* 
import hudson.*
import PodTemplates

def call() {
 
    if (env.GIT_BRANCH == "master"){
        def helmEnv = "prod"
    }  else if ( env.GIT_BRANCH == "preprod") {
        def helmEnv = "preprod"
    } else if ( env.GIT_BRANCH == "develop"){
        def helmEnv = "dev"
    } else {
        echo "Only develop, preprod and master branches are going to execute helmUtils method"
        echo "Skip helmUtils"
        return
    }
    def imageName = "${env.DOCKER_REPO}/${env.PROJECT_NAME}/${APP_NAME}"
    def imageTag = "${BUILD_ID}"
    slaveTemplates = new PodTemplates()
    slaveTemplates.helmTemplate() {
        stage("Check valueTemp methods"){
            node("helm"){
                //git branch: "master",
                //    credentialsId: "${GIT_CRED}",
                //    url: "https://bitbucket.kapitalbank.az/scm/jt/helm-template.git"
                container ("helm") {
                    sh "ls -la"
                    sh "helm repo add HelmRepo https://nexus.kapitalbank.az/repository/HelmRepo/"
                    sh "helm pull ${PROJECT_NAME}-${APP_NAME} --untar"
                    yamlEdit(helmEnv, imageName, imageTag)
                    sh "helm package ."
                    sh "curl -u admin:admin12345 https://nexus.kapitalbank.az/repository/HelmRepo/ --upload-file ./*.tgz"
                    sh "cat dev.yaml"
                }
            }
        }
    }
}

def valueTemp (input, variables) {
    def engine = new StreamingTemplateEngine() 
    return engine.createTemplate(input).make(variables).toString()
}