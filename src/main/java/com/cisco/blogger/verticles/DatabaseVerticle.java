package com.cisco.blogger.verticles;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.cisco.blogger.model.Blog;
import com.cisco.blogger.model.Comment;
import com.cisco.blogger.model.User;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class DatabaseVerticle extends AbstractVerticle{
	
	private static Datastore datatstore;
	
	public DatabaseVerticle() {
		if(datatstore==null){
			MongoClient mongo = new MongoClient("localhost", 27017);
			Morphia morphia = new Morphia();
			datatstore= morphia.createDatastore(mongo, "cmad-morphia");
		}
	}
	
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("com.cisco.blogger.blog.fetch", message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Fetched = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer("com.cisco.blogger.blog.add", message -> {
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Added = "+blog);
			message.reply(true);
		});
		
		vertx.eventBus().consumer("com.cisco.blogger.blog.search", message -> {
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Searched = "+blog);
			message.reply(true);
		});
		
		vertx.eventBus().consumer("com.cisco.blogger.blog.update", message -> {
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Updated = "+blog);
			message.reply(true);
		});
		
		vertx.eventBus().consumer("com.cisco.blogger.user.add", message -> {
			User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User Added = "+user);
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
		
		vertx.eventBus().consumer("com.cisco.blogger.user.fetch", message -> {
			User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User Fetched = "+user);
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
		
		vertx.eventBus().consumer("com.cisco.blogger.user.update", message -> {
			User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("User updated = "+user);
			message.reply(true);
		});
		
		vertx.eventBus().consumer("com.cisco.blogger.comment.add", message -> {
			Comment comment = Json.decodeValue(message.body().toString(), Comment.class);
			System.out.println("Comment Added = "+comment);
			message.reply(true);
		});
		
		vertx.eventBus().consumer("com.cisco.blogger.comment.get", message -> {
			Comment comment = Json.decodeValue(message.body().toString(), Comment.class);
			System.out.println("Comment Fetched = "+comment);
			message.reply(true);
		});
	}

	@Override
	public void stop() throws Exception {
		if(datatstore!=null){
			datatstore.getMongo().close();
			datatstore = null;
		}
	}
	
	

}
