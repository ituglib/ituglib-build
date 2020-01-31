#!/bin/sh

PROGNAME=$0

Usage() {
	echo "Usage: $PROGNAME base-name ( nse | nsx ) [-v version-file] [-d dest-dir]"
}

if [ "$1" = "" -o "$2" = "" ]; then
	Usage
	exit 1
fi

export BASENAME=$1
shift
export VERSION_PATH=/usr/local/bin/$BASENAME
export DEST=/home/ituglib/randall/stage
export MANIFEST=${BASENAME}.bin.list
export INSTALL_LOCATION=/usr/local
export SYSTYPE=$1
shift

case $SYSTYPE in
	nse)
		echo "Packaging for NSE (H/J-series) systems."
		;;
	nsx)
		echo "Packaging for NSX (L-series) systems."
		;;
	*)
		echo "SYSTYPE $SYSTYPE is invalid. Must be nse or nsx."
		exit 1
		;;
esac

while [ "$1" != "" ]; do
	case $1 in
		-v)
			shift
			if [ "$1" = "" ]; then
				Usage
				exit 1
			fi
			export VERSION_PATH=/usr/local/bin/$1
			;;
		-d)
			shift
			if [ "$1" = "" ]; then
				Usage
				exit 1
			fi
			export DEST=$1
			;;
		*)
			Usage
			exit 1
			;;
	esac
	shift
done
echo Using BASENAME=$BASENAME
echo Using VERSION_PATH=$VERSION_PATH
echo Using DEST=$DEST
echo Using MANIFEST=$MANIFEST
echo Using INSTALL_LOCATION=$INSTALL_LOCATION

. ./dist.info.manual
bash package.bin.std -local ${MANIFEST}
