package org.example.domain;

import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Book extends AbstractEntity {

    @NotNull(message = "Name is required")
    @Size(min = 3, max = 40, message = "name must be longer than 3 and less than 40 characters")
    private String name;

    @Temporal(javax.persistence.TemporalType.DATE)
    private Date publishDate;

    @ManyToOne
    private Category category;

    public Book(String name) {
        this.name = name;
    }

    public Book() {
        this("");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return name;
    }

}
