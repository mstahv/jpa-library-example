package org.example;

import com.vaadin.cdi.CDIView;
import com.vaadin.cdi.UIScoped;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import org.example.backend.LibraryFacade;
import org.example.domain.Book;
import org.vaadin.cdiviewmenu.ViewMenuItem;
import org.vaadin.viritin.button.MButton;
import org.vaadin.viritin.fields.MTable;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.fields.MValueChangeEvent;
import org.vaadin.viritin.label.Header;
import org.vaadin.viritin.layouts.MHorizontalLayout;
import org.vaadin.viritin.layouts.MVerticalLayout;

@UIScoped
@CDIView("")
@ViewMenuItem(order = ViewMenuItem.BEGINNING, icon = FontAwesome.USER)
public class BookView extends CssLayout implements View {
    
    @Inject
    LibraryFacade ef;

    @Inject // With Vaadin CDI one can also inject basic ui components
    BookForm form;

    // Instantiate and configure a Table to list PhoneBookEntries
    MTable<Book> entryList = new MTable<>(Book.class)
            .withHeight("450px")
            .withFullWidth()
            .withProperties("name")
            .withColumnHeaders("Name");

    // Instanticate buttons, hook directly to listener methods in this class
    Button addNew = new MButton(FontAwesome.PLUS, this::addNew);
    TextField filter = new MTextField().withInputPrompt("filter...");

    private void addNew(Button.ClickEvent e) {
        entryList.setValue(null);
        editEntry(new Book());
    }

    private void deleteEntry(Book b) {
        ef.delete(b);
        // TODO
        listEntries();
        entryList.setValue(null);
    }

    private void listEntries(String filter) {
        entryList.setBeans(ef.findBooks(filter));
    }

    private void listEntries() {
        listEntries(filter.getValue());
    }

    public void entryEditCanceled(Book entry) {
        editEntry(entryList.getValue());
    }

    public void entrySelected(MValueChangeEvent<Book> event) {
        editEntry(event.getValue());
    }

    /**
     * Assigns the given entry to form for editing.
     *
     * @param entry
     */
    private void editEntry(Book entry) {
        if (entry == null) {
            form.setVisible(false);
        } else {
            boolean persisted = entry.getId() != null;
            form.setDeleteHandler(persisted ? this::deleteEntry : null);
            form.setEntity(entry);
            form.focusFirst();
        }
    }

    public void entrySaved(Book value) {
        try {
            ef.save(value);
            form.setVisible(false);
        } catch (Exception e) {
            // Most likely optimistic locking exception
            Notification.show("Saving entity failed!", e.
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

        addComponents(
                new MVerticalLayout(
                        new Header("Company library").setHeaderLevel(3),
                        new MHorizontalLayout(filter, addNew),
                        new MHorizontalLayout(entryList, form)
                )
        );

        // List all entries and select first entry in the list
        listEntries();
        entryList.setValue(entryList.firstItemId());
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent event) {

    }

}
