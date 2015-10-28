package org.example;


import com.vaadin.ui.Component;
import com.vaadin.ui.TextField;
import org.example.domain.Category;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MFormLayout;
import org.vaadin.viritin.layouts.MMarginInfo;
import org.vaadin.viritin.layouts.MVerticalLayout;

public class CategoryForm extends AbstractForm<Category> {

    TextField name = new MTextField("Name");

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new MFormLayout(
                        name
                ).withMargin(false),
                getToolbar()
        ).withMargin(new MMarginInfo(false, true));
    }

}
