pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk-21'
    }

    environment {
        SONAR_ORG = 'tmq2k'
        SONAR_PROJECT_PREFIX = 'yas'
    }

    stages {

        // ====================================================================
        // STAGE 1: Detect which services have changed
        // ====================================================================
        stage('Detect Changes') {
            steps {
                script {
                    def allServices = [
                        'common-library',
                        'product', 'cart', 'customer', 'inventory', 'location',
                        'media', 'order', 'payment', 'payment-paypal', 'promotion',
                        'rating', 'search', 'tax', 'webhook', 'sampledata',
                        'backoffice-bff', 'storefront-bff',
                        'recommendation', 'delivery'
                    ]

                    def changedFiles = []

                    // Primary: git diff against origin/main (works for PR branches)
                    try {
                        sh 'git fetch origin main:refs/remotes/origin/main'
                        def diffOutput = sh(
                            script: 'git diff --name-only origin/main...HEAD',
                            returnStdout: true
                        ).trim()
                        if (diffOutput) {
                            changedFiles = diffOutput.split('\n').toList()
                        }
                    } catch (e) {
                        echo "git diff failed: ${e.message}. Falling back to changeSets."
                    }

                    // Fallback: SCM changeSets (polling / non-PR builds)
                    if (changedFiles.isEmpty()) {
                        for (changeLogSet in currentBuild.changeSets) {
                            for (entry in changeLogSet) {
                                for (file in entry.affectedFiles) {
                                    changedFiles.add(file.path)
                                }
                            }
                        }
                    }

                    // No changes detected – skip build & test
                    if (changedFiles.isEmpty()) {
                        env.CHANGED_SERVICES = ''
                        echo "No service changes detected – skipping build & test."
                        return
                    }

                    // Shared module changes trigger a rebuild of every service
                    def sharedChanged = changedFiles.any {
                        it.startsWith('common-library/') ||
                        it == 'pom.xml' ||
                        it.startsWith('checkstyle/')
                    }

                    def changedServices = sharedChanged
                        ? allServices
                        : allServices.findAll { svc ->
                              changedFiles.any { it.startsWith("${svc}/") }
                          }

                    env.CHANGED_SERVICES = changedServices.join(',')
                    echo "Changed services: ${env.CHANGED_SERVICES ?: 'none'}"
                }
            }
        }

        // ====================================================================
        // STAGE 2: Security - scan secrets with Gitleaks
        // ====================================================================
        stage('Secret Scan (Gitleaks)') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                script {
                    def exitCode = sh(
                        script: '''
                            gitleaks detect \
                              --source . \
                              --config gitleaks.toml \
                              --no-git \
                              --gitleaks-ignore-path .gitleaksignore \
                              --report-format sarif \
                              --report-path gitleaks-report.sarif \
                              --redact \
                              --verbose
                        ''',
                        returnStatus: true
                    )

                    archiveArtifacts artifacts: 'gitleaks-report.sarif', allowEmptyArchive: true

                    if (exitCode != 0) {
                        error 'Gitleaks failed: potential secret(s) were detected. Check gitleaks-report.sarif artifact.'
                    }
                }
            }
        }

        // ====================================================================
        // STAGE 3: Build – compile classes for changed services (skip tests)
        // ====================================================================

        stage('Build') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                sh "mvn -f pom.xml clean install -pl ${env.CHANGED_SERVICES} -am -DskipTests"
            }
        }

        // ====================================================================
        // STAGE 4: Test – compile + run tests + generate coverage
        // ====================================================================
        stage('Test') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                sh "mvn -f pom.xml test jacoco:report -pl ${env.CHANGED_SERVICES} -am -Dmaven.test.failIfNoSpecifiedTests=false"
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                    script {
                        def services = env.CHANGED_SERVICES?.split(',') ?: []
                        def pattern = services
                            .findAll { it != 'common-library' }
                            .collect { "${it}/target/site/jacoco/jacoco.xml" }
                            .join(',')

                        if (pattern) {
                            recordCoverage(
                                tools: [[parser: 'JACOCO', pattern: pattern]],
                                id: 'jacoco', name: 'JaCoCo Coverage',
                                sourceCodeRetention: 'EVERY_BUILD',
                                qualityGates: [
                                    [threshold: 70.0, metric: 'LINE',   baseline: 'PROJECT', unstable: true],
                                    [threshold: 70.0, metric: 'BRANCH', baseline: 'PROJECT', unstable: true]
                                ]
                            )

                            if (currentBuild.result == 'UNSTABLE') {
                                error "Build failed: Code coverage is below the minimum 70% threshold!"
                            }
                        } else {
                            echo "No service coverage reports to check."
                        }
                    }
                }
            }
        }
        
        // ====================================================================
        // STAGE 5: Code quality scan with SonarCloud (PR Support added)
        // ====================================================================
        stage('Code Quality Scan (SonarCloud)') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                withSonarQubeEnv('SonarCloud') {
                    script {
                        def sonarOrg = (env.SONAR_ORG ?: '').trim()
                        def sonarProjectPrefix = (env.SONAR_PROJECT_PREFIX ?: 'yas').trim()

                        if (!sonarOrg || sonarOrg == 'CHANGE_ME_SONARCLOUD_ORG') {
                            error('SONAR_ORG is not configured. Update environment.SONAR_ORG in Jenkinsfile with your SonarCloud organization key.')
                        }

                        def services = (env.CHANGED_SERVICES ?: '')
                            .split(',')
                            .collect { it.trim() }
                            .findAll { it }

                        echo "SonarCloud target services: ${services.join(', ')}"

                        if (services.isEmpty()) {
                            echo 'No valid services found for SonarCloud scan.'
                            return
                        }

                        def failedServices = []

                        services.each { svc ->
                            if (!fileExists("${svc}/pom.xml")) {
                                echo "Skip ${svc}: pom.xml not found."
                            } else {
                                def sonarProjectKey = "${sonarOrg}_${sonarProjectPrefix}-${svc}"
                                def sonarProjectName = "${sonarProjectPrefix}-${svc}"
                                
                                echo "SonarCloud scan ${svc} -> projectKey=${sonarProjectKey}, projectName=${sonarProjectName}"

                                def sonarBranchParams = ""
                                if (env.CHANGE_ID) {
                                    sonarBranchParams = "-Dsonar.pullrequest.key=${env.CHANGE_ID} -Dsonar.pullrequest.branch=${env.CHANGE_BRANCH} -Dsonar.pullrequest.base=${env.CHANGE_TARGET}"
                                    echo "Detect PR Build: PR-${env.CHANGE_ID}"
                                } else if (env.BRANCH_NAME && env.BRANCH_NAME != 'main' && env.BRANCH_NAME != 'master') {
                                    sonarBranchParams = "-Dsonar.branch.name=${env.BRANCH_NAME}"
                                    echo "Detect Branch Build: ${env.BRANCH_NAME}"
                                }

                                def exitCode = sh(
                                    script: """
                                        mvn -f ${svc}/pom.xml org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
                                        -Dsonar.host.url=${SONAR_HOST_URL} \
                                        -Dsonar.organization=${sonarOrg} \
                                        -Dsonar.projectKey=${sonarProjectKey} \
                                        -Dsonar.projectName=${sonarProjectName} \
                                        -Dsonar.token=${SONAR_AUTH_TOKEN} \
                                        -Dsonar.qualitygate.wait=true \
                                        -Dsonar.qualitygate.timeout=300 \
                                        -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml \
                                        ${sonarBranchParams}
                                    """,
                                    returnStatus: true
                                )

                                if (exitCode != 0) {
                                    failedServices.add(svc)
                                }
                            }
                        }

                        if (!failedServices.isEmpty()) {
                            error("SonarCloud scan failed for: ${failedServices.join(', ')}")
                        }
                    }
                }
            }
        }

        // ====================================================================
        // STAGE 6: Security - scan dependencies with Snyk
        // ====================================================================
        stage('Dependency Scan (Snyk)') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                withCredentials([string(credentialsId: 'snyk-token', variable: 'SNYK_TOKEN')]) {
                    script {
                        sh 'snyk --version'
                        // Ensure Maven wrappers are executable on Linux agents.
                        sh 'find . -type f -name mvnw -exec chmod +x {} +'
                        
                        // Resolve the CI-friendly revision from the root POM.
                        def projectRevision = sh(
                            script: "sed -n 's:.*<revision>\\(.*\\)</revision>.*:\\1:p' pom.xml | head -n1",
                            returnStdout: true
                        ).trim()

                        if (!projectRevision) {
                            error('Unable to resolve <revision> from root pom.xml for Snyk Maven scan.')
                        }

                        echo 'Running Snyk scan from root using Maven aggregate project mode...'

                        // Use Maven aggregate mode for multi-module reactor builds.
                        def exitCode = sh(
                            script: """
                                snyk test \
                                --file=pom.xml \
                                --maven-aggregate-project \
                                --severity-threshold=high \
                                --sarif-file-output=snyk-report.sarif \
                                -- -Drevision=${projectRevision} -U
                            """,
                            returnStatus: true
                        )

                        archiveArtifacts artifacts: 'snyk-report.sarif', allowEmptyArchive: true

                        if (exitCode == 1) {
                            error('Snyk found vulnerabilities. Check snyk-report.sarif for details.')
                        } else if (exitCode > 1) {
                            error("Snyk execution failed with exit code ${exitCode}.")
                        }
                    }
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
