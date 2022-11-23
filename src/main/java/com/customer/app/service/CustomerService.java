package com.customer.app.service;

import java.util.List;

import com.customer.app.model.CustomerDTO;

public interface CustomerService {

	List<CustomerDTO> getAllCustomers();

	CustomerDTO getCustomerById(int id);

	String addCustomer(CustomerDTO customerDTO) throws Exception;

	void updateCustomer(CustomerDTO customerDTO, int id) throws Exception;

	void deleteCustomer(int id) throws Exception;

	void bulkAdd(List<CustomerDTO> customerDTOs);

}
