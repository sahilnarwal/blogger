package com.cisco.blogger.microservice.user;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class SharedRouter {
	
	public static final Router router = Router.router(Vertx.vertx());

}
