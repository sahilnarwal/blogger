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
		vertx.deployVerticle(DatabaseVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(UserVerticle.class.getName(), new DeploymentOptions().setWorker(true));
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
		
		router.route("/api/user/registeration").handler(BodyHandler.create());
		router.post("/api/user/registeration").handler(rctx -> {
			System.out.println("MainVerticle.start() inside register ");
			String name = rctx.request().getParam("name");
			String pwd = rctx.request().getParam("pwd");
			System.out.println("MainVerticle.start() name " + name);
			System.out.println("MainVerticle.start()pwd " + pwd);
			// final DictionaryItem item = new
			// DictionaryItem(name,"dummy","anonym");
			vertx.eventBus().send("com.cisco.cmad.projects.register", rctx.getBodyAsJson(), r -> {
				System.out.println("MainVerticle.start() register message " + r.result().body().toString());
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
			/*
			 * rctx.response().setStatusCode(200).putHeader("content-type",
			 * "application/json; charset=utf-8") .end();
			 */
		});
		router.route("/api/login").handler(BodyHandler.create());
		router.post("/api/login").handler(rctx -> {
			vertx.eventBus().send("com.cisco.cmad.projects.login", rctx.getBodyAsJson(), r -> {
				System.out.println("MainVerticle.start() message " + r.result().body().toString());
				rctx.response().setStatusCode(200).end(r.result().body().toString());
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
