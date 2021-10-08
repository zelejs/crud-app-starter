#!/bin/sh
jar=$1
entryPattern=$2


indexes=$3
if [ ! $indexes ];then
   indexes='indexes'
fi
if [ ! -d $indexes ];then
   mkdir indexes
fi


JAR_BIN=$(which jar)

createjarindexes(){
   jar=$1
   pattern=$2
   indexesroot=$3

   unset entries
   if [ $pattern ];then
      entries=$("$JAR_BIN" -tf $jar | grep $pattern)
   elif [ $pattern = '*' ];then
      entries=$("$JAR_BIN" -tf $jar)
   fi
   echo $entries
   exit

   for entry in $entries;do
        ## get file name
        filename=${entry##*/}
        if [ ! $filename ];then
           continue
        fi    
        ext=${filename##*.}

        if [ $ext = 'jar' ];then
            echo $entry

            #echo $entry
            ## extra it
            if [ ! -f $entry ];then
               jar xf $jar $entry
            fi

            indexes=$(jar tf $entry);
            for index in $indexes;do
                name=${index##*/}
                if [ ! $name ];then
                   continue
                fi
                firstletter=${name::1}  ##first letter
                #firstletter=${firstletter,,}  ##lower case for bash >=4.0
                # lower
                posfix=${name##*.}

                if [ $posfix = 'class' ];then
                    echo $name
                    if [ -f $indexesroot/$firstletter ];then
                        echo "$name,$index,$entry" >> $indexesroot/$firstletter
                    else 
                        echo "$name,$index,$entry" > $indexesroot/$firstletter
                    fi
                fi
            done
        fi
        #echo $entry
    done
}


## main
patternall='*'
createjarindexes $jar $patternall $indexes
#createjarindexes $jar $entryPattern $indexes
