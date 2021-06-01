FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/routing-0.0.1-SNAPSHOT.jar routing.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/routing.jar"]
