package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/test")
@SecurityRequirement(name = "keycloak")
public class TestController {

	
	@GetMapping("/sso")
	public String getMethodName() {
		return "sdfsf";
	}
	
}
