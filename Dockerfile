FROM openjdk:latest
LABEL authors="shamanth"

VOLUME /tmp

EXPOSE 8080

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} warehouse-0.0.1-SNAPSHOT.jar

CMD apt-get update -y

ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/warehouse-0.0.1-SNAPSHOT.jar"]