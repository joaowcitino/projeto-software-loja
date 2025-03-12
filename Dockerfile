FROM eclipse-temurin:21-jre-alpine

COPY target/loja-0.0.1-SNAPSHOT.jar app.jar

<<<<<<< HEAD
ENTRYPOINT ["java", "-jar", "/app.jar"]
=======
ENTRYPOINT ["java", "-jar", "/app.jar"]
>>>>>>> b09784293c40b5b1449508ed133f893250a76497
