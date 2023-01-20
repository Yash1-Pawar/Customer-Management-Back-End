package com.customer.app.controller;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.customer.app.Exception.CustomerException;
import com.customer.app.model.CustomerDTO;
import com.customer.app.model.RestPasswordDTO;
import com.customer.app.service.CustomerService;

@CrossOrigin
@RestController
@RequestMapping("/customerApp")
public class CustomerController {

	@Autowired
	private CustomerService customerService;

	@GetMapping("/getAllCustomers")
	public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
		try {
			List<CustomerDTO> customerDTOs = customerService.getAllCustomers();
			System.out.println(customerDTOs);
			return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCustomer/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable String id) {
		CustomerDTO customerDTOs = customerService.getCustomerById(id);
		System.out.println(customerDTOs);
		if (Objects.nonNull(customerDTOs))
			return new ResponseEntity<>(customerDTOs, HttpStatus.OK);
		else
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping("/updateCustomer/{id}")
	public ResponseEntity<String> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable String id) {
		try {
			customerService.updateCustomer(customerDTO, id);
			return new ResponseEntity<>("Customer successfully updated", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Customer not found with the id: " + id, HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/addFriend/{id}")
	public ResponseEntity<Object> addFriend(@PathVariable String id, @RequestBody List<String> friendIds) {
		try {
			CustomerDTO customerDTO = customerService.addFriend(id, friendIds);
			return new ResponseEntity<>(customerDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Some error occured while adding friend", HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/addFollower/{id}")
	public ResponseEntity<Object> addFollower(@PathVariable String id, @RequestBody List<String> followerIds) {
		try {
			CustomerDTO customerDTO = customerService.addFollowers(id, followerIds);
			return new ResponseEntity<>(customerDTO, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Some error occured while adding friend", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
		try {
			customerService.deleteCustomer(id);
			return new ResponseEntity<>("{\"response\" : \"Customer deleted successfully\"}", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Customer not found with the id: " + id, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/addCustomers")
	public ResponseEntity<String> bulkAdd(@RequestBody List<CustomerDTO> customerDTO) {
		try {
			customerService.bulkAdd(customerDTO);
			return new ResponseEntity<>("Bulk addition done", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/helloAdmin")
	public ResponseEntity<String> sayHelloAdmin() {
		System.out.println("Hello from secured endpoint");
		return ResponseEntity.ok("Hello Admin from secured endpoint");
	}

	@GetMapping("/helloUser")
	public ResponseEntity<String> sayHelloUser() {
		System.out.println("Hello from secured endpoint");
		return ResponseEntity.ok("Hello User from secured endpoint");
	}
	
	@PutMapping("/changePassword/{id}")
	public ResponseEntity<String> changePassword(@RequestBody RestPasswordDTO restPasswordDTO, @PathVariable String id) {
		try {
			customerService.changePassword(restPasswordDTO, id);
			return new ResponseEntity<>("Password Changed Successful", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/follow/{id}")
	public ResponseEntity<String> follow(@PathVariable String id, @RequestBody String followingId) {
		try {
			if(StringUtils.equals(id, followingId)) throw new CustomerException("You can't follow userself");
			customerService.follow(id, followingId);
			return ResponseEntity.ok("You started folowing " + followingId);
		} catch (CustomerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping("/unfollow/{id}")
	public ResponseEntity<String> unfollow(@PathVariable String id, @RequestBody String followerId) {
		try {
			if(StringUtils.equals(id, followerId)) throw new CustomerException("You can't unfollow userself");
			customerService.unFollow(id, followerId);
			return ResponseEntity.ok("You Unfollowed " + followerId);
		} catch (CustomerException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

}
