FROM amazoncorretto:17

ARG JAR_FILE=target/realworld-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} realworld-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "realworld-0.0.1-SNAPSHOT.jar"]