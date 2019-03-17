# Introduction

This is a sample that fully demonstrates the use of Azure Monitor and Application Insights for end-to-end telemetry across a client (Reach + Electron), a microservices back-end (Java Spring Boot), and dependencies hosted in Azure (Event Hubs, MySQL, Azure API Management). The microservices back-end can be hosted in Azure Kubernetes Service (AKS) Key monitoring features highlighted in this sample is:

- Auto detection of dependencies
- Telemetry filtering
- Logging and traces
- Container log management via Container Insights

## Build and Run

Make sure you're currently using JDK 1.8. Clone this repository, ensure you have Docker running, and execute the following command to build: `mvn clean install`

You can run this applicaiton directly via this command:

```#!/bin/bash
java -jar user-service/target/user-service-0.0.2-SNAPSHOT.jar -javaagent:user-service/applicationinsights-agent-2.3.1.jar
```
Or you can launch the Docker container:

```#!/bin/bash
docker run -d -P trniel/user-service:0.0.2-SNAPSHOT
```

In either case, the user-service will be available at local host port 8210.

## Deploy to Kubernetes (AKS)

TBD