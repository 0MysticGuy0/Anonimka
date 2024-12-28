#build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR ./searcher
COPY mvn-settings.xml /root/.m2/settings.xml
COPY . ./
ARG NEXUS_URL
ENV NEXUS_URL=${NEXUS_URL}
RUN mvn clean package

#run stage
FROM maven:3.8.4-openjdk-17
WORKDIR /searcher

ARG jar_name=CompanionSearcher-1.0.jar
COPY --from=build /searcher/target/${jar_name} ./searcher.jar
CMD ["java", "-jar", "searcher.jar"]