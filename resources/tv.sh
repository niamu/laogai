#!/bin/sh

if [ "$1" == "on" ]
then
    echo "TV turning on..."
    echo "on 0" | cec-client -s -d 1 >> /dev/null; echo "on 5" | cec-client -s -d 1 >> /dev/null
fi

if [ "$1" == "off" ]
then
    echo "TV turning off..."
    echo "standby 5" | cec-client -s -d 1 >> /dev/null
fi

if [ "$1" == "state" ]
then
    echo "TV state is..."
    echo "pow 5" | cec-client -s -d 1 | grep "status"
fi

if [ "$1" == "active-input" ]
then
    echo "TV switching to Plex input"
    echo "as" | cec-client -s -d 1
fi
