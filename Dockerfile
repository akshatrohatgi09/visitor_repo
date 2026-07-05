# Use an OpenJDK 17 base image
FROM eclipse-temurin:17-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the host machine to the Docker container
COPY target/evisitor-1.0.0.jar /app/app.jar

# Expose the port that the Spring Boot application runs on (default 8080)
EXPOSE 8006

# Command to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
