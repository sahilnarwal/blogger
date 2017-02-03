package com.cisco.blogger.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class UserVerticle extends AbstractVerticle {

	
	@Override
	public void start() throws Exception {
		System.out.println("Strating User Verticle");
		Router router = SharedRouter.router;
		// Add User Routes
		registerUserRegistrationRoute(router);
		
		registerUserLoginRoute(router);
		
		registerUserUpdateRoute(router);
	}
	
	private void registerUserUpdateRoute(Router router) {

		router.route(Routes.UPDATE_USER).handler(BodyHandler.create());
		router.post(Routes.UPDATE_USER).handler(rctx -> {
			vertx.eventBus().send(Topics.UPDATE_USER, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	
	}

	private void registerUserLoginRoute(Router router) {
		router.route(Routes.LOGIN).handler(BodyHandler.create());
		router.post(Routes.LOGIN).handler(rctx -> {
			vertx.eventBus().send(Topics.GET_USER, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerUserRegistrationRoute(Router router) {
		router.route(Routes.USER).handler(BodyHandler.create());
		router.post(Routes.USER).handler(rctx -> {
			String name = rctx.request().getParam("name");
			String pwd = rctx.request().getParam("pwd");
			vertx.eventBus().send(Topics.ADD_USER, rctx.getBodyAsJson(), r -> {
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
