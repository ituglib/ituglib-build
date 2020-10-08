#!/bin/sh

echo "Unpaking $1"

# Clean out stuff
echo sudo gtacl -c 'fup purge (jenktemp.*,jenkins.temppak) !'
sudo gtacl -c 'fup purge (jenktemp.*,jenkins.temppak) !'

# Copy the to a temp location.
echo sudo cp $1 /G/data01/jenkins/temppak
sudo cp $1 /G/data01/jenkins/temppak
if [ $? != 0 ]; then
	exit 1
fi

echo sudo gtacl -c 'fup alter jenkins.temppak,code 1729'
sudo gtacl -c 'fup alter jenkins.temppak,code 1729'
if [ $? != 0 ]; then
	exit 1
fi

# It is now a PAK file.

# Unpack it
echo sudo gtacl -c 'unpak jenkins.temppak,*.*.*,myid,vol $data01.jenktemp,listall'
sudo gtacl -c 'unpak jenkins.temppak,*.*.*,myid,vol $data01.jenktemp,listall'
exit $?
