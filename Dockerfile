FROM adoptopenjdk/openjdk11:jdk-11.0.4_11-alpine-slim
COPY target/newspaper-*.jar app.jar
ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -noverify -jar app.jar
