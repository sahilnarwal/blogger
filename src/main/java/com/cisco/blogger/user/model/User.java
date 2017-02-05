package com.cisco.blogger.user.model;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class User {

	@Id
	private String id = new ObjectId().toString();
	
	private String username;
	private String pwd;
	private String fullName;
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullname) {
		this.fullName = fullname;
	}
	
	public String getId() {
		return id;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "UserDetail  username "+username+",fullname "+", pwd "+pwd;
	}
	
}
