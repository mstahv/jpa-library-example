
package org.example.backend.repository;

import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Matti Tahvonen
 */
public class CdiConfig {

    @Produces
    @Dependent
    @PersistenceContext(name = "bookpu")
    public EntityManager entityManager;
 
}