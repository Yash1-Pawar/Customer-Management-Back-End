package com.customer.app.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
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
			List<String> friends = new ArrayList<>();
			for (String ele : e.getFriendsId().split(",")) {
				friends.add(ele.trim());
			}
			customerDTOs.add(
					new CustomerDTO(e.getId(), e.getName(), e.getSkills(), e.getDesc(), e.getGender(), friends));
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
			List<String> friendDTOs = new ArrayList<>();
			for (String e : customerEntity.getFriendsId().split(",")) {
				friendDTOs.add(e.trim());
			}
			customerDTO = new CustomerDTO(customerEntity.getId(), customerEntity.getName(), customerEntity.getSkills(),
					customerEntity.getDesc(), customerEntity.getGender(), friendDTOs);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return customerDTO;
	}

	@Override
	public String addCustomer(CustomerDTO customerDTO) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(customerDTO.getId());
		if (optional.isPresent()) {
			throw new Exception("Customer already exists with id: " + customerDTO.getId());
		}
		CustomerEntity customerEntity = new CustomerEntity(customerDTO.getName(), customerDTO.getSkills(),
				customerDTO.getDesc(), customerDTO.getGender(), "");
		customerEntity.setId(customerDTO.getId());
		CustomerEntity customerEntityfromDB = customerRepo.save(customerEntity);
		return Integer.toString(customerEntityfromDB.getId());
	}

	@Override
	public void updateCustomer(CustomerDTO customerDTO, int id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
		customerEntity.setDesc(customerDTO.getDesc());
		customerEntity.setGender(customerDTO.getGender());
		customerEntity.setName(customerDTO.getName());
		customerEntity.setSkills(customerDTO.getSkills());
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
		for (CustomerDTO e : customerDTOs) {
			try {
				this.addCustomer(e);
			} catch (Exception e1) {
				System.out.println(e1.getMessage());
			}
		}
	}

	@Override
	public CustomerDTO addFriend(int id, List<Integer> friendIds) {
		CustomerDTO customerDTO = this.getCustomerById(id);
		if (Objects.nonNull(customerDTO)) {
			List<String> friendDTOs = new ArrayList<>();
			for (int e : friendIds) {
				if(id == e || customerDTO.getfriends().contains(Integer.toString(e))) {
					System.out.println("Skipping User: "+ e);
					continue;
				}
				CustomerDTO friendDTO = this.getCustomerById(e);
				if (Objects.nonNull(friendDTO)) {
					friendDTOs.add(Integer.toString(friendDTO.getId()));
				} else {
					System.out.println("Customer not found with id: " + e);
				}
			}
			for(String e: customerDTO.getfriends()) {
				if(StringUtils.isNotBlank(e)) friendDTOs.add(e);
			}
			StringBuilder friendsEntity = new StringBuilder();
			int n = friendDTOs.size(),i=1;
			for(String e: friendDTOs) {
				if(i<n)
					friendsEntity.append(e + ",");
				else if(i==n)
					friendsEntity.append(e);
				i++;
			}
			CustomerEntity customerEntity = new CustomerEntity(customerDTO.getName(), customerDTO.getSkills(),
					customerDTO.getDesc(), customerDTO.getGender(), friendsEntity.toString());
			customerEntity.setId(customerDTO.getId());
			customerRepo.save(customerEntity);
			customerDTO.setfriends(friendDTOs);
			System.out.println("Friends added");
		} else {
			System.out.println("Customer not found with id: " + id);
		}
		return customerDTO;
	}

}
