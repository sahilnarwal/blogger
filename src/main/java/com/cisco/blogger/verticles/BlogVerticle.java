package com.cisco.blogger.verticles;

import com.cisco.blogger.model.Blog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BlogVerticle extends AbstractVerticle{

	@Override
	public void start() throws Exception {
		Router router = SharedRouter.router;
		
		router.get("/api/blog/:title").handler(rctx -> {
			String title = rctx.request().getParam("title");
			final Blog blog = new Blog(title, "Mongo on Vertx");
			rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(blog));
		});
		
		router.get("/api/blog").handler(rctx -> {
			vertx.eventBus().send("com.cisco.blogger.blog.fetch", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end();
			});
		});
		
		router.route("/api/blog").handler(BodyHandler.create());
		router.post("/api/blog").handler(rctx -> {
			vertx.eventBus().send("com.cisco.blogger.blog.add", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end();
			});
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("stopping...");
		super.stop();
	}
	
}
