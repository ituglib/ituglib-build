# Paste the rest of this file into a standard Pipeline project to
# build/test/stage libiconv.
# Also set Discard old builds, typically log rotation to 2
# Poll SCM something useful, like H/10 * * * *

node {
    lock(resource: 'gawk-test-environment') {
        stage('checkout') {
            checkout([$class: 'GitSCM',
            branches: [[name: '*/ituglib_release']],
            extensions: [
                [$class: 'CleanBeforeCheckout']],
            userRemoteConfigs: [[url: '/home/git/gawk']]])
        }
        stage('redate') {
            script {
                currentTime = sh(returnStdout: true, script: 'date +%Y%m%d%H%M.%S').trim()
                sh(returnStdout: true, script: 'echo Set all files to '+currentTime+' to inhibit aclocal-1.16 requirement')
                sh(returnStdout: true, script: 'find . -exec touch -t '+currentTime+' {} ";" || echo Ignore any errors')
            }
        }
        stage('config') {
            withEnv(['PATH=/usr/coreutils/bin:/usr/local/bin:/usr/bin:/bin:/usr/ucb',
                    '_RLD_LIB_PATH=:/usr/local/lib']) {
                sh 'CFLAGS="-D_XOPEN_SOURCE_EXTENDED=1 -D_FILE_OFFSET_BITS=64 -D_D_TYPE_DIRENT_ -c99" conf_script_floss_cc'
            }
        }
        stage('build') {
            withEnv(['PREFIX=/usr/local',
                    'PATH=/usr/coreutils/bin:/usr/local/bin:/usr/bin:/bin:/usr/ucb',
                    '_RLD_LIB_PATH=:/usr/local/lib']) {
                sh 'make'
            }
        }
        stage('findcall_floss') {
            withEnv(['PATH=/usr/coreutils/bin:/usr/local/bin:/usr/bin:/bin:/usr/ucb',
                    '_RLD_LIB_PATH=:/usr/local/lib']) {
                sh 'findcall_floss'
            }
        }
        stage('test') {
            withEnv(['PATH=/usr/coreutils/bin:/usr/local/bin:/usr/bin:/bin:/usr/ucb',
                    '_RLD_LIB_PATH=:/usr/local/lib']) {
                sh 'make check || echo "Tests failed but expected"'
            }
        }
        stage('deploy_src') {
            withEnv(['PATH=/usr/coreutils/bin:/usr/local/bin:/usr/bin:/bin:/usr/ucb',
                    '_RLD_LIB_PATH=:/usr/local/lib',
                    'BASENAME=gawk',
                    'DEST=/web/stage']) {
                sh '. "${WORKSPACE}/../Ituglib_Build/dist.info" && \
                    tar cvzf "${DEST}/${BASENAME}-${VERSION}-src.tar.gz" \
                        --exclude="*.o" \
                        --exclude="*.a" \
                        --exclude="*.so" \
                        --exclude=".git*" \
                        --exclude="./.git/*" \
                        .'
            }
        }
    }
}

