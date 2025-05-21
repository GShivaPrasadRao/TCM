pipeline {
    agent any

    triggers {
        cron('H H * * *') // Daily at a different hour on each node (for randomness/load balancing)
    }

    parameters {
        choice(name: 'PROJECT_NAME', choices: ['Select All', 'KF_Insights', 'KF_MUX', 'KF_ML'], description: 'Select Project or All')
        choice(name: 'ENVIRONMENT', choices: ['dev', 'qa', 'demo', 'prod'], description: 'Select Environment')
    }

    environment {
        TEAMS_WEBHOOK = 'YOUR_TEAMS_WEBHOOK_URL' // <-- Replace with actual webhook
    }

    stages {
        stage('Checkout Code') {
            steps {
                git credentialsId: 'gitlab-credentials-id', url: 'https://gitlab.com/your-org/CK_APITesting.git', branch: 'main'
            }
        }

        stage('Install Newman') {
            steps {
                sh 'npm install -g newman'
            }
        }

        stage('Run API Tests') {
            steps {
                script {
                    def projectsToRun = []
                    if (params.PROJECT_NAME == 'Select All') {
                        projectsToRun = ['KF_Insights', 'KF_MUX', 'KF_ML']
                    } else {
                        projectsToRun = [params.PROJECT_NAME]
                    }

                    for (proj in projectsToRun) {
                        def collectionFile = sh(
                            script: "ls ${proj}/collections/*.json | head -1",
                            returnStdout: true
                        ).trim()

                        def envFile = "${proj}/environments/${params.ENVIRONMENT.toLowerCase()}_env.json"

                        echo "Running ${proj} collection..."
                        sh """
                            newman run "${collectionFile}" -e "${envFile}" --reporters cli,html --reporter-html-export ${proj}-${params.ENVIRONMENT}-report.html
                        """
                    }
                }
            }
        }

        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: '*-report.html', fingerprint: true
            }
        }
    }

    post {
        success {
            script {
                def msg = "✅ API Tests Passed for *${params.PROJECT_NAME}* in *${params.ENVIRONMENT.toUpperCase()}* environment."
                sendToTeams(msg)
            }
        }

        failure {
            script {
                def msg = "❌ API Tests *FAILED* for *${params.PROJECT_NAME}* in *${params.ENVIRONMENT.toUpperCase()}* environment.\nCheck Jenkins for details."
                sendToTeams(msg)
            }
        }
    }
}

def sendToTeams(message) {
    def payload = """{
        "text": "${message}"
    }"""

    httpRequest httpMode: 'POST',
                contentType: 'APPLICATION_JSON',
                requestBody: payload,
                url: env.TEAMS_WEBHOOK
}
