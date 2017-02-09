package com.cisco.blogger.blog;

public interface BlogRoutes {
	
	String BLOG = "/api/blog";
	
	String SPECIFIC_BLOG = BLOG + "/:identifier";
	
}
