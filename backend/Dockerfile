# 1. Usamos una imagen de Maven con JDK 17 para compilar el código
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
# Copiamos el archivo de configuración de Maven y el código fuente
COPY pom.xml .
COPY src ./src
# Ejecutamos la "construcción" del proyecto saltando los tests para ir más rápido
RUN mvn clean package -DskipTests

# 2. Usamos una imagen mucho más pequeña que solo tiene el JRE para ejecutar
FROM openjdk:17-jdk-slim
WORKDIR /app
# Traemos el archivo .jar generado en la etapa anterior
# NOTA: Asegúrate de que el nombre coincida con el que genera tu pom.xml
COPY --from=build /app/target/*.jar app.jar
# Exponemos el puerto donde corre tu backend (comúnmente el 8080)
EXPOSE 8080
# Comando para iniciar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]