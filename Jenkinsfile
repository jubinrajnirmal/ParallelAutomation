pipeline {
    agent any

    triggers {
        // GitHub webhook triggers build on commit
        pollSCM('* * * * *')

        // Daily scheduled to run tests at 12PM
        cron('H 12 * * *')
    }

    stages {

        stage('Start Selenium Grid') {
            steps {
                dir('G:\\selenium') {
                    bat 'start cmd /c "java -jar selenium-server-4.35.0.jar hub"'
                    bat 'start cmd /c "java -jar selenium-server-4.35.0.jar node --config node.toml --detect-drivers false"'
                }
                echo '✅ Selenium Grid Hub + Nodes started'
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/jubinrajnirmal/ParallelAutomation.git'
            }
        }

        stage('Run Tests') {
            steps  {
        		catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
            		bat 'mvn clean test'
        }
        }
}
        stage('Archive Reports') {
            steps {
                archiveArtifacts artifacts: 'target/cucumber-reports.html, target/cucumber-reports.json, target/cucumber-reports.xml, test-output/**', fingerprint: true
            }
        }

        stage('Publish HTML Report') {
            steps {
                publishHTML([allowMissing: false,
                             alwaysLinkToLastBuild: true,
                             keepAll: true,
                             reportDir: 'target',
                             reportFiles: 'cucumber-reports.html',
                             reportName: 'Cucumber HTML Report'])
            }
        }
    }

    post {
        always {
            emailext(
                to: 'jubinrajnirmal10@gmail.com',
                subject: "Automation Report - Build ${currentBuild.fullDisplayName} [${currentBuild.currentResult}]",
                body: """Hi,

The build finished with: ${currentBuild.currentResult}
Build URL: ${env.BUILD_URL}

Report attached.
""",
                attachmentsPattern: 'target/cucumber-reports.html'
            )
        }
    }
}
