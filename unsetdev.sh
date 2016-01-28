#!/bin/sh
# Shell script to change the version string of a product to remove 

Usage() {
	echo "usage: $0 versionfile versionkey suffix"
	exit 1
}

if [ "$1" = "" -o "$2" = "" -o "$3" = "" ]; then
	Usage
fi

VERSIONFILE=$1
VERSIONKEY=$2
SUFFIX=$3

ed $1 << FIXME
g/${VERSIONKEY}/s/${SUFFIX}//g
g/${VERSIONKEY}/
w
q
FIXME

exit $?
