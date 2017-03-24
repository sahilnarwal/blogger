package com.cisco.blogger.microservice.blog;

public interface BlogTopics {

	String GET_BLOG_BY_ID = "com.cisco.blogger.blog.fetch";
	
	String ADD_BLOG = "com.cisco.blogger.blog.add";
	
	String GET_BLOG_BY_TITLE = "com.cisco.blogger.blog.search";
	
	String UPDATE_BLOG = "com.cisco.blogger.blog.update";
	
	String DELETE_BLOG = "com.cisco.blogger.blog.delete";
	
	String FAV_BLOG = "com.cisco.blogger.blog.fav";
	
}
