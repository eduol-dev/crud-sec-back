package com.crudsec.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.crudsec.entities.User;

public interface UserRepository extends CrudRepository<User, UUID> {

	
	Optional<User> findByUserName(String userName);
	
}
