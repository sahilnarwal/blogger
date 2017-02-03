package com.cisco.blogger.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class CommentVerticle extends AbstractVerticle{

	@Override
	public void start() throws Exception {
		System.out.println("Strating Comment Verticle");
		Router router = SharedRouter.router;

		// Add Comment Routes
		registerGetCommentRoute(router);
		
		registerAddCommentRoute(router);
	}

	private void registerAddCommentRoute(Router router) {
		router.route(Routes.COMMENT).handler(BodyHandler.create());
		router.post(Routes.COMMENT).handler(rctx -> {
			vertx.eventBus().send(Topics.ADD_COMMENT, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerGetCommentRoute(Router router) {
		router.get(Routes.SEARCH_COMMENT).handler(rctx -> {
			String title = rctx.request().getParam("blog");
			System.out.println("Blog title to search comments for="+title);
			vertx.eventBus().send(Topics.GET_COMMENT, title, r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Comment Verticle");
		super.stop();
	}
	
}
