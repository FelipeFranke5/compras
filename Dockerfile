FROM eclipse-temurin:17.0.14_7-jre-ubi9-minimal
RUN mkdir /opt/app
COPY target/compras-0.0.1-SNAPSHOT.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
