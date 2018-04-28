FROM centos:7
MAINTAINER Mat Bentley <mathewkbentley@gmail.com>

ENV JAVA_VERSION 1.8.0
ENV GRADLE_VERSION 3.4.1
ENV MAVEN_VERSION 3.5.3

# install wget, git, curl, jdk, which
RUN yum remove -y java &&\
    yum install -y wget git curl unzip java-$JAVA_VERSION-openjdk-devel which

# install gradle
RUN wget https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip &&\
    mkdir -p /etc/alternatives/gradle &&\
    unzip -d /etc/alternatives/gradle gradle-$GRADLE_VERSION-bin.zip &&\
    ln -s /etc/alternatives/gradle/gradle-$GRADLE_VERSION /opt/gradle

ENV PATH $PATH:/opt/gradle/bin

# install maven
RUN wget http://www-eu.apache.org/dist/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.zip &&\
    mkdir -p /etc/alternatives/maven &&\
    unzip -d /etc/alternatives/maven apache-maven-$MAVEN_VERSION-bin.zip &&\
    ln -s /etc/alternatives/maven/apache-maven-$MAVEN_VERSION /opt/maven

ENV PATH $PATH:/opt/maven/bin


RUN JAVA_HOME=$(readlink $(readlink `which java`) | gawk '$0=gensub(/\/jre\/bin\/java/,"",1)')
