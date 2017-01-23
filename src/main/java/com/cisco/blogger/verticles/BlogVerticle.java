package com.cisco.blogger.verticles;

import com.cisco.blogger.model.Blog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

public class BlogVerticle extends AbstractVerticle{

	@Override
	public void start(Future<Void> future) throws Exception {
		System.out.println("starting...");
		Router router = Router.router(vertx);
		vertx.deployVerticle("com.cisco.blogger.verticles.DatabaseVerticle", new DeploymentOptions().setWorker(true));
		router.route("/about").handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		router.route("/static/*").handler(StaticHandler.create("web"));
		router.get("/api/blog/:title").handler(rctx -> {
			String title = rctx.request().getParam("title");
			final Blog blog = new Blog(title, "Mongo on Vertx");
			rctx.response().setStatusCode(200).putHeader("content-type", "application/json; charset=utf-8")
					.end(Json.encodePrettily(blog));
		});
		router.route("/api/blog").handler(BodyHandler.create());
		router.post("/api/blog").handler(rctx -> {
			vertx.eventBus().send("com.cisco.blog.save", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end();
			});
		});

		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port", 8082),
				result -> {
					if (result.succeeded()) {
						future.complete();
					} else {
						future.fail(result.cause());
					}
				});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("stopping...");
		super.stop();
	}
	
}
