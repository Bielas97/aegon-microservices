FROM openjdk:15
WORKDIR ./app
EXPOSE 8200
ENV JAVA_OPTS="--enable-preview"
ADD application/target/table-service.jar app.jar
CMD ["sh", "-c", "java --enable-preview -jar app.jar ${SEASON}" ]
