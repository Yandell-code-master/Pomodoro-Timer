## este archivo sirve para poder crear una imagen, osea un contenedor, entonces lo que hacemos es crear la imagen que va a utilizar dos
# otras imagenes que serían una de maven que me va a servir para poder

## por ejemplo aquí lo que hacemos es decir que vamos a usar la imagen de maven para poder compilar mi codigo
# entiendo mucho mejor esto es como si fuero por fases esto primero que vemos acá es toda la fase que hacemos para poder hacer el
# proyecto java en un binario compilado osea el .jar que es un archivo que comprime todos los .class
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
# clean y package son dos comandos completamente diferentes 
# lo que hace clean es limpiar la carpeta target que ahí es donde se guarda el archivo .jar con todo el proyecto compilado
# y el package lo que hace es empaquetar el proyecto que encuentra en el directorio de trabajo -DskipTests es decirle a package que
# se salte los tests unitarios
RUN mvn clean package -DskipTests

# aquí es como si fuera la segunda fase y lo que hacemos es de la primera fase obtenemos el archivo .jar, lo hacemos de esta forma ya 
# docker lo que hace es solo quedarse con la ultima fase que se ejecuta y de esta forma lo que hacemos es terminar con un contenedor 
# que tiene solamente el jdk slim y el archivo que obtuvimos antes
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
COPY .env .
EXPOSE 8080
# esto se llama exec Form
# el primer valor de la lista es lo que se llama el Rey del contenedor que es el preceso PID 1 en este caso es java, y lo que pasa 
# es que cuando se quiere detener el contendor docker envia una señal llamada SIGTERM y como tenemos a java como PID 1 entonces 
# esta señal le llega directo a java lo que hace que se apague de forma correcta sin ningun error, no usamos intermediarios como
# podría ser powershell
ENTRYPOINT ["java", "-jar", "app.jar"]