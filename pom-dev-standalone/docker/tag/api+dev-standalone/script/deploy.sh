#!/bin/sh
################################
#export ROLLBACK_KEEP_NUM=2
################################
keep=${ROLLBACK_KEEP_NUM}  # keep rollback instances

## global definition
app='app.jar'

rollback() {
  app=$1
  rollback=$2

  if [ ! -f $app ];then
     echo mv $rollback $app
     mv $rollback $app
     echo done!
     exit
  fi

  ROLLBACK=$app.rollback_$(date "+%m-%d")
  if [ ! -f $ROLLBACK ];then
     echo cp $app $ROLLBACK
     cp $app $ROLLBACK
     echo predeploy.sh rollback keep $app.rollback_ $keep
     predeploy.sh rollback keep $app.rollback_ $keep
  fi
}

search_one() {
  pattern=$1

  result=$(ls $pattern 2> /dev/null)
  if [[ -z $result ]];then
    echo no $pattern files found ! > /dev/stderr
    exit
  fi

  ## error
  num=0
  for it in $result;do
     num=$(($num+1))
  done

  if [ $num -eq 1 ];then
     echo $result
  fi
  echo ''
}

## main
rollback=$(ls *-standalone.jar 2> /dev/null)
if [ -z $rollback ];then
   echo no *-standalone.jar, no need to rollback ! >/dev/stderr
   exit
fi

rollback=$(search_one "*-standalone.jar")
if [ ! $rollback ];then
   echo 'no (or multi) -standalone.jar !' >/dev/stderr
   exit
fi

## rollback first
echo => start to rollback:  $app $rollback
rollback $app $rollback

## deploy
echo mv $rollback $app
mv $rollback $app

