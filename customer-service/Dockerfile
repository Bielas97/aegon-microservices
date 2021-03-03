FROM openjdk:15
WORKDIR ./app
EXPOSE 8300
ENV JAVA_OPTS="--enable-preview"
ADD application/target/customer-service.jar app.jar
CMD ["sh", "-c", "java --enable-preview -jar app.jar ${SEASON}" ]
