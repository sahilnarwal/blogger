package com.cisco.blogger.microservice.blog;

import com.cisco.blogger.microservice.blog.db.BlogDBVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class BlogVerticle extends AbstractVerticle{

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Strating Blog Verticle");
		
		Router router = SharedRouter.router;
		
		// Deploy Verticles
		vertx.deployVerticle(BlogDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		
		
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		// Add Blog Routes
		registerAddBlogRoute(router);
		registerSearchBlogRoute(router);
		registerDeleteBlogRoute(router);
		registerUpdateBlogRoute(router);
		
		// Start server and listen
		vertx.createHttpServer(/*new HttpServerOptions().setSsl(true)
				.setKeyStoreOptions(new JksOptions().setPath("keystores/server.jks").setPassword("password"))*/)
				.requestHandler(router::accept).listen(config().getInteger("http.port", 80), result -> {
					if (result.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(result.cause());
					}
				});
	}
	
	private void registerUpdateBlogRoute(Router router) {
		router.route(BlogRoutes.BLOG).handler(BodyHandler.create());
		router.put(BlogRoutes.BLOG).handler(rctx -> {
			vertx.eventBus().send(BlogTopics.UPDATE_BLOG, rctx.getBodyAsJson(), r -> {
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
		router.get(BlogRoutes.SPECIFIC_BLOG).handler(rctx -> {
			String type = rctx.request().getParam("type");
			System.out.println(type);
			if("title".equalsIgnoreCase(type)){
				String title = rctx.request().getParam("identifier");
				System.out.println(title);
				vertx.eventBus().send(BlogTopics.GET_BLOG_BY_TITLE, title, r -> {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				});
			}else if("id".equalsIgnoreCase(type)){
				String id = rctx.request().getParam("identifier");
				System.out.println(id);
				vertx.eventBus().send(BlogTopics.GET_BLOG_BY_ID, id, r -> {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				});
			}else if("tag".equalsIgnoreCase(type)){
				String areaOfInterest = rctx.request().getParam("identifier");
				vertx.eventBus().send(BlogTopics.FAV_BLOG, areaOfInterest, r -> {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				});
			}else {
				rctx.response().setStatusCode(400).end("Invalid type parameter");
			}
		});
	}

	private void registerDeleteBlogRoute(Router router) {
		router.delete(BlogRoutes.SPECIFIC_BLOG).handler(rctx -> {
			String type = rctx.request().getParam("type");
			System.out.println(type);
			/*if(type.equalsIgnoreCase("title")){
				String title = rctx.request().getParam("title");
				System.out.println(title);
				vertx.eventBus().send(BlogTopics.GET_BLOG_BY_TITLE, title, r -> {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				});
			}else */if("id".equalsIgnoreCase(type)){
				String id = rctx.request().getParam("identifier");
				System.out.println(id);
				vertx.eventBus().send(BlogTopics.DELETE_BLOG, id, r -> {
					rctx.response().setStatusCode(200).end(r.result().body().toString());
				});
			}else {
				rctx.response().setStatusCode(400).end("Invalid type parameter");
			}
		});
		
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Blog Verticle");
		super.stop();
	}
	
}
