package com.customer.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customer.app.entity.CustomerEntity;
import com.customer.app.model.CustomerDTO;
import com.customer.app.repository.CustomerRepo;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	private CustomerRepo customerRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public List<CustomerDTO> getAllCustomers() {
		List<CustomerEntity> customerEntities = customerRepo.findAll();
		List<CustomerDTO> customerDTOs = new ArrayList<>();
		for (CustomerEntity e : customerEntities) {
			List<String> friends = new ArrayList<>();
			for (String ele : e.getFriendsId().split(",")) {
				friends.add(ele.trim());
			}
			customerDTOs.add(new CustomerDTO(e.getId(), e.getName(), e.getSkills(), e.getDesc(), e.getGender(), friends,
					e.getRoles()));
		}
		if (customerDTOs.isEmpty()) {
			System.out.println("No Customers Founds");
		}
		return customerDTOs;
	}

	@Override
	public CustomerDTO getCustomerById(String id) {
		CustomerDTO customerDTO = null;
		try {
			Optional<CustomerEntity> optional = customerRepo.findById(id);
			CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
			List<String> friendDTOs = new ArrayList<>();
			for (String e : customerEntity.getFriendsId().split(",")) {
				friendDTOs.add(e.trim());
			}
			customerDTO = new CustomerDTO(customerEntity.getId(), customerEntity.getName(),
					customerEntity.getPassword(), customerEntity.getSkills(), customerEntity.getDesc(),
					customerEntity.getGender(), friendDTOs, customerEntity.getRoles());
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
		CustomerEntity customerEntity = new CustomerEntity(customerDTO.getId(), customerDTO.getName(),
				passwordEncoder.encode(customerDTO.getPassword()), customerDTO.getSkills(), customerDTO.getDesc(),
				customerDTO.getGender(), "", Roles.USER.toString());
		CustomerEntity customerEntityfromDB = customerRepo.save(customerEntity);
		return customerEntityfromDB.getId();
	}

	@Override
	public void updateCustomer(CustomerDTO customerDTO, String id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
		customerEntity.setId(id);
		if (StringUtils.isNotBlank(customerDTO.getName()))
			customerEntity.setName(customerDTO.getName());
		if (StringUtils.isNotBlank(customerDTO.getSkills()))
			customerEntity.setSkills(customerDTO.getSkills());
		if (StringUtils.isNotBlank(customerDTO.getDesc()))
			customerEntity.setDesc(customerDTO.getDesc());
		if (StringUtils.isNotBlank(customerDTO.getGender()))
			customerEntity.setGender(customerDTO.getGender());
		if (StringUtils.isNotBlank(customerDTO.getRoles()))
			customerEntity.setRoles(customerDTO.getRoles());
		customerRepo.save(customerEntity);
	}
	
	@Override
	public void resetPassword(String password, String id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
		customerEntity.setId(id);
		customerEntity.setPassword(passwordEncoder.encode(password));
		System.out.println("Password Reset successfull. Login with new password");
		customerRepo.save(customerEntity);
	}

	@Override
	public void deleteCustomer(String id) throws Exception {
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
	public CustomerDTO addFriend(String id, List<String> friendIds) {
		CustomerDTO customerDTO = null;
		try {
			customerDTO = this.getCustomerById(id);
			Optional<CustomerEntity> optional = customerRepo.findById(id);
			CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
			List<String> friendDTOs = new ArrayList<>();
			for (String e : friendIds) {
				if (id.equals(e) || customerDTO.getFriends().contains(e)) {
					System.out.println("Skipping User: " + e);
					continue;
				}
				CustomerDTO friendDTO = this.getCustomerById(e);
				if (Objects.nonNull(friendDTO)) {
					friendDTOs.add(friendDTO.getId());
				} else {
					System.out.println("Customer not found with id: " + e);
				}
			}
			for (String e : customerDTO.getFriends()) {
				if (StringUtils.isNotBlank(e))
					friendDTOs.add(e);
			}
			StringBuilder friendsEntity = new StringBuilder();
			int n = friendDTOs.size(), i = 1;
			for (String e : friendDTOs) {
				if (i < n)
					friendsEntity.append(e + ",");
				else if (i == n)
					friendsEntity.append(e);
				i++;
			}
			customerEntity.setFriendsId(friendsEntity.toString());
			customerRepo.save(customerEntity);
			customerDTO.setFriends(friendDTOs);
			System.out.println("Friends added");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return customerDTO;
	}

}
