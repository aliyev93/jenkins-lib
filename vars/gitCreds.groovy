import jenkins.model.*
def call( String uname ) {
    def creds = com.cloudbees.plugins.credentials.CredentialsProvider.lookupCredentials(
      com.cloudbees.plugins.credentials.Credentials.class,
      Jenkins.instance,
      null,
      null
    );
    def credential = creds.find {it.id == "${uname}"}
    if (!credential) {
      return "Unable to pickup credential from Jenkins"
    }

return credential.password;
}