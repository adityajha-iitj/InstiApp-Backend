# Stage 1: Build with Maven
FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar

# Accept datasource credentials via build arguments
ARG SPRING_DATASOURCE_URL
ARG SPRING_DATASOURCE_USERNAME
ARG SPRING_DATASOURCE_PASSWORD

# Set environment variables inside the container (available at runtime)
ENV SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
ENV SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
ENV SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}

EXPOSE 8080

# Default active profile is 'dev'
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=dev"]
