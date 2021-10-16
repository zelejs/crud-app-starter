#!/usr/bin/env bash
################################
## define your var for lib deploy
################################
JAR_BIN=$(which jar)
JAVA_BIN=$(which java)

checkdependency() {
  app=$1
  jar=$2

  jarlibroot=/usr/local/lib
  ## for unit test
  if [ ${JAR_D_ROOT} ];then
    jarlibroot=${JAR_D_ROOT}
  fi

  result=$("$JAVA_BIN" -jar $jarlibroot/jar-dependency.jar -cm  $app $jar)
  echo $result
}

putlocaljars() {
  app=$1
  libroot=$2
  ext=${app##*.}

  for lib in $(ls $libroot)
  do
     jar=$libroot/$lib

     # only .jar get jar
     libext=${lib##*.}
     if [ ! $libext = 'jar' ];then
        continue
     fi

   #   ## check dependency, if required new dependencies, skip
   #      dependencies=$(checkdependency $app $jar)
   #      if [ ${#dependencies} -gt 0 ];then
   #          echo fail to depoy lib for dependencies: >/dev/stderr
   #          for it in $dependencies;do
   #            echo $'\t'$it >/dev/stderr
   #          done
   #          continue
   #      fi
   #   ## end dependency

     ## main ##
     jarlib=$(basename $lib)
     echo ...$jarlib
     jarok=$("$JAR_BIN" tf $app | grep $jarlib)
     if [ ! $jarok ];then
        ## means new jar
        ## .war for WEB-INF, .jar for BOOT-INF
        local INF
        if [ $ext = 'war' ];then
           INF=WEB-INF
        else
           INF=BOOT-INF 
        fi
        echo "$INF/lib/$jarlib"
        jarok="$INF/lib/$jarlib"
     fi

     if [ $jarok ];then
       ## update lib
       jardir=$(dirname $jarok)
       if [ ! -d $jardir ];then
          echo mkdir -p $jardir
          mkdir -p $jardir
       fi

       # core
       echo mv $jar $jardir
       mv $jar $jardir
       echo jar 0uf $app $jarok
       "$JAR_BIN" 0uf $app $jarok

       ## rm after jar updated
       echo rm -f $jarok
       rm -f $jarok
     fi
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
  ext=$1
  if [ $ext = jar ];then 
    if [ -d BOOT-INF ];then
      rm -rf BOOT-INF/
    fi
  fi
  
  if [ $ext = war ];then
    if [ -d WEB-INF ];then
      rm -rf WEB-INF/
    fi
  fi
}


## main
if [ -z $(ls lib/*.jar 2>/dev/null) ];then
   echo 'no lib to deploy !' >/dev/stderr
   exit
fi

#check standalone
standalone=$(search_one "app.jar *-standalone.jar *.war")
if [ ! $standalone ];then
    echo 'no (or multi) -standalone.jar !' >/dev/stderr
    exit
fi
standalone_basename=$(basename $standalone)
standalone_filename=${standalone_basename%.*}
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

  #echo putlocaljars $fixapp lib
  putlocaljars $fixapp lib
else
  echo "deploy lib done with $fixapp!"
fi

cleanup $standalone_ext
