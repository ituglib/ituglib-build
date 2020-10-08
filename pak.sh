#!/bin/sh

echo "Paking $1"

# Pack it
echo sudo gtacl -c 'pak -purge jenkins.temppak,jenktemp.*,vol $system.system,listall'
sudo gtacl -c 'pak -purge jenkins.temppak,jenktemp.*,vol $system.system,listall'
if [ $? != 0 ]; then
	exit 1
fi

# Copy the to a temp location.
echo sudo cp /G/data01/jenkins/temppak $1
sudo cp /G/data01/jenkins/temppak $1

exit $?
