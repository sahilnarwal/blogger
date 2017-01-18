package com.glarimy.cmad.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.glarimy.cmad.api.Book;
import com.glarimy.cmad.api.Library;
import com.glarimy.cmad.biz.SimpleLibrary;

@Path("/library")
public class LibraryController {
	Library library = new SimpleLibrary();

	@POST
	@Path("/book")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response insert(Book book) {
		library.add(book);
		return Response.ok().entity(book).build();
	}

	@GET
	@Path("/book/{isbn}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response find(@PathParam("isbn") int isbn) {
		Book book = library.find(isbn);
		return Response.ok().entity(book).build();
	}
}
