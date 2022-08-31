FROM gradle:7.2 as builder
WORKDIR /usr/src/app
COPY src ./src
COPY build.gradle .
RUN ["gradle", "bootJar"]

EXPOSE 8080

FROM mcr.microsoft.com/openjdk/jdk:17-ubuntu
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder /usr/src/app/${JAR_FILE} app.jar
#RUN apt-get update; apt-get install curl;
ENTRYPOINT ["java", "-jar", "/app.jar"]