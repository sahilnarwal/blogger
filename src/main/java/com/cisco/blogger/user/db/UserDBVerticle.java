package com.cisco.blogger.user.db;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.cisco.blogger.user.UserTopics;
import com.cisco.blogger.user.model.User;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.Json;

public class UserDBVerticle extends AbstractVerticle {
	
	private Datastore datatstore;
	
	public UserDBVerticle() {
		MongoClient mongo = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		datatstore = morphia.createDatastore(mongo, "users");
	}

	@Override
	public void start() throws Exception {
		
		System.out.println("Strating User DB Verticle");
		
		vertx.eventBus().consumer(UserTopics.GET_USER, message -> {
			getUserDetails(message);
			System.out.println("User Fetched = ");
		//	message.reply(true);
		});
		
		vertx.eventBus().consumer(UserTopics.ADD_USER, message -> {
			//User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User Added = ");
			processAddUser(message);
		});
		
		vertx.eventBus().consumer(UserTopics.UPDATE_USER, message -> {
			System.out.println("User updated = ");
			processUpdateUser(message);
			//message.reply(true);
		});
	}

	private void processUpdateUser(Message<Object> message) {
		User regData = Json.decodeValue(message.body().toString(), User.class);
		if(regData!=null){
			System.out.println("UserDBVerticle.processUpdateUser()");
			System.out.println("getFullName "+regData.getName());
			System.out.println("id "+regData.getId());
			System.out.println("pwd "+regData.getPwd());
			System.out.println(" usrName"+regData.getUsername());
			System.out.println(" areofIntrest "+regData.getAreaOfInterest());
			System.out.println(" phoneNumber "+regData.getPhoneNumber());
			System.out.println(" emailId "+regData.getEmail());
		}
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		Query<User> query=dao.createQuery();
		query.and(
				query.criteria("username").equal(regData.getUsername()));
		UpdateOperations<User> update = dao.createUpdateOperations().set("name", regData.getName())
				.set("pwd", regData.getPwd()).set("areaOfInterest", regData.getAreaOfInterest()).
				set("phoneNumber", regData.getPhoneNumber()).set("email", regData.getEmail());
		int updatedCount = dao.updateFirst(query, update).getUpdatedCount();
		System.out.println("UserDBVerticle.processUpdateUser()updatedCount"+updatedCount);
		if( updatedCount==1){
		//	message.reply(" User updated");
			message.reply(Json.encodePrettily(regData));
		}else{
			message.fail(404, "User not updated");
			//message.reply(" User not updated ");
		}
		
	}

	private void processAddUser(Message<Object> message) {
		User regData = Json.decodeValue(message.body().toString(), User.class);
		if(regData!=null){
			System.out.println("UserDBVerticle.processAddUser()");
			System.out.println("getFullName "+regData.getName());
			System.out.println("pwd "+regData.getPwd());
			System.out.println(" usrName"+regData.getUsername());
			System.out.println(" areofIntrest "+regData.getAreaOfInterest());
			System.out.println(" phoneNumber "+regData.getPhoneNumber());
			System.out.println(" emailId "+regData.getEmail());
			
		}
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		Object user=dao.save(regData);
		System.out.println("UserDBVerticle.processAddUser() user "+user);
				if(user==null){
					message.fail(404, "No User Created");
			//message.reply("No User created");
		}else{
//			message.reply(Json.encodePrettily(user));
			System.out.println("UserDBVerticle.processAddUser() regData = "+regData);
			message.reply(Json.encodePrettily(regData));
		}
	}

	private void getUserDetails(Message<Object> message) {
		User userDetail = Json.decodeValue(message.body().toString(), User.class);
		System.out.println("UserDBVerticle.getUserDetails() body "+message.body().toString());
		System.out.println("UserDBVerticle.getUserDetails() user "+userDetail);
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		System.out.println("UserDBVerticle.getUserDetails() getUsername  "+userDetail.getUsername());
		System.out.println("UserDBVerticle.getUserDetails() getPwd  "+userDetail.getPwd());
		Query<User> query=dao.createQuery();
		query.and(
				query.criteria("username").equal(userDetail.getUsername()),
				query.criteria("pwd").equal(userDetail.getPwd()));
		User user =query.get();
		if(user==null){
			message.fail(404, "No User Found");
			//message.reply("No User Found");
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
