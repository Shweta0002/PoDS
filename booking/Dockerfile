# Use a Linux-based base image with OpenJDK and Maven installed
FROM maven:3.8-openjdk-17 AS build

# Set working directory
WORKDIR /app

# Copy the Maven project files
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package

# Start with a fresh base image
FROM openjdk:17-jdk-slim

# Set the working directory to /app
WORKDIR /app

# Copy the JAR file from the build stage to the /app directory in the new image
COPY --from=build /app/target/*.jar app.jar

# Expose the port
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "app.jar"]