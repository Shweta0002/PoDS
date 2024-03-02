FROM openjdk:8-jre-alpine

ENV H2_VERSION=1.4.200

# Download and extract H2 database
RUN wget -O h2.zip http://www.h2database.com/h2-$H2_VERSION.zip && \
    unzip h2.zip -d /opt/h2 && \
    rm h2.zip

EXPOSE 1521

CMD ["java", "-cp", "/opt/h2/bin/h2*.jar", "org.h2.tools.Server", "-ifNotExists"]
