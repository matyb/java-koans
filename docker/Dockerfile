FROM centos:7
MAINTAINER Mat Bentley <mathewkbentley@gmail.com>

ENV JAVA_VERSION 1.8.0
ENV GRADLE_VERSION 3.4.1

# install wget, git, curl, jdk, which
RUN yum remove -y java &&\
    yum install -y wget git curl unzip java-$JAVA_VERSION-openjdk-devel which

# install gradle
RUN wget https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip &&\
    mkdir -p /etc/alternatives/gradle &&\
    unzip -d /etc/alternatives/gradle gradle-$GRADLE_VERSION-bin.zip &&\
    ln -s /etc/alternatives/gradle/gradle-$GRADLE_VERSION /opt/gradle

ENV PATH $PATH:/opt/gradle/bin
RUN JAVA_HOME=$(readlink $(readlink `which java`) | gawk '$0=gensub(/\/jre\/bin\/java/,"",1)')
