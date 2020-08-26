/* Gawk deploy pipeline. */

pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }
    stages {
        stage('src') {
            steps {
                lock('gawk-deploy') {
                    withEnv(['PATH=/usr/local/bin:/usr/coreutils/bin:/usr/bin:/bin:/usr/ucb',
                            '_RLD_LIB_PATH=/usr/tandem/jdbcMx/current/lib:/usr/local/lib',
                            'GROOVY_PATH=/home/jenkinstag/.sdkman/candidates/groovy/current/bin/',
                            'BASENAME=gawk',
                            'DIST=/web/stage',
                            'DEST=/web/opensrc',
                            'SCHEMA=ITUG.OPENLIB',
                            'SUFFIX=-src',
                            'TYPE=src']) {
                        sh 'sudo rm -rf /tmp/${BASENAME}'
                        sh 'cd "${WORKSPACE}/../../Ituglib_Build/workspace" && \
                            ${GROOVY_PATH}/groovy normal.tar.groovy'
                    }
                }
            }
        }
        stage('nse') {
            steps {
                lock('gawk-deploy') {
                    withEnv(['PATH=/usr/local/bin:/usr/coreutils/bin:/usr/bin:/bin:/usr/ucb',
                            '_RLD_LIB_PATH=/usr/tandem/jdbcMx/current/lib:/usr/local/lib',
                            'GROOVY_PATH=/home/jenkinstag/.sdkman/candidates/groovy/current/bin/',
                            'BASENAME=gawk',
                            'DIST=/web/stage',
                            'DEST=/web/opensrc',
                            'SCHEMA=ITUG.OPENLIB',
                            'SUFFIX=-nse',
                            'TYPE=TNS/E',
                            'NONSTOP_EXTENSIONS=L19.08/J06.22 and above only',
                            'DEPENDENCIES=Check Readme file for older RVUs']) {
                        sh 'sudo rm -rf /tmp/${BASENAME}'
                        sh 'cd "${WORKSPACE}/../../Ituglib_Build/workspace" && \
                            ${GROOVY_PATH}/groovy platform.tar.groovy'
                    }
                }
            }
        }
        stage('nsx') {
            steps {
                lock('gawk-deploy') {
                    withEnv(['PATH=/usr/local/bin:/usr/coreutils/bin:/usr/bin:/bin:/usr/ucb',
                            '_RLD_LIB_PATH=/usr/tandem/jdbcMx/current/lib:/usr/local/lib',
                            'GROOVY_PATH=/home/jenkinstag/.sdkman/candidates/groovy/current/bin/',
                            'BASENAME=gawk',
                            'DIST=/web/stage',
                            'DEST=/web/opensrc',
                            'SCHEMA=ITUG.OPENLIB',
                            'SUFFIX=-nsx',
                            'TYPE=TNS/X',
                            'NONSTOP_EXTENSIONS=L19.08/J06.22 and above only',
                            'DEPENDENCIES=Check Readme file for older RVUs']) {
                        sh 'sudo rm -rf /tmp/${BASENAME}'
                        sh 'cd "${WORKSPACE}/../../Ituglib_Build/workspace" && \
                            ${GROOVY_PATH}/groovy platform.tar.groovy'
                    }
                }
            }
        }
    }
    post {
        always {
            mail bcc: '', body: "${currentBuild.currentResult}: Job ${env.JOB_NAME} build ${env.BUILD_NUMBER}\nDuration: ${currentBuild.durationString}\nChange: ${currentBuild.changeSets}\n More info at: ${env.BUILD_URL}", cc: '', from: 'rsbecker@nexbridge.com', replyTo: '', subject: "[Jenkins Stage HPITUG] ${currentBuild.currentResult}: job ${env.JOB_NAME}", to: 'rsbecker@nexbridge.com'
        }
    }
}
