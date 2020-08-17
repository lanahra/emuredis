FROM openjdk:11.0.8-jre-buster

ENTRYPOINT ["java", "-jar", "/usr/share/emuredis/emuredis.jar"]

EXPOSE 8080

ADD target/emuredis-1.0.jar /usr/share/emuredis/emuredis.jar