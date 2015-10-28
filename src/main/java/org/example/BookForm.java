package org.example;

import org.example.domain.Book;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.TextField;
import javax.inject.Inject;
import org.example.backend.LibraryFacade;
import org.example.domain.Category;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.TypedSelect;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

/**
 * This class introduces a Form to edit Book pojos. It is a good habit to
 * separate logical pieces of your UI code to classes. This will improve
 * re-usability, readability, maintainability, testability.
 *
 * @author Matti Tahvonen <matti@vaadin.com>
 */
public class BookForm extends AbstractForm<Book> {

    TextField name = new MTextField("Name");
    DateField publishDate = new DateField("Publish date");
    TypedSelect<Category> category = new TypedSelect<Category>("Category")
            .setCaptionGenerator(e -> e.getName());

    @Inject
    LibraryFacade ef;

    @Override
    protected Component createContent() {
        category.setBeans(ef.findCategories());
        return new MVerticalLayout(
                new MFormLayout(
                        name,
                        publishDate,
                        category
                ).withMargin(false),
                getToolbar()
        ).withMargin(new MMarginInfo(false, true));
    }

}
