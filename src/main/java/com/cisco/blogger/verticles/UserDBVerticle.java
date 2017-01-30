package com.cisco.blogger.verticles;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.cisco.blogger.model.User;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
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
			message.reply(true);
			
			
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
			message.reply(true);
		});
	}

	@Override
	public void stop() throws Exception {
		if(datatstore != null){
			datatstore.getMongo().close();
			datatstore = null;
		}
	}

}
