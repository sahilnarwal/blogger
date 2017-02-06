package com.cisco.blogger.user.model;

import java.util.Arrays;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class User {

	@Id
	private String id = new ObjectId().toString();
	private String name;
	private String username;
	private String email;
	private long phoneNumber;
	private String[] areaOfInterest;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public long getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String[] getAreaOfInterest() {
		return areaOfInterest;
	}
	public void setAreaOfInterest(String[] areaOfInterest) {
		this.areaOfInterest = areaOfInterest;
	}
	public String getId() {
		return id;
	}
	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", username=" + username + ", email="
				+ email + ", phoneNumber=" + phoneNumber + ", areaOfInterest=" + Arrays.toString(areaOfInterest) + "]";
	}

}
