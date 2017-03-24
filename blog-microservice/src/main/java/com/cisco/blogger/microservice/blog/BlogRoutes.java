package com.cisco.blogger.microservice.blog;

public interface BlogRoutes {
	
	String BLOGS = "/api/blogs";
	
	String BLOG = "/api/blog";
	
	String SPECIFIC_BLOG = BLOG + "/:identifier";
	
}
