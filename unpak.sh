#!/bin/sh

echo "Unpaking $1"

# Clean out stuff
echo gtacl -c 'fup purge ($data05.jenktemp.*,$data05.jenkins.temppak) !'
gtacl -c 'fup purge ($data05.jenktemp.*,$data05.jenkins.temppak) !'
if [ $? -ne 0 ]; then
	echo 'gtacl -c fup purge ($data05.jenktemp.*,$data05.jenkins.temppak) !' failed
fi

# Copy the to a temp location.
echo sudo /usr/coreutils/bin/cp $1 /G/data05/jenkins/temppak
sudo /usr/coreutils/bin/cp $1 /G/data05/jenkins/temppak
if [ $? -ne 0 ]; then
	echo sudo /usr/coreutils/bin/cp $1 /G/data05/jenkins/temppak failed
	exit 1
fi

echo gtacl -c 'fup alter $data05.jenkins.temppak,code 1729'
gtacl -c 'fup alter $data05.jenkins.temppak,code 1729'
if [ $? -ne 0 ]; then
	echo gtacl -c 'fup alter $data05.jenkins.temppak,code 1729' failed
	exit 1
fi

# It is now a PAK file.

# Unpack it
echo gtacl -c 'unpak $data05.jenkins.temppak,*.*.*,myid,vol $data05.jenktemp,listall'
gtacl -c 'unpak $data05.jenkins.temppak,*.*.*,myid,vol $data05.jenktemp,listall'
if [ $? -ne 0 ]; then
	echo gtacl -c 'unpak $data05.jenkins.temppak,*.*.*,myid,vol $data05.jenktemp,listall' failed
fi

exit $?
