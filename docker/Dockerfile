FROM eclipse-temurin:25-jre-alpine

RUN addgroup -S jroom36 && adduser -S jroom36 -G jroom36

USER jroom36

WORKDIR /jroom36

COPY src/jroom36-api/target/jroom36-api-*.jar jroom36.jar

EXPOSE 8080

ENTRYPOINT ["java", "-Xmx512m", "-Xms256m", "-jar", "jroom36.jar"]
