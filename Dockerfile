FROM adoptopenjdk/openjdk11:latest
ADD /target/reading-is-good-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT java -jar /app.jar