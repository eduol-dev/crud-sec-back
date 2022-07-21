package com.crudsec.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class PasswordsNotMatchingException extends RuntimeException {
	public PasswordsNotMatchingException() {
		super();
	}
	
	public PasswordsNotMatchingException(String msg) {
		super(msg);
	}
}
