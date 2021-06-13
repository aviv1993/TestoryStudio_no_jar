#!/bin/bash

if [ ! -f "target/testory-0.0.1-SNAPSHOT.uber.jar.original" ]; then
    echo Missing jar file "testory-0.0.1-SNAPSHOT.uber.jar.original" in the target directory
    exit -1
fi
if [ ! -f "target/testory-0.0.1-SNAPSHOT.uber.jar" ]; then
    echo Missing jar file "testory-0.0.1-SNAPSHOT.uber.jar" in the target directory
    exit -2
fi

java -cp target/testory-0.0.1-SNAPSHOT.uber.jar.original:target/testory-0.0.1-SNAPSHOT.uber.jar\
   testory.Main\
   --path=$*

