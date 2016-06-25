#!/bin/bash

azkaban_dir=$(dirname $0)/..

if [[ -z "$tmpdir" ]]; then
tmpdir=/tmp
fi

for file in $azkaban_dir/lib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

for file in $azkaban_dir/extlib/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

for file in $azkaban_dir/plugins/*/*.jar;
do
  CLASSPATH=$CLASSPATH:$file
done

if [ "HADOOP_HOME" != "" ]; then
	for file in $HADOOP_HOME/hadoop-core*.jar ;
	do
		CLASSPATH=$CLASSPATH:$file
	done
	CLASSPATH=$CLASSPATH:$HADOOP_HOME/conf
    JAVA_LIB_PATH="-Djava.library.path=$HADOOP_HOME/lib/native/Linux-amd64-64"
else
	echo "Error: HADOOP_HOME is not set. Hadoop job types will not run properly."
fi

if [ "HIVE_HOME" != "" ]; then
    CLASSPATH=$CLASSPATH:$HIVE_HOME/conf
fi

echo $azkaban_dir;
echo $CLASSPATH;

executorport=`cat $azkaban_dir/conf/azkaban.properties | grep executor.port | cut -d = -f 2`
echo "Starting AzkabanExecutorServer on port $executorport ..."
serverpath=`pwd`

if [ -z $AZKABAN_OPTS ]; then
  AZKABAN_OPTS="-Xmx4G"
fi
AZKABAN_OPTS="$AZKABAN_OPTS -server -Dcom.sun.management.jmxremote -Djava.io.tmpdir=$tmpdir -Dexecutorport=$executorport -Dserverpath=$serverpath"

java $AZKABAN_OPTS $JAVA_LIB_PATH -cp $CLASSPATH azkaban.execapp.AzkabanExecutorServer -conf $azkaban_dir/conf $@ 1>logs/exec.nohup.log 2>&1  &

echo $! > currentpid

