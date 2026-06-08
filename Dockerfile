FROM gradle:9-jdk21 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootWar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-Xms128m", "-Xmx512m", "-jar", "app.war"]