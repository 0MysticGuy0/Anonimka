#build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR ./dispatcher
COPY mvn-settings.xml /root/.m2/settings.xml
COPY . ./
ARG NEXUS_URL
ENV NEXUS_URL=${NEXUS_URL}
RUN mvn clean package

#run stage
FROM maven:3.8.4-openjdk-17
WORKDIR /dispatcher

ARG jar_name=Dispatcher-1.0.jar
COPY --from=build /dispatcher/target/${jar_name} ./dispatcher.jar
CMD ["java", "-jar", "dispatcher.jar"]