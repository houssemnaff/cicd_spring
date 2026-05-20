pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'JDK17'
    }

    environment {
        SONAR_PROJECT_KEY = 'cicd_spring'
        NEXUS_URL         = 'http://localhost:8081'
        NEXUS_REPO        = 'maven-releases'
        NEXUS_CREDENTIALS = 'nexus-credentials'
    }

    stages {

        stage('Checkout') {
            steps {
                git branch: 'main',
                    url: 'https://github.com/votre-user/cicd_spring.git'
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Tests') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn sonar:sonar \
                            -Dsonar.projectKey=${SONAR_PROJECT_KEY} \
                            -Dsonar.projectName=${SONAR_PROJECT_KEY} \
                            -Dsonar.java.binaries=target/classes
                    '''
                }
            }
        }

        stage('Quality Gate') {
            steps {
                timeout(time: 2, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }

        stage('Deploy to Nexus') {
            steps {
                nexusArtifactUploader(
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    nexusUrl: "${NEXUS_URL}",
                    groupId: 'com.example',
                    version: '1.0.0',
                    repository: "${NEXUS_REPO}",
                    credentialsId: "${NEXUS_CREDENTIALS}",
                    artifacts: [
                        [
                            artifactId: 'cicd_spring',
                            classifier: '',
                            file: 'target/cicd_spring-1.0.0.jar',
                            type: 'jar'
                        ]
                    ]
                )
            }
        }
    }

    post {
        success {
            echo 'Pipeline reussi !'
        }
        failure {
            echo 'Pipeline echoue !'
        }
        always {
            cleanWs()
        }
    }
}
