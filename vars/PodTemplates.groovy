    public void mavenTemplate(body) {
      podTemplate(
            cloud: 'kubernetes',
            name: 'maven',
            label: 'maven',
            envVars: [envVar (key: 'MAVEN_CONFIG', value: '/var/maven/.m2'),
                      envVar ( key: "MAVEN_HOME", value: '/var/maven')],
            containers: [containerTemplate(name: 'maven', image: "nexus.kblab.local:8089/v1/repositories/baseimages/maven:${MAVEN_VERSION}", command: 'cat', ttyEnabled: true)],
            volumes: [persistentVolumeClaim(claimName: 'mvn-m2', mountPath: '/var/maven/.m2')]) {
                body.call()
            }   
    }
     
    public void mavenSonarTemplate(body) {
      podTemplate(
            cloud: 'kubernetes',
            name: 'maven-sonar',
            label: 'maven-sonar',
            envVars: [envVar (key: 'MAVEN_CONFIG', value: '/var/maven/.m2'),
                      envVar ( key: "MAVEN_HOME", value: '/var/maven'),
                      envVar (key: "SONAR_HOST_URL", value: "http://sonarqube.kapitalbank.az/"),
                      envVar (key:"SONAR_LOGIN", value: "c2c5ab40efb9ff915c8cdebb446f9c48ec37141e")],
            containers: [containerTemplate(name: 'maven-sonar', image: "nexus.kblab.local:8089/v1/repositories/baseimages/maven:${MAVEN_VERSION}", command: 'cat', ttyEnabled: true)],
            volumes: [persistentVolumeClaim(claimName: 'mvn-m2', mountPath: '/var/maven/.m2')]) {
                body.call()
            }   
    } 

    public void gradleTemplate(body) {
        podTemplate(
            cloud: 'kubernetes',
            name: 'gradle',
            label: 'gradle',
            containers: [containerTemplate(name: 'gradle', image: 'openjdk:11.0.10-slim', command: 'cat', ttyEnabled: true )],
            //volumes: [persistentVolumeClaim(claimName: 'gradle-home', mountPath: '/home/gradle/')]
            ){
                body.call()
            }
    }

    public void kanikoTemplate(body) {
        podTemplate(
            cloud: 'kubernetes',
            name: 'kaniko',
            label: 'kaniko',
            containers: [containerTemplate(name: 'kaniko', image: 'nexus.kblab.local:8089/v1/repositories/baseimages/kaniko-execotor:debug', command: '/busybox/cat', ttyEnabled: true)],
            volumes: [secretVolume(secretName: 'regcred', mountPath: '/kaniko/.docker')]) {
                body.call()
            }
    }

    public void sonarTemplate(body) {
        podTemplate(
            cloud: 'kubernetes',
            name: 'sonar-scanner',
            label: 'sonar-scanner',
            envVars: [ envVar (key: "SONAR_HOST_URL", value: "http://sonarqube.kapitalbank.az/"),
                        envVar (key:"SONAR_LOGIN", value: "c2c5ab40efb9ff915c8cdebb446f9c48ec37141e")],
            containers: [containerTemplate(name: "sonar-scanner", image: 'sonarsource/sonar-scanner-cli:4.6', command: 'sonar-scanner', args: '-v', ttyEnabled: true)]){
                body.call()
            }
    }


    public void fortifyCiTemplate(body) {
        podTemplate(
            cloud: 'kubernetes',
            name: 'fortify-ci',
            label: 'fortify-ci',
           // envVars: [ envVar (key: "SONAR_HOST_URL", value: "http://sonarqube.kapitalbank.az/"),
           //             envVar (key:"SONAR_LOGIN", value: "c2c5ab40efb9ff915c8cdebb446f9c48ec37141e")],
            envVars: [ envVar (key: 'PATH', value: '/usr/local/openjdk-8/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/opt/bin')],
            containers: [containerTemplate(name: "fortify-ci", image: 'nexus.kblab.local:8089/v1/repositories/baseimages/fortify-ci-tools:1.0.1', command: '/bin/sh -c', args: 'cat', ttyEnabled: true)]){
                body.call()
            }
    }

    public void alpineTemplate(body) {
        podTemplate(
            cloud: 'kubernetes',
            name: 'alpine',
            label: 'alpine',
            containers: [containerTemplate(name: 'alpine', image: 'nexus.kblab.local:8089v1/repositories/baseimages/alpine:3.10',command: '/bin/sh -c', args: 'cat')]){
                body.call()
            }
    }

    public void helmTemplate(body) {
        podTemplate(
            cloud: 'kubernetes',
            name: 'helm',
            label: 'helm',
            containers: [containerTemplate(name: 'helm', image: 'nexus.kblab.local:8089/v1/repositories/baseimage/helm:v3.5.2', command: 'bash', ttyEnabled: true )]){
                body.call()
            }
    }

    return this