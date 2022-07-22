package com.crudsec.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.mapping.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ValidateFilter extends BasicAuthenticationFilter {

	public static final String HEADER_ATT = "Authorization";
	public static final String PREFIX_ATT = "Bearer ";
	
	private String secretKeyRead;
	private String secretKeyWrite;

	public ValidateFilter(AuthenticationManager authenticationManager, String secretKeyRead, String secretKeyWrite) {
		super(authenticationManager);
		this.secretKeyRead = secretKeyRead;
		this.secretKeyWrite = secretKeyWrite;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		String atributo = request.getHeader(HEADER_ATT);

		if (atributo == null) {
			chain.doFilter(request, response);
			return;
		}

		if (!atributo.startsWith(PREFIX_ATT)) {
			chain.doFilter(request, response);
			return;
		}

		String token = atributo.replace(PREFIX_ATT, "");
		try {
			UsernamePasswordAuthenticationToken authenticationToken = getAuthenticationToken(token);
			SecurityContextHolder.getContext().setAuthentication(authenticationToken);
			chain.doFilter(request, response);
		} catch (TokenExpiredException e) {
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			HashMap<String, String> error = new HashMap<>();
			error.put("error", e.getMessage());
			new ObjectMapper().writeValue(response.getOutputStream(), error);
		}
	}

	private UsernamePasswordAuthenticationToken getAuthenticationToken(String token) {
		String usuario = JWT.require(Algorithm.HMAC512(secretKeyRead)).build().verify(token).getSubject();
		if (usuario == null) {
			return null;
		}
		return new UsernamePasswordAuthenticationToken(usuario, null, new ArrayList<>());
	}

}
