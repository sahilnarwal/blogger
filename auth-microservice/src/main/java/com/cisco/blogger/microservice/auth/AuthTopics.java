package com.cisco.blogger.microservice.auth;

public interface AuthTopics {
	
	String ADD_USER_CRED = "com.cisco.blogger.auth.addcredentials";
	
	String UPDATE_USER_CRED = "com.cisco.blogger.auth.updatecredentials";

	String AUNTHENTICATE = "com.cisco.blogger.auth.authenticate";
	
	String VALIDATE_TOKEN = "com.cisco.blogger.auth.validatetoken";
	
	String NEW_TOKEN = "com.cisco.blogger.auth.newtoken";
	
}
