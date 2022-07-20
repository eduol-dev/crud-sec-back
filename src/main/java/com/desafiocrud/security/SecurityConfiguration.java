package com.desafiocrud.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.desafiocrud.security.service.SecurityServiceImpl;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Value("${jwt.secret}")
	private String SECRET_KEY;
	
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
	                .addFilter(new AuthenticationFilter(authenticationManager(),SECRET_KEY))
	                .addFilter(new ValidateFilter(authenticationManager(),SECRET_KEY))
	                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	    }
	
}
