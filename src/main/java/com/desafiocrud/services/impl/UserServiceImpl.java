package com.desafiocrud.services.impl;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.desafiocrud.dtos.UserDTO;
import com.desafiocrud.entities.User;
import com.desafiocrud.exceptions.BusinessException;
import com.desafiocrud.exceptions.PasswordsNotMatchingException;
import com.desafiocrud.repositories.UserRepository;
import com.desafiocrud.security.data.SecurityUserDetails;
import com.desafiocrud.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public User registration(UserDTO userRegistration) throws InvalidParameterException {
		if(userRegistration.getPassword().compareTo(userRegistration.getMatchingPassword()) != 0) {
			throw new PasswordsNotMatchingException("Passwords not matching.");
		}
		if(!userRepository.findByUserName(userRegistration.getUsername()).isEmpty()) {
			throw new BusinessException("Username already taken.");
		}
		User user = new User(userRegistration.getUsername(), passwordEncoder.encode(userRegistration.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public Optional<User> findByUsername(String username) throws UsernameNotFoundException {
		return userRepository.findByUserName(username);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> userOpt = userRepository.findByUserName(username);
		return new SecurityUserDetails(userOpt);
	}
}
