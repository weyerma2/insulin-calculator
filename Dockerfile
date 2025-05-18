# Dockerfile für Spring Boot Projekt mit Java 21
FROM eclipse-temurin:21-jdk

WORKDIR /app

COPY target/insulincalculator-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
