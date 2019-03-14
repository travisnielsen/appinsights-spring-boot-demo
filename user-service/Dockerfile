# FROM openjdk:11.0.1-jre-slim-sid
FROM openjdk:8-jre-slim-stretch

EXPOSE 8120/tcp

# Add App Insights Java Agent
COPY applicationinsights-agent-2.3.1.jar /usr/share/myservice/applicationinsights-agent-2.3.1.jar
COPY AI-Agent.xml /usr/share/myservice/AI-Agent.xml

# Add the service itself
ARG JAR_FILE
COPY target/${JAR_FILE} /usr/share/myservice/myservice.jar
ENTRYPOINT ["java", "-javaagent:/usr/share/myservice/applicationinsights-agent-2.3.1.jar", "-jar", "/usr/share/myservice/myservice.jar"]