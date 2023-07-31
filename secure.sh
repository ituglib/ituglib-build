#!/bin/sh

echo "Securing archive as \"$1\""

echo gtacl -p /G/system/system/fup secure "jenktemp.*,\"$1\""
gtacl -p /G/system/system/fup secure "\$data05.jenktemp.*,\"$1\""

exit $?
