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
                script {
                    try {
                        def response = httpRequest url: 'http://localhost:4444/status'
                        if (response.status == 200 && response.content.contains('"ready":true')) {
                            echo '✅ Selenium Grid is up and ready'
                        } else {
                            error('❌ Selenium Grid is not ready')
                        }
                    } catch (Exception e) {
                        error("❌ Could not reach Selenium Grid at http://localhost:4444/status")
                    }
                }
            }
        }

        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/jubinrajnirmal/ParallelAutomation.git'
            }
        }

        stage('Run Tests') {
            steps {
                catchError(buildResult: 'UNSTABLE', stageResult: 'FAILURE') {
                    // Save console output to file
                    bat 'mvn clean test > cucumber_output.txt'
                }

                script {
                    // Extract all cucumber report links
                    def reportLinks = powershell(
                        returnStdout: true,
                        script: """
                            Select-String -Path cucumber_output.txt -Pattern 'https://reports.cucumber.io/reports/' |
                            ForEach-Object { $_.Matches.Value }
                        """
                    ).trim().split("\\r?\\n")

                    if (reportLinks && reportLinks.size() > 0) {
                        env.CUCUMBER_REPORTS = reportLinks.join("\\n")
                        echo "Found Cucumber reports:\n${env.CUCUMBER_REPORTS}"
                    } else {
                        env.CUCUMBER_REPORTS = "No cucumber report links found."
                        echo "No cucumber report links found in test output."
                    }
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
    } // ✅ closing stages

    post {
        always {
            emailext(
                to: 'jubinrajnirmal10@gmail.com',
                subject: "Automation Report - Build ${currentBuild.fullDisplayName} [${currentBuild.currentResult}]",
                body: """Hi,

The build finished with: ${currentBuild.currentResult}
Build URL: ${env.BUILD_URL}

Cucumber Cloud Reports:
${env.CUCUMBER_REPORTS}
""",
                attachLog: false
            )
        }
    }
}
