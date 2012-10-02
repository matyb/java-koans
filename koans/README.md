
Running Instructions:
=====================
* Download and unarchive the contents of the most recent java-koans in development from:
https://github.com/matyb/java-koans/archives/master

* Open a terminal and cd to the directory you unarchived:
```cd <the directory you just unarchived>```
* Within it you'll find:
    * *koans*: this directory contains the application and its lessons, it is all that is needed to advance through the koans themselves and **it can be distributed independently**
    * *koans-lib*: the directory for source code for the engine that executes the koans 
    * *koans-tests*: the directory for tests to check the sanity of the application
* Change directory to the koans directory: ```cd koans```

* If you are using windows enter: ```run.bat``` or ```sh run.sh``` if you are using Mac or Linux.

Developing a Koan:
==================
<<<<<<< HEAD
* Follow any of the existing koans as an example to create a new class with koan methods (indicated by the @Koan annotation, they're public and specify no arguments)

* Define the order the koan suite (if it's new) will run in the koans/config/PathToEnlightenment.xml file

* Optionally you may use dynamic content in your lesson, examples are located in the XmlVariableInjector class (and Test) and the AboutKoans.java file
=======
1. Follow any of the existing koans as an example to create a new class w/ koan methods (indicated by the @Koan annotation)
2. Define the order and metadata associated with each koan in the PathToEnlightenment.xml
3. If necessary - use dynamic content in your lesson, examples are located in XmlVariableInjector class (and Test) and the AboutKoans.java file
>>>>>>> 81d381746890bea20bccf589fa8e5ed63a9ec90b
