# Obey script to load variables into the environment directly for make's use

#if [ "$BASENAME" = "" ]; then
#	echo Error: NAME must be specified
#	exit 1
#fi

#if [ -f ${WORKSPACE}/../../Ituglib_Build/${BASENAME}.dist ]; then
	echo "Loading ${WORKSPACE}/../../Ituglib_Build/workspace/${BASENAME}.dist"
	. ${WORKSPACE}/../../Ituglib_Build/workspace/${BASENAME}.dist
#fi

export NAME=${BASENAME}-${VERSION}
export LIST=${NAME}.bin.list
export SRCINSTALL=/tmp/${NAME}
export SRCFILE=${PREFIX}${NAME}-src.tar
export NSETAR=${PREFIX}${NAME}-nse.tar
export RELEASENAME=${NAME}-release-nse
echo Loaded
