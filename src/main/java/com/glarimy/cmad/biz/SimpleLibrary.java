package com.glarimy.cmad.biz;

import com.glarimy.cmad.api.Book;
import com.glarimy.cmad.api.Library;
import com.glarimy.cmad.data.LibraryDao;
import com.glarimy.cmad.data.LibraryJPADao;

public class SimpleLibrary implements Library {
	static LibraryDao dao = new LibraryJPADao();

	@Override
	public void add(Book book) {
		if (book == null || book.getIsbn() < 0)
			throw new RuntimeException("invalid book");
		dao.create(book);
	}

	@Override
	public Book find(int isbn) {
		Book book = dao.read(isbn);
		if (book == null)
			throw new RuntimeException("Not Found");
		return book;
	}

}
