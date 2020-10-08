/* Ituglib_Build - Paste into this Pipeline Project Name. */

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    triggers {
        pollSCM('H * * * *')
    }
    stages {
        stage('refresh') {
            steps {
                checkout([$class: 'GitSCM',
                    branches: [[name: '*/master']],
                    extensions: [
                        [$class: 'CleanBeforeCheckout'],
                        [$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true,
                                recursiveSubmodules: true, reference: '', trackingSubmodules: false]],
                        doGenerateSubmoduleConfigurations: false, submoduleCfg: [],
                    userRemoteConfigs: [[url: 'https://github.com/ituglib/ituglib-build.git']]])
            }
        }
    }
    post {
        always {
            mail bcc: '', body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\nDuration: ${currentBuild.durationString}\nChange: ${currentBuild.changeSets}\n More info at: ${env.BUILD_URL}", cc: '', from: 'rsbecker@nexbridge.com', replyTo: '', subject: "[Jenkins HPITUG] ${currentBuild.currentResult}: job ${env.JOB_NAME}", to: 'rsbecker@nexbridge.com'
        }
    }
}

