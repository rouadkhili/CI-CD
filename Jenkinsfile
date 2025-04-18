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
        stage('Cloner le projet') {
            steps {
                git branch: 'master', 
                    url: 'https://github.com/rouadkhili/CI-CD.git'
            }
        }



         
        }

        stage('Build Spring Boot') {
            steps {
                dir('spring-boot-server') {
                    sh 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Tests Unitaires Spring Boot') {
            steps {
                dir('spring-boot-server') {
                    sh 'mvn test'
                }
            }
        }
        stage('Tests unitaires Frontend') {
            steps {
                dir('angular-17-client') {
                    sh 'npm install'
                    sh 'ng test --watch=false --no-progress --browsers=ChromeHeadless || true'
                }
            }
        }

        stage('Tests d\'intégration avec PostgreSQL') {
            steps {
                sh 'docker-compose -f docker-compose.test.yml up -d'
                sh 'sleep 30'  // Attente pour laisser PostgreSQL démarrer
                dir('spring-boot-server') {
                    sh 'mvn verify -P integration-tests'
                }
                sh 'docker-compose -f docker-compose.test.yml down'
            }
        }
        

        stage('Tests End-to-End avec Cypress') {
            steps {
                script {
                    dir('angular-17-client') {
                        
                       sh 'npx http-server ./dist/angular-17-crud -p 4200 &'
                       sh 'npx wait-on http://localhost:4200 --timeout 60000'

                        sh 'curl http://localhost:4200 || true'
                        sh 'xvfb-run --auto-servernum --server-args="-screen 0 1920x1080x24" npx cypress run'
                    }
                }
            }
        }

        stage('Build Docker Images') {
            steps {
                script {
                    dir('spring-boot-server') {
                        sh 'docker build -t spring-boot-server .'
                    }
                    dir('angular-17-client') {
                        sh 'docker build -t angular-17-client .'
                    }
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose up -d'
            }
        }
    }

    post {
        always {
            // Archive les captures d’écran de Cypress en cas d’échec
            archiveArtifacts artifacts: 'angular-17-client/cypress/screenshots/**/*.png', fingerprint: true
        }
    }
}
