# Build stage
FROM maven:3.8.5-openjdk-17-slim AS build
WORKDIR /app
COPY common-library ./common-library
RUN mvn -f common-library/pom.xml clean install
COPY eureka-server/pom.xml .
COPY eureka-server/src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8761
ENTRYPOINT ["java", "-Dmanagement.metrics.binders.processor.enabled=false", "-jar", "app.jar"]
