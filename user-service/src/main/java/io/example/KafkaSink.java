package io.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import io.micrometer.core.instrument.binder.kafka.KafkaConsumerMetrics;

@EnableBinding(Sink.class)
public class KafkaSink {
   private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSink.class);

   @Bean
   KafkaConsumerMetrics kafkaConsumerMetrics() {
      return new KafkaConsumerMetrics();
   }

   @StreamListener(Sink.INPUT)
   public void handleMessage(String message) {
      LOGGER.trace("New message received: " + message);
      LOGGER.info("New message received: " + message);
   }
}