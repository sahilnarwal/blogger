package com.cisco.blogger.microservice.user;

import com.cisco.blogger.microservice.user.db.UserDBVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;

public class UserVerticle extends AbstractVerticle {

	
	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Strating User Verticle");
		Router router = SharedRouter.router;
		
		// Deploy Verticles
		vertx.deployVerticle(UserDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));

		router.route().handler(CorsHandler.create("*")
			      .allowedMethod(HttpMethod.GET)
			      .allowedMethod(HttpMethod.POST)
			      .allowedMethod(HttpMethod.PUT)
			      .allowedMethod(HttpMethod.DELETE)
			      .allowedMethod(HttpMethod.OPTIONS)
			      .allowedHeader("Authorization")
			      .allowedHeader("www-authenticate")
			      .allowedHeader("Content-Type"));
		
		// Add User UserRoutes
		registerUserDetailRoute(router);
		registerUserRegistrationRoute(router);
		registerUserLoginRoute(router);
		registerUserUpdateRoute(router);
		// Start server and listen
				vertx.createHttpServer(new HttpServerOptions().setSsl(true)
						.setKeyStoreOptions(new JksOptions().setPath("keystores/server.jks").setPassword("password")))
						.requestHandler(router::accept).listen(config().getInteger("http.port", 443), result -> {
							if (result.succeeded()) {
								startFuture.complete();
							} else {
								startFuture.fail(result.cause());
							}
						});
	}
	
	private void registerUserDetailRoute(Router router) {
		router.get(UserRoutes.USER_DETAIL).handler(rctx -> {
			vertx.eventBus().send(UserTopics.GET_USER, rctx.request().getParam("username"), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
		
	}

	private void registerUserLoginRoute(Router router) {
		router.route(UserRoutes.USER_LOGIN).handler(BodyHandler.create());
		System.out.println("Doing user login");
		router.post(UserRoutes.USER_LOGIN).handler(rctx -> {
			vertx.eventBus().send("com.cisco.blogger.auth.authenticate", rctx.getBodyAsJson(), r -> {
				System.out.println("Authenticate callback succedded val = "+r.succeeded());
				System.out.println("Authenticate return val"+r.result().body().toString());
				if(r.succeeded() && r.result().body().toString().equals("true")){
					vertx.eventBus().send("com.cisco.blogger.auth.newtoken", "", res-> {
						System.out.println("New token return val="+res.result().body().toString());
						rctx.response().setStatusCode(200).end(res.result().body().toString());
					});
				}else {
					rctx.response().setStatusCode(401).end();
				}
			});
		});
		
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
