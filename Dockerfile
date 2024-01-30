FROM openjdk:17
EXPOSE 8080
ADD target/movie-catalog-service.jar movie-catalog-service.jar
ENTRYPOINT ["java","-jar","/movie-catalog-service.jar"]