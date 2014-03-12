@echo off
cls
setLocal EnableDelayedExpansion
set CLASSPATH="%~dp0app\bin
for /R "%~dp0\app\lib" %%a in (*.jar) do (
  set CLASSPATH=!CLASSPATH!;%%a
)
set CLASSPATH=!CLASSPATH!;"
javac -version
if ERRORLEVEL 3 goto no_javac
java -version
if ERRORLEVEL 1 goto no_java
cls
java -Dapplication.basedir="%~dp0"" -classpath %CLASSPATH% com.sandwich.koan.runner.AppLauncher %1 %2 %3 %4 %5 %6 %7 %8 %9

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
