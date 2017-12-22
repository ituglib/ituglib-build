#!/bin/sh

echo Creating Jenkins tag in git ${GIT_URL}

echo git tag -a --file=- jenkins/${BUILD_TAG} ${GIT_COMMIT}
git tag -a --file=- jenkins/${BUILD_TAG} ${GIT_COMMIT} <<ANNOTATION
url: ${BUILD_URL}
job: ${JOB_NAME}
workspace: ${WORKSPACE}
instance: ${JENKINS_URL}
git: ${GIT_URL}
ANNOTATION

echo git push origin refs/tags/jenkins/${BUILD_TAG}:refs/tags/jenkins/${BUILD_TAG}
git push origin refs/tags/jenkins/${BUILD_TAG}:refs/tags/jenkins/${BUILD_TAG}
exit $?
