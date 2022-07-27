FROM adoptopenjdk/openjdk11

COPY target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "-Dspring.config.activate.on-profile=prod", "/app.jar"]