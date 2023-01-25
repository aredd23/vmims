package com.apps.pims.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.apps.pims.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	public User findByEmail(String email);

	@Query("SELECT u FROM User u WHERE u.username = :username")
	public User getUserByUsername(@Param("username") String username);

}
