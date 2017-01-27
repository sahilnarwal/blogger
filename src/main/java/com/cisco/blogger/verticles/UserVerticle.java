package com.cisco.blogger.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class UserVerticle extends AbstractVerticle {

	
	@Override
	public void start() throws Exception {
		
		Router router = SharedRouter.router;
		
		userRegistration(router);
		userLogin(router);
	}

	private void userLogin(Router router) {
		router.route("/api/user/login").handler(BodyHandler.create());
		router.post("/api/user/login").handler(rctx -> {
			vertx.eventBus().send("com.cisco.blogger.user.fetch", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void userRegistration(Router router) {
		router.route("/api/user").handler(BodyHandler.create());
		router.post("/api/user").handler(rctx -> {
			String name = rctx.request().getParam("name");
			String pwd = rctx.request().getParam("pwd");
			vertx.eventBus().send("com.cisco.blogger.user.add", rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}
}
