package com.crudsec.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class BusinessException extends RuntimeException {
	public BusinessException() {
		super();
	}
	
	public BusinessException(String msg) {
		super(msg);
	}
}
