package com.cisco.blogger.verticles;

import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.QueryResults;

import com.cisco.blogger.model.Blog;
import com.mongodb.MongoClient;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.Message;
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
			System.out.println("Blog Fetched = ");
			processFetchBlog(message);
		});
		
		vertx.eventBus().consumer(Topics.ADD_BLOG, message -> {
			createBlog(message);
		});
		
		vertx.eventBus().consumer(Topics.SEARCH_BLOG, message -> {
			System.out.println("Blog Searched = ");
			searchBlog(message);
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.UPDATE_BLOG, message -> {
			//Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println("Blog Updated = ");
			message.reply(true);
		});
		
		vertx.eventBus().consumer(Topics.DELETE_BLOG, message -> {
			System.out.println("Blog deleted = ");
			deleteBlog(message);
		});
		
	}

	private void searchBlog(Message<Object> message) {
		String title = message.body().toString();
		System.out.println("BlogDBVerticle.processFetchBlog()"+title);
		String finalStr="/.*"+title+".*/";
		System.out.println("BlogDBVerticle.searchBlog() finalStr "+finalStr);
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		List<Blog> retreivedBlogs=dao.createQuery().field("title").containsIgnoreCase(finalStr).asList();
		System.out.println("BlogDBVerticle.processFetchBlog() retreivedBlog "+retreivedBlogs);
		if(retreivedBlogs==null){
			message.reply("Blog Doesn't Exist");
		}else{
			message.reply(Json.encodePrettily(retreivedBlogs));
			
		}
		
	}

	private void processFetchBlog(Message<Object> message) {
		String id = message.body().toString();
		System.out.println("BlogDBVerticle.processFetchBlog()"+id);
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		Blog retreivedBlog=dao.createQuery().field(Mapper.ID_KEY).equal(id).get();
		System.out.println("BlogDBVerticle.processFetchBlog() retreivedBlog "+retreivedBlog);
		if(retreivedBlog==null){
			message.reply("Blog Doesn't Exist");
		}else{
			message.reply(Json.encodePrettily(retreivedBlog));
			
		}
		
	}

	private void deleteBlog(Message<Object> message) {

		String msgBody = message.body().toString();
		Blog blog = Json.decodeValue(msgBody, Blog.class);
		if(blog!=null){
			System.out.println("deleteBlog id "+blog.getId());
		}
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		Object blogId=dao.deleteByQuery(dao.createQuery().field(Mapper.ID_KEY).equal(blog.getId()));
				if(blogId==null){
			message.reply("No blog delted");
		}else{
			System.out.println("Blog deleted = ");
			System.out.println("BlogDBVerticle.delete  blogId "+blogId);
			message.reply(Json.encodePrettily(blogId));
		}
	
		
	}

	private void createBlog(Message<Object> message) {
		String msgBody = message.body().toString();
		System.out.println("BlogDBVerticle.createBlog()"+msgBody);
		Blog blog = Json.decodeValue(msgBody, Blog.class);
		if(blog!=null){
			System.out.println("createBlog content "+blog.getContent());
			System.out.println("createBlog title "+blog.getTitle());
			System.out.println("createBlog author"+blog.getAuthor());
			System.out.println("createBlog() tags "+blog.getTags());
		}
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		Object blogId=dao.save(blog).getId();
				if(blogId==null){
			message.reply("No blog created");
		}else{
			System.out.println("Blog Added = ");
			System.out.println("BlogDBVerticle.createBlog() blogId "+blogId);
			message.reply(Json.encodePrettily(blogId));
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