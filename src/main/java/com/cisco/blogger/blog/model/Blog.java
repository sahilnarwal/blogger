package com.cisco.blogger.blog.model;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
@Entity
public class Blog {
	
	@Id
	private String id = new ObjectId().toString();
	
	

	private String title;
	
	private String[] tags;
	
	private String content;
	
	private Date timeOfCreation = new Date();
	
	private String author;
	
	
	public Blog(){
		
	}

	public Blog(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	public String[] getTags() {
		return tags;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getId() {
		return id;
	}
	@Override
	public String toString() {
		return "Blog [title=" + title + ", content=" + content + "id "+id+"]";
	}

}
