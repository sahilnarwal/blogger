package com.cisco.blogger.microservice.auth;

import java.util.ArrayList;

import com.cisco.blogger.microservice.auth.db.AuthDBVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.CorsHandler;

public class AuthVerticle extends AbstractVerticle{
	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Starting Auth Verticle");
		Router router = Router.router(vertx);
		
		// Deploy Verticles
		vertx.deployVerticle(AuthDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		
		router.route().handler(CorsHandler.create("*")
			      .allowedMethod(HttpMethod.GET)
			      .allowedMethod(HttpMethod.POST)
			      .allowedMethod(HttpMethod.PUT)
			      .allowedMethod(HttpMethod.DELETE)
			      .allowedMethod(HttpMethod.OPTIONS)
			      .allowedHeader("Authorization")
			      .allowedHeader("www-authenticate")
			      .allowedHeader("Content-Type"));
        
		JsonObject config = new JsonObject().put("keyStore", new JsonObject()
			    .put("path", "keystores/jwt-keystore.jceks")
			    .put("type", "jceks")
			    .put("password", "secret"));

		JWTAuth provider = JWTAuth.create(vertx, config);

		/*router.route(AuthRoutes.REGISTER_USER).handler(BodyHandler.create());
		router.post(AuthRoutes.REGISTER_USER).handler(rctx -> {
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
		
		router.post(AuthRoutes.VALIDATE_TOKEN).handler(rctx -> {
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
		});*/
		
		vertx.eventBus().consumer(AuthTopics.VALIDATE_TOKEN, message -> {
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
						.requestHandler(router::accept).listen(config().getInteger("http.port", 9004), result -> {
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
