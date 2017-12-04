#!/bin/sh
# Shell script to change the version string of a product to remove or change

Usage() {
	echo "usage: $0 versionfile versionkey suffix [new-string]"
	exit 1
}

if [ "$1" = "" -o "$2" = "" -o "$3" = "" ]; then
	Usage
fi

VERSIONFILE=$1
VERSIONKEY=$2
SUFFIX=$3
REPLACE=$4

if [ "$REPLACE" = "" ]; then
	ed $1 << FIXME
g/${VERSIONKEY}/s/${SUFFIX}//g
g/${VERSIONKEY}/
w
q
FIXME

else
	ed $1 << FIXME
g/${VERSIONKEY}/s/${SUFFIX}/${REPLACE}/g
g/${VERSIONKEY}/
w
q
FIXME

fi

exit $?
