#!/bin/bash
DIR="$( cd -P "$( dirname "$0" )" && pwd )"
exitOnError()
{
    rc=$?
    if [[ $rc != 0 ]] ; then
        echo ${1}' is missing from your PATH.'
        exit $rc
    fi
}

buildClasspath()
{
    appDir=$1
    classpath=$appDir/bin
    IFS=$'\n'

    for jar in $appDir/lib/* 
    do
        classpath=$classpath:$jar
    done
}
javac -help > /dev/null 2>&1
exitOnError 'javac'
java -version > /dev/null 2>&1
exitOnError 'java'
buildClasspath "$DIR"/app
cmd="java -Dapplication.basedir=\"$DIR\" -classpath \"$classpath\" com.sandwich.koan.runner.AppLauncher "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9""
eval $cmd