
package org.example.backend.repository;

import java.util.List;
import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.example.domain.Book;
import org.example.domain.Category;

/**
 *
 * @author Matti Tahvonen
 */
@Repository
public interface BookRepository extends EntityRepository<Book, Long> {
    
    public List<Book> findByNameLikeIgnoreCase(String filter);

    public List<Book> findByCategory(Category value);

}
