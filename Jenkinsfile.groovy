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
                sh """
                /usr/local/bin/terraform init -input=false
                """
            }
        }
        stage("terraform plan") {
            steps {
                sh '''
                export TF_VAR_AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
                export TF_VAR_AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
                export TF_VAR_AWS_DEFAULT_REGION="ap-southeast-2"
                /usr/local/bin/terraform plan -out=tfplan -input=false
                '''
            }
        }
        stage("Approval required") {
            input {
            message "Apply or Abort?"
            }
        }
        stage("terraform apply") {
            steps {
                sh '''
                export TF_VAR_AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
                export TF_VAR_AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
                export TF_VAR_AWS_DEFAULT_REGION="ap-southeast-2"
                /usr/local/bin/terraform apply -input=false tfplan
                '''
            }
        }
        stage("clean up") {
            steps {
                sh '''
                rm ./tfplan
                '''
            }
        }
    }
}