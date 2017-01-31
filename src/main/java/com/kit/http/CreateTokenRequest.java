package com.kit.http;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 */
public final class CreateTokenRequest {

	@JsonProperty("email")
	public final String email;
	@JsonProperty("password")
	public final String password;

	public CreateTokenRequest(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
