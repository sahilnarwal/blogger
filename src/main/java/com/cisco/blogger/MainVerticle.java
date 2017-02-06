package com.cisco.blogger;

import com.cisco.blogger.auth.AuthVerticle;
import com.cisco.blogger.auth.db.AuthDBVerticle;
import com.cisco.blogger.blog.BlogVerticle;
import com.cisco.blogger.blog.db.BlogDBVerticle;
import com.cisco.blogger.comments.CommentVerticle;
import com.cisco.blogger.comments.db.CommentDBVerticle;
import com.cisco.blogger.user.UserVerticle;
import com.cisco.blogger.user.db.UserDBVerticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.net.JksOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class MainVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Strating Main Verticle");
		
		Router router = SharedRouter.router;
		
		// Add Routes
		registerGeneralRoutes(router);
		
		// Deploy Verticles
		vertx.deployVerticle(BlogDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(UserDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(CommentDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(AuthDBVerticle.class.getName(), new DeploymentOptions().setWorker(true));
		vertx.deployVerticle(AuthVerticle.class.getName());
		vertx.deployVerticle(UserVerticle.class.getName());
		vertx.deployVerticle(BlogVerticle.class.getName());
		vertx.deployVerticle(CommentVerticle.class.getName());
		
		// Start server and listen
		vertx.createHttpServer(new HttpServerOptions().setSsl(true)
				.setKeyStoreOptions(new JksOptions().setPath("keystores/server.jks").setPassword("password")))
				.requestHandler(router::accept).listen(config().getInteger("http.port", 9000), result -> {
					if (result.succeeded()) {
						startFuture.complete();
					} else {
						startFuture.fail(result.cause());
					}
				});
	}

	private void registerGeneralRoutes(Router router) {
		router.route(Routes.ABOUT).handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		router.route(Routes.STATIC_CONTENT).handler(StaticHandler.create("web"));
		
		/*AuthHandler redirectAuthHandler = RedirectAuthHandler.create(authProvider);
		router.route(Routes.SECURE_CONTENT).handler(redirectAuthHandler);
		// Handle the actual login
		router.route("/login").handler(FormLoginHandler.create(authProvider));*/
		
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Main Verticle");
		super.stop();
	}
	
}
