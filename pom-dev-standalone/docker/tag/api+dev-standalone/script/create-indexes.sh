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
   jarpath=$1
   indexesroot=$2

   entries=$("$JAR_BIN" -tf $jarpath)

   ## start
   jarname=$(basename $jarpath)
   for entry in $entries;do
#   echo $entry

        ## get file name
        entrypath=$(dirname $entry)
        entryname=$(basename $entry)
        if [ ! $entryname ];then
           continue
        fi
        ext=${entryname##*.}
        if [ ! $ext ];then
          continue
        fi
        entrynew=$entrypath/$entryname

        if [ $ext = 'jar' -o $ext = 'class' ];then
            echo "$entryname,$entrynew,$jarname"

            firstletter=${entryname::1}  ##first letter
            #firstletter=${firstletter,,}  ##lower case for bash >=4.0
            if [ -f $indexesroot/$firstletter ];then
                echo "$entryname,$entrynew,$jarname" >> $indexesroot/$firstletter
            else
                echo "$entryname,$entrynew,$jarname" > $indexesroot/$firstletter
            fi
        fi
    done
}


createjarindexes_with_entries(){
   jar=$1
   pattern=$2
   indexesroot=$3

   entries=$("$JAR_BIN" -tf $jar | grep $pattern)

   for entry in $entries;do
#   echo $entry

        ## get file name
        entrypath=$(dirname $entry)
        entryname=$(basename $entry)
        if [ ! $entryname ];then
           continue
        fi
        ext=${entryname##*.}
        entrynew=$entrypath/$entryname

        if [ $ext = 'jar' ];then
#            echo $entry
            ## extra it
            if [ ! -f $entry ];then
#               echo $entrynew
               "$JAR_BIN" -xf $jar $entrynew
            fi

            if [ -f $entrynew ];then
                echo ... $entrynew
                createjarindexes $entrynew $indexesroot
            fi
        fi
    done
}


## main
createjarindexes $jar $indexes
createjarindexes_with_entries $jar $entryPattern $indexes
