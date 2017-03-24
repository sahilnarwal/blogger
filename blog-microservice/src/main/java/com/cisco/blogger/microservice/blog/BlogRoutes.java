package com.cisco.blogger.microservice.blog;

public interface BlogRoutes {
	
	String BLOG = "/api/blog";
	
	String SPECIFIC_BLOG = BLOG + "/:identifier";
	
}
