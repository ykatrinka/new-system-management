FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test


FROM openjdk:21-slim
COPY --from=build app/news-service-api/build/libs/*.jar news-service.jar
ENTRYPOINT ["java","-jar","news-service.jar"]

#EXPOSE 8081
