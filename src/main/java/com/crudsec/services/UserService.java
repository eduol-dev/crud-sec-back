package com.crudsec.services;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.crudsec.dtos.UserDTO;
import com.crudsec.entities.User;

public interface UserService extends UserDetailsService  {

	User registration(UserDTO user);

	Optional<User> findByUsername(String username) throws UsernameNotFoundException;
	
	Optional<String> change2fa(Boolean use2fa) throws UserPrincipalNotFoundException;
	
}
