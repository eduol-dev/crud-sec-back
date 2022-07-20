package com.desafiocrud.controllers;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.desafiocrud.dtos.UserDTO;
import com.desafiocrud.entities.User;
import com.desafiocrud.services.UserService;

@RestController
public class UserController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@PostMapping("/registration")
	ResponseEntity<Void> registration(@Valid UserDTO userRegistration) {
		userService.registration(userRegistration);
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PostMapping("/login")
	ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password) {
		Optional<User> userOpt = userService.findByUsername(username);
		
		if(userOpt.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}
		
		User user = userOpt.get();
		boolean valid = passwordEncoder.matches(password, user.getPassword());
		HttpStatus status = valid ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;
		return new ResponseEntity<>(status);
	}
}
