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
		registerUpdateBlogRoute(router);
		registerFavBlogRoute(router);
		registerGetAllBlogRoutes(router);
	}
	
	
	private void registerUpdateBlogRoute(Router router) {
		router.route(BlogRoutes.BLOG).handler(BodyHandler.create());
		router.put(BlogRoutes.BLOG).handler(rctx -> {
			vertx.eventBus().send(BlogTopics.UPDATE_BLOG, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerFavBlogRoute(Router router) {
		router.get(BlogRoutes.FAV_BLOG).handler(rctx -> {
			String title = rctx.request().getParam("areaOfInterest");
			System.out.println("BlogVerticle.registerSearchBlogRoute() title "+title);
			vertx.eventBus().send(BlogTopics.FAV_BLOG, title, r -> {
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

	private void registerSearchBlogRoute(Router router) {
		router.get(BlogRoutes.BLOG_WITH_TITLE).handler(rctx -> {
			String title = rctx.request().getParam("title");
			System.out.println("BlogVerticle.registerSearchBlogRoute() title "+title);
			vertx.eventBus().send(BlogTopics.GET_BLOG_BY_TITLE, title, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerDeleteBlogRoute(Router router) {
		router.delete(BlogRoutes.BLOG_WITH_ID).handler(rctx -> {
			String blogId = rctx.request().getParam("id");
			System.out.println("BlogVerticle.registerDeleteBlogRoute() id "+blogId);
			vertx.eventBus().send(BlogTopics.DELETE_BLOG, blogId, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
	}

	private void registerGetBlogRoute(Router router) {
		System.out.println("BlogVerticle.registerGetBlogRoute()");
		router.get(BlogRoutes.BLOG_WITH_ID).handler(rctx -> {
		String blogId=	rctx.request().getParam("id");
		System.out.println("BlogVerticle.registerGetBlogRoute() id "+blogId);
			vertx.eventBus().send(BlogTopics.GET_BLOG_BY_ID, blogId, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}
	
	private void registerGetAllBlogRoutes(Router router) {
		System.out.println("BlogVerticle.registerGetAllBlogRoutes()");
				//router.route(Routes.BLOGS).handler(BodyHandler.create());
				router.get(BlogRoutes.BLOGS).handler(rctx -> {
					System.out.println("BlogVerticle.registerGetAllBlogRoutes() got call ");
					
					vertx.eventBus().send(BlogTopics.GET_BLOGS, rctx.getBodyAsJson(), r -> {
						if(r.result()!=null ){
							rctx.response().setStatusCode(200).end(r.result().body().toString());
							}else{
								rctx.response().setStatusCode(400).end(r.cause().getMessage());
							}
						
						//rctx.response().setStatusCode(200).end(r.result().body().toString());
					});
				});
				}


	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Blog Verticle");
		super.stop();
	}
	
}
