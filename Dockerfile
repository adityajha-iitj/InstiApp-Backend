# Stage 1: build with Maven
FROM maven:3.9.0-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: runtime
FROM eclipse-temurin:17-jdk-jammy
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

# At runtime rely on bootstrap.properties → AWS SSM Parameter Store
# SPRING_PROFILES_ACTIVE defaults to 'dev' if you don’t set it in the environment
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar \
  --spring.profiles.active=${SPRING_PROFILES_ACTIVE:-dev}"]
