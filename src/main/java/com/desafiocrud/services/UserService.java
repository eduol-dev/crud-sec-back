package com.desafiocrud.services;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.desafiocrud.dtos.UserDTO;
import com.desafiocrud.entities.User;

public interface UserService extends UserDetailsService  {

	User registration(UserDTO user);

	Optional<User> findByUsername(String username) throws UsernameNotFoundException;
	
}
