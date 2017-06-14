#!/bin/sh

PWD=`pwd`
cd `dirname $0`

cd ../nose/
git pull
mvn clean
mvn package -DskipTests=true
mvn dependency:copy-dependencies

rm -rf ../lib/*
mv -f target/dependency/*.jar ../lib
mv -f target/*.jar ../lib

cd $PWD