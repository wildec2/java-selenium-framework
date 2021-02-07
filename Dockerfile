FROM gradle:jdk11
RUN mkdir selenium
WORKDIR /java-selenium-framework
COPY . /java-selenium-framework
RUN gradle clean build