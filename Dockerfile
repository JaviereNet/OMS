# Multi-stage Dockerfile para construir y ejecutar la aplicación Spring Boot
# Stage 1: build con Maven
FROM maven:3.9.4-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copiamos los ficheros necesarios para aprovechar la cache de dependencias
COPY pom.xml mvnw .mvn/ ./
COPY src ./src

# Ejecutar el build (sin tests para acelerar)
RUN mvn -B -DskipTests package

# Stage 2: runtime
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiamos el JAR generado desde el stage de build
COPY --from=build /workspace/target/*.jar app.jar

# Exponer puerto (Render usará la variable PORT y la aplicacion la recogerá)
EXPOSE 8080

# Opcionales: limitar memoria por defecto
ENV JAVA_TOOL_OPTIONS="-Xms256m -Xmx512m"

# Ejecutar la aplicación usando el puerto que Render proporciona en $PORT (fallback 8080)
# Usamos shell form para que la variable $PORT sea expandida cuando se arranque el contenedor
ENTRYPOINT ["sh", "-c", "java $JAVA_TOOL_OPTIONS -Djava.security.egd=file:/dev/./urandom -Dserver.port=${PORT:-8080} -jar /app/app.jar"]

