package com.glarimy.cmad.api;

public interface Library {
	public void add(Book book);

	public Book find(int isbn);
}
