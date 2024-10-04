# Sử dụng Tomcat 10.1.28 với JDK 21.0.1
FROM tomcat:10.1.28
RUN rm -rf /usr/local/tomcat/webapps/*
COPY ROOT.war /usr/local/tomcat/webapps

ENV PORT 8080

EXPOSE 8080

CMD ["catalina.sh", "run"]