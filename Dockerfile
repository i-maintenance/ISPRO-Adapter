FROM openjdk:7

# copy resources
WORKDIR /usr/src/app
COPY ./target/ISPROConsumer.jar ./app.jar

CMD ["java", "-jar", "./app.jar"]