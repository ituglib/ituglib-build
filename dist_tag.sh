#!/bin/sh

if [ "$1" = "" ]; then
	echo "Usage: $0 platform"
	exit 1
fi
export PLATFORM=$1

echo Creating Distribution tag in git ${GIT_URL}

echo git tag -a --file=- dist/${PLATFORM}/${BUILD_TAG} ${GIT_COMMIT}
git tag -a --file=- dist/${PLATFORM}/${BUILD_TAG} ${GIT_COMMIT} <<ANNOTATION
platform: ${PLATFORM}
url: ${BUILD_URL}
job: ${JOB_NAME}
workspace: ${WORKSPACE}
instance: ${JENKINS_URL}
git: ${GIT_URL}
ANNOTATION

echo git push origin refs/tags/dist/${PLATFORM}/${BUILD_TAG}:refs/tags/dist/${PLATFORM}/${BUILD_TAG}
git push origin refs/tags/dist/${PLATFORM}/${BUILD_TAG}:refs/tags/dist/${PLATFORM}/${BUILD_TAG}
exit $?
