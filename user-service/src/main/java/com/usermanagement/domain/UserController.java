package com.usermanagement.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Optional;

import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.telemetry.Duration;
import com.microsoft.applicationinsights.telemetry.RemoteDependencyTelemetry;
import com.microsoft.applicationinsights.web.internal.correlation.TraceContextCorrelation;

import com.usermanagement.domain.*;

@Controller
@RequestMapping(path="/")
@EnableBinding(Source.class)
public class UserController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private Source messageClient;
	
	@Autowired
	TelemetryClient telemetryClient;

	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED)
	public @ResponseBody String createUser (@RequestBody User user) throws Exception {

		boolean success = false;

		// Simulate uncaught exception. Any user with a first name starting with the letter M will fail
		if (user.getFirstName().startsWith("M")) {
			throw new Exception("can't add users starting with M");
		}

		try {
			user.setCreatedAt();
			int id = userRepository.create(user);
			user.setId(id);
		} catch (Exception ex) {
			LOGGER.trace("Error creating user: " + user.toString());
			throw new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "A transactional error occurred");
		}

		UserEvent event = new UserEvent(user, EventType.USER_CREATED);

		// create a ChildTraceParent from the request parent.
		String traceParent = TraceContextCorrelation.generateChildDependencyTraceparent();

		// Create ApplicationInsights format child correlationId from W3C format Traceparent.
		String childId = TraceContextCorrelation.createChildIdFromTraceparentString(traceParent);
		event.setTraceId(childId);

		long start = System.currentTimeMillis();

		try {
			messageClient.output().send(MessageBuilder.withPayload(event).build(), 30000L);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			long end = System.currentTimeMillis();
			RemoteDependencyTelemetry rdd = new RemoteDependencyTelemetry("enqueue", "enqueue", new Duration(end - start), success);
			rdd.setTimestamp(new Date());
			rdd.setType("Kafka");
			rdd.setId(childId);
			telemetryClient.trackDependency(rdd);
		}

		return "Added " + user.toString();
	}

	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String updateUser (@PathVariable("id") Integer id, @RequestBody User user ) {
		Assert.state(user != null, "User payload must not equal null");
		Assert.state(id != null, "The userId must not equal null");
		Assert.state(user.getId().equals(id), "The id supplied in the URI path does not match the payload");
		return "done";
	}

	@GetMapping
	public @ResponseBody Iterable<User> getAllUsers() {
		return userRepository.findAll();
	}

	@GetMapping("/{id}")
    public @ResponseBody Optional<User> getUser(@PathVariable Integer id) {
        return Optional.ofNullable(userRepository.findById(id));
    }

	@GetMapping(path="/probe")
	public @ResponseBody String defaultProbe() {
		return "Hello";
	}

	@DeleteMapping("/{id}")
    public @ResponseBody String deleteUser(@PathVariable Integer id) {
        userRepository.deleteById(id);
        return "Deleted " + id;
	}

}