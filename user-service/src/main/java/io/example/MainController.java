package io.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.example.User;
import io.example.UserRepository;

@Controller
@RequestMapping(path="/")
public class MainController {

	@Autowired
	private UserRepository userRepository;

	private static final Logger LOGGER = LoggerFactory.getLogger(MainController.class);

	@PostMapping(path="/add")
	public @ResponseBody String addNewUser (@RequestParam String name, @RequestParam String email) throws Exception {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		if (name.startsWith("M")) {
			throw new Exception("can't add users starting with M");
		}

		User n = new User();
		n.setName(name);
		n.setEmail(email);
		userRepository.save(n);
		LOGGER.trace("Saved new user: " + name);
		return "Saved";
	}

	@GetMapping(path="/all")
	public @ResponseBody Iterable<User> getAllUsers() {
		// This returns a JSON or XML with the users
		LOGGER.trace("Querying all users");
		return userRepository.findAll();
	}

	@GetMapping(path="/probe")
	public @ResponseBody String defaultProbe() {
		return "Hello";
	}

	@GetMapping(path="/")
	public @ResponseBody String homePath() {
		return "Hello";
	}

}