FROM alpine/git AS clone
WORKDIR /app
RUN git clone https://github.com/acc4dev404/RESTAPI2.git

FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
WORKDIR /app
COPY --from=clone /app/RESTAPI2 /app
RUN mvn package

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/RESTAPI-0.0.1-SNAPSHOT.jar /app
EXPOSE 8000
CMD ["java", "-jar", "RESTAPI-0.0.1-SNAPSHOT.jar"]