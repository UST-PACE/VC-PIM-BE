pipeline {
    agent any
	environment {
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
		DOCKER_IMAGE_NAME = 'layamoorthy170673/pim-be' 
        IMAGE_TAG = '1' 
    }
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
	stage('Check and Install Buildah') {
            steps {
                script {
                    echo "Checking if Buildah is installed..."
                    if (sh(script: 'command -v buildah > /dev/null 2>&1', returnStatus: true) != 0) {
                        echo "Buildah not found. Installing Buildah..."
                        sh '''
                        apt-get update
                        apt-get install -y buildah
                        '''
                    } else {
                        echo "Buildah is already installed."
                    }
                }
            }
        }

    	stage('Build and Push Image') {
  	   steps {
       	       script {

	 	      echo "Building and pushing Docker image using Buildah..."
					  // Build Docker image
                      withCredentials([usernameColonPassword(credentialsId: 'docker-hub-credentials', variable: 'REG_CREDS')]) {
                        sh 'buildah bud --no-cache --pull --force-rm --format docker --creds ${r"${REG_CREDS}"}' + " -t ${r"${DOCKER_IMAGE_NAME}"}:latest --iidfile iid ."
					    sh 'buildah images --format "ImageID: {{.ID}} {{.Name}} {{.Tag}} {{.Digest}} {{.CreatedAt}} {{.Size}} {{.CreatedAtRaw}}" ' +  "${r"${DOCKER_IMAGE_NAME}"}:latest"
                        sh 'buildah push --rm --creds ${r"${REG_CREDS}"} $(cat iid) ' + " docker://docker.io/${r"${DOCKER_IMAGE_NAME}"}:latest"
                    
					
		   
			}
       		  }
  	     }
	}
        
    }
}