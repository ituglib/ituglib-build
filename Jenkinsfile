pipeline {
	agent any
	options {
		buildDiscarder(logRotator(numToKeepStr: '2'))
	}
	stages {
		stage('refresh') {
			when {
				branch 'master'
			}
			steps {
				checkout([$class: 'GitSCM',
				    branches: [[name: '*/master']],
				    extensions: [
					[$class: 'CleanBeforeCheckout'],
					[$class: 'SubmoduleOption', disableSubmodules: false, parentCredentials: true,
						recursiveSubmodules: true, reference: '', trackingSubmodules: false]],
					doGenerateSubmoduleConfigurations: false, submoduleCfg: [],
				    userRemoteConfigs: [[url: '/home/git/ituglib-build.git']]])
			}
		}
	}
}

