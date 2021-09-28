#!/bin/sh
################################
#export ROLLBACK_KEEP_NUM=2
################################

cmd=$1

usage() {
   echo "Usage:"
   echo '   predeploy.sh rollback keep <pattern> <num>'
   exit
}

cmd2=$1$2

if [ $cmd2 = rollbackkeep ];then 
   pattern=$3
   num=$4
   if [ ! $num ];then
      usage
   fi
   cmd=cmdrollbackkeep
fi

rollbackkeep() {
   pattern=$1
   num=$2
   
   result=$(ls "$pattern"* -t)
   i=1
   for it in $result;do
      if [ $i -gt $num ];then
        echo $it
        rm -f $it
      fi
      ## increase
      i=$(($i+1))
   done
}

if [ ${ROLLBACK_KEEP_NUM} ];then
   num=${ROLLBACK_KEEP_NUM}
fi

if [ $cmd = cmdrollbackkeep ];then 
   pattern=$3
   num=$4
   rollbackkeep $pattern $num
else 
   usage
fi


