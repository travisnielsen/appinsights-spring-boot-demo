# FROM openjdk:11.0.1-jre-slim-sid
FROM openjdk:8-jre-slim-stretch

# Add App Insights Java Agent
COPY applicationinsights-agent-2.3.1.jar /usr/share/myservice/applicationinsights-agent-2.3.1.jar
COPY AI-Agent.xml /usr/share/myservice/AI-Agent.xml

# Add the service itself
ARG JAR_FILE
COPY target/${JAR_FILE} ./myservice.jar

EXPOSE 8120/tcp
ENTRYPOINT ["java", "-jar", "/usr/share/myservice/myservice.jar", "-javaagent:/usr/share/myservice/applicationinsights-agent-2.3.1.jar"]
