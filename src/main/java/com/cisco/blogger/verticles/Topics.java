package com.cisco.blogger.verticles;

public interface Topics {

	String GET_BLOG = "com.cisco.blogger.blog.fetch";
	
	String ADD_BLOG = "com.cisco.blogger.blog.add";
	
	String SEARCH_BLOG = "com.cisco.blogger.blog.search";
	
	String UPDATE_BLOG = "com.cisco.blogger.blog.update";
	
	String DELETE_BLOG = "com.cisco.blogger.blog.delete";
	
	String GET_USER = "com.cisco.blogger.user.fetch";
	
	String ADD_USER = "com.cisco.blogger.user.add";
	
	String UPDATE_USER = "com.cisco.blogger.user.update";
	
	String GET_COMMENT = "com.cisco.blogger.comment.fetch";
	
	String ADD_COMMENT = "com.cisco.blogger.comment.add";
	
	String FAV_BLOG = "com.cisco.blogger.blog.fav";
	
}
