package org.example;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.example.domain.Book;
import org.example.domain.Category;

/**
 *
 * @author Matti Tahvonen
 */
@Stateless
public class TestDataGenerator {

    @PersistenceContext(name = "bookpu")
    public EntityManager em;

    public void insertDemoData() {

        Long count = (Long) em.createQuery("select count(c) from Category c").
                getSingleResult();
        if (count == 0) {

            final Category nature = new Category("Nature");
            em.persist(nature);
            final Category sciencefiction = new Category("Science Fiction");
            em.persist(sciencefiction);
            final Category it = new Category("IT");
            em.persist(it);
            em.persist(new Category("Sport"));
            em.persist(new Category("Medical"));
            final Book bookOfVaadin = new Book("Book of Vaadin");
            bookOfVaadin.setCategory(it);
            em.persist(bookOfVaadin);
            Book book = new Book("Effective Java");
            book.setCategory(it);
            em.persist(book);
            book = new Book("Java Concurrency in Practice");
            book.setCategory(it);
            em.persist(book);
            book = new Book("Java Generics and Collections");
            book.setCategory(it);
            em.persist(book);
            book = new Book("Head First OO Analysis and Design");
            book.setCategory(it);
            em.persist(book);
            final Book moon = new Book("The dark side of the Moon");
            moon.setCategory(sciencefiction);
            em.persist(moon);
        }

    }
}
