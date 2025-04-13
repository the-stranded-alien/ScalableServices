# Use a JDK base image
FROM openjdk:17-jdk-slim

# Set app directory
WORKDIR /app

# Copy the built JAR into the image
COPY target/hotel-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8082

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]