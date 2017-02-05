package com.cisco.blogger.blog;

import com.cisco.blogger.SharedRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BlogVerticle extends AbstractVerticle{

	@Override
	public void start() throws Exception {
		System.out.println("Strating Blog Verticle");
		
		Router router = SharedRouter.router;
		
		// Add Blog Routes
		registerAddBlogRoute(router);
		registerSearchBlogRoute(router);
		registerDeleteBlogRoute(router);
		
		registerGetBlogRoute(router);
		
		
	}

	private void registerDeleteBlogRoute(Router router) {
		router.route(BlogRoutes.BLOG).handler(BodyHandler.create());
		router.delete(BlogRoutes.BLOG).handler(rctx -> {
			vertx.eventBus().send(BlogTopics.DELETE_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
	}

	private void registerAddBlogRoute(Router router) {
		router.route(BlogRoutes.BLOG).handler(BodyHandler.create());
		router.post(BlogRoutes.BLOG).handler(rctx -> {
			vertx.eventBus().send(BlogTopics.ADD_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerGetBlogRoute(Router router) {
		System.out.println("BlogVerticle.registerGetBlogRoute()");
		router.get(BlogRoutes.BLOG).handler(rctx -> {
		String id=	rctx.request().getHeader("id");
		System.out.println("BlogVerticle.registerGetBlogRoute() id "+id);
		
			vertx.eventBus().send(BlogTopics.GET_BLOG, id, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerSearchBlogRoute(Router router) {
		router.get(BlogRoutes.SEARCH_BLOG).handler(rctx -> {
			String title = rctx.request().getParam("title");
			System.out.println("BlogVerticle.registerSearchBlogRoute() title "+title);
			vertx.eventBus().send(BlogTopics.SEARCH_BLOG, title, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Blog Verticle");
		super.stop();
	}
	
}
