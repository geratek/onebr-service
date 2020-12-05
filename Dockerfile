FROM openjdk:11.0.6-jre
MAINTAINER Lacau <llacau@gmail.com>

RUN mkdir -p /home/onebr/images
COPY config/analytics.json /home/onebr/analytics.json

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/share/myservice/myservice.jar

ENV PROFILE dev
ENV TZ="America/Sao_Paulo"

ENTRYPOINT ["java", "-cp", "/usr/share/myservice/myservice.jar", "org.springframework.boot.loader.PropertiesLauncher", "--spring.profiles.active=${PROFILE}"]