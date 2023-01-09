package com.customer.app.controller;

import java.util.List;
import java.util.Objects;

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

import com.customer.app.model.CustomerDTO;
import com.customer.app.service.CustomerService;

@CrossOrigin(value = "*")
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

	@PostMapping("/addCustomer")
	public ResponseEntity<Object> addCustomer(@RequestBody CustomerDTO customerDTO) {
		try {
			String id = customerService.addCustomer(customerDTO);
			return new ResponseEntity<>("Customer created with id: " + id, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Customer already exists with id: " + customerDTO.getId(),
					HttpStatus.BAD_REQUEST);
		}
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

	@PutMapping("/resetPassword/{id}")
	public ResponseEntity<String> resetPassword(@RequestBody CustomerDTO customerDTO, @PathVariable String id) {
		try {
			customerService.resetPassword(customerDTO.getPassword(), id);
			return new ResponseEntity<>("Customer successfully updated", HttpStatus.ACCEPTED);
		} catch (Exception e) {
			return new ResponseEntity<>("Customer not found with the id: " + id, HttpStatus.NOT_FOUND);
		}
	}

	
	@PutMapping("/addFriend/{id}")
	public ResponseEntity<Object> addFriend(@PathVariable String id, @RequestBody List<String> friendIds) {
		try {
			CustomerDTO customerDTO = customerService.addFriend(id, friendIds);
			return new ResponseEntity<>(customerDTO, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Some error occured while adding friend", HttpStatus.BAD_REQUEST);
		}
	}

	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<String> deleteCustomer(@PathVariable String id) {
		try {
			customerService.deleteCustomer(id);
			return new ResponseEntity<>("Customer deleted successfully", HttpStatus.OK);
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

}
