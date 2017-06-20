#!/bin/sh

set -x -v

export BASENAME=git
export VERSION_PATH=/usr/local/bin/git
export DEST=/home/ituglib/randall/stage
export MANIFEST=git.bin.list
export INSTALL_LOCATION=/usr/local

. ./dist.info.manual
bash package.bin.std -local ${MANIFEST}
