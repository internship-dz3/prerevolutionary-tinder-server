FROM openjdk:11
EXPOSE 8080
EXPOSE 5432
ADD target/prerevolutionary-tinder-server-docker.jar prerevolutionary-tinder-server-docker.jar
ENTRYPOINT ["java", "-jar", "/prerevolutionary-tinder-server-docker.jar"]