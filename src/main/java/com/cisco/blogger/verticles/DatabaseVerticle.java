package com.cisco.blogger.verticles;

import com.cisco.blogger.model.Blog;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;

public class DatabaseVerticle extends AbstractVerticle{
	@Override
	public void start() throws Exception {
		vertx.eventBus().consumer("com.cisco.blog.save", message -> {
			Blog blog = Json.decodeValue(message.body().toString(), Blog.class);
			System.out.println(blog);
			message.reply(true);
		});
	}

}
