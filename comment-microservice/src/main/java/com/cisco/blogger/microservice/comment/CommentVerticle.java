package com.cisco.blogger.microservice.comment;

import com.cisco.blogger.microservice.comment.db.CommentDBVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class CommentVerticle extends AbstractVerticle{

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Strating Comment Verticle");
		Router router = SharedRouter.router;

		vertx.deployVerticle(CommentDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		
		router.route().handler(CorsHandler.create("*")
			      .allowedMethod(HttpMethod.GET)
			      .allowedMethod(HttpMethod.POST)
			      .allowedMethod(HttpMethod.PUT)
			      .allowedMethod(HttpMethod.DELETE)
			      .allowedMethod(HttpMethod.OPTIONS)
			      .allowedHeader("Authorization")
			      .allowedHeader("www-authenticate")
			      .allowedHeader("Content-Type"));
		
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from Commenbt Verticle my first Vert.x 3 application via routers</h1>");
		});
		// Add Comment CommentRoutes
		registerGetCommentRoute(router);
		
		registerAddCommentRoute(router);
		
		// Start server and listen
				vertx.createHttpServer(/*new HttpServerOptions().setSsl(true)
						.setKeyStoreOptions(new JksOptions().setPath("keystores/server.jks").setPassword("password"))*/)
						.requestHandler(router::accept).listen(config().getInteger("http.port", 9003), result -> {
							if (result.succeeded()) {
								startFuture.complete();
							} else {
								startFuture.fail(result.cause());
							}
						});
	}

	private void registerAddCommentRoute(Router router) {
		router.route(CommentRoutes.COMMENT).handler(BodyHandler.create());
		router.post(CommentRoutes.COMMENT).handler(rctx -> {
			vertx.eventBus().send(CommentTopics.ADD_COMMENT, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerGetCommentRoute(Router router) {
		router.get(CommentRoutes.COMMENT_FOR_BLOG).handler(rctx -> {
			String blogId = rctx.request().getParam("blogid");
			System.out.println("Blog title to search comments for="+blogId);
			vertx.eventBus().send(CommentTopics.GET_COMMENT, blogId, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Comment Verticle");
		super.stop();
	}
	
}
