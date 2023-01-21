package com.customer.app.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.customer.app.Exception.CustomerException;
import com.customer.app.entity.CustomerEntity;
import com.customer.app.model.CustomerDTO;
import com.customer.app.model.RestPasswordDTO;
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
			List<String> followers = new ArrayList<>();
			for (String friend : e.getFriendsId().split(",")) {
				friends.add(friend.trim());
			}
			for (String follower : e.getFollowers().split(",")) {
				followers.add(follower.trim());
			}
			customerDTOs.add(new CustomerDTO(e.getId(), e.getName(), e.getSkills(), e.getDesc(), e.getGender(), friends,
					followers, e.getRoles()));
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
			List<String> followersDTOs = new ArrayList<>();
			for (String e : customerEntity.getFriendsId().split(",")) {
				friendDTOs.add(e.trim());
			}
			for (String e : customerEntity.getFollowers().split(",")) {
				followersDTOs.add(e.trim());
			}
			customerDTO = new CustomerDTO(customerEntity.getId(), customerEntity.getName(),
					customerEntity.getPassword(), customerEntity.getSkills(), customerEntity.getDesc(),
					customerEntity.getGender(), friendDTOs, followersDTOs, customerEntity.getRoles());
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
				customerDTO.getPassword(), customerDTO.getSkills(), customerDTO.getDesc(), customerDTO.getGender(), "",
				"", Roles.USER.toString());
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
	public void changePassword(RestPasswordDTO restPasswordDTO, String id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
		if (passwordEncoder.matches(restPasswordDTO.getOldPassword(), customerEntity.getPassword())) {
			customerEntity.setId(id);
			customerEntity.setPassword(passwordEncoder.encode(restPasswordDTO.getNewPassword()));
			customerRepo.save(customerEntity);
			System.out.println("Password Reset successfull. Login with new password");
		} else {
			System.out.println("Old Password doesnot match");
			throw new Exception("Old Password doesnot match");
		}
	}

	@Override
	public void resetPassword(String newPassword, String id) throws Exception {
		Optional<CustomerEntity> optional = customerRepo.findById(id);
		CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
		customerEntity.setId(id);
		System.out.println("changePassowrd: " + newPassword);
		customerEntity.setPassword(passwordEncoder.encode(newPassword));
		customerRepo.save(customerEntity);
		System.out.println("Password Reset successfull. Login with new password");
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

	@Override
	public CustomerDTO addFollowers(String id, List<String> followers) {
		CustomerDTO customerDTO = null;
		try {
			customerDTO = this.getCustomerById(id);
			Optional<CustomerEntity> optional = customerRepo.findById(id);
			CustomerEntity customerEntity = optional.orElseThrow(() -> new Exception("Customer Not Found"));
			List<String> followersDTOs = new ArrayList<>();
			for (String e : followers) {
				if (id.equals(e) || customerDTO.getFollowers().contains(e)) {
					System.out.println("Skipping User: " + e);
					continue;
				}
				CustomerDTO followersDTO = this.getCustomerById(e);
				if (Objects.nonNull(followersDTO)) {
					followersDTOs.add(followersDTO.getId());
				} else {
					System.out.println("Customer not found with id: " + e);
				}
			}
			for (String e : customerDTO.getFollowers()) {
				if (StringUtils.isNotBlank(e))
					followersDTOs.add(e);
			}
			StringBuilder followersEntity = new StringBuilder();
			int n = followersDTOs.size(), i = 1;
			for (String e : followersDTOs) {
				if (i < n)
					followersEntity.append(e + ",");
				else if (i == n)
					followersEntity.append(e);
				i++;
			}
			customerEntity.setFollowers(followersEntity.toString());
			customerRepo.save(customerEntity);
			customerDTO.setFollowers(followersDTOs);
			System.out.println("Followers added");
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return customerDTO;
	}

	@Override
	public void follow(String userId, String followingId) throws CustomerException {
		Optional<CustomerEntity> customerOptional = customerRepo.findById(userId);
		CustomerEntity customerEntity = customerOptional.orElseThrow(() -> new CustomerException("Customer Not Found"));
		CustomerEntity friendEntity = customerRepo.findById(followingId)
				.orElseThrow(() -> new CustomerException("Friend Not Found"));
		String friends = customerEntity.getFriendsId();
		friends += "," + followingId;
		if (StringUtils.startsWith(friends, ",")) {
			friends = StringUtils.substring(friends, 1);
		}
		String followers = friendEntity.getFollowers();
		followers += "," + userId;
		if (StringUtils.startsWith(followers, ",")) {
			followers = StringUtils.substring(followers, 1);
		}
//		add following to user
		customerEntity.setFriendsId(friends);
//		add follower to following
		friendEntity.setFollowers(followers);
		customerRepo.save(customerEntity);
		customerRepo.save(friendEntity);
	}

	@Override
	public void unFollow(String userId, String followerId) throws CustomerException {
		Optional<CustomerEntity> customerOptional = customerRepo.findById(userId);
		CustomerEntity customerEntity = customerOptional.orElseThrow(() -> new CustomerException("Customer Not Found"));
		CustomerEntity friendEntity = customerRepo.findById(followerId)
				.orElseThrow(() -> new CustomerException("Following customer Not Found"));

		StringBuilder followings = new StringBuilder();
		String[] friendsList = customerEntity.getFriendsId().split(",");
		Arrays.asList(friendsList).stream().filter((friend) -> !friend.equals(followerId))
				.forEach((e) -> followings.append(e + ","));
//		remove following from userId
		if (StringUtils.endsWith(followings, ",")) {
			customerEntity.setFriendsId(followings.substring(0, followings.length() - 1));
		}else {
			customerEntity.setFriendsId(followings.toString());
		}

		StringBuilder followers = new StringBuilder();
		String[] followersList = friendEntity.getFollowers().split(",");
		Arrays.asList(followersList).stream().filter((follower) -> !follower.equals(userId))
				.forEach((e) -> followers.append(e + ","));
//		remove follower from followerId
		if (StringUtils.endsWith(followers, ",")) {
			friendEntity.setFollowers(followers.substring(0, followers.length() - 1));
		}else {
			friendEntity.setFollowers(followers.toString());
		}

		customerRepo.save(customerEntity);
		customerRepo.save(friendEntity);
	}
	
	@Override
	public void removeFollower(String userId, String followerId) throws CustomerException {
		Optional<CustomerEntity> customerOptional = customerRepo.findById(userId);
		CustomerEntity customerEntity = customerOptional.orElseThrow(() -> new CustomerException("Customer Not Found"));
		CustomerEntity friendEntity = customerRepo.findById(followerId)
				.orElseThrow(() -> new CustomerException("Following customer Not Found"));

		StringBuilder followings = new StringBuilder();
		String[] friendsList = friendEntity.getFriendsId().split(",");
		Arrays.asList(friendsList).stream().filter((friend) -> !friend.equals(userId))
				.forEach((e) -> followings.append(e + ","));
//		remove userId from friend's followings
		if (StringUtils.endsWith(followings, ",")) {
			friendEntity.setFriendsId(followings.substring(0, followings.length() - 1));
		}else {
			friendEntity.setFriendsId(followings.toString());
		}

		StringBuilder followers = new StringBuilder();
		String[] followersList = customerEntity.getFollowers().split(",");
		Arrays.asList(followersList).stream().filter((follower) -> !follower.equals(followerId))
				.forEach((e) -> followers.append(e + ","));
//		remove follower from user's Followers
		if (StringUtils.endsWith(followers, ",")) {
			customerEntity.setFollowers(followers.substring(0, followers.length() - 1));
		}else {
			customerEntity.setFollowers(followers.toString());
		}

		customerRepo.save(customerEntity);
		customerRepo.save(friendEntity);
	}

}
