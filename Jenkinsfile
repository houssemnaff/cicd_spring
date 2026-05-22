pipeline {
    agent any
    tools {
        maven 'Maven3'
    }
   
    options {
        timeout(time: 30, unit: 'MINUTES')
        buildDiscarder(logRotator(numToKeepStr: '10'))
        timestamps()
    }
   
    stages {
        stage('Checkout') {
            steps {
                echo 'Récupération du code...'
                checkout scm
            }
        }
       
        stage('Build & Tests') {
            steps {
                echo 'Compilation et tests...'
                sh 'mvn clean package'
            }
            post {
                success {
                    echo ' Build et tests réussis'
                }
                failure {
                    echo ' Build ou tests échoués'
                }
            }
        }
       
        stage('SonarQube Analysis') {
            steps {
                echo 'Analyse qualité...'
                withSonarQubeEnv('SonarQube') {
                    sh '''
                        mvn clean verify sonar:sonar -Dsonar.projectKey=calculator -Dsonar.host.url=http://localhost:9000 -Dsonar.login=squ_e883edc9cf703a66ee2e9e36c01638450e95f09d
                    '''
                }
            }
        }
       
        stage('Deploy to Nexus') {
            steps {
                echo 'Publication sur Nexus...'
                sh 'mvn deploy -DskipTests -s settings.xml'
            }
        }
    }
   
    post {
        success {
            echo 'Pipeline réussi - artifact publié sur Nexus !'
            echo 'SonarQube: http://localhost:9000/dashboard?id=calculator'
            echo 'Nexus: http://localhost:8082/repository/maven-snapshots/'
        }
        failure {
            echo ' Pipeline échoué !'
        }
    }
}