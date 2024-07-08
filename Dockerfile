FROM maven:3.9.7-sapmachine-21 as BUILDER
ARG VERSION=0.0.1-SNAPSHOT
LABEL authors="Nurbek Malikov"

WORKDIR /build/
COPY pom.xml /build/
COPY src /build/src/

RUN mvn clean package
COPY target/LibraryManagementSystem-${VERSION}.jar target/application.jar

FROM openjdk:21
WORKDIR /app/

COPY --from=BUILDER /build/target/application.jar /app/
CMD ["java", "-Dspring.profiles.active=prod", "-jar", "application.jar"]