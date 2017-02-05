package com.cisco.blogger.comments;

public interface CommentRoutes {
	
	String COMMENT = "/api/comment";
	
	String COMMENT_FOR_BLOG = COMMENT + "/:blogid";
	
}
