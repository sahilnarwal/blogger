package com.cisco.blogger.microservice.blog;

public interface BlogTopics {

	String GET_BLOG_BY_ID = "com.cisco.blogger.blog.fetchById";
	
	String ADD_BLOG = "com.cisco.blogger.blog.add";
	
	String GET_BLOG_BY_TITLE = "com.cisco.blogger.blog.fetchByTitle";
	
	String UPDATE_BLOG = "com.cisco.blogger.blog.update";
	
	String DELETE_BLOG = "com.cisco.blogger.blog.delete";
	
	String GET_BLOGS_BY_TAG = "com.cisco.blogger.blog.fetchByTag";
	
	String GET_ALL_BLOGS = "com.cisco.blogger.blogs.fetchAll";
	
}
