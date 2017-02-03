package com.cisco.blogger.verticles;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;

import com.cisco.blogger.model.User;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;

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
		
		vertx.eventBus().consumer(Topics.GET_USER, message -> {
			getUserDetails(message);
			//User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User Fetched = ");
			message.reply(true);
			
			/*LoginDTO loginData = Json.decodeValue(message.body().toString(), LoginDTO.class);
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println(blog);
			message.reply(true);
			
			
			BasicDAO<User, String> dao = new BasicDAO<>(User.class, MongoService.getDataStore());
			Query<User> query=dao.createQuery();
			query.and(
					query.criteria("username").equal(loginData.getUsername()),
					query.criteria("pwd").equal(loginData.getPwd()));
			User user =query.get();
			if(user==null){
				message.reply("No User Found");
			}else{
				message.reply(Json.encodePrettily(user));
			}*/
		});
		
		vertx.eventBus().consumer(Topics.ADD_USER, message -> {
			//User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User Added = ");
			processAddUser(message);
			
			/*User regData = Json.decodeValue(message.body().toString(), User.class);
			if(regData!=null){
				System.out.println("UserVerticle.start()getFullName "+regData.getFullName());
				System.out.println("UserVerticle.start()pwd "+regData.getPwd());
				System.out.println("UserVerticle.start() usrName"+regData.getUsername());
			}
			BasicDAO<User, String> dao = new BasicDAO<>(User.class, MongoService.getDataStore());
			dao.save(regData);
			Query<UserDetail> query=dao.createQuery();
			query.
			query.and(
					query.criteria("username").equal(loginData.getUsername()),
					query.criteria("pwd").equal(loginData.getPwd()));
			Object user =dao.save(regData).getId();query.get();
			if(user==null){
				message.reply("No User created");
			}else{
				message.reply(Json.encodePrettily(user));
			}*/
			
		});
		
		vertx.eventBus().consumer(Topics.UPDATE_USER, message -> {
			//User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User updated = ");
			processUpdateUser(message);
			message.reply(true);
		});
	}

	private void processUpdateUser(Message<Object> message) {
		User regData = Json.decodeValue(message.body().toString(), User.class);
		if(regData!=null){
			System.out.println("getFullName "+regData.getFullName());
			System.out.println("pwd "+regData.getPwd());
			System.out.println(" usrName"+regData.getUsername());
		}
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		Query<User> query=dao.createQuery();
		query.and(
				query.criteria("username").equal(regData.getUsername()));
		UpdateOperations<User> update = dao.createUpdateOperations().set("fullName", regData.getFullName())
				.set("pwd", regData.getPwd()).set("areaOfInterest", regData.getAreaOfInterest());
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
			System.out.println("getFullName "+regData.getFullName());
			System.out.println("pwd "+regData.getPwd());
			System.out.println(" usrName"+regData.getUsername());
			System.out.println(" areofIntrest "+regData.getAreaOfInterest());
		}
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		Object user=dao.save(regData).getId();
				if(user==null){
			message.reply("No User created");
		}else{
			message.reply(Json.encodePrettily(user));
		}
	}

	private void getUserDetails(Message<Object> message) {
		User userDetail = Json.decodeValue(message.body().toString(), User.class);
		BasicDAO<User, String> dao = new BasicDAO<>(User.class, datatstore);
		System.out.println("UserDBVerticle.getUserDetails() getUsername  "+userDetail.getUsername());
		System.out.println("UserDBVerticle.getUserDetails() getPwd  "+userDetail.getPwd());
		Query<User> query=dao.createQuery();
		query.and(
				query.criteria("username").equal(userDetail.getUsername()),
				query.criteria("pwd").equal(userDetail.getPwd()));
		User user =query.get();
		
		if(user==null){
			message.reply("No User Found");
		}else{
			System.out.println("UserDBVerticle.getUserDetails()"+user.getFullName());
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
