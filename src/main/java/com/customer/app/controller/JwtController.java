package com.customer.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.app.model.AuthenticationResponse;
import com.customer.app.model.CustomerDTO;
import com.customer.app.model.RestPasswordDTO;
import com.customer.app.service.AuthenticationService;
import com.customer.app.service.CustomerService;

@CrossOrigin(value = "*")
@RestController
@RequestMapping("/jwt")
public class JwtController {

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private CustomerService customerService;

	@GetMapping("/hello")
	public ResponseEntity<String> sayHello() {
		System.out.println("Hello from unsecured endpoint");
		return ResponseEntity.ok("Hello from unsecured endpoint");
	}

	@PostMapping("/registerUser")
	public ResponseEntity<String> register(@RequestBody CustomerDTO customerDTO) {
		System.out.println("Registering User: "+ customerDTO);
		try {
			authenticationService.register(customerDTO);
			return new ResponseEntity<String>("Customer Registered Successfully", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

//	login
	@PostMapping("/getToken")
	public ResponseEntity<AuthenticationResponse> getToken(@RequestBody CustomerDTO customerDTO) {
		try {
			AuthenticationResponse authenticationResponse= authenticationService.authenticate(customerDTO);
			return ResponseEntity.ok(authenticationResponse);
		} catch (Exception e) {
			return new ResponseEntity<AuthenticationResponse>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/resetPassword/{id}")
	public ResponseEntity<String> resetPassword(@RequestBody String newPassword, @PathVariable String id) {
		try {
			customerService.resetPassword(newPassword, id);
			return new ResponseEntity<>("Password Reset Successful", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
