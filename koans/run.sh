DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir "$DIR"/app
mkdir "$DIR"/app/bin
function exitOnError()
{
	rc=$?
	if [[ $rc != 0 ]] ; then
	    exit $rc
	fi
}
javac -help
exitOnError
java -version
exitOnError
clear
java -classpath "$DIR"/app/bin:"$DIR"/app/lib/koans.jar com.sandwich.koan.runner.AppLauncher "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9"
