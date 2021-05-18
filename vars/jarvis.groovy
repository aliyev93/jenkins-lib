import hudson.model.*
import envGet
import mavenUtils
import gradleUtils
import helmUtils


    def call() {

        envget = new envGet()
        env.GIT_URL = envget.getRepoName()
        env.GIT_BRANCH = envget.getBranchName()
        env.GIT_CRED = '2cb4fd4b-8f7f-46a3-93c7-a4f92ce7c0d3'
        env.PROJECT_NAME = envget.getProjectName()
        env.PROJECT_FULL_NAME = envget.getProjectFullName()
        env.APP_NAME = envget.getRepoName()

        if (APP_TYPE.toLowerCase() == "maven"){
            mavenUtils()
        } else if( APP_TYPE.toLowerCase() == "gradle") {
            gradleUtils()
        } else if ( APP_TYPE == null) {
            echo "Please define APP_TYPE to gradle or maven"
        }

        mavenUtils()
        //helmUtils()
    }
