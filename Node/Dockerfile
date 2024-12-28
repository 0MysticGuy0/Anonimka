#build stage
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR ./node
COPY mvn-settings.xml /root/.m2/settings.xml
COPY . ./
ARG NEXUS_URL
ENV NEXUS_URL=${NEXUS_URL}
RUN mvn clean package

#run stage
FROM maven:3.8.4-openjdk-17
WORKDIR /node

ARG jar_name=Node-1.0.jar
COPY --from=build /node/target/${jar_name} ./node.jar
CMD ["java", "-jar", "node.jar"]