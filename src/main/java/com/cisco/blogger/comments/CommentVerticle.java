package com.cisco.blogger.comments;

import com.cisco.blogger.SharedRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class CommentVerticle extends AbstractVerticle{

	@Override
	public void start() throws Exception {
		System.out.println("Strating Comment Verticle");
		Router router = SharedRouter.router;

		// Add Comment CommentRoutes
		registerGetCommentRoute(router);
		
		registerAddCommentRoute(router);
	}

	private void registerAddCommentRoute(Router router) {
		router.route(CommentRoutes.COMMENT).handler(BodyHandler.create());
		router.post(CommentRoutes.COMMENT).handler(rctx -> {
			vertx.eventBus().send(CommentTopics.ADD_COMMENT, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end(r.result().body().toString());
			});
		});
	}

	private void registerGetCommentRoute(Router router) {
		router.get(CommentRoutes.COMMENT_FOR_BLOG).handler(rctx -> {
			String blogId = rctx.request().getParam("blogid");
			System.out.println("Blog title to search comments for="+blogId);
			vertx.eventBus().send(CommentTopics.GET_COMMENT, blogId, r -> {
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
