Booking Service
	•	Make new bookings
	•	Cancel bookings
	•	Maintain user bookings history
	•	Check availability (by calling Hotel Service)
	
	
Docker-compose yml changes

version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql
    restart: always
    environment:
      MYSQL_ROOT_PASSWORD: mySQLdb@25
      MYSQL_DATABASE: bookingdb
    ports:
      - "3307:3306"
    volumes:
      - mysql-data:/var/lib/mysql

  booking-service:
    build: .
    container_name: booking-service
    depends_on:
      - mysql
    ports:
      - "8081:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/bookingdb?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
      SPRING_DATASOURCE_USERNAME: root
      SPRING_DATASOURCE_PASSWORD: mySQLdb@25

volumes:
  mysql-data:
