package com.cisco.blogger.microservice.user.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.cisco.blogger.microservice.user.UserTopics;
import com.cisco.blogger.microservice.user.model.User;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;

public class UserDBVerticle extends AbstractVerticle {
	
	private Datastore datatstore;
	
	public UserDBVerticle() {
		MongoClient mongo = new MongoClient("user-microservice-db", 27017);
		Morphia morphia = new Morphia();
		datatstore = morphia.createDatastore(mongo, "users");
	}

	@Override
	public void start() throws Exception {
		
		System.out.println("Strating User DB Verticle");
		
		vertx.eventBus().consumer(UserTopics.GET_USER, message -> {
			System.out.println("fetching user details for username = "+message.body().toString());
			getUserDetails(message);
			
		});
		
		vertx.eventBus().consumer(UserTopics.ADD_USER, message -> {
			//User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User Added = ");
			processAddUser(message);
			
		});
		
		vertx.eventBus().consumer(UserTopics.UPDATE_USER, message -> {
			//User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User updated = ");
			processUpdateUser(message);
		});
	}

	private void processUpdateUser(Message<Object> message) {
		User regData = Json.decodeValue(message.body().toString(), User.class);
		if(regData!=null){
			System.out.println("getFullName "+regData.getName());
			System.out.println(" usrName"+regData.getUsername());
		}
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		Query<User> query=dao.createQuery();
		query.and(
				query.criteria("username").equal(regData.getUsername()));
	UpdateOperations<User>	update=dao.createUpdateOperations().set("name", regData.getName()).
			set("username", regData.getUsername()).set("email", regData.getEmail())
			.set("phoneNumber", regData.getPhoneNumber()).set("areaOfInterest", regData.getAreaOfInterest());
		int updatedCount = dao.updateFirst(query, update).getUpdatedCount();
		System.out.println("UserDBVerticle.processUpdateUser()updatedCount"+updatedCount);
		if( updatedCount==1){
			message.reply(" User updated");
		}else{    
			message.reply(" User not updated ");   
		}
		
	}

	private void processAddUser(Message<Object> message) {
		User regData = Json.decodeValue(message.body().toString(), User.class);
		if(regData!=null){
			System.out.println("getFullName "+regData.getName());
			System.out.println(" usrName"+regData.getUsername());
		}
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		Object user=dao.save(regData).getId();
		JsonObject userCred = new JsonObject().put("username", regData.getUsername()).put("password", regData.getPassword());
		vertx.eventBus().send("com.cisco.blogger.auth.addcredentials", userCred);
		if(user==null){
			message.reply("No User created");
		}else{
			message.reply(Json.encodePrettily(user));
		}
	}

	private void getUserDetails(Message<Object> message) {
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		System.out.println("UserDBVerticle.getUserDetails() getUsername  "+message.body().toString());
		Query<User> query=dao.createQuery();
		query.and(query.criteria("username").equal(message.body().toString()));
		User user =query.get();
		
		if(user==null){
			message.reply("No User Found");
		}else{
			System.out.println("UserDBVerticle.getUserDetails()"+user.getName());
			message.reply(Json.encodePrettily(user));
		}
	}

	@Override
	public void stop() throws Exception {
		if(datatstore != null){
			datatstore.getMongo().close();
			datatstore = null;
		}
	}

}
