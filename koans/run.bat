@echo off
cls

javac -version
if ERRORLEVEL 3 goto no_javac
java -version
if ERRORLEVEL 1 goto no_java
mkdir "%~dp0\app"
mkdir "%~dp0\app\bin"
cls
java -classpath "%~dp0\app\bin;%~dp0\app\lib\koans.jar" com.sandwich.koan.runner.AppLauncher %1 %2 %3 %4 %5 %6 %7 %8 %9

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
