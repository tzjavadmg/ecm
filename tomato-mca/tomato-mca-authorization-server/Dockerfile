FROM registry01.fanqiebianli.com/library/frolvlad/alpine-oraclejdk8:v1
VOLUME /tmp,/data/logs/ecm
ADD target/tomato-mca-authorization-server.jar app.jar
EXPOSE 20600
ENTRYPOINT ["java","-server","-Xms256M","-Xmx256M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
