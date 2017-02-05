package com.cisco.blogger.comments.db;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;

import com.cisco.blogger.comments.CommentTopics;
import com.cisco.blogger.comments.model.Comment;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
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

		vertx.eventBus().consumer(CommentTopics.GET_COMMENT, message -> {
			//Comment comment = Json.decodeValue(message.body().toString(), Comment.class);
			System.out.println("Comment Fetched = ");
			fetchComment(message);
			message.reply(true);
		});
		
		vertx.eventBus().consumer(CommentTopics.ADD_COMMENT, message -> {
			//Comment comment = Json.decodeValue(message.body().toString(), Comment.class);
			System.out.println("Comment Added = ");
			createComment(message);
		});
	}

	private void fetchComment(Message<Object> message) {

		String id = message.body().toString();
		System.out.println("CommentDBVerticle.fetchComment()"+id);
		BasicDAO<Comment, String> dao = new BasicDAO<>(Comment.class, datatstore);
		List<Comment> retreiveComments=dao.createQuery().field("blogId").equal(id).asList();
		System.out.println("CommentDBVerticle.fetchComment() retreiveComments "+retreiveComments);
		if(retreiveComments==null){
			message.reply("No Comments");
		}else{
			message.reply(Json.encodePrettily(retreiveComments));
			
		}
		
		
	}

	private void createComment(Message<Object> message) {

		String msgBody = message.body().toString();
		System.out.println("CommentDBVerticle.createComment() msgBody "+msgBody);
		Comment comment = Json.decodeValue(msgBody, Comment.class);
		if(comment!=null){
			System.out.println("CommentDBVerticle.createComment() blogId "+comment.getBlogId());
			System.out.println("CommentDBVerticle.createComment() userName "+comment.getUserName());
			System.out.println("CommentDBVerticle.createComment()comment "+comment.getComment());
		}
		BasicDAO<Comment, String> dao = new BasicDAO<>(Comment.class, datatstore);
		Object commentId=dao.save(comment).getId();
				if(commentId==null){
			message.reply("No comment created");
		}else{
			System.out.println("CommentDBVerticle.createBlog() blogId "+commentId);
			message.reply(Json.encodePrettily(commentId));
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
