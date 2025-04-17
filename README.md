Scalable Service Assignment
1. Sahil Gupta (2023mt03523)
2. Avaneesh A. (2023mt03623)
3. Sahil Srivastava (2023mt03626)
4. Venkat Raman R. (2023mt03611)

# Running the Hotel Management System Locally

This guide provides instructions for running the Hotel Management System components locally, including Kafka, the Hotel Service, and the Notification Service.

## Prerequisites

- Docker and Docker Compose
- Java 17 or later
- Maven

## Running with Docker Compose

The easiest way to run all components together is using Docker Compose:

```bash
# Build and start all services
docker-compose up -d

# Check the status of the services
docker-compose ps

# View logs for a specific service
docker-compose logs -f hotel-service
docker-compose logs -f notification-service
docker-compose logs -f kafka

# Stop all services
docker-compose down
```

## Running Services Individually

### 1. Start Kafka

```bash
# Start only Kafka
docker-compose up -d kafka

# Wait for Kafka to be fully up and running (check logs)
docker-compose logs -f kafka
```

### 2. Run the Hotel Service

```bash
cd hotel-service

# Build the service
./mvnw clean package -DskipTests

# Run the service
java -jar target/hotel-service-0.0.1-SNAPSHOT.jar
```

### 3. Run the Notification Service

```bash
cd notification-service

# Build the service
./mvnw clean package -DskipTests

# Run the service
java -jar target/notification-service-0.0.1-SNAPSHOT.jar
```

## Testing the Setup

1. The Hotel Service will be available at: http://localhost:8082
2. The Notification Service will be available at: http://localhost:8083

To test Kafka messaging:

1. Send a request to the Hotel Service to create a notification
2. Check the logs of the Notification Service to see if it received the message

## Troubleshooting

### Kafka Connection Issues

If services can't connect to Kafka:

1. Ensure Kafka is running: `docker-compose ps kafka`
2. Check Kafka logs: `docker-compose logs kafka`
3. Verify the bootstrap server configuration in application.properties matches the Kafka address

### Service Startup Issues

If services fail to start:

1. Check the logs: `docker-compose logs service-name`
2. Ensure all dependencies are running
3. Verify the configuration in application.properties
