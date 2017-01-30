package com.cisco.blogger.verticles;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.cisco.blogger.model.Blog;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class BlogDBVerticle extends AbstractVerticle{
	
	private Datastore datatstore;
	
	public BlogDBVerticle() {
		MongoClient mongo = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		datatstore = morphia.createDatastore(mongo, "blogs");
	}
	
	@Override
	public void start() throws Exception {
		System.out.println("Strating Blog DB Verticle");
		
		// Add Topic Listeners
		vertx.eventBus().consumer(Topics.GET_BLOG, message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Fetched = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.ADD_BLOG, message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Added = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.SEARCH_BLOG, message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Searched = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.UPDATE_BLOG, message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Updated = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.DELETE_BLOG, message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog deleted = ");
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