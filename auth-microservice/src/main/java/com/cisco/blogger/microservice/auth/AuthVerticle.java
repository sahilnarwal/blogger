package com.cisco.blogger.microservice.auth;

import java.util.ArrayList;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.CorsHandler;
import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class AuthVerticle extends AbstractVerticle{
	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Starting Auth Verticle");
		Router router = Router.router(vertx);
		
		router.route().handler(CorsHandler.create("*")
			      .allowedMethod(HttpMethod.GET)
			      .allowedMethod(HttpMethod.POST)
			      .allowedMethod(HttpMethod.PUT)
			      .allowedMethod(HttpMethod.DELETE)
			      .allowedMethod(HttpMethod.OPTIONS)
			      .allowedHeader("Authorization")
			      .allowedHeader("www-authenticate")
			      .allowedHeader("Content-Type"));
		router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        
		JsonObject config = new JsonObject().put("keyStore", new JsonObject()
			    .put("path", "keystores/jwt-keystore.jceks")
			    .put("type", "jceks")
			    .put("password", "secret"));

		JWTAuth provider = JWTAuth.create(vertx, config);
		
		
		router.route("/api/*").handler(JWTAuthHandler.create(provider, AuthRoutes.GET_TOKEN));
        
		
		
		
      
		router.route(AuthRoutes.REGISTER).handler(BodyHandler.create());
		router.post(AuthRoutes.REGISTER).handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.ADD_USER_CRED, rctx.getBodyAsJson(), res -> {
				rctx.response().setStatusCode(200).end("User Added");
			});
		});
		
		router.get(AuthRoutes.GET_TOKEN).handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.NEW_TOKEN, "", res -> {
				rctx.response().setStatusCode(200).end(res.result().body().toString());
			});
		});
		
		router.post(AuthRoutes.TEST_TOKEN_VALIDATION).handler(rctx -> {
			String authHeader = rctx.request().getHeader("Authorization");
			System.out.println("Authentication headers"+authHeader);
			String token = authHeader.split(" ")[1];
			vertx.eventBus().send(AuthTopics.AUTHORIZE, token, res -> {
				rctx.response().setStatusCode(200).end("Token Validated	"+res.result().body().toString());
			});
		});
		
		vertx.eventBus().consumer(AuthTopics.AUTHORIZE, message -> {
			System.out.println("Doing Authorize for token = "+message.body().toString());
			provider.authenticate(new JsonObject().put("jwt", message.body().toString()), res -> {
				if(res.succeeded()){
					message.reply(true);
				}else {
					message.reply(false);
				}
			});
		});
		
		vertx.eventBus().consumer(AuthTopics.NEW_TOKEN, message -> {
			System.out.println("Issuing new token");
			//f ("paulo".equals("username") && "super_secret".equals("password")) {
			String token = provider.generateToken(new JsonObject().put("sub", "").put("role", ""), new JWTOptions()
					.setAlgorithm("RS512").setExpiresInMinutes(Long.valueOf(120)).setPermissions(new ArrayList<>()));
				  // now for any request to protected resources you should pass this string in the HTTP header Authorization as:
				  // Authorization: Bearer <token>
				  System.out.println("Token generated = "+token);
				  message.reply(token);
			//	}
		});
		
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

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Auth Verticle");
		super.stop();
	}
	
}
