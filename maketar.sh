#!/bin/sh

echo "Creating $2"
ls -ld $1
cd $1 && sudo tar czf $2 . 2>&1
RESULT=$?
ls -l $2
exit ${RESULT}
