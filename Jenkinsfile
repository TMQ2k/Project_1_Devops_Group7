pipeline {
    agent any

    tools{
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
                    // All services managed by this pipeline (add new services here)
                    def allServices = [
                        'common-library',
                        'product', 'cart', 'customer', 'inventory', 'location',
                        'media', 'order', 'payment', 'payment-paypal', 'promotion',
                        'rating', 'search', 'tax', 'webhook', 'sampledata',
                        'backoffice-bff', 'storefront-bff',
                        'recommendation', 'delivery'
                    ]

                    // Detect changed files: prefer git diff against main,
                    // fall back to SCM changeSets, then build everything
                    def changedFiles = []

                    // Primary: git diff against origin/main (works for PR branches)
                    // Must fetch main first – Jenkins only checks out the PR branch
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

                    // No changes detected
                    if (changedFiles.isEmpty()) {
                            env.CHANGED_SERVICES = ''
                            echo "No service changes detected – skipping build & test."
                        }
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

                // ====================================================================
            // STAGE 2: Test – compile + run tests + generate coverage
            //   • `-pl svc1,svc2,... -am` tests only what changed + deps
            //   • No `clean` in build stage → reuse compiled artifacts
            // ====================================================================
            stage('Test') {
                when {
                    expression { return env.CHANGED_SERVICES?.trim() }
                }
                steps {
                    sh "mvn -f pom.xml clean test jacoco:report -pl ${env.CHANGED_SERVICES} -am -Dmaven.test.failIfNoSpecifiedTests=false"
                }
                post {
                    always {
                        // Aggregate JUnit results across all tested services
                        junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true

                        // Aggregate JaCoCo coverage – single report, shared thresholds
                        jacoco(
                            execPattern:           '**/target/jacoco.exec',
                            classPattern:          '**/target/classes',
                            sourcePattern:         '**/src/main/java',
                            inclusionPattern:      '**/*.class',
                            changeBuildStatus:     true,
                            minimumLineCoverage:   '70',
                        )
                    }
                }
            }

            // ====================================================================
            // STAGE 3: Build – package JARs reusing compiled classes from Test
            //   • No `clean` → reuse artifacts already compiled in Test stage
            //   • `-DskipTests` → tests already passed
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
