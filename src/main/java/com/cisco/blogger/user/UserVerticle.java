package com.cisco.blogger.user;

import com.cisco.blogger.SharedRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class UserVerticle extends AbstractVerticle {

	
	@Override
	public void start() throws Exception {
		System.out.println("Strating User Verticle");
		Router router = SharedRouter.router;
		// Add User UserRoutes
		registerUserRegistrationRoute(router);
		
		registerUserUpdateRoute(router);
	}
	
	private void registerUserUpdateRoute(Router router) {
		router.route(UserRoutes.USER).handler(BodyHandler.create());
		router.put(UserRoutes.USER).handler(rctx -> {
			vertx.eventBus().send(UserTopics.UPDATE_USER, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	
	}

	private void registerUserRegistrationRoute(Router router) {
		router.route(UserRoutes.USER).handler(BodyHandler.create());
		router.post(UserRoutes.USER).handler(rctx -> {
			//String name = rctx.request().getParam("name");
			//String pwd = rctx.request().getParam("pwd");
			vertx.eventBus().send(UserTopics.ADD_USER, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}
	
	@Override
	public void stop() throws Exception {
		System.out.println("Stopping User Verticle");
		super.stop();
	}

}
