pipeline {
    agent any
    environment {
        DOCKER_IMAGE = "one-bucket:latest"
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
                    docker.build(DOCKER_IMAGE)
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