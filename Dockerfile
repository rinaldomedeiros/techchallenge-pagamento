FROM openjdk:17-jdk-slim

VOLUME /tmp

# Define o nome do JAR gerado pelo Maven
ARG JAR_FILE=target/order-payment-0.0.1-SNAPSHOT.jar

# Copia o JAR para a imagem com o nome app.jar
COPY ${JAR_FILE} app.jar


EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
