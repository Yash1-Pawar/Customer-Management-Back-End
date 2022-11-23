package com.customer.app.controller;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.customer.app.model.CustomerDTO;
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
			return new ResponseEntity<List<CustomerDTO>>(customerDTOs, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<List<CustomerDTO>>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/getCustomer/{id}")
	public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable int id) {
		try {
			CustomerDTO customerDTOs = customerService.getCustomerById(id);
			if (Objects.nonNull(customerDTOs))
				return new ResponseEntity<CustomerDTO>(customerDTOs, HttpStatus.OK);
			else
				return new ResponseEntity<CustomerDTO>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/addCustomer")
	public ResponseEntity<Object> addCustomer(@RequestBody CustomerDTO customerDTO) {
		try {
			String id = customerService.addCustomer(customerDTO);
			return new ResponseEntity<Object>("Customer created with id: "+id, HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer already exists with this id");
		}
	}
	
	@PutMapping("/updateCustomer/{id}")
	public ResponseEntity<Object> updateCustomer(@RequestBody CustomerDTO customerDTO, @PathVariable int id) {
		try {
			customerService.updateCustomer(customerDTO,id);
			return new ResponseEntity<Object>("Customer successfully updated", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Customer not found with the id: "+id, HttpStatus.NOT_FOUND);
		}
	}
	
	@DeleteMapping("/deleteCustomer/{id}")
	public ResponseEntity<Object> deleteCustomer(@PathVariable int id) {
		try {
			customerService.deleteCustomer(id);
			return new ResponseEntity<Object>("Customer deleted successfully", HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<Object>("Customer not found with the id: "+id, HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/addCustomers")
	public ResponseEntity<Object> bulkAdd(@RequestBody List<CustomerDTO> customerDTO) {
		try {
			customerService.bulkAdd(customerDTO);
			return new ResponseEntity<Object>("Bulk addition done", HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
