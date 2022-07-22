package com.crudsec.controllers;

import java.nio.file.attribute.UserPrincipalNotFoundException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.crudsec.dtos.UserDTO;
import com.crudsec.entities.User;
import com.crudsec.security.gauth.GAuthProvider;
import com.crudsec.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class UserController {

	private static Logger log = LoggerFactory.getLogger(UserController.class);

	@Value("${jwt.secret.write}")
	private String SECRET_KEY_WRITE;

	@Autowired
	private UserService userService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private GAuthProvider gAuthProvider;

	@PostMapping("/registration")
	ResponseEntity<Void> registration(@Valid UserDTO userRegistration) {
		userService.registration(userRegistration);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/login")
	ResponseEntity<Void> login(@RequestParam String username, @RequestParam String password) {
		Optional<User> userOpt = userService.findByUsername(username);

		if (userOpt.isEmpty()) {
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
			Optional<String> resOpt = userService.change2fa(use2fa);
			if (resOpt.isPresent()) {
				return ResponseEntity.ok(resOpt.get());
			}
		} catch (UserPrincipalNotFoundException e) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		return ResponseEntity.ok().build();
	}

	@GetMapping("/valid2fa")
	void valid2fa(@RequestParam String code, HttpServletResponse response) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		Optional<User> userOpt = userService.findByUsername(userName);
		if (userOpt.isEmpty()) {
			log.info("User %s not found.".formatted(userName));
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
		}
		boolean valid = gAuthProvider.isValidCode(userOpt.get().getKey2fa(), code);
		try {
			if (valid) {
				Instant in = LocalDateTime.now().plusSeconds(5).atZone(ZoneId.systemDefault()).toInstant();
				Date expireAt = Date.from(in);
				String token = JWT.create().withSubject(userName).withExpiresAt(expireAt)
						.sign(Algorithm.HMAC512(SECRET_KEY_WRITE));
				HashMap<String, String> msg = new HashMap<>();
				msg.put("write_token", token);
				response.setContentType(MediaType.APPLICATION_JSON_VALUE);
				response.setStatus(HttpStatus.OK.value());
				new ObjectMapper().writeValue(response.getOutputStream(), msg);
			} else {
				log.info("Token not valid for %s.".formatted(userName));
				HashMap<String, String> msg = new HashMap<>();
				msg.put("error", "Code is not valid.");
				response.setStatus(HttpStatus.UNAUTHORIZED.value());
				new ObjectMapper().writeValue(response.getOutputStream(), msg);
			}
		} catch (Exception e) {
			log.error("valid2fa writeValue:", e);
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		}
	}
}
