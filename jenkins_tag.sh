#!/bin/sh

echo Creating Jenkins tag in git ${GIT_URL}

git tag -a --file=- jenkins/${BUILD_TAG} ${GIT_COMMIT} <<ANNOTATION
url: ${BUILD_URL}
job: ${JOB_NAME}
workspace: ${WORKSPACE}
instance: ${JENKINS_URL}
git: ${GIT_URL}
ANNOTATION
