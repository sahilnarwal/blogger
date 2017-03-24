package com.cisco.blogger.microservice.comment.model;


import java.time.Instant;

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
	
	private Instant dateOfCreation = Instant.now();

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

	public Instant getDateOfCreation() {
		return dateOfCreation;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", blogId=" + blogId + ", userName=" + userName + ", comment=" + comment
				+ ", dateOfCreation=" + dateOfCreation + "]";
	}
	
}
