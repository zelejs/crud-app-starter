#!/bin/sh
################################
#export ROLLBACK_KEEP_NUM=2
################################

## rollback
keep=${ROLLBACK_KEEP_NUM}  # keep 7 rollback instances
app= 'app.jar'

## main
rollback=${ls *-standalone.jar}
ROLLBACK=$rollback.rollback_$(date "+%m-%d")

rollback() {
   if [ ! -f $ROLLBACK ];then
      echo cp $app $ROLLBACK
      cp $app $ROLLBACK
      echo predeploy.sh rollback keep ${rollback}.rollback_ $keep
      predeploy.sh rollback keep ${rollback}.rollback_ $keep
   fi
}


if [ ! $rollback ];then
   echo no standalone, no need to rollback
   exit
fi

## first deploy
if [ ! -f $app ];then
   echo mv $rollback $app
   mv $rollback $app
else
   rollback
fi
