#!/bin/sh

echo "Changing archive content ownership to \"$1\""

echo sudo gtacl -p /G/system/system/fup give "jenktemp.*,$1"
sudo gtacl -p /G/system/system/fup give "jenktemp.*,$1"

exit $?
