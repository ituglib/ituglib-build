#!/bin/sh

echo "Creating $2"
ls -ld $1
cd $1 && sudo tar cvzf $2 . 2>&1
RESULT=$?
ls -ld $2
exit ${RESULT}
