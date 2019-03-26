package com.recommendations.domain.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;

import io.micrometer.core.instrument.binder.kafka.KafkaConsumerMetrics;

import java.util.Date;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import com.microsoft.applicationinsights.web.internal.RequestTelemetryContext;
import com.microsoft.applicationinsights.web.internal.ThreadContext;
import com.microsoft.applicationinsights.web.internal.correlation.tracecontext.Traceparent;

@EnableBinding(Sink.class)
public class UserProcessor {

    @Autowired
    TelemetryClient telemetryClient;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserProcessor.class);

    @Bean
    KafkaConsumerMetrics kafkaConsumerMetrics() {
        return new KafkaConsumerMetrics();
    }

    @StreamListener(Sink.INPUT)
    public void handleMessage(Message<UserEvent> userEvent) {

        // Retrieve the parent correlation-id from the incoming message.
        String parentCorrelationId = userEvent.getPayload().getTraceId();
        if (parentCorrelationId == null) { return; }

        // TODO: Need more research on getting message received time in Kafka. kafka_receivedTimstamp seems to match
        // creation time on the producer. Go wtih current time for now.
        // MessageHeaders headers = userEvent.getHeaders();
        // Long receivedTimestamp = (Long) headers.get("kafka_receivedTimestamp");
        // RequestTelemetry requestTelemetry = createRequestTelemetry(parentCorrelationId, receivedTimestamp);
        RequestTelemetry requestTelemetry = createRequestTelemetry(parentCorrelationId, System.currentTimeMillis());
        telemetryClient.trackRequest(requestTelemetry);
    }

    /**
     * A method to create RequestTelemetry from parentCorrelationId
     * @param parentCorrelationId
     * @return RequestTelemetry
     */
    private RequestTelemetry createRequestTelemetry(String parentCorrelationId, Long startTimestamp) {
        RequestTelemetryContext requestTelemetryContext = new RequestTelemetryContext(new Date().getTime());
        RequestTelemetry requestTelemetry = requestTelemetryContext.getHttpRequestTelemetry();

        // Get Incoming TraceId from the parentCorrelationID (AI Format)
        String incomingTraceId = getIncomingTraceParent(parentCorrelationId);

        // Create a new traceparent for child request
        Traceparent traceparent = new Traceparent(0, incomingTraceId, null, 0);

        // Set the Id of the RequestTelemetry
        requestTelemetry.setId("|" + traceparent.getTraceId() + "." + traceparent.getSpanId()
                + ".");

        // set operation id as W3C TraceID
        requestTelemetry.getContext().getOperation().setId(incomingTraceId);

        // Set parent id of the request as the incoming parentId
        requestTelemetry.getContext().getOperation().setParentId(parentCorrelationId);

        requestTelemetry.setName("dequeue");
        requestTelemetry.setSource("Kafka");
        requestTelemetry.setTimestamp(new Date());
        requestTelemetry.setDuration(new Duration(System.currentTimeMillis() - startTimestamp));

        // Set the RequestTelemetryContext in the TLS.
        ThreadContext.setRequestTelemetryContext(requestTelemetryContext);
        return requestTelemetry;
    }

    private String getIncomingTraceParent(String diagnosticId) {
        diagnosticId = diagnosticId.substring(1, diagnosticId.length()-1);
        return diagnosticId.split("\\.")[0];
    }
}