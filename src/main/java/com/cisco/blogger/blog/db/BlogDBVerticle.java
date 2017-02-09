package com.cisco.blogger.blog.db;

import java.util.Date;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.query.FindOptions;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.Sort;
import org.mongodb.morphia.query.UpdateOperations;

import com.cisco.blogger.blog.BlogTopics;
import com.cisco.blogger.blog.model.Blog;
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
		vertx.eventBus().consumer(BlogTopics.GET_BLOG_BY_ID, message -> {
			System.out.println("Blog Fetched = ");
			getBlogById(message);
		});
		
		vertx.eventBus().consumer(BlogTopics.ADD_BLOG, message -> {
			createBlog(message);
		});
		
		vertx.eventBus().consumer(BlogTopics.GET_BLOG_BY_TITLE, message -> {
			System.out.println("Blog Searched = ");
			getBlogByTitle(message);
		});
		
		vertx.eventBus().consumer(BlogTopics.UPDATE_BLOG, message -> {
			updateBlog(message);
		});
		
		vertx.eventBus().consumer(BlogTopics.DELETE_BLOG, message -> {
			System.out.println("Blog deleted = ");
			deleteBlog(message);
		});
		
		vertx.eventBus().consumer(BlogTopics.FAV_BLOG, message -> {
			System.out.println("Fetch Fav Blog = ");
			favBlog(message);
		});
		
	}
	
	private void updateBlog(Message<Object> message) {

		String msgBody = message.body().toString();
		System.out.println("BlogDBVerticle.createBlog()"+msgBody);
		Blog blog = Json.decodeValue(msgBody, Blog.class);
		if(blog!=null){
			System.out.println("updateBlog content "+blog.getContent());
			System.out.println("updateBlog title "+blog.getTitle());
			System.out.println("updateBlog author"+blog.getAuthor());
			System.out.println("updateBlog() tags "+blog.getTags());
		}
		
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		Query<Blog>query=dao.createQuery();
		query.and(
				query.criteria(Mapper.ID_KEY).equal(blog.getId()));
		UpdateOperations<Blog> update = dao.createUpdateOperations().set("title", blog.getTitle())
				.set("content", blog.getContent()).set("author", blog.getAuthor()).set("tags", blog.getTags()).
				set("updateDate", new Date());
		int updatedCount = dao.updateFirst(query, update).getUpdatedCount();

		
		if( updatedCount==1){
			message.reply(" blog updated");
		}else{
			message.reply(" blog not updated ");
		}
		
	}
	
	private void favBlog(Message<Object> message) {
		String title = message.body().toString();
		System.out.println("BlogDBVerticle.favBlog()"+title);
		//String finalStr="/.*"+title+".*/";
	//	System.out.println("BlogDBVerticle.searchBlog() finalStr "+finalStr);
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		List<Blog> retreivedBlogs = dao.createQuery().field("tags").hasThisOne(title)
				.order(Sort.descending("updateDate")).asList(new FindOptions().limit(4));
		System.out.println("BlogDBVerticle.favBlog() retreivedBlog "+retreivedBlogs);
		if(retreivedBlogs==null){
			message.reply("Blog Doesn't Exist");
		}else{
			message.reply(Json.encodePrettily(retreivedBlogs));
			
		}
		
	}

	private void getBlogById(Message<Object> message) {
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

	private void getBlogByTitle(Message<Object> message) {
		String title = message.body().toString();
		System.out.println("BlogDBVerticle.processFetchBlog()"+title);
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		List<Blog> retreivedBlogs=dao.createQuery().field("title").containsIgnoreCase(title).asList();
		System.out.println("BlogDBVerticle.processFetchBlog() retreivedBlog "+retreivedBlogs);
		if(retreivedBlogs==null){
			message.reply("Blog Doesn't Exist");
		}else{
			message.reply(Json.encodePrettily(retreivedBlogs));
		}
		
	}

	private void deleteBlog(Message<Object> message) {

		String blogId = message.body().toString();
		BasicDAO<Blog, String> dao = new BasicDAO<>(Blog.class, datatstore);
		Object deletedBlog = dao.deleteByQuery(dao.createQuery().field(Mapper.ID_KEY).equal(blogId));
		if (deletedBlog == null) {
			message.reply("No blog delted");
		} else {
			System.out.println("Blog deleted = ");
			System.out.println("BlogDBVerticle.delete  blogId " + deletedBlog);
			message.reply(Json.encodePrettily(deletedBlog));
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