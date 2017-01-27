package com.cisco.blogger.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("starting...");
		Router router = SharedRouter.router;
		vertx.deployVerticle(DatabaseVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(UserVerticle.class.getName());
		vertx.deployVerticle(BlogVerticle.class.getName());
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		router.route("/static/*").handler(StaticHandler.create("web"));
		
		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 8082),
				result -> {
					if (result.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(result.cause());
					}
				});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("stopping...");
		super.stop();
	}

}
