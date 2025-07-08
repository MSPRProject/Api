FROM maven:3.9.6-eclipse-temurin-21

WORKDIR /app

COPY pom.xml .
COPY src ./src

EXPOSE 8080

CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.arguments=--server.address=0.0.0.0"]
CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.arguments=--server.address=0.0.0.0"]
