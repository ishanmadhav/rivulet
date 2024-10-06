# Use an official OpenJDK runtime as a parent image
FROM openjdk:22-slim-bullseye

# Set the working directory inside the container
WORKDIR /app

# Copy the application JAR to the container
COPY target/rivulet-0.0.1-SNAPSHOT.jar /app/rivulet.jar

# Expose a port (optional, doesn't need to be fixed)
EXPOSE 8080

# Run the application with an environment variable for the port
ENTRYPOINT ["java", "-jar", "/app/rivulet.jar"]