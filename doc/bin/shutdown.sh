#!/bin/sh

PWD=`pwd`
cd `dirname $0`

SERVER="cn.huwhy.nose.NoseApp"
PIDS=`ps -ef | grep java | grep "$SERVER" |awk '{print $2}'`
if [ -z "$PIDS" ]; then
    echo "stop fail! The $SERVER not start!"
    exit 1
fi

for PID in $PIDS ;
do
    kill -15 $PID > /dev/null 2>&1
done
echo "$SERVER stop success! pid:"$PIDS

cd $PWD