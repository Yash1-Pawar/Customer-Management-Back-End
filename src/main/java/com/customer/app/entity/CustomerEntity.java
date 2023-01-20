package com.customer.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "Customer_Table")
public class CustomerEntity {

	@Id
//	@Column(name = "customer_id")
	private String id;

	private String name;
	private String password;
	private String skills;
	@Column(name = "description")
	private String desc;
	private String gender;
	private String friendsId;
	private String followers;
	private String roles;

}
