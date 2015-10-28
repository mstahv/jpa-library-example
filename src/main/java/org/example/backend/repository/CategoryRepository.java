
package org.example.backend.repository;

import org.apache.deltaspike.data.api.EntityRepository;
import org.apache.deltaspike.data.api.Repository;
import org.example.domain.Category;

/**
 *
 * @author Matti Tahvonen
 */
@Repository
public interface CategoryRepository extends EntityRepository<Category, Long> {

}
