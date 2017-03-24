package com.cisco.blogger.microservice.blog.model;

import java.time.Instant;
import java.util.Arrays;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
@Entity
public class Blog {
	
	@Id
	private String id = new ObjectId().toString();

	private String title;
	
	private String[] tags;
	
	private String content;
	
	private Instant timeOfCreation;
	
	private Instant updateDate;
	
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

	public Instant getTimeOfCreation() {
		return timeOfCreation;
	}

	public void setTimeOfCreation(Instant timeOfCreation) {
		this.timeOfCreation = timeOfCreation;
	}
	
	public Instant getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Instant updateDate) {
		this.updateDate = updateDate;
	}

	@PrePersist
	public void prePersist() {
		System.out.println("Blog.prePersist() timeOfCreation "+timeOfCreation);
		System.out.println("Blog.prePersist() updateDate "+updateDate);
		timeOfCreation = (timeOfCreation == null) ? Instant.now() : timeOfCreation;
	    updateDate = (updateDate == null) ? timeOfCreation : Instant.now();
	}

	@Override
	public String toString() {
		return "Blog [id=" + id + ", title=" + title + ", tags=" + Arrays.toString(tags) + ", content=" + content
				+ ", timeOfCreation=" + timeOfCreation + ", updateDate=" + updateDate + ", author=" + author + "]";
	}

}
