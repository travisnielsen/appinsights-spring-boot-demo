# Introduction

This is a sample that fully demonstrates the use of Azure Monitor and Application Insights for end-to-end telemetry across a client (React), a microservices back-end (Java Spring Boot), and dependencies hosted in Azure (Event Hubs, Azure SQL Database, Azure API Management). The microservices back-end can be hosted in Azure Kubernetes Service (AKS) Key monitoring features highlighted in this sample is:

- Automatic and SDK-instrumented dependency tracking
- Telemetry filtering (i.e. exempt probe requests)
- Application logging and traces
- Container log management via Container Insights
- Micrometer statictics

## Configure Azure Services

This demo requires the following services deployed to an Azure Subscription:

- Application Insights
- Azure API Management: Developer SKU
- Event Hub Namespace: Standard tier with Kafka enabled
- Azure SQL Database: General Purpose SKU to support VNET Service Endpoints.
- Azure Kubernetes Service (AKS): Use Advanced Networking option to deploy into a VNET

### Database Configuration


## Configure Local Environment

Clone this repository and and ensure you're currently using JDK 1.8 in your development environment. Create two files: `application.properties` and `logback-spring.xml` and copy the content from `application.properties.sample` and `logback-spring.xml.sample` into them. Add configuration values based on your current Azure environment.

## Build and Run

Ensure you have Docker running and execute the following command to build: `mvn clean install`

This will automatically build and push a Docker container to your local computer. Start the container via:

```#!/bin/bash
docker run -d -P trniel/user-service:0.0.3-SNAPSHOT
```

The conainter will be accessible via an auto-assigned port. You can identify the port via the `docker ps` command.
Alternatively, you can run this applicaiton directly from the project root:

```#!/bin/bash
java -javaagent:user-service/applicationinsights-agent-2.3.1.jar -jar user-service/target/user-service-0.0.3-SNAPSHOT.jar
```

In this case, the user-service will be available at local host port 8210.

## Deploy to Kubernetes (AKS)

TBD