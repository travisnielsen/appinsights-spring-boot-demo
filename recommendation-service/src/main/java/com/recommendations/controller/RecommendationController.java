package com.recommendations.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecommendationController {

    @RequestMapping("/")
    String hello() {
        return "Hello World!";
    }

    /**
     * Handles Kubernetes readiness and liveliness probes
     * Ensure Kubernetes deployment manifests specify the /probe path
     */
    @GetMapping(path="/probe")
	public @ResponseBody String defaultProbe() {
		return "Hello";
	}
}
