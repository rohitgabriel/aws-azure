pipeline {
    agent any
    environment {
        AWS_CRED=credentials('AWS-Account-Credentials')
        AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
        AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
        AWS_DEFAULT_REGION="ap-southeast-2"
    }
    stages {
        stage("Checkout repo") {
            steps {
                checkout([
                    $class: 'GITSCM'
                    branches: [[name: 'refs/head/master']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [[
                        $class: 'SubmoduleOption',
                        disableSubmodules: false,
                        parentCredentials: false,
                        recursiveSubmodules: false,
                        reference: '',
                        trackingSubmodules: false
                    ]],
                    submoduleCfg: [],
                    userRemoteConfigs: [[url: 'https://github.com/rohitgabriel/aws-azure.git']]
                ])
            }
        }
        stage("test") {
            steps {
                sh '''
                pwd
                '''
            }
        }
        stage("test") {
            steps {
                sh '''
                pwd
                '''
            }
        }
        stage("test") {
            steps {
                sh '''
                pwd
                '''
            }
        }
        stage("test") {
            steps {
                sh '''
                pwd
                '''
            }
        }
    }
}