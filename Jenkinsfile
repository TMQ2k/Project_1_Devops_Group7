pipeline {
    agent any

    stages {


        stage('Product Service') {
            when {
                anyOf {
                    changeset "product/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Product') {
                    steps {
                        dir('product') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl product -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'product/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'product/target/jacoco.exec',
                                classPattern:     'product/target/classes',
                                sourcePattern:    'product/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Product') {
                    steps {
                        dir('product') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl product -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Cart Service') {
            when {
                anyOf {
                    changeset "cart/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Cart') {
                    steps {
                        dir('cart') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl cart -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'cart/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'cart/target/jacoco.exec',
                                classPattern:     'cart/target/classes',
                                sourcePattern:    'cart/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Cart') {
                    steps {
                        dir('cart') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl cart -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Customer Service') {
            when {
                anyOf {
                    changeset "customer/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Customer') {
                    steps {
                        dir('customer') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl customer -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'customer/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'customer/target/jacoco.exec',
                                classPattern:     'customer/target/classes',
                                sourcePattern:    'customer/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Customer') {
                    steps {
                        dir('customer') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl customer -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Inventory Service') {
            when {
                anyOf {
                    changeset "inventory/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Inventory') {
                    steps {
                        dir('inventory') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl inventory -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'inventory/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'inventory/target/jacoco.exec',
                                classPattern:     'inventory/target/classes',
                                sourcePattern:    'inventory/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Inventory') {
                    steps {
                        dir('inventory') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl inventory -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Location Service') {
            when {
                anyOf {
                    changeset "location/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Location') {
                    steps {
                        dir('location') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl location -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'location/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'location/target/jacoco.exec',
                                classPattern:     'location/target/classes',
                                sourcePattern:    'location/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Location') {
                    steps {
                        dir('location') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl location -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Media Service') {
            when {
                anyOf {
                    changeset "media/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Media') {
                    steps {
                        dir('media') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl media -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'media/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'media/target/jacoco.exec',
                                classPattern:     'media/target/classes',
                                sourcePattern:    'media/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Media') {
                    steps {
                        dir('media') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl media -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Order Service') {
            when {
                anyOf {
                    changeset "order/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Order') {
                    steps {
                        dir('order') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl order -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'order/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'order/target/jacoco.exec',
                                classPattern:     'order/target/classes',
                                sourcePattern:    'order/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Order') {
                    steps {
                        dir('order') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl order -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Payment Service') {
            when {
                anyOf {
                    changeset "payment/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Payment') {
                    steps {
                        dir('payment') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl payment -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'payment/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'payment/target/jacoco.exec',
                                classPattern:     'payment/target/classes',
                                sourcePattern:    'payment/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Payment') {
                    steps {
                        dir('payment') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl payment -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Payment-Paypal Service') {
            when {
                anyOf {
                    changeset "payment-paypal/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Payment-Paypal') {
                    steps {
                        dir('payment-paypal') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl payment-paypal -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'payment-paypal/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'payment-paypal/target/jacoco.exec',
                                classPattern:     'payment-paypal/target/classes',
                                sourcePattern:    'payment-paypal/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Payment-Paypal') {
                    steps {
                        dir('payment-paypal') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl payment-paypal -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Promotion Service') {
            when {
                anyOf {
                    changeset "promotion/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Promotion') {
                    steps {
                        dir('promotion') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl promotion -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'promotion/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'promotion/target/jacoco.exec',
                                classPattern:     'promotion/target/classes',
                                sourcePattern:    'promotion/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Promotion') {
                    steps {
                        dir('promotion') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl promotion -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Rating Service') {
            when {
                anyOf {
                    changeset "rating/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Rating') {
                    steps {
                        dir('rating') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl rating -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'rating/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'rating/target/jacoco.exec',
                                classPattern:     'rating/target/classes',
                                sourcePattern:    'rating/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Rating') {
                    steps {
                        dir('rating') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl rating -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Search Service') {
            when {
                anyOf {
                    changeset "search/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Search') {
                    steps {
                        dir('search') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl search -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'search/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'search/target/jacoco.exec',
                                classPattern:     'search/target/classes',
                                sourcePattern:    'search/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Search') {
                    steps {
                        dir('search') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl search -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Tax Service') {
            when {
                anyOf {
                    changeset "tax/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Tax') {
                    steps {
                        dir('tax') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl tax -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'tax/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'tax/target/jacoco.exec',
                                classPattern:     'tax/target/classes',
                                sourcePattern:    'tax/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Tax') {
                    steps {
                        dir('tax') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl tax -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Webhook Service') {
            when {
                anyOf {
                    changeset "webhook/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Webhook') {
                    steps {
                        dir('webhook') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl webhook -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'webhook/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'webhook/target/jacoco.exec',
                                classPattern:     'webhook/target/classes',
                                sourcePattern:    'webhook/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Webhook') {
                    steps {
                        dir('webhook') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl webhook -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Sampledata Service') {
            when {
                anyOf {
                    changeset "sampledata/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Sampledata') {
                    steps {
                        dir('sampledata') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl sampledata -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'sampledata/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'sampledata/target/jacoco.exec',
                                classPattern:     'sampledata/target/classes',
                                sourcePattern:    'sampledata/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Sampledata') {
                    steps {
                        dir('sampledata') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl sampledata -am -DskipTests'
                        }
                    }
                }
            }
        }

        // ====================================================================
        // BFF SERVICES (Spring Cloud Gateway - Maven + mvnw)
        // ====================================================================

        stage('Backoffice-BFF Service') {
            when {
                anyOf {
                    changeset "backoffice-bff/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Backoffice-BFF') {
                    steps {
                        dir('backoffice-bff') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl backoffice-bff -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'backoffice-bff/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'backoffice-bff/target/jacoco.exec',
                                classPattern:     'backoffice-bff/target/classes',
                                sourcePattern:    'backoffice-bff/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Backoffice-BFF') {
                    steps {
                        dir('backoffice-bff') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl backoffice-bff -am -DskipTests'
                        }
                    }
                }
            }
        }

        stage('Storefront-BFF Service') {
            when {
                anyOf {
                    changeset "storefront-bff/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Storefront-BFF') {
                    steps {
                        dir('storefront-bff') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl storefront-bff -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'storefront-bff/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'storefront-bff/target/jacoco.exec',
                                classPattern:     'storefront-bff/target/classes',
                                sourcePattern:    'storefront-bff/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Storefront-BFF') {
                    steps {
                        dir('storefront-bff') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl storefront-bff -am -DskipTests'
                        }
                    }
                }
            }
        }

        // ====================================================================
        // RECOMMENDATION SERVICE (không có mvnw riêng, dùng mvnw từ product)
        // ====================================================================

        stage('Recommendation Service') {
            when {
                anyOf {
                    changeset "recommendation/**"
                    changeset "common-library/**"
                    changeset "pom.xml"
                    changeset "checkstyle/**"
                }
            }
            stages {
                stage('Test Recommendation') {
                    steps {
                        dir('product') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean test -pl recommendation -am -Dmaven.test.failure.ignore=true'
                        }
                    }
                    post {
                        always {
                            junit testResults: 'recommendation/target/surefire-reports/*.xml', allowEmptyResults: true
                            jacoco(
                                execPattern:      'recommendation/target/jacoco.exec',
                                classPattern:     'recommendation/target/classes',
                                sourcePattern:    'recommendation/src/main/java',
                                inclusionPattern: '**/*.class'
                            )
                        }
                    }
                }
                stage('Build Recommendation') {
                    steps {
                        dir('product') {
                            sh 'chmod +x ./mvnw'
                            sh './mvnw -f ../pom.xml clean package -pl recommendation -am -DskipTests'
                        }
                    }
                }
            }
        }

        // ====================================================================
        // FRONTEND SERVICES (Next.js - npm)
        // ====================================================================

        stage('Backoffice Frontend') {
            when {
                changeset "backoffice/**"
            }
            stages {
                stage('Test Backoffice') {
                    steps {
                        dir('backoffice') {
                            sh 'npm ci'
                            sh 'npm run lint'
                        }
                    }
                }
                stage('Build Backoffice') {
                    steps {
                        dir('backoffice') {
                            sh 'npm run build'
                        }
                    }
                }
            }
        }

        stage('Storefront Frontend') {
            when {
                changeset "storefront/**"
            }
            stages {
                stage('Test Storefront') {
                    steps {
                        dir('storefront') {
                            sh 'npm ci'
                            sh 'npm run lint'
                        }
                    }
                }
                stage('Build Storefront') {
                    steps {
                        dir('storefront') {
                            sh 'npm run build'
                        }
                    }
                }
            }
        }

    }
}
