package com.cisco.blogger.microservice.blog.model;

import com.cisco.blogger.microservice.blog.BlogTopics;

import io.vertx.core.AbstractVerticle;

public class ClusterTest extends AbstractVerticle{

	public static void main(String[] args) {
		
	}

	@Override
	public void start() throws Exception {
		getVertx().setPeriodic(1000L, id -> {
            getVertx().eventBus().send(BlogTopics.GET_BLOG_BY_TITLE, "myblog", response -> {
                if (response.succeeded()) {
                	System.out.println("Fetched result="+response.result().body());
                }
                else System.out.println("Error");;
            });
        });

	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		super.stop();
	}
	
	
	
}
