pipeline {
    agent any
    environment {
        AWS_CRED=credentials('AWSNTT-Account-Credentials')
        AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
        AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
        AWS_DEFAULT_REGION="ap-southeast-2"
    }
    stages {
        // stage('Checkout repo') {
        //     steps {
        //         git branch: 'master',
        //         credentialsId: 'mygitcredid',
        //         url: 'https://github.com/rohitgabriel/aws-azure.git'
        //     }
        // }
        stage("Packer build") {
            steps {
                sh '''
                export AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
                export AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
                export AWS_DEFAULT_REGION="ap-southeast-2"
                ./build-image-with-packer.sh
                '''
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
            steps {
                echo 'Terraform state is maintained at app.terraform.io, org: int, workspace: demo'
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
        stage("Refresh Instance in ASG") {
            steps {
                withAWS(credentials: 'TerraformAWSCreds', region: 'ap-southeast-2') {
                sh './refresh-asg.sh'
              }
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