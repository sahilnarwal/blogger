package com.cisco.blogger.blog.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
@Entity(noClassnameStored=true)
public class Blog {
	
	@Id
	private String id = new ObjectId().toString();

	private String title;
	
	private String[] tags;
	
	private String content;
	
	private String timeOfCreation;
	
	private String updateDate;
	
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

	public String getTimeOfCreation() {
		return timeOfCreation;
	}

	public void setTimeOfCreation(String timeOfCreation) {
		this.timeOfCreation = timeOfCreation;
	}
	
	public String getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(String updateDate) {
		this.updateDate = updateDate;
	}

	@PrePersist
	public void prePersist() {
		System.out.println("Blog.prePersist() timeOfCreation "+timeOfCreation);
		System.out.println("Blog.prePersist() updateDate "+updateDate);
		timeOfCreation=(timeOfCreation == null) ? getFormateDate(Instant.now()) : timeOfCreation;
		updateDate=(updateDate==null)?timeOfCreation:getFormateDate(Instant.now());
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
		return "Blog [id=" + id + ", title=" + title + ", tags=" + Arrays.toString(tags) + ", content=" + content
				+ ", timeOfCreation=" + timeOfCreation + ", updateDate=" + updateDate + ", author=" + author + "]";
	}

}
