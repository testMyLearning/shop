
FROM eclipse-temurin:21-jre-jammy

COPY common-yml /common-yml/
# Путь изменился, так как мы собирали внутри /app/api-gateway
COPY api-gateway/target/api-gateway-1.0.0.jar /app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]