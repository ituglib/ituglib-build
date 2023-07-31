#!/bin/sh

echo "Paking $1"

# Pack it
echo gtacl -c 'pak -purge $data05.jenkins.temppak,jenktemp.*,vol $system.system,listall'
gtacl -c 'pak -purge $data05.jenkins.temppak,$data05.jenktemp.*,vol $system.system,listall'
if [ $? != 0 ]; then
	exit 1
fi

# Remove the prior copy, if it exists
if [ -f $1 ]; then
	echo sudo rm -f $1
	sudo rm -f $1
    if [ $? != 0 ]; then
	    echo sudo rm -f $1 failed!
	    exit 1
    fi
fi
	
# Copy the to a temp location.
echo /usr/coreutils/bin/cp /G/data05/jenkins/temppak $1
/usr/coreutils/bin/cp /G/data05/jenkins/temppak $1
if [ $? != 0 ]; then
	echo /usr/coreutils/bin/cp /G/data05/jenkins/temppak $1 failed!
	exit 1
fi

echo chmod 0664 $1
chmod 0664 $1
if [ $? != 0 ]; then
	echo chmod 0664 $1 failed!
	exit 1
fi

echo sudo chown super.webmastr:SUPER $1
sudo chown super.webmastr:SUPER $1
if [ $? != 0 ]; then
	echo sudo chown super.webmastr:SUPER $1 failed!
	exit 1
fi

exit $?
