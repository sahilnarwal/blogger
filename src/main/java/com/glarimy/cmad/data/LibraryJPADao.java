package com.glarimy.cmad.data;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import com.glarimy.cmad.api.Book;

public class LibraryJPADao implements LibraryDao {
	static EntityManagerFactory factory = Persistence.createEntityManagerFactory("lib");

	@Override
	public void create(Book book) {
		EntityManager em = factory.createEntityManager();
		em.getTransaction().begin();
		em.persist(book);
		em.getTransaction().commit();
		em.close();
	}

	@Override
	public Book read(int pk) {
		EntityManager em = factory.createEntityManager();
		Book book = em.find(Book.class, pk);
		em.close();
		return book;
	}

}
