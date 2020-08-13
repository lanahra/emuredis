FROM openjdk:11.0.8-jre-buster

ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/emuredis/emuredis.jar"]

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/emuredis/emuredis.jar