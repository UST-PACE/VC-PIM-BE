pipeline {
    agent any
    stages {
        stage('Git Checkout') {
            steps {
                checkout scmGit(branches: [[name: '*/maven']], extensions: [], userRemoteConfigs: [[credentialsId: 'github-laya', url: 'https://github.com/UST-PACE/VC-PIM-BE']])
            }
        }
     
        stage('Checkout Az Repo') {
            steps {
                script {
                   checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[credentialsId: 'azure-repo-laya', url: 'https://dev.azure.com/EmergingTechnologySolutions/Pim/_git/Pim_Backend']])
                }
            }
        } 
       
        stage('Maven Build') {
            steps {
                withMaven(globalMavenSettingsConfig: '', jdk: '', maven: 'maven_', mavenSettingsConfig: '3b011989-862e-453d-bbf6-65a3bd09218c', traceability: false) {
                  sh script: 'mvn clean install -U -DskipTests'
 
                }
            }
        }
        
    }
}
