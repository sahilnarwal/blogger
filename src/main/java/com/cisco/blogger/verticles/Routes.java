package com.cisco.blogger.verticles;

public interface Routes {
	
	String ABOUT = "/about";
	
	String STATIC_CONTENT = "/static/*";
	
	String USER = "/api/user";
	
	String BLOG = "/api/blog";
	
	String SEARCH_BLOG = "/api/blog/:title";
	
	String LOGIN = "/api/user/login";
	
	
	String COMMENT = "/api/blog/comment";
	
	String SEARCH_COMMENT = "/api/:blog/comment";
	
	String UPDATE_USER = "/api/user/update";

}
