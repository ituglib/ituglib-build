#!/bin/sh

echo "Securing archive as \"$1\""

echo sudo gtacl -p /G/system/system/fup secure "jenktemp.*,\"$1\""
sudo gtacl -p /G/system/system/fup secure "jenktemp.*,\"$1\""

exit $?
