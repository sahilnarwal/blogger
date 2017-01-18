package com.glarimy.cmad.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.glarimy.cmad.api.Word;
import com.glarimy.cmad.biz.SimpleDictionary;

@Path("/dictionary")
public class DictionaryService {

	SimpleDictionary dictionary = new SimpleDictionary();
	
	@Path("/{word}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getWordDetails(@PathParam("word") String word, @QueryParam("detail") String detail){
		Response response ;
		switch (detail) {
		case "meaning":
			String meaning = dictionary.getMeaning(word);
			response = Response.ok().entity(meaning).build();
			break;
		case "synonym":
			String synonym = dictionary.getSynonym(word);
			response = Response.ok().entity(synonym).build();
			break;

		default:
			response = Response.status(Status.NOT_ACCEPTABLE).build();
			break;
		}
		
		return response;
	}
	
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addNewWord(Word word){
		dictionary.saveWord(word);
		return Response.ok().build();
	}
	
	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	public Response updateWord(Word word){
		dictionary.updateMeaning(word);
		dictionary.updateSynonym(word);
		return Response.ok().build();
	}
	
}
