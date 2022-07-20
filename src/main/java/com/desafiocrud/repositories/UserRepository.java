package com.desafiocrud.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.desafiocrud.entities.User;

public interface UserRepository extends CrudRepository<User, UUID> {

	
	Optional<User> findByUserName(String userName);
	
}
