#!/bin/sh

echo "Changing archive content ownership to \"$1\""

echo chown super.super:SUPER /G/data05/jenktemp/*
chown super.super:SUPER /G/data05/jenktemp/*

exit $?
