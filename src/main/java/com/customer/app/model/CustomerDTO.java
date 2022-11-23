package com.customer.app.model;

import java.util.Objects;

public class CustomerDTO {

	private int id;
	private String name;
	private String skills;
	private String desc;
	private String gender;

	public CustomerDTO(int id, String name, String skills, String desc, String gender) {
		super();
		this.id = id;
		this.name = name;
		this.skills = skills;
		this.desc = desc;
		this.gender = gender;
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
				+ gender + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, desc, gender, name, skills);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerDTO other = (CustomerDTO) obj;
		return Objects.equals(desc, other.desc) && Objects.equals(id, other.id) && Objects.equals(gender, other.gender)
				&& Objects.equals(name, other.name) && Objects.equals(skills, other.skills);
	}

}
