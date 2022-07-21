package com.crudsec.security;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.crudsec.security.data.SecurityUserDetails;


public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
		
	private AuthenticationManager authenticationManager;	
	private String secretKey;
	private Integer expiredTimeInSec;
	
	public AuthenticationFilter(AuthenticationManager authenticationManager, String secretKey, Integer expiredTimeInSec) {
		this.authenticationManager = authenticationManager;
		this.secretKey = secretKey;
		this.expiredTimeInSec = expiredTimeInSec;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				request.getParameter("username"), request.getParameter("password"), new ArrayList<>()));
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		SecurityUserDetails userDetail = (SecurityUserDetails) authResult.getPrincipal();
		Instant in = LocalDateTime.now().plusSeconds(expiredTimeInSec).atZone(ZoneId.systemDefault()).toInstant();
		Date expireAt = Date.from(in);
		String token = JWT.create()
						.withSubject(userDetail.getUsername())
						.withExpiresAt(expireAt)
						.sign(Algorithm.HMAC512(secretKey));
		response.addHeader("Authorization", "Bearer " + token);
	}
}
