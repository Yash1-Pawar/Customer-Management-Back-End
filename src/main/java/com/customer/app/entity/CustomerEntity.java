package com.customer.app.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Customer_Table")
public class CustomerEntity {

	@Id
	@Column(name = "customer_id")
	private int id;

	private String name;
	private String skills;
	@Column(name = "description")
	private String desc;
	private String gender;
	private String friendsId;

	public CustomerEntity() {
		super();
	}

	public CustomerEntity(String name, String skills, String desc, String gender) {
		super();
		this.name = name;
		this.skills = skills;
		this.desc = desc;
		this.gender = gender;
	}

	public CustomerEntity(String name, String skills, String desc, String gender, String friendsId) {
		super();
		this.name = name;
		this.skills = skills;
		this.desc = desc;
		this.gender = gender;
		this.friendsId = friendsId;
	}

	public String getFriendsId() {
		return friendsId;
	}

	public void setFriendsId(String friendsId) {
		this.friendsId = friendsId;
	}

	@Override
	public String toString() {
		return "CustomerEntity [id=" + id + ", name=" + name + ", skills=" + skills + ", desc=" + desc + ", gender="
				+ gender + ", friendsId=" + friendsId + "]";
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSkills() {
		return skills;
	}

	public void setSkills(String skills) {
		this.skills = skills;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

}
