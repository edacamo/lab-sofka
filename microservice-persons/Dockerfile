# Etapa de construcción
FROM maven:3.8.1-openjdk-17-slim AS build

# Directorio donde se copiará la aplicación
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn package -DskipTests

# Etapa de ejecución
FROM eclipse-temurin:17

EXPOSE 8001

COPY --from=build /app/target/microservice-persons*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]