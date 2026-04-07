FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S jroom36 && adduser -S jroom36 -G jroom36

USER jroom36

WORKDIR /app

COPY src/jroom36-api/target/jroom36-api-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "app.jar"]
