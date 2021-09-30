#!/bin/sh

app=app.jar
webapp=ROOT.war
if [ ! -f $app ];then
  app=$webapp
fi

indexesroot='indexes'
if [ ! -d $indexesroot ];then
   mkdir $indexindexesrootes
fi

jar=$1

create_root_indexes() {
    entries=$(jar -tf $app)
 
    ## clean up first
    rm -f $indexesroot/*
    for entry in $entries;do
        ## get file name
        filename=${entry##*/}
        if [ ! $filename ];then
           continue
        fi    

        firstletter=${filename::1}  ##first letter
        ext=${filename##*.}
        
        if [ $ext = 'jar' -o $ext = 'class' ];then
            echo $entry

            if [ -f $indexes/$firstletter ];then
               echo "$filename,$entry,$app" >> $indexesroot/$firstletter
            else 
               echo "$filename,$entry,$app" > $indexesroot/$firstletter
            fi
        fi
    done
}

create_jar_indexes(){
   pattern=$1
   entries=$(jar -tf $app | grep $pattern)
   for entry in $entries;do
   echo $entry
        ## get file name
        filename=${entry##*/}
        if [ ! $filename ];then
           continue
        fi    
        ext=${filename##*.}

        if [ $ext = 'jar' ];then
            #echo $entry
            ## extra it
            if [ ! -f $entry ];then
               jar xf $app $entry
            fi

            indexes=$(jar tf $entry);
            for index in $indexes;do
                name=${index##*/}
                if [ ! $name ];then
                   continue
                fi
                firstletter=${name::1}  ##first letter
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


if [ $jar ];then
   create_jar_indexes $jar
else 
   create_root_indexes
fi