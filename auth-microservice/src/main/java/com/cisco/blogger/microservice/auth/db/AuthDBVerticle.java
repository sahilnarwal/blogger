package com.cisco.blogger.microservice.auth.db;

import java.util.HashMap;
import java.util.Map;

import com.cisco.blogger.microservice.auth.AuthTopics;
import com.cisco.blogger.microservice.auth.SharedRouter;
import com.cisco.blogger.microservice.auth.model.User;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuth;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CookieHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;

public class AuthDBVerticle extends AbstractVerticle{
	
	
	@Override
	public void start() throws Exception {
		System.out.println("Strating Auth DB Verticle");
		
		// We need cookies, sessions and request bodies
		
		Map<String, Object> clientConfig = new HashMap<>();
		clientConfig.put("db_name", "credentials");
		//clientConfig.put("connection_string", "mongodb://localhost:27017");
		clientConfig.put("host", "auth-microservice-db");
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

		// Add Topic Listeners
		vertx.eventBus().consumer(AuthTopics.AUNTHENTICATE, message -> {
			User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("Doing Authentication = "+ user);
			authProvider.authenticate(new JsonObject().put("username", user.getUsername()).put("password", user.getPassword()), res -> {
				if(res.succeeded()){
					io.vertx.ext.auth.User validatedUser = res.result();
					System.out.println("Authentication succedded with result ="+validatedUser);
					message.reply(true);
				}else {
					System.out.println("Authentication failed");
					message.reply(false);
				}
			});
		});

		// Add Topic Listeners
		vertx.eventBus().consumer(AuthTopics.ADD_USER_CRED, message -> {
			User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("Adding new user="+ user);
			authProvider.insertUser(user.getUsername(), user.getPassword(), user.getRoles(), user.getPermissions(), res -> {
				if(res.succeeded()){
					System.out.println("User added to DB"+res.result());
				} else {
					System.out.println("User not added to DB"+res.result());
				}
			});
			message.reply(true);
		});

		// Add Topic Listeners
		vertx.eventBus().consumer(AuthTopics.UPDATE_USER_CRED, message -> {
			System.out.println("Updating user creds"+ Json.decodeValue(message.body().toString(), User.class));
			message.reply(true);
		});

	}

	@Override
	public void stop() throws Exception {
		System.out.println("Stopping Auth DB Verticle");
	}
	
}