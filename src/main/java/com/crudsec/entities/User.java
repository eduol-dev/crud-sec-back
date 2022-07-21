package com.crudsec.entities;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

@Entity
@Table(name="users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "user_id")
	@JsonProperty(access = Access.WRITE_ONLY)
	private UUID id;
	
	@Column(name = "user_username", unique = true)
	private String userName;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "user_password")
	private String password;
	
	@Column(name = "user_2fa")
	private Boolean using2fa = false;
	
	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(name = "user_2fa_key")
	private String key2fa;
	
	public User() {}
	
	public User(String userName, String password) {
		this.userName = userName;
		this.password = password;
	}
		
	public UUID getId() {
		return id;
	}
	public void setId(UUID id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean getUsing2fa() {
		return using2fa;
	}

	public void setUsing2fa(Boolean using2fa) {
		this.using2fa = using2fa;
	}

	public String getKey2fa() {
		return key2fa;
	}

	public void setKey2fa(String key2fa) {
		this.key2fa = key2fa;
	}
	
}
