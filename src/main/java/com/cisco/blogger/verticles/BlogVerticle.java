package com.cisco.blogger.verticles;

import com.cisco.blogger.model.Blog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BlogVerticle extends AbstractVerticle{

	@Override
	public void start() throws Exception {
		System.out.println("Strating Blog Verticle");
		
		Router router = SharedRouter.router;
		
		// Add Blog Routes
		
		registerSearchBlogRoute(router);
		
		registerGetBlogRoute(router);
		
		registerAddBlogRoute(router);
	}

	private void registerAddBlogRoute(Router router) {
		router.route(Routes.BLOG).handler(BodyHandler.create());
		router.post(Routes.BLOG).handler(rctx -> {
			vertx.eventBus().send(Topics.ADD_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end();
			});
		});
	}

	private void registerGetBlogRoute(Router router) {
		router.get(Routes.BLOG).handler(rctx -> {
			vertx.eventBus().send(Topics.GET_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end();
			});
		});
	}

	private void registerSearchBlogRoute(Router router) {
		router.get(Routes.SEARCH_BLOG).handler(rctx -> {
			String title = rctx.request().getParam("title");
			final Blog blog = new Blog(title, "Mongo on Vertx");
			rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(blog));
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Blog Verticle");
		super.stop();
	}
	
}
