FROM openjdk:17-jdk-slim
COPY build/libs/matna-0.0.1-SNAPSHOT.jar
EXPOSE 8080
CMD {"java", "-jar", "matna-0.0.1-SNAPSHOT.jar"}
