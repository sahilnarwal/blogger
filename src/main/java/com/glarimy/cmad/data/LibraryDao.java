package com.glarimy.cmad.data;

import com.glarimy.cmad.api.Book;

public interface LibraryDao {
	public void create(Book book);
	public Book read(int pk);
}
