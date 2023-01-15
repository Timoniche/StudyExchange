FROM openjdk:17-jdk-slim

COPY target/StudyExchange-1.0-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
