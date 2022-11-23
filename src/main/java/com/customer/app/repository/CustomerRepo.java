package com.customer.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.customer.app.entity.CustomerEntity;

public interface CustomerRepo extends JpaRepository<CustomerEntity, Integer> {

}
