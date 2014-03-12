#Java Koans [![Build Status](https://travis-ci.org/matyb/java-koans.png?branch=master)](https://travis-ci.org/matyb/java-koans)

Running Instructions:
=====================
* Download and unarchive the contents of the most recent java-koans in development from:
https://github.com/matyb/java-koans/archive/master.zip
* Open a terminal and cd to the directory you unarchived:
```cd <the directory you just unarchived>```
* Within it you'll find:
    * *koans*: this directory contains the application and its lessons, it is all that is needed to advance through the koans themselves and **it can be distributed independently**
    * *lib/koans-lib*: the directory for source code for the engine that executes the koans 
    * *lib/koans-tests*: the directory for tests to check the sanity of the application
    * *lib/file-compiler*: the dynamic compiler module that loads newly saved java files into the JVM as classes
    * *lib/file-monitor*: the file monitoring module for notifying the app when files are saved 
    * *lib/util*: application agnostic interfaces and utilities shared in projects
* Change directory to the koans directory: ```cd koans```
* If you are using windows enter: ```run.bat``` or ```./run.sh``` if you are using Mac or Linux.

Developing a Koan:
==================
* Follow any of the existing koans as an example to create a new class with koan methods (indicated by the @Koan annotation, they're public and specify no arguments)
* Define the order the koan suite (if it's new) will run in the koans/app/config/PathToEnlightenment.xml file
* Optionally you may use dynamic content in your lesson, examples are located in the XmlVariableInjector class (and Test) and the AboutKoans.java file

Something's wrong:
==================
* If the koans app is constantly timing out compiling a koan, your computer may be too slow to compile the koan classes with the default timeout value. Update the compile_timeout_in_ms property in koans/app/config/config.properties with a larger value and try again.
