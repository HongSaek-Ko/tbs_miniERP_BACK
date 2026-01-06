# build
FROM gradle:8-jdk17 AS build
WORKDIR /app
COPY . .
RUN gradle clean bootJar --no-daemon -x test

# run
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
CMD ["sh", "-c", "java -jar app.jar --server.port=${PORT:-10000}"]
