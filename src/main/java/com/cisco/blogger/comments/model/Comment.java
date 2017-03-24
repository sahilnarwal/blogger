package com.cisco.blogger.comments.model;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.TimeZone;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
@Entity(noClassnameStored=true)
public class Comment {
	
	@Id
	private String id = new ObjectId().toString();

	private String blogId;
	
	private String userName;
	
	private String comment;
	
	private String dateOfCreation ;

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

	public String getDateOfCreation() {
		return dateOfCreation;
	}

	@PrePersist
	public void prePersist() {
		dateOfCreation=(dateOfCreation == null) ? getFormateDate(Instant.now()) : dateOfCreation;
	}

	private String getFormateDate(Instant instant) {
		long epoch= instant.getEpochSecond()*1000;
		Date date = new Date(epoch);
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("IST"));
		String formatted = format.format(date);
		return formatted;
	}
	@Override
	public String toString() {
		return "Comment [id=" + id + ", blogId=" + blogId + ", userName=" + userName + ", comment=" + comment
				+ ", dateOfCreation=" + dateOfCreation + "]";
	}
	
}
