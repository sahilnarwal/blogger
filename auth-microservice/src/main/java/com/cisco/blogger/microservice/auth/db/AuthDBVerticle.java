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
		
		Router router = SharedRouter.router;
		
		// We need cookies, sessions and request bodies
	    router.route().handler(CookieHandler.create());
	    router.route().handler(BodyHandler.create());
	    router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
		
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

		
		// We need a user session handler too to make sure the user is stored in the session between requests
	   // router.route().handler(UserSessionHandler.create(authProvider));
	    
	    // Any requests to URI starting '/private/' require login
	    //router.route("/private/*").handler(RedirectAuthHandler.create(authProvider, "/static/loginpage.html"));
	    
	 // Serve the static private pages from directory 'private'
	    router.route("/private/*").handler(StaticHandler.create().setCachingEnabled(false).setWebRoot("private"));
	    
	    
	 // Handles the actual login
	    //router.route("/loginhandler").handler(FormLoginHandler.create(authProvider));
	    
	 // Implement logout
	    router.route("/logout").handler(context -> {
	      context.clearUser();
	      // Redirect back to the index page
	      context.response().putHeader("location", "/").setStatusCode(302).end();
	    });
		
		router.route("/loginhandler").handler(BodyHandler.create());
		router.post("/loginhandler").handler(rctx -> {
			//String cred = rctx.request().getHeader("Authorization");
			//System.out.println("Authentication headers"+cred);
			vertx.eventBus().send(AuthTopics.AUNTHENTICATE, rctx.getBodyAsJson(), r -> {
				rctx.response().setStatusCode(200).end("User Authenticated");
			});
		});
		// Add Topic Listeners
		vertx.eventBus().consumer(AuthTopics.AUNTHENTICATE, message -> {
			User user = Json.decodeValue(message.body().toString(), User.class);
			System.out.println("Doing Authentication = "+ user);
			authProvider.authenticate(new JsonObject().put("username", user.getUsername()).put("password", user.getPassword()), res -> {
				if(res.succeeded()){
					io.vertx.ext.auth.User validatedUser = res.result();
					System.out.println("Authentication succedded with result ="+validatedUser);
				}else {
					System.out.println("Authentication failed");
				}
			});
			message.reply(true);
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