# Dodo Newspaper

This repository contains the code and the API documentation of a newspaper application.

## Requirements

* Docker
* JDK 11 or newer & Maven for development.

## API Documentation

The API documentation is located at `<DOMAIN>/swagger-ui.html`.

E.g., when running the application locally, it is located at `http://localhost:8080/swagger-ui.html`.

## Building the Docker image

* Run `./mvnw clean install` to build the application. 
* Run `docker build -t newspaper .` to build a docker image.
* Run `docker run -p 8080:8080 newspaper` to run the docker image.

### Technologies

This application is based on Spring Boot framework. The most notable ones are:

* [Spring Boot](https://spring.io/projects/spring-boot)
* [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)

