package com.crudsec.security.gauth;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.jboss.aerogear.security.otp.Totp;
import org.springframework.stereotype.Component;

@Component
public class GAuthProvider {

	private final String QR_URL = "https://chart.googleapis.com/chart?chs=300x300&chld=M%%7C0&cht=qr&chl=";

	public String buildQRCodUrl(String username, String secret) {
		String result = QR_URL;
		try {
			result += URLEncoder.encode(
					String.format("otpauth://totp/CrudSec:%s?secret=%s&issuer=CrudSec", username, secret), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public boolean isValidCode(String userKey, String code) {
		Totp totp = new Totp(userKey);		
		return totp.verify(code);
	}

}
