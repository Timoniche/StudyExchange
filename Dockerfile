FROM maven:3.8.1-openjdk-17-slim AS build
RUN mkdir -p /workspace
WORKDIR /workspace
COPY pom.xml /workspace
COPY src /workspace/src
RUN mvn -B package --file pom.xml -DskipTests

FROM openjdk:17-jdk-slim
COPY --from=build /workspace/target/StudyExchange-1.0-jar-with-dependencies.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]
