FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test


FROM openjdk:21-slim
COPY app/build/libs/*.jar server-config.jar
ENTRYPOINT ["java","-jar","server-config.jar"]

#EXPOSE 8081
