DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir -p "$DIR"/app
mkdir -p "$DIR"/app/bin
function exitOnError()
{
	rc=$?
	if [[ $rc != 0 ]] ; then
	    exit $rc
	fi
}

function buildClasspath()
{
    appDir=$1
    classpath=$appDir/bin

    for jar in $appDir/lib/*; do
        classpath=$classpath:$jar
    done
}
javac -help > /dev/null 2>&1
exitOnError
java -version > /dev/null 2>&1
exitOnError
buildClasspath "$DIR"/app
java -classpath $classpath com.sandwich.koan.runner.AppLauncher "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9"
