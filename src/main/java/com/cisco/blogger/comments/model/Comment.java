package com.cisco.blogger.comments.model;


import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
@Entity
public class Comment {
	
	@Id
	private String id = new ObjectId().toString();
	

	private String blogId;
	
	private String userName;
	
	private String comment;
	
	private Date dateOfCreation = new Date();

	public String getBlogId() {
		return blogId;
	}

	public void setBlogId(String blogId) {
		this.blogId = blogId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	
	public String getId(){
		return id;
	}
	
	@Override
	public String toString() {
		return "Comment [comment=" + comment + ", blogid=" + blogId + "user "+userName+"]";
		}
}
