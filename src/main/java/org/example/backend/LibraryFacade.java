package org.example.backend;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.example.backend.repository.BookRepository;
import org.example.backend.repository.CategoryRepository;
import org.example.domain.Book;
import org.example.domain.Category;
import org.example.domain.Category_;
import org.example.domain.QBook;

/**
 * A facade via UI layer talks to backend.
 */
@Stateless
public class LibraryFacade {

    @Inject
    BookRepository br;
    @Inject
    CategoryRepository cr;

    @PersistenceContext(name = "bookpu")
    public EntityManager em;
    
    
    private JPAQuery<Book> bookRoot() {
        return new JPAQuery<Book>(em).from(QBook.book);
    }

    /**
     * A simple QueryDSL usage example.
     * 
     * @param filter an optional filter parameter
     * @param category an optional category parameter
     * @return 
     */
    public List<Book> findWithQueryDSL(String filter, Category category) {
        JPAQuery<Book> q = bookRoot();
        if(filter != null) {
            q = q.where(QBook.book.name.startsWithIgnoreCase(filter));
        }
        if(category != null) {
            q = q.where(QBook.book.category.eq(category));
        }
        return q.fetch();
    }
    
    /**
     * A QueryDSL example of providing API for Facade users where they can just 
     * pass the built predicate for "backend". This could be e.g. built
     * by a UI code implementing a complex search features.
     * 
     * @param p the predicate for the query
     * @return 
     */
    public List<Book> findWithQueryDSL(Predicate p) {
        return bookRoot().where(p).fetch();
    }

    public List<Book> findBooks(String filter) {
        if (filter != null && !filter.isEmpty()) {
            return br.findByNameLikeIgnoreCase(filter + "%");
        }
        return br.findAll();
    }

    public List<Book> findBooksByCategory(Category value) {
        return br.findByCategory(value);
    }

    public void delete(Book b) {
        // Simple remove won't work as the entity may be coming form UI layer
        // that uses detached entities
        br.attachAndRemove(b);
    }

    public void save(Book value) {
        br.save(value);
    }

    public List<Category> findCategories(String filter) {
        if (filter == null || filter.isEmpty()) {
            return cr.findAll();
        }
        Category category = new Category();
        category.setName(filter);
        return cr.findByLike(category, Category_.name);
    }

    public List<Category> findCategories() {
        return findCategories(null);
    }

    public void save(Category value) {
        cr.save(value);
    }

    public void delete(Category e) {
        cr.attachAndRemove(e);
    }
}
