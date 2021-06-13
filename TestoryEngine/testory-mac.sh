#!/bin/bash
java -cp TestoryEngine/target/testory-0.0.1-SNAPSHOT.uber.jar.original:TestoryEngine/target/testory-0.0.1-SNAPSHOT.uber.jar\
   testory.Main\
   --path=$*

