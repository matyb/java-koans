Running Instructions:
=====================
1. Download and unarchive the contents of the most recent java-koans in development from:
   https://github.com/matyb/java-koans/archives/master
2. Open a terminal and cd to the koans directory within the directory you unarchived
    * koans: this directory contains the application and its lessons, it is all that is needed to advance through the koans themselves.
    * koans-lib: the directory for the koans the application
    * koans-tests is the directory for tests to check the sanity of the application
3. Run run.bat or run.sh (whichever is applicable for your OS)

Developing a Koan:
==================
1. Follow any of the existing koans as an example to create a new class w/ koan methods (indicated by the @Koan annotation)
2. Define the order and metadata associated with each koan in the PathToEnlightenment.xml
3. If necessary - use dynamic content in your lesson, examples are located in XmlVariableInjector class (and Test) and the AboutKoans.java file
