# Use a base image with Java 11
FROM openjdk:11-jre-slim

RUN apt-get update && apt-get install -y iputils-ping net-tools

ENV TZ=UTC

# Copy the compiled JAR file into the container
COPY build/libs/fraud-detection-application-0.0.1-SNAPSHOT.jar /app/fraud-detection-application.jar


# Set the working directory
WORKDIR /app

# Expose the port the app runs on
EXPOSE 8080

# Run the JAR file when the container starts
ENTRYPOINT ["java", "-jar", "/app/fraud-detection-application.jar"]



# FROM adoptopenjdk:11-jre
# # Install networking tools for debugging
# RUN apt-get update && apt-get install -y iputils-ping net-tools curl awscli

# ENV TZ=UTC

# # Run the JAR file when the container starts
# ENTRYPOINT ["sleep", "30000s"]

