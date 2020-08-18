#!/usr/bin/env groovy

import org.ituglib.deploy.*;

def call(String name = 'human') {
	echo "Hello, ${name}"
	echo "Hello, "+GlobalVars.foo
}

