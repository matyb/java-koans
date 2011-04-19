#compile class files
clear
rm -r ../bin
mkdir ../bin
javac -d ../bin -classpath ../lib/koans.jar beginner/*.java
javac -d ../bin -classpath ../lib/koans.jar intermediate/*.java
javac -d ../bin -classpath ../lib/koans.jar advanced/*.java
clear
java -classpath ../bin:../lib/koans.jar com.sandwich.koan.runner.AppLauncher "$1" "$2" "$3" "$4" "$5" "$6" "$7" "$8" "$9"