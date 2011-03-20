#compile class files
clear
javac -d ../bin -classpath ../lib/koans.jar beginner/*.java
javac -d ../bin -classpath ../lib/koans.jar intermediate/*.java
javac -d ../bin -classpath ../lib/koans.jar advanced/*.java
java -version
clear
java -classpath ../bin:../lib/koans.jar com.sandwich.koan.runner.KoanSuiteRunner
