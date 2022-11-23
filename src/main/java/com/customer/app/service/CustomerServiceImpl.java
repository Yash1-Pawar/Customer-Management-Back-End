package com.customer.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.customer.app.entity.CustomerEntity;
import com.customer.app.model.CustomerDTO;
import com.customer.app.repository.CustomerRepo;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepo customerRepo;

	@Override
	public List<CustomerDTO> getAllCustomers() {
		List<CustomerEntity> customerEntities = customerRepo.findAll();
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		for (CustomerEntity e : customerEntities) {
			customerDTOs.add(new CustomerDTO(e.getId(), e.getName(), e.getSkills(), e.getDesc(), e.getGender()));
		}
		if (customerDTOs.isEmpty()) {
			System.out.println("No Customers Founds");
		}
		return customerDTOs;
	}

	@Override
	public CustomerDTO getCustomerById(int id) {
		CustomerDTO customerDTO = null;
		try {
			Optional<CustomerEntity> optional = customerRepo.findById(id);
			CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
			customerDTO = new CustomerDTO(customerEntity.getId(), customerEntity.getName(), customerEntity.getSkills(),
					customerEntity.getDesc(), customerEntity.getGender());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return customerDTO;
	}

	@Override
	public String addCustomer(CustomerDTO customerDTO) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(customerDTO.getId());
		if(optional.isPresent()) {
			throw new Exception("Customer already exists with id: "+ customerDTO.getId());
		}
		CustomerEntity customerEntity = new CustomerEntity(customerDTO.getName(), customerDTO.getSkills(),
				customerDTO.getDesc(), customerDTO.getGender());
		customerEntity.setId(customerDTO.getId());
		CustomerEntity customerEntityfromDB = customerRepo.save(customerEntity);
		return Integer.toString(customerEntityfromDB.getId());
	}

	@Override
	public void updateCustomer(CustomerDTO customerDTO, int id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		optional.orElseThrow(() -> new Exception("Customer Not Found"));
		CustomerEntity customerEntity = new CustomerEntity(customerDTO.getName(), customerDTO.getSkills(),
				customerDTO.getDesc(), customerDTO.getGender());
		customerEntity.setId(id);
		customerRepo.save(customerEntity);
	}
	
	@Override
	public void deleteCustomer(int id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		optional.orElseThrow(() -> new Exception("Customer Not Found"));
		customerRepo.deleteById(id);
		System.out.println("Customer Deleted Successfully");
	}
	
	@Override
	public void bulkAdd(List<CustomerDTO> customerDTOs) {
		for(CustomerDTO e: customerDTOs) {
			try {
				this.addCustomer(e);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}
	}

}
