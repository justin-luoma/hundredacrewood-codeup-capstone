FROM openjdk:8-stretch
LABEL maintainer="justin@justinluoma.com"
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=app-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} app-0.0.1-SNAPSHOT.jar
ADD application.properties application.properties
ADD wait-for-it.sh wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh", "db:3306", "--", "java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app-0.0.1-SNAPSHOT.jar", "--server.port=8080"]