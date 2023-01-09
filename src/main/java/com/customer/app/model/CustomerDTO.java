package com.customer.app.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SuppressWarnings("serial")
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomerDTO implements UserDetails  {

	private String id;
	private String name;
	private String password;	
	private String skills;
	private String desc;
	private String gender;
	private List<String> friends;
	private String roles;
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities;
		if(StringUtils.isNotEmpty(this.roles)) {
			String[] rolesList = StringUtils.split(this.roles ,",");
			authorities = Arrays.asList(rolesList).stream()
				.map((role) -> new SimpleGrantedAuthority("ROLE_"+role)).collect(Collectors.toList());
		}
		else {
			authorities = new ArrayList<>();
		}
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public String getUsername() {
		return this.id;
	}

	public CustomerDTO(String id, String name, String skills, String desc, String gender, List<String> friends,
			String roles) {
		super();
		this.id = id;
		this.name = name;
		this.skills = skills;
		this.desc = desc;
		this.gender = gender;
		this.friends = friends;
		this.roles = roles;
	}
	
}
