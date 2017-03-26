package com.cisco.blogger.microservice.auth;

public interface AuthRoutes {
	
	String REGISTER_USER = "/api/register";
	
	String AUTHENTICATE_USER = "/api/authenticate";
	
	String GET_TOKEN = "/api/new_token";
	
	String VALIDATE_TOKEN = "/api/token_info";
	
}
