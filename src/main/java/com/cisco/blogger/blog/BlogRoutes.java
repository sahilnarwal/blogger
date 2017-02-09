package com.cisco.blogger.blog;

public interface BlogRoutes {
	
	String BLOG = "/api/blog";
	
	String BLOG_WITH_TITLE = BLOG + "/:title";
	
	String BLOG_WITH_ID = BLOG + "/:id";
	
	String FAV_BLOG="/api/:areaOfInterest/blog";
	
}
