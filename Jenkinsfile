pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "one-bucket:latest"
        SERVER_IP = "192.168.219.135"
        SERVER_USER = "sang"
        DOCKER_IMAGE_FILE = "one-bucket.tar.gz"
   }

   stages {
        stage('Checkout') {
            steps {
                //source code checkout
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build'
            }
        }

        stage('Test') {
            steps {
                sh './gradlew test'
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    sh 'docker build -t ${DOCKER_IMAGE} .'
                }
            }
        }

        stage('Save Docker Image') {
            steps {
                script {
                    sh "docker save ${DOCKER_IMAGE} | gzip > ${DOCKER_IMAGE_FILE}"
                    archiveArtifacts artifacts: "${DOCKER_IMAGE_FILE}", allowEmptyArchive: false
                }
            }
        }

        stage('Transfer and Deploy Docker Image') {
            steps {
                script {
                    // Start SSH agent and add the key
                    sh """
                    eval \$(ssh-agent -s)
                    ssh-add /var/jenkins_home/.ssh/id_rsa
                    """

                    // Transfer Docker image to server2
                    sh "scp -i /var/jenkins_home/.ssh/id_rsa ${DOCKER_IMAGE_FILE} ${SERVER_USER}@${SERVER_IP}:/tmp/"
                    // Deploy the new Docker image on server2
                    sh """
                    ssh -i /var/jenkins_home/.ssh/id_rsa ${SERVER_USER}@${SERVER_IP} << 'EOF'
                        docker load -i /tmp/${DOCKER_IMAGE_FILE}
                        docker stop one-bucket-container || true
                        docker rm one-bucket-container || true
                        docker run -d --name one-bucket-container -p 8080:8080 one-bucket:latest
                        docker system prune -f
                    << EOF
                    """
                }
            }
        }
    }

   post {
        always {
            cleanWs()
        }
        success {
            echo 'Build and archive completed successfully!'
        }
        failure {
            echo 'Build or archive failed'
        }
   }
}