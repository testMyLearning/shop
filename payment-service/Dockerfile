
FROM eclipse-temurin:21-jre-jammy
# Копируем общие настройки, если они нужны при запуске
COPY common-yml /common-yml/
# Берем JAR именно из папки сборки модуля
COPY payment-service/target/payment-service-1.0.0.jar /app.jar

EXPOSE 8086
ENTRYPOINT ["java", "-jar", "/app.jar"]