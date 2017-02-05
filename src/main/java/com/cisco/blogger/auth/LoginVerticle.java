package com.cisco.blogger.auth;

import java.util.HashMap;
import java.util.Map;

import com.cisco.blogger.SharedRouter;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;

public class LoginVerticle extends AbstractVerticle{
	
	@Override
	public void start() throws Exception {
		Map<String, Object> clientConfig = new HashMap<>();
		clientConfig.put("db_name", "credentials");
		//clientConfig.put("connection_string", "mongodb://localhost:27017");
		clientConfig.put("host", "localhost");
		clientConfig.put("port", 27017);
		MongoClient client = MongoClient.createShared(vertx, new JsonObject(clientConfig));
		JsonObject authProperties = new JsonObject();
		MongoAuth authProvider = MongoAuth.create(client, authProperties);
		authProvider.setCollectionName("user");
		// mongo column names
		authProvider.setUsernameField("username");
		authProvider.setPasswordField("password");
		// user object field names
		authProvider.setUsernameCredentialField("username");
		authProvider.setPasswordCredentialField("password");
		authProvider.setPermissionField("permission");
		authProvider.setRoleField("roles");
		System.out.println("Starting Login Verticle");
		Router router = SharedRouter.router;
		
		router.route(AuthRoutes.LOGIN).handler(rctx -> {
			String cred = rctx.request().getHeader("Authorization");
			System.out.println("Authentication headers"+cred);
		});
	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Login Verticle");
		super.stop();
	}
	
}
