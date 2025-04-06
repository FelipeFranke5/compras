FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal
RUN mkdir /opt/app
COPY target /opt/app
CMD ["java", "-jar", "/opt/app/target/compras-0.0.1-SNAPSHOT.jar"]
