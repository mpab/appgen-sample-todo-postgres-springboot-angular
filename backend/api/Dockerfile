FROM alpine:3.23

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN apk update && apk add openjdk25

RUN ./mvnw clean install -DskipTests

CMD ["./mvnw", "spring-boot:run"]
