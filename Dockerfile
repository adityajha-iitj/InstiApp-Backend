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

# At runtime, load external config from /etc/secrets/ if present.
# SPRING_PROFILES_ACTIVE can be set in Render to "dev" (or others).
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar \
  --spring.config.additional-location=file:/etc/secrets/ \
  ${SPRING_PROFILES_ACTIVE:+--spring.profiles.active=${SPRING_PROFILES_ACTIVE}}"]