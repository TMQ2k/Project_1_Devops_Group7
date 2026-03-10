pipeline {
    agent any

    tools {
        maven 'maven'
        jdk 'jdk-21'
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
        // STAGE 2: Test – compile + run tests + generate coverage
        // ====================================================================
        stage('Test') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                sh "mvn -f pom.xml clean verify jacoco:report -pl ${env.CHANGED_SERVICES} -am -Dmaven.test.failIfNoSpecifiedTests=false"
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                    recordCoverage(
                        tools: [[parser: 'JACOCO']],
                        id: 'jacoco', name: 'JaCoCo Coverage',
                        sourceCodeRetention: 'EVERY_BUILD',
                        qualityGates: [
                            [threshold: 70.0, metric: 'LINE',   baseline: 'PROJECT', unstable: true],
                            [threshold: 70.0, metric: 'BRANCH', baseline: 'PROJECT', unstable: true]
                        ]
                    )
                    script {
                        if (currentBuild.result == 'UNSTABLE') {
                            error "Build failed: Code coverage is below the minimum 70% threshold!"
                        }
                    }
                }
            }
        }

        // ====================================================================
        // STAGE 3: Build – package JARs reusing compiled classes from Test
        // ====================================================================
        stage('Build') {
            when {
                expression { return env.CHANGED_SERVICES?.trim() }
            }
            steps {
                sh "mvn -f pom.xml package -pl ${env.CHANGED_SERVICES} -am -DskipTests"
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
