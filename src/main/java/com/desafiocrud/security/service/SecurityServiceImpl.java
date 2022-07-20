package com.desafiocrud.security.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.desafiocrud.entities.User;
import com.desafiocrud.repositories.UserRepository;
import com.desafiocrud.security.data.SecurityUserDetails;

@Component
public class SecurityServiceImpl implements UserDetailsService {
	
	@Autowired private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user = userRepository.findByUserName(username);
		if(user.isEmpty()) 
			throw new UsernameNotFoundException(String.format("Username %s not found.", username));
		return new SecurityUserDetails(user);
	}

}
