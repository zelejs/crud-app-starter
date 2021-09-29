#!/bin/sh
################################
#export ROLLBACK_KEEP_NUM=2
################################
keep=${ROLLBACK_KEEP_NUM}  # keep rollback instances

## global definition
app='app.jar'
webapp='ROOT.war'

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
  if [ ${#result} -eq 0 ]];then
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
rollback=$(ls *-standalone.jar *.war 2> /dev/null)
if [ ${#rollback} -eq 0 ];then
   echo no *-standalone.jar or .war, no need to rollback ! >/dev/stderr
   exit
fi

rollback=$(search_one "*-standalone.jar *.war")
if [ ! $rollback ];then
   echo 'no (or multi) -standalone.jar or .war found !' >/dev/stderr
   exit
fi

## wether app.jar or ROOT.war
warornot=${rollback}
warornot=${rollback##*.}  ## get ext
if [ $warornot = war ];then
   app=$webapp
fi

## rollback first
echo => start to rollback: $app $rollback
rollback $app $rollback

## deploy app.jar or ROOT.war
echo mv $rollback $app
mv $rollback $app

