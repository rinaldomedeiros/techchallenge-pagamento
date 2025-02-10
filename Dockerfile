# Utiliza uma imagem base com o JDK 17
FROM openjdk:17-jdk-slim

# Define o diretório de trabalho
WORKDIR /app

# Copia o jar gerado (supondo que o jar se chame payment-microservice.jar)
COPY target/order-payment-1.0.0.jar app.jar

# Expõe a porta em que a aplicação rodará (por exemplo, 8080)
EXPOSE 8080

# Comando para executar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
