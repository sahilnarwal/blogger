package com.cisco.blogger.microservice.desktop;


import java.util.HashMap;
import java.util.Map;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.AuthHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.UserSessionHandler;
import io.vertx.ext.web.sstore.ClusteredSessionStore;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class DesktopVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Strating Desktop Verticle");
		
		Router router = SharedRouter.router;
		
		router.route().handler(CorsHandler.create("*")
			      .allowedMethod(HttpMethod.GET)
			      .allowedMethod(HttpMethod.POST)
			      .allowedMethod(HttpMethod.PUT)
			      .allowedMethod(HttpMethod.DELETE)
			      .allowedMethod(HttpMethod.OPTIONS)
			      .allowedHeader("Authorization")
			      .allowedHeader("www-authenticate")
			      .allowedHeader("Content-Type"));
		
		// Add Routes
		router.route(Routes.ABOUT).handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});

		JsonObject config = new JsonObject().put("keyStore", new JsonObject()
			    .put("path", "keystores/jwt-keystore.jceks")
			    .put("type", "jceks")
			    .put("password", "secret"));

		JWTAuth provider = JWTAuth.create(vertx, config); 
		
		router.route().handler(CookieHandler.create());
		router.route().handler(SessionHandler.create(ClusteredSessionStore.create(vertx)));
		router.route().handler(UserSessionHandler.create(provider)); 

		AuthHandler redirectAuthHandler = RedirectAuthHandler.create(provider, "/login.html");
		
		router.route(Routes.SECURE_CONTENT).handler(redirectAuthHandler); 
		
		router.route(Routes.STATIC_CONTENT).handler(StaticHandler.create("webapp")); 
		
		// Start server and listen
		vertx.createHttpServer(/*new HttpServerOptions().setSsl(true)
				.setKeyStoreOptions(new JksOptions().setPath("keystores/server.jks").setPassword("password"))*/)
				.requestHandler(router::accept).listen(config().getInteger("http.port", 9000), result -> {
					if (result.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(result.cause());
					}
				});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Desktop Verticle");
		super.stop();
	}
	
}
