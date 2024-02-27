FROM eclipse-temurin:21-jdk-alpine
COPY ./build/libs/terryscape-0.1.0.jar server.jar

RUN addgroup -S terryscape && adduser -S terryscape -G terryscape

USER terryscape

EXPOSE 8080

ENTRYPOINT exec java $JAVA_OPTS -jar server.jar