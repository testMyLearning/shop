
FROM eclipse-temurin:21-jre-jammy
COPY common-yml /common-yml/
COPY product-service/target/*.jar /app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "/app.jar"]