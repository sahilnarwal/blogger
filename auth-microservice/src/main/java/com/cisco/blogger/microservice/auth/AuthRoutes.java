package com.cisco.blogger.microservice.auth;

public interface AuthRoutes {
	
	String REGISTER = "/api/user/register";
	
	String GET_TOKEN = "/api/new_token";
	
	String TEST_USER_AUTHENTICATION = "/api/user/authenticate";
	
	String TEST_TOKEN_VALIDATION = "/api/token_info";
	
}
