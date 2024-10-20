FROM gradle:8.5-jdk21 AS build
COPY --chown=gradle:gradle . /app
WORKDIR /app
RUN gradle build -x test


FROM openjdk:21-slim
COPY --from=build app/auth-service-api/build/libs/*.jar auth-service.jar
ENTRYPOINT ["java","-jar","auth-service.jar"]

#EXPOSE 8003
