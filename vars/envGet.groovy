import hudson.model.*

def call(){
    def env = System.getenv()
        env.each{
            println it
            } 
}


String getProjectFullName(){
    def pmap = [
        "jt": "JenkinsTest"
        ]
        
    return pmap."${scm.getUserRemoteConfigs()[0].getUrl().split('/')[-2]}"
}

String getRepo() {
    return scm.getUserRemoteConfigs()[0].getUrl() 
    //return "Asdfskldfv s ksjdf"
}

String getRepoName() {
    return scm.getUserRemoteConfigs()[0].getUrl().split('/')[-1] - '.git'
}

String getProjectName() {
    return scm.getUserRemoteConfigs()[0].getUrl().split('/')[-2]
}

String getBranchName() {
    return scm.getBranches()[0].getName()
    //return scm.getUserRemoteConfigs()
}
String mergeTarget(){
    return scm.getMergeOptions().getMergeTarget()
}

String mergeRemote(){
    return scm.getMergeOptions().getRemoteBranchName()
}

String mergeStrategy(){
    return scm.getMergeOptions().getMergeStrategy()
}
//String mergeRef(){
//    return scm.getMergeOptions().getRef(every())
//}
def printUrl() {
    println getRepo()
    println getProjectName()
    println getRepoName()  
    println getBranchName()
    println ("target " + mergeTarget())
    println mergeStrategy()
    println ("remote " + mergeRemote())
    println getProjectFullName()
//    println mergeRef()
}
