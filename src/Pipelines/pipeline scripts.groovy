pipeline {
    agent any
    triggers {
        cron('H 11 * * *')
    }
    stages {
        stage('Cleanup') {
            steps {
                deleteDir()
            }
        }
        stage('Copy Code from ProjectConnectSync') {
            steps {
                copyArtifacts filter: '**/*', projectName: 'ProjectConnectSync', selector: lastSuccessful()
            }
        }
        stage('Run CleanUpConnectorsandProject') {
            steps {
                sh 'mvn test -Dcucumber-tags=@CleanUpConnectorsandProject'
            }
        }
        stage('Publish Report') {
            steps {
                cucumber buildStatus: 'UNSTABLE', fileIncludePattern: '**/cucumber.json', sortingMethod: 'ALPHABETICAL'
            }
        }
    }
    post {
        always {
            script {
                currentBuild.result = currentBuild.result ?: 'SUCCESS'
                emailext subject: "Jenkins Job: ${currentBuild.fullDisplayName}",
                         body: "Status: ${currentBuild.result}",
                         to: 'your-email@example.com'
            }
        }
    }
}
    