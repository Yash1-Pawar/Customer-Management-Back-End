package com.customer.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.app.model.AuthenticationResponse;
import com.customer.app.model.CustomerDTO;
import com.customer.app.service.AuthenticationService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/jwt")
public class JwtController {

	@Autowired
	private AuthenticationService authenticationService;

	@GetMapping("/hello")
	public ResponseEntity<String> sayHello() {
		System.out.println("Hello from unsecured endpoint");
		return ResponseEntity.ok("Hello from unsecured endpoint");
	}

	@PostMapping("/registerUser")
	public ResponseEntity<AuthenticationResponse> register(@RequestBody CustomerDTO customerDTO) {
		System.out.println("Registering User: "+ customerDTO);
		try {
			AuthenticationResponse authenticationResponse= authenticationService.register(customerDTO);
			return ResponseEntity.ok(authenticationResponse);
		} catch (Exception e) {
			return new ResponseEntity<AuthenticationResponse>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/getToken")
	public ResponseEntity<AuthenticationResponse> getToken(@RequestBody CustomerDTO customerDTO) {
		try {
			AuthenticationResponse authenticationResponse= authenticationService.authenticate(customerDTO);
			return ResponseEntity.ok(authenticationResponse);
		} catch (Exception e) {
			return new ResponseEntity<AuthenticationResponse>(HttpStatus.BAD_REQUEST);
		}
	}

}
