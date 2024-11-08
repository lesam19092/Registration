FROM openjdk:21
LABEL authors="lesam19092"

COPY ./target/registration.jar registration.jar

ENTRYPOINT ["java","-jar","/registration.jar"]