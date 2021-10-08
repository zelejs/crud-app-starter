#!/usr/bin/env bash

JAR_BIN=$(which jar)
JAVA_BIN=$(which java)


## read indexes
read_indexes(){
   echo '' > /dev/null
}

indexjarlibfromclass() {
   classname=$1
   indexesroot=$2
}

## pu one .class file to .jar file
putclasstojar(){
   javaclass=$1
   jar=$2

   # only .class
   clsext=${javaclass##*.}
   if [ ! $clsext = 'class' ];then
      exit
   fi

   ## main ##
   classname=$(basename $javaclass)
   echo ...$classname
   classok=$("$JAR_BIN" tf $targetjar | grep $classname)
   if [ $classok ];then
      ## update lib
      classdir=$(dirname $classok)
      if [ ! -d $classdir ];then
         echo mkdir -p $classdir > /dev/stderr
         mkdir -p $classdir
      fi

      # core
      echo mv $javaclass $classdir > /dev/stderr
      mv $javaclass $classdir
      echo jar 0uf $targetjar $classok > /dev/stderr
      "$JAR_BIN" 0uf $targetjar $classok > /dev/null

      echo rm -r $classok > /dev/stderr
      rm -f $classok

      echo $classok
   fi
}

## put classes/*.class to jar
putlocalclassestojar() {
   targetjar=$1
   classesroot=$2
   indexesroot=$2

   indexes=()

  for cls in $(ls $classesroot)
  do
     javaclass=$classesroot/$cls
     classname=${basename $javaclass}

     index=$(indexjarlibfromclass $classname $indexes)

     #classdone=$(putclasstojar $targetjar $javaclass)
     ## means continer to indexes into jar lib
  done
}

search_one() {
  pattern=$1

  result=$(ls $pattern 2> /dev/null)
  if [ -z $result ];then
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

## clean up
cleanup() {
  if [ -d BOOT-INF ];then
    rm -rf BOOT-INF/
  fi
  if [ -d WEB-INF ];then
    rm -rf WEB-INF/
  fi
}

## main
if [ -z $(ls classes/*.class 2>/dev/null) ];then
   echo 'no .class to deploy !' >/dev/stderr
   exit
fi

#check standalone
standalone=$(search_one "app.jar *-standalone.jar *.war")
if [ ! $standalone ];then
    echo 'no (or multi) -standalone.jar !' >/dev/stderr
    exit
fi
standalone_filename=$(basename $standalone)
standalone_filename=${standalone_filename%.*}
standalone_ext=${standalone##*.}


## get fixapp to be deploy
unset fixapp
if [ $standalone_ext = 'war' ];then
   fixapp=$standalone_filename.war.FIX
else
   fixapp=$standalone_filename.jar.FIX
fi

if [ ! -f $fixapp ];then
  echo cp $standalone $fixapp
  cp $standalone $fixapp

  ## check to find 
  echo putlocalclassestojar $fixapp classes
  putlocalclassestojar $fixapp classes

else
  echo "deploy lib done with $fixapp!"
fi



## test deploy 
jar=/Users/vincenthuang/workspace/zelejs/crud-app-starter/pom-dev-standalone/target/dev-standalone-1.0.0-standalone.jar
classpath=/Users/vincenthuang/workspace/zelejs/crud-app-starter/pom-dev-standalone/target/classes

echo putlocalclassestojar $jar $classpath
putlocalclassestojar $jar $classpath


cleanup