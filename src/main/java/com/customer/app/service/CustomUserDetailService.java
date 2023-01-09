package com.customer.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.customer.app.model.CustomerDTO;


@Component
public class CustomUserDetailService implements UserDetailsService {

	@Autowired
	private CustomerService customerService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		CustomerDTO customerDTO;
		try {
			customerDTO = customerService.getCustomerById(username);
		} catch (Exception e) {
			System.out.println("User Not Found");
			throw new UsernameNotFoundException("User Not Found");
		}
		return customerDTO;
	}

}
