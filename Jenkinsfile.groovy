pipeline {
        agent any
        environment {
                AWS_CRED=credentials('AWS-Account-Credentials')
                AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
                AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
                AWS_DEFAULT_REGION="ap-southeast-2"
        }
        stages {
            stage ("Checkout") {
                steps {
                  checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'refs/heads/master']],
                        doGenerateSubmoduleConfigurations: false,
                        extensions: [[
                            $class: 'SubmoduleOption',
                            disableSubmodules: false,
                            parentCredentials: false,
                            recursiveSubmodules: false,
                            reference: '',
                            trackingSubmodules: false
                        ]],
                        submoduleCfg: [],
                        userRemoteConfigs: [[url: 'https://github.com/rohitgabriel/aws-azure.git']]
                    ])
                }
            }
            stage("terraform init") {
                steps {
                    sh '''
                    /usr/local/bin/terraform init -input=false
                    '''
                }
            }
            stage("terraform plan") {
                steps {
                     sh '''
                     AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
                     AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
                     AWS_DEFAULT_REGION="ap-southeast-2"
                     /usr/local/bin/terraform plan -out=tfplan -input=false
                     '''
               }
            }
    //        stage("terraform apply") {
    //            steps {
    //                 sh '''
    //                 AWS_ACCESS_KEY_ID="${AWS_CRED_USR}"
    //                 AWS_SECRET_ACCESS_KEY="${AWS_CRED_PSW}"
    //                 AWS_DEFAULT_REGION="ap-southeast-2"
    //                 /usr/local/bin/terraform apply -input=false tfplan
    //                 '''
    //           }
    //        }
            stage("clean up") {
                steps {
                    sh '''
                    rm ./tfplan
                    '''
               }
            }
    }
}