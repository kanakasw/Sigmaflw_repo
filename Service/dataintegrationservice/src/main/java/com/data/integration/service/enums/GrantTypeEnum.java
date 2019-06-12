package com.data.integration.service.enums;


public enum GrantTypeEnum {

	PASSWORD("password"),
	AUTHORIZATION_CODE("authorization_code"),
	REFRESH_TOKEN("refresh_token"),
	IMPLICIT("implicit"),
	CLIENT_CREDENTIALS("client_credentials");
	
	private String key;

	private GrantTypeEnum(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
