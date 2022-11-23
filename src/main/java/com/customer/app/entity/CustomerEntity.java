package com.customer.app.entity;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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

	@Override
	public int hashCode() {
		return Objects.hash(desc, gender, id, name, skills);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CustomerEntity other = (CustomerEntity) obj;
		return Objects.equals(desc, other.desc) && Objects.equals(gender, other.gender) && id == other.id
				&& Objects.equals(name, other.name) && Objects.equals(skills, other.skills);
	}

	@Override
	public String toString() {
		return "CustomerEntity [id=" + id + ", name=" + name + ", skills=" + skills + ", desc=" + desc + ", gender="
				+ gender + "]";
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
