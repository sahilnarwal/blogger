package com.cisco.blogger.verticles;

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
		registerUpdateBlogRoute(router);
		
		registerGetBlogRoute(router);
		
		registerFavBlogRoute(router);
		
		
	}

	private void registerUpdateBlogRoute(Router router) {
		router.route(Routes.UPDATE_BLOG).handler(BodyHandler.create());
		router.post(Routes.UPDATE_BLOG).handler(rctx -> {
			vertx.eventBus().send(Topics.UPDATE_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerFavBlogRoute(Router router) {
		router.get(Routes.FAV_BLOG).handler(rctx -> {
			String title = rctx.request().getParam("areaOfInterest");
			System.out.println("BlogVerticle.registerSearchBlogRoute() title "+title);
			vertx.eventBus().send(Topics.FAV_BLOG, title, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerDeleteBlogRoute(Router router) {
		router.route(Routes.DELETE_BLOG).handler(BodyHandler.create());
		router.post(Routes.DELETE_BLOG).handler(rctx -> {
			vertx.eventBus().send(Topics.DELETE_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
	}

	private void registerAddBlogRoute(Router router) {
		router.route(Routes.BLOG).handler(BodyHandler.create());
		router.post(Routes.BLOG).handler(rctx -> {
			vertx.eventBus().send(Topics.ADD_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerGetBlogRoute(Router router) {
		System.out.println("BlogVerticle.registerGetBlogRoute()");
		router.get(Routes.BLOG).handler(rctx -> {
		String id=	rctx.request().getHeader("id");
		System.out.println("BlogVerticle.registerGetBlogRoute() id "+id);
		
			vertx.eventBus().send(Topics.GET_BLOG, id, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerSearchBlogRoute(Router router) {
		router.get(Routes.SEARCH_BLOG).handler(rctx -> {
			String title = rctx.request().getParam("title");
			System.out.println("BlogVerticle.registerSearchBlogRoute() title "+title);
			vertx.eventBus().send(Topics.SEARCH_BLOG, title, r -> {
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
