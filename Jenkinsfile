pipeline {
    agent any

    environment {
        // Define environment variables here
         DOCKER_IMAGE_NAME = "my-docker-image"
        DOCKER_TAG = "latest"
        // Temporary SSL workaround
        GIT_SSL_NO_VERIFY = "1"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the source code from your repository
                git branch: 'master', url: 'https://github.com/rouadkhili/CI-CD.git'
            }
        }

        stage('Build') {
            steps {
                script {
                    // Build the application (e.g., with Maven or npm)
                    bat 'npm install' 
                    bat 'npm install --only=dev'  // Use npm commands if it's a Node.js app
                }
            }
        }

        stage('Test') {
            steps {
               dir('frontend') {
                    bat 'npm install'
                    bat 'npm test'
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    // Build the Docker image
                    docker.build("${DOCKER_IMAGE}:${env.BUILD_ID}")
                }
            }
        }

        stage('Docker Push') {
            steps {
                script {
                    // Push the Docker image to the registry
                    docker.withRegistry("https://${REGISTRY}", "${REGISTRY_CREDENTIALS}") {
                        docker.image("${DOCKER_IMAGE}:${env.BUILD_ID}").push()
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                script {
                    // Deploy the application using the Docker image
                    sh 'docker run -d -p 8080:8080 ${DOCKER_IMAGE}:${env.BUILD_ID}'
                }
            }
        }
    }
    }
    post {
        always {
            // Cleanup workspace after the pipeline execution
            cleanWs()
        }
        failure {
            // Notifications for failure
            echo 'Pipeline failed!'
        }
        success {
            // Notifications for success
            echo 'Pipeline succeeded!'
        }
    }
}
