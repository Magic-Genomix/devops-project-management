# Use an OpenJDK 21 base image
FROM openjdk:21-jdk-slim AS builder

# Install Maven
RUN apt-get update && apt-get install -y maven

# Set the working directory inside the container
WORKDIR /app

# Copy the Maven project files (pom.xml) first to take advantage of Docker's cache
COPY pom.xml /app/

# Download dependencies (this will be cached unless pom.xml changes)
RUN mvn clean install -DskipTests

# Now copy the entire source code
COPY src /app/src

# Build the project
RUN mvn clean package -DskipTests

# Second stage: Use OpenJDK to run the application
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the builder image to the current image
COPY --from=builder /app/target/app-0.0.1-SNAPSHOT.jar /app/event-project.jar

# Expose the port your Spring Boot app will run on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/event-project.jar"]
