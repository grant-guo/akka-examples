#!/bin/bash

if [ -f "password" ]
then
    rm password
fi

export PW=`pwgen -Bs 10 1`
echo $PW > password