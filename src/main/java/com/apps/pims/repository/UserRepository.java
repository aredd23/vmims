package com.apps.pims.repository;

import org.springframework.data.repository.CrudRepository;

import com.apps.pims.entity.UserDetails;

public interface UserRepository extends CrudRepository<UserDetails, Integer>{

	UserDetails findByEmail(String email);

}
