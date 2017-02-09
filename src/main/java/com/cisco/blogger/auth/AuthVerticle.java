package com.cisco.blogger.auth;

import java.util.ArrayList;

import com.cisco.blogger.SharedRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class AuthVerticle extends AbstractVerticle{
	
	@Override
	public void start() throws Exception {
		System.out.println("Starting Auth Verticle");
		Router router = SharedRouter.router;
		
		router.route().handler(CookieHandler.create());
        router.route().handler(BodyHandler.create());
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        
		JsonObject config = new JsonObject().put("keyStore", new JsonObject()
			    .put("path", "keystores/jwt-keystore.jceks")
			    .put("type", "jceks")
			    .put("password", "secret"));

		JWTAuth provider = JWTAuth.create(vertx, config);
		
		// protect the API
        // Any requests to URI starting '/api/' require login
        router.route("/api/*").handler(RedirectAuthHandler.create(provider, "/auth.html"));
        
        router.route("/logout").handler(ctx -> {
            //ctx.clearUser();
            //How to invalidate the Token
            // Redirect back to the index page
            ctx.response().putHeader("location", "/").setStatusCode(302).end();
        });
		
		router.route(AuthRoutes.REGISTER).handler(BodyHandler.create());
		router.route(AuthRoutes.REGISTER).handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.ADD_USER_CRED, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end("User Added");
			});
		});
		
		router.route(AuthRoutes.TEST_GET_TOKEN).handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.NEW_TOKEN, "", r -> {
				rctx.response().setStatusCode(200).end("issued new tokem"+r.result().body().toString());
			});
		});
		
		router.route(AuthRoutes.TEST_TOKEN_VALIDATION).handler(rctx -> {
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
		
		router.route(AuthRoutes.TEST_USER_AUTHENTICATION).handler(BodyHandler.create());
		router.route(AuthRoutes.TEST_USER_AUTHENTICATION).handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.AUNTHENTICATE, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end("User Authenticated");
			});
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Auth Verticle");
		super.stop();
	}
	
}
