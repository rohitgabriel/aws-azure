pipeline {
    agent any
    environment {
        AWS_CRED=credentials('AWSNTT-Account-Credentials')
        AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
        AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
        AWS_DEFAULT_REGION="ap-southeast-2"
    }
    stages {
        stage('Checkout repo') {
            steps {
                git branch: 'master',
                credentialsId: 'mygitcredid',
                url: 'https://github.com/rohitgabriel/aws-azure.git'
            }
        }
        stage("terraform init") {
            steps {
                sh '/usr/local/bin/terraform init -input=false'
            }
        }
        stage("terraform plan") {
            steps {
                sh '''
                AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
                AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
                AWS_DEFAULT_REGION="ap-southeast-2"
                /usr/local/bin/terraform plan -out=tfplan -input=false
                '''
            }
        }
        stage("terraform apply") {
            steps {
                sh '''
                pwd
                '''
            }
        }
        stage("clean up") {
            steps {
                sh '''
                pwd
                '''
            }
        }
    }
}