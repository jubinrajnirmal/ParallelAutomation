pipeline {
    agent any

    triggers {
        // GitHub webhook triggers build on commit
        pollSCM('* * * * *')

        // Daily scheduled to run tests at 12PM
        cron('H 12 * * *')
    }

    stages {

        stage('Check Selenium Grid') {
            steps {
                script {
                    try {
                        def gridCheck = bat(
                            script: 'powershell -Command "(Invoke-WebRequest -Uri http://localhost:4444/status).Content"',
                            returnStdout: true
                        ).trim()

                        if (gridCheck.contains('"ready":true')) {
                            echo '✅ Selenium Grid is UP and ready'
                        } else {
                            echo '⚠️ Selenium Grid responded but not ready'
                        }
                    } catch (Exception e) {
                        echo '⚠️ Could not reach Selenium Grid at http://localhost:4444/status — continuing anyway'
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
                    // Run Maven, show output in Jenkins console, and also write to cucumber_output.txt
                    powershell """
                        mvn clean test 2>&1 | Tee-Object -FilePath cucumber_output.txt
                    """
                }

                script {
                    // Extract all cucumber report links safely
                    def reportLinks = powershell(
                        returnStdout: true,
                        script: """
                            Select-String -Path cucumber_output.txt -Pattern 'https://reports.cucumber.io/reports/' |
                            ForEach-Object { \$_.Matches.Value }
                        """
                    ).trim().split("\\r?\\n")

                    if (reportLinks && reportLinks.size() > 0) {
                        env.CUCUMBER_REPORTS = reportLinks.join("\n")
                        echo "Found Cucumber reports:\n${env.CUCUMBER_REPORTS}"
                    } else {
                        env.CUCUMBER_REPORTS = "No cucumber report links found."
                        echo "No cucumber report links found in test output."
                    }
                }
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

Cucumber Cloud Reports:
${env.CUCUMBER_REPORTS}
""",
                attachLog: false
            )
        }
    }
}
