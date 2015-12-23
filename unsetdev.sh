#!/bin/sh
# Shell script to change the version string of a product to remove 

Usage() {
	echo "usage: $0 versionfile versionkey suffix"
	exit 1
}

if [ "$1" -eq "" -o "$2" -eq "" -o "$3" -eq "" ]; then
	Usage
fi

VERSIONFILE=$1
VERSIONKEY=$2
SUFFIX=$3

ed $1 << FIXME
/${VERSIONKEY}/s/${SUFFIX}//
w
q
FIXME

exit $?
