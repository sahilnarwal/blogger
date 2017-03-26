package com.cisco.blogger.microservice.user;

public interface UserRoutes {
	
	String USER = "/api/user";
	
	String USER_DETAIL = USER + "/:username";
	
	String USER_LOGIN = "/api/user/login";
	
}
