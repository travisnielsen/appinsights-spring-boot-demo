package io.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@EnableBinding(Source.class)
@RestController
public class KafkaSource {

    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaSource.class);

    @Autowired
    private Source source;

    // @Autowired
    // RestTemplate restTemplate;

    @PostMapping("/messages")
    public String sendMessage(@RequestBody String message) {
        LOGGER.trace("Sending new message: " + message);
        this.source.output().send(new GenericMessage<>(message));
        return message;
    }
}