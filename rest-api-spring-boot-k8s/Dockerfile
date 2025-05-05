FROM openjdk:17-jdk-alpine
COPY ./target/rest-api-spring-boot-k8s-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT exec java -jar app.jar --debug