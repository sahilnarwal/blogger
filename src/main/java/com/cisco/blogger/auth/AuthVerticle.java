package com.cisco.blogger.auth;

import com.cisco.blogger.SharedRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class AuthVerticle extends AbstractVerticle{
	
	@Override
	public void start() throws Exception {
		System.out.println("Starting Auth Verticle");
		Router router = SharedRouter.router;
		
		router.route(AuthRoutes.REGISTER).handler(BodyHandler.create());
		router.route(AuthRoutes.REGISTER).handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.ADD_USER_CRED, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end("User Added");
			});
		});
		
		
		vertx.eventBus().consumer(AuthTopics.AUTHORIZE, message -> {
			System.out.println("Doing Authorize = ");
		});
		
		vertx.eventBus().consumer(AuthTopics.NEW_TOKEN, message -> {
			System.out.println("Issuing new token");
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Auth Verticle");
		super.stop();
	}
	
}
