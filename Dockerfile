FROM tomcat:10.1-jdk17
COPY target/laundry_management.war /usr/local/tomcat/webapps/ROOT.war
EXPOSE 8080
CMD ["catalina.sh", "run"]
