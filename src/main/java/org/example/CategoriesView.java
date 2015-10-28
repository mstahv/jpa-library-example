package org.example;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.example.backend.LibraryFacade;
import org.example.domain.Book;
import org.example.domain.Category;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScoped
@CDIView("categories")
public class CategoriesView extends MVerticalLayout implements View {

    @Inject // With Vaadin CDI one can also inject basic ui components
    CategoryForm form;

    @Inject
    LibraryFacade ef;

    // Instantiate and configure a Table to list PhoneBookEntries
    MTable<Category> entryList = new MTable<>(Category.class)
            .withFullHeight()
            .withFullWidth()
            .withProperties("name");

    MTable<Book> booksInSelectedCategory = new MTable<>(Book.class)
            .withCaption("Books in category:")
            .withProperties("name", "publishDate");

    // Instanticate buttons, hook directly to listener methods in this class
    Button addNew = new MButton(FontAwesome.PLUS, this::addNew);
    TextField filter = new MTextField().withInputPrompt("filter...");

    private void addNew(Button.ClickEvent e) {
        entryList.setValue(null);
        editEntry(new Category());
    }

    private void delete(Category e) {
        ef.delete(e);
        listEntries();
        entryList.setValue(null);
    }

    private void listEntries(String filter) {
        entryList.setBeans(ef.findCategories(filter));
    }

    private void listEntries() {
        listEntries(filter.getValue());
    }

    public void entryEditCanceled(Category entry) {
        editEntry(entryList.getValue());
    }

    public void entrySelected(MValueChangeEvent<Category> event) {
        editEntry(event.getValue());
        booksInSelectedCategory.setBeans(ef.
                findBooksByCategory(event.getValue()));
    }

    /**
     * Assigns the given entry to form for editing.
     *
     * @param entry
     */
    private void editEntry(final Category entry) {
        if (entry == null) {
            form.setVisible(false);
        } else {
            boolean persisted = entry.getId() != null;
            form.setDeleteHandler(persisted ? e -> delete(entry) : null);
            form.setEntity(entry);
            form.focusFirst();
        }
    }

    public void entrySaved(Category value) {
        try {
            ef.save(value);
            form.setVisible(false);
        } catch (Exception e) {
            Notification.show("Saving entity failed due to " + e.
                    getLocalizedMessage(), Notification.Type.WARNING_MESSAGE);
        }
        // deselect the entity
        entryList.setValue(null);
        // refresh list
        listEntries();
    }

    @PostConstruct
    void init() {
        // Add some event listners, e.g. to hook filter input to actually 
        // filter the displayed entries
        filter.addTextChangeListener(e -> {
            listEntries(e.getText());
        });
        entryList.addMValueChangeListener(this::entrySelected);
        form.setSavedHandler(this::entrySaved);
        form.setResetHandler(this::entryEditCanceled);

        add(
                new Header("Categories").setHeaderLevel(3),
                new MHorizontalLayout(filter, addNew));
        expand(
                new MHorizontalLayout(entryList, form).withFullHeight(),
                booksInSelectedCategory.withFullHeight()
        );
        withFullHeight();

        // List all entries and select first entry in the list
        listEntries();
        entryList.setValue(entryList.firstItemId());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
