package com.crudsec.services.impl;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.security.InvalidParameterException;
import java.util.Optional;

import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.crudsec.security.gauth.GAuthProvider;
import com.crudsec.services.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private GAuthProvider gAuthProvider;
	
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

	@Override
	public Optional<String> change2fa(Boolean use2fa) throws UserPrincipalNotFoundException {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> userOpt = userRepository.findByUserName(userName);
		if (userOpt.isEmpty()) {
			throw new UserPrincipalNotFoundException(userName);
		}		
		User user = userOpt.get();
		user.setUsing2fa(use2fa);
		user.setKey2fa(Base32.random());
		userRepository.save(user);
		if(use2fa) {
			return Optional.of(gAuthProvider.buildQRCodUrl(user.getUserName(), user.getKey2fa()));
		}
		return Optional.empty();
	}
}
