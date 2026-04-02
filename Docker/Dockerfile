# Build stage
FROM maven:3.9.8-eclipse-temurin-25 AS build
WORKDIR /workspace

# Copia pom+source per buildare il jar fat/cool
COPY Selenium/pom.xml ./
COPY Selenium/src ./src

RUN mvn -f pom.xml -DskipTests clean package

# Runtime stage
FROM eclipse-temurin:25-jre
WORKDIR /app

# Copia il JAR generato
COPY --from=build /workspace/target/SeleniumApp.jar ./SeleniumApp.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "SeleniumApp.jar"]
