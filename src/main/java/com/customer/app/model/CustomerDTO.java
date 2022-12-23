package com.customer.app.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDTO {

	private int id;
	private String name;
	private String skills;
	private String desc;
	private String gender;
	private List<String> friends;

	public CustomerDTO(int id, String name, String skills, String desc, String gender) {
		super();
		this.id = id;
		this.name = name;
		this.skills = skills;
		this.desc = desc;
		this.gender = gender;
	}
	
	public CustomerDTO(int id, String name, String skills, String desc, String gender, List<String> friends) {
		super();
		this.id = id;
		this.name = name;
		this.skills = skills;
		this.desc = desc;
		this.gender = gender;
		this.friends = friends;
	}
	

	public List<String> getfriends() {
		return friends;
	}

	public void setfriends(List<String> friends) {
		this.friends = friends;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public CustomerDTO() {
		super();
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

	@Override
	public String toString() {
		return "CustomerDTO [id=" + id + ", name=" + name + ", skills=" + skills + ", desc=" + desc + ", gender="
				+ gender + ", friends=" + friends + "]";
	}


}
