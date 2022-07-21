package com.crudsec.services.impl;

import java.security.InvalidParameterException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crudsec.dtos.UserDTO;
import com.crudsec.entities.User;
import com.crudsec.exceptions.BusinessException;
import com.crudsec.exceptions.PasswordsNotMatchingException;
import com.crudsec.repositories.UserRepository;
import com.crudsec.security.data.SecurityUserDetails;
import com.crudsec.services.UserService;

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
