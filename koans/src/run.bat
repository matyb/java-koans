@echo off
cls

javac -version
if ERRORLEVEL 3 goto no_javac
java -version
if ERRORLEVEL 1 goto no_java
mkdir ..\bin
cls
java -classpath ..\bin;..\lib\koans.jar com.sandwich.koan.runner.AppLauncher
 
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
