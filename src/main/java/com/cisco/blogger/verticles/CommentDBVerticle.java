package com.cisco.blogger.verticles;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.cisco.blogger.model.Comment;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class CommentDBVerticle extends AbstractVerticle {
	
	private Datastore datatstore;
	
	public CommentDBVerticle() {
		MongoClient mongo = new MongoClient("localhost", 27017);
		Morphia morphia = new Morphia();
		datatstore = morphia.createDatastore(mongo, "comments");
	}

	@Override
	public void start() throws Exception {
		
		System.out.println("Strating Comment DB Verticle");

		vertx.eventBus().consumer(Topics.GET_COMMENT, message -> {
			//Comment comment = Json.decodeValue(message.body().toString(), Comment.class);
			System.out.println("Comment Fetched = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.ADD_COMMENT, message -> {
			//Comment comment = Json.decodeValue(message.body().toString(), Comment.class);
			System.out.println("Comment Added = ");
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
