pipeline {
    // 1. Agent: S'exécute sur n'importe quel agent Jenkins
    // (Dans notre cas, il s'exécutera sur le conteneur Jenkins lui-même)
    agent any

    // 2. Outils: Indique à Jenkins d'utiliser l'installation Maven
    // que nous allons configurer dans l'interface web de Jenkins.
    tools {
        maven 'Maven-Default' // Ce nom doit correspondre
    }

    // 3. Variables d'environnement:
    environment {
        // Récupère le Token Sonar depuis le gestionnaire de credentials de Jenkins
        SONAR_TOKEN = credentials('sonar-token')
    }

    // 4. Étapes: Le workflow de build
    stages {

        stage('Checkout') {
            steps {
                // Récupère le code du SCM configuré dans le job
                checkout scm
            }
        }

        stage('Build & Test') {
            steps {
                // Exécute 'verify' : compile, teste, ET vérifie
                // la couverture de 80% (grâce à votre pom.xml)
                sh "mvn clean verify"
            }
        }

        stage('SonarQube Analysis') {
            steps {
                // Utilise le nom du serveur SonarQube configuré dans Jenkins
                withSonarQubeEnv('SonarQube Local') {

                    // NOTE IMPORTANTE:
                    // On utilise 'http://sonarqube:9000'
                    // 'sonarqube' est le nom du service dans docker-compose.yml
                    // Docker gère la résolution DNS entre les conteneurs.

                    sh "mvn sonar:sonar \
                        -Dsonar.projectKey=logitrack-api \
                        -Dsonar.host.url=http://sonarqube:9000 \
                        -Dsonar.login=$SONAR_TOKEN"
                }
            }
        }

        stage('Quality Gate') {
            steps {
                // Attend le verdict de SonarQube
                timeout(time: 10, unit: 'MINUTES') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
    }

    // 5. Actions post-build (Rapports)
    post {
        always {
            junit 'target/surefire-reports/*.xml'
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            jacoco(execPattern: 'target/jacoco.exec', classPattern: 'target/classes', sourcePattern: 'src/main/java')
        }
        failure {
            echo "PIPELINE FAILED: Envoi de notification..."
            // mail to: '...'
        }
    }
}