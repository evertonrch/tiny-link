FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="Everton Rocha"
WORKDIR /app
COPY target/tiny-link-0.0.1-SNAPSHOT.jar /app
EXPOSE 8080
CMD ["java", "-jar", "tiny-link-0.0.1-SNAPSHOT.jar"]