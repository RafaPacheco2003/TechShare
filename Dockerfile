# Etapa 1: Build
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (esto se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir la aplicación (saltando tests para build más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Crear directorio para imágenes subidas
USER root
RUN mkdir -p /app/uploaded-images && chown -R spring:spring /app/uploaded-images
USER spring:spring

# Exponer el puerto
EXPOSE 8080

# Variables de entorno por defecto (se pueden sobrescribir en docker-compose)
ENV SPRING_PROFILES_ACTIVE=docker

# Ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
