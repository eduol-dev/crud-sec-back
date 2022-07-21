package com.crudsec.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crudsec.dtos.UserDTO;
import com.crudsec.entities.User;
import com.crudsec.services.UserService;

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
	
	@GetMapping("/active2fa")
	ResponseEntity<String> active2fa(@RequestParam Boolean use2fa) {
		try {
			Optional<String> resOpt =  userService.change2fa(use2fa);
			if(resOpt.isPresent()) {
				return ResponseEntity.ok(resOpt.get());
			}
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok().build();
	}
}
