#!/usr/coreutils/bin/bash

PROGNAME=$0

set -eo pipefail

exec 2>&1

Usage() {
	echo usage: $PROGNAME src-directory dest-archive
	exit 1
}

echo "Creating $2 from $1:"
ls -ld $1
if [ "$PACKAGE_WITH_SUDO" = "true" ]; then
	cd $1 && sudo tar czf $2 . 2>&1
else
	cd $1 && tar czf $2 . 2>&1
fi

echo "Archive:"
ls -l $2
