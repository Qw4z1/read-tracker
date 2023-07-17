FROM gradle:7-jdk17 AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle buildFatJar --no-daemon

FROM amazoncorretto:17.0.7-alpine
EXPOSE 80:80
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/read-tracker.jar
ENTRYPOINT ["java","-jar","/app/read-tracker.jar"]