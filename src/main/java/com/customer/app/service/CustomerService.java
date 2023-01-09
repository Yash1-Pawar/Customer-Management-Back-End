package com.customer.app.service;

import java.util.List;

import com.customer.app.model.CustomerDTO;

public interface CustomerService {

	List<CustomerDTO> getAllCustomers();

	CustomerDTO getCustomerById(String id);

	String addCustomer(CustomerDTO customerDTO) throws Exception;

	void updateCustomer(CustomerDTO customerDTO, String id) throws Exception;

	void deleteCustomer(String id) throws Exception;

	void bulkAdd(List<CustomerDTO> customerDTOs);

	CustomerDTO addFriend(String id, List<String> friendIds);

	void resetPassword(String password, String id) throws Exception;

}
