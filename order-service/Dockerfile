FROM eclipse-temurin:21-jre-jammy
# Копируем общие настройки, если они нужны при запуске
COPY common-yml /common-yml/
# Берем JAR именно из папки сборки модуля
COPY order-service/target/order-service-1.0.0.jar /app.jar

EXPOSE 8085
ENTRYPOINT ["java", "-jar", "/app.jar"]