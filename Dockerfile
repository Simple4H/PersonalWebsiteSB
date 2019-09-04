FROM simple4h/centos_java:1.1
MAINTAINER Simple4H <crescentcxm@gmail.com>
ADD target/PersonalWebsiteSB-1.1.0-RELEASE.war application.jar
ENTRYPOINT ["java","-jar","/application.jar"]