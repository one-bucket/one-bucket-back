pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "one-bucket:latest"
        SERVER_IP = "192.168.219.135"
        SERVER_USER = "sang"
        DOCKER_IMAGE_FILE = "one-bucket.tar.gz"

        //jenkins env
        JENKINS_MYSQL_HOST = "172.20.0.5"
        JENKINS_MYSQL_USER = "root"
        JENKINS_MYSQL_PASSWORD = "m7128226"
        JENKINS_REDIS_HOST = "172.20.0.4"


        //main server env
        SERVER_MYSQL_HOST = "192.168.219.101"
        SERVER_REDIS_HOST = "192.168.219.101"
        SERVER_MYSQL_USER = "root"
        SERVER_MYSQL_PASSWORD = "m7128226"
        MONGO_HOST = "192.168.219.101"
        MONGO_DB = "testdb"
        MONGO_USER = "springUser"
        MONGO_PWD = "ezAcEOvxX0"
        MINIO_URL = "http://192.168.219.144:9000"

         JAVA_TOOL_OPTIONS = "-Duser.timezone=Asia/Seoul"
   }

   stages {
       
        stage('Checkout') {
            steps {
                //source code checkout
                checkout scm
            }
        }

        stage('Test') {
            steps {
                withEnv([
                    "MYSQL_HOST=${JENKINS_MYSQL_HOST}",
                    "REDIS_HOST=${JENKINS_REDIS_HOST}",
                    "MYSQL_USER=${JENKINS_MYSQL_USER}",
                    "MYSQL_PASSWORD=${JENKINS_MYSQL_PASSWORD}",
                    "MONGO_HOST=${MONGO_HOST}",
                    "MONGO_DB=${MONGO_DB}",
                    "MINIO_URL=${MINIO_URL}",
                    "MONGO_USER=${MONGO_USER}",
                    "MONGO_PWD=${MONGO_PWD}",
                    "MAIL_HOST=${MAIL_HOST}",
                    "MAIL_PASSWORD=${MAIL_PASSWORD}",
                    "MAIL_PORT=${MAIL_PORT}",
                    "MAIL_USERNAME=${MAIL_USERNAME}",
                    "MAIL_PROTOCOL=${MAIL_PROTOCOL}",
                    "MAIL_SMTP_AUTH=${MAIL_SMTP_AUTH}",
                    "MAIL_SMTP_STARTTLS_ENABLE=${MAIL_SMTP_STARTTLS_ENABLE}"
                ]) {
                    sh './gradlew test'
                }
            }
        }

        stage('Build') {
            steps {
                sh './gradlew build'
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
                        docker run -d --name one-bucket-container -p 8080:8080 \\
                            -e MYSQL_HOST=${SERVER_MYSQL_HOST} \\
                            -e MYSQL_USER=${SERVER_MYSQL_USER} \\
                            -e MYSQL_PASSWORD=${SERVER_MYSQL_PASSWORD} \\
                            -e REDIS_HOST=${SERVER_REDIS_HOST} \\
                            -e MONGO_HOST=${MONGO_HOST} \\
                            -e MONGO_DB=${MONGO_DB} \\
                            -e MINIO_URL=${MINIO_URL} \\
                            -e MONGO_USER=${MONGO_USER} \\
                            -e MONGO_PWD=${MONGO_PWD} \\
                            -e MAIL_HOST=${MAIL_HOST} \\
                            -e MAIL_PASSWORD=${MAIL_PASSWORD} \\
                            -e MAIL_PORT=${MAIL_PORT} \\
                            -e MAIL_USERNAME=${MAIL_USERNAME} \\
                            -e MAIL_PROTOCOL=${MAIL_PROTOCOL} \\
                            -e MAIL_SMTP_AUTH=${MAIL_SMTP_AUTH} \\
                            -e MAIL_SMTP_STARTTLS_ENABLE=${MAIL_SMTP_STARTTLS_ENABLE} \\
                            ${DOCKER_IMAGE}
                        docker system prune -f
                    << EOF
                    """
                }
            }
        }
    }

   post {
//         always {
//             cleanWs()
//         }
        success {
            echo 'Build and archive completed successfully!'
        }
        failure {
            echo 'Build or archive failed'
        }
   }
}
