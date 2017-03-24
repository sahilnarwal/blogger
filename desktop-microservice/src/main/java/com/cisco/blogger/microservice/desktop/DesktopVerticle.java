package com.cisco.blogger.microservice.desktop;


import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class DesktopVerticle extends AbstractVerticle {

	@Override
	public void start(Future<Void> startFuture) throws Exception {
		System.out.println("Strating Desktop Verticle");
		
		Router router = SharedRouter.router;
		
		// Add Routes
		router.route(Routes.ABOUT).handler(rctx -> {
			HttpServerResponse response = rctx.response();
			response.putHeader("content-type", "text/html")
					.end("<h1>Hello from my first Vert.x 3 application via routers</h1>");
		});
		router.route(Routes.STATIC_CONTENT).handler(StaticHandler.create("webapp"));
		
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
		System.out.println("Stopping Desktop Verticle");
		super.stop();
	}
	
}
