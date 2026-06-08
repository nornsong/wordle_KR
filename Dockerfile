FROM gradle:9-jdk21 AS build
WORKDIR /app
ENV GRADLE_OPTS="-Dorg.gradle.jvmargs=-Xmx384m -Dorg.gradle.workers.max=1 -Dorg.gradle.daemon=false"
COPY . .
RUN gradle clean bootWar --no-daemon --max-workers=1

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.war app.war
EXPOSE 8080
ENTRYPOINT ["java", "-Xms128m", "-Xmx320m", "-jar", "app.war"]