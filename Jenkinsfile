pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "one-bucket:latest"
        MINIO_SERVER = "http://192.168.219.144:9001"
        MINIO_ACCESS_KEY = "jack8226"
        MINIO_SECRET_KEY = "m7128226"
        BUCKET_NAME = "one-bucket"
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
                    sh 'docker build -t one-bucket:latest .'
                }
            }
        }

        stage('Save Docker Image') {
            steps {
                script {
                    sh "docker save ${DOCKER_IMAGE} | gzip > ${DOCKER_IMAGE}.tar.gz"
                    archiveArtifacts artifacts: "${DOCKER_IMAGE}.tar.gz", allowEmptyArchive: false
                }
            }
        }

        stage('Upload to Minio') {
            steps {
                //minio client install
                sh '''
                curl -O https://dl.min.io/client/mc/release/linux-amd64/mc
                chmod +x mc
                mv mc /usr/local/bin/
                '''

                //minio server connect config
                sh '''
                mc alias myminio ${MINIO_SERVER} ${MINIO_ACCESS_KEY} ${MINIO_SECRET_KEY}
                '''

                //docker image upload
                sh '''
                mc cp ${DOCKER_IMAGE}.tar.gz myminio/%{BUCKET_NAME}
                '''
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