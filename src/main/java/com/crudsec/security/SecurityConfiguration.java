package com.crudsec.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crudsec.security.service.SecurityServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	private int DEFAULT_EXPIRED_TIME = 60 * 60 * 24; // Convert Day in Seconds
	
	@Value("${jwt.secret.read}")
	private String SECRET_KEY_READ;
	
	@Value("${jwt.secret.write}")
	private String SECRET_KEY_WRITE;
	
	@Autowired private SecurityServiceImpl securityServiceImpl;
	@Autowired private PasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(securityServiceImpl).passwordEncoder(passwordEncoder);
	}
	
	 @Override
	    protected void configure(HttpSecurity http) throws Exception {
	        http.csrf().disable().authorizeRequests()
	                .antMatchers("/registration", "/login").permitAll()
	                .anyRequest().authenticated()
	                .and()
	                .addFilter(new AuthenticationFilter(authenticationManager(),SECRET_KEY_READ, DEFAULT_EXPIRED_TIME))
	                .addFilter(new ValidateFilter(authenticationManager(),SECRET_KEY_READ))
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    }
	
}
