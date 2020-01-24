FROM maven:3.6.3-jdk-11-slim as BUILD
WORKDIR /texas
COPY ./ .
RUN mvn clean install

FROM jetty:9.4.18-jre11
EXPOSE 80 4711
RUN echo "jetty.http.port=80" >> /var/lib/jetty/start.d/http.ini
COPY --from=BUILD /texas/server/target/*.war /var/lib/jetty/webapps/ROOT.war
USER root