@echo off
cls

REM compile class files
mkdir ..\bin
cls
javac -d ..\bin -classpath ..\lib\koans.jar beginner\*.java
if ERRORLEVEL 3 goto no_javac
javac -d ..\bin -classpath ..\lib\koans.jar intermediate\*.java
javac -d ..\bin -classpath ..\lib\koans.jar advanced\*.java
java -version
if ERRORLEVEL 1 goto no_java
cls
java -classpath ..\bin;..\lib\koans.jar com.sandwich.koan.runner.AppLauncher %1 %2 %3 %4 %5 %6 %7 %8 %9
 
goto end

:no_java
REM cls
@echo java is not bound to PATH variable.
goto end

:no_javac
REM cls
@echo javac is not bound to PATH variable.
goto end

:end
