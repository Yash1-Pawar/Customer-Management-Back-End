package com.customer.app.service;

import lombok.RequiredArgsConstructor;

import java.net.UnknownHostException;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customer.app.config.JwtService;
import com.customer.app.model.AuthenticationResponse;
import com.customer.app.model.CustomerDTO;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final CustomerService customerService;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	public AuthenticationResponse register(CustomerDTO customerDTO) throws Exception {
		customerDTO.setRoles(Roles.USER.toString());
		customerDTO.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
		customerService.addCustomer(customerDTO);
		var user = User.builder().username(customerDTO.getName())
						.password(passwordEncoder.encode(customerDTO.getPassword()))
						.roles(Roles.USER.toString())
						.build();
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).tokenExpiration(jwtService.getTokenExpiration(jwtToken)).build();
	}

	public AuthenticationResponse authenticate(CustomerDTO customerDTO) throws Exception {
		authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(customerDTO.getUsername(), customerDTO.getPassword()));
		var user = customerService.getCustomerById(customerDTO.getUsername());
		var jwtToken = jwtService.generateToken(user);
		return AuthenticationResponse.builder().token(jwtToken).tokenExpiration(jwtService.getTokenExpiration(jwtToken)).build();
	}
}
