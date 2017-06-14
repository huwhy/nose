#!/bin/sh

PWD=`pwd`
cd `dirname $0`

SERVER="cn.huwhy.nose.NoseApp"
PIDS=`ps -ef | grep java | grep "$SERVER" |awk '{print $2}'`
if [ -n "$PIDS" ]; then
    echo "start fail! The $SERVER already started!"
    exit 1
fi
LOG_CONF="-Dlog4j.configurationFile=../conf/log4j2.xml -Dlog.path=../logs -Dconf.path=../conf"
nohup java -Xms512m -Xmx512m -Xss512k -Djava.ext.dirs=../lib:/usr/java/default/jre/lib/ext $LOG_CONF $SERVER > ../logs/nohup.log 2>&1 &
echo "$SERVER start success!"

cd $PWD
