FROM openjdk:11.0.8-jre-buster

ENTRYPOINT ["java", "-jar", "/usr/share/emuredis/emuredis.jar"]

EXPOSE 8080

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/emuredis/emuredis.jar