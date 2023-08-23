#!/usr/coreutils/bin/bash

set -eo pipefail

echo "Creating $2"
ls -ld $1
if [ "$PACKAGE_WITH_SUDO" = "true" ]; then
	cd $1 && sudo tar czf $2 . 2>&1
else
	cd $1 && tar czf $2 . 2>&1
endif

ls -l $2
