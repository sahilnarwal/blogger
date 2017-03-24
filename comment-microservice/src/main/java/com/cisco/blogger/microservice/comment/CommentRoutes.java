package com.cisco.blogger.microservice.comment;

public interface CommentRoutes {
	
	String COMMENT = "/api/comment";
	
	String COMMENT_FOR_BLOG = COMMENT + "/:blogid";
	
}
