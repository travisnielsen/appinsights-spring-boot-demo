# FROM openjdk:11.0.1-jre-slim-sid
FROM openjdk:8-jre-slim-stretch

RUN apt-get update && apt-get install -y --no-install-recommends nano

# Added user to container to support writing log files to the home directory
# RUN useradd -ms /bin/bash javauser
# USER javauser
# WORKDIR /home/javauser

# Add App Insights Java Agent
COPY applicationinsights-agent-2.3.1.jar /usr/share/myservice/applicationinsights-agent-2.3.1.jar
COPY AI-Agent.xml /usr/share/myservice/AI-Agent.xml
# COPY applicationinsights-agent-2.3.1.jar ./applicationinsights-agent-2.3.1.jar
# COPY AI-Agent.xml ./AI-Agent.xml

# Add the service itself
ARG JAR_FILE
COPY target/${JAR_FILE} ./myservice.jar

EXPOSE 8120/tcp
ENTRYPOINT ["java", "-jar", "/usr/share/myservice/myservice.jar", "-javaagent:/usr/share/myservice/applicationinsights-agent-2.3.1.jar"]
# ENTRYPOINT ["java", "-jar", "/home/javauser/myservice.jar", "-javaagent:/home/javauser/applicationinsights-agent-2.3.1.jar"]
# ENTRYPOINT ["tail", "-f", "/dev/null"]