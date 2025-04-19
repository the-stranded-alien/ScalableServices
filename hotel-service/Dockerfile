# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY common-library ./common-library
RUN mvn -f common-library/pom.xml clean install
COPY hotel-service/pom.xml .
COPY hotel-service/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "app.jar"]
