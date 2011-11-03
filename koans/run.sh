DIR="$( cd -P "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
mkdir "$DIR"/bin
clear
java -classpath "$DIR"/bin:"$DIR"/lib/koans.jar com.sandwich.koan.runner.AppLauncher "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9"
