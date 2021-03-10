package views.panels;

import controllers.DatabaseAPI;
import controllers.ViewModelHandler;
import models.Event;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin panel. In this view, (admin) users can search up a user 
 * of the database and change their profile attributes.
 */
public class AdminPanel extends Panel {

  /** Generated serial ID */
  private static final long serialVersionUID = 542069743291702880L;

  /** Profile info panel object displaying user information */
  private ProfilePanelInfo profileInfo;

  /** Scroll pane of suggestions field */
  private JScrollPane sgscroll;

  /** Background panel of search field */
  private Panel searchBack;

  /** Search field */
  private TextField searchField;

  /** Currently logged user of this session */
  private User currentUser;

  /** User found in search field */
  private User searchedUser;

  /** Deletion button */
  private Button deleteBtn;

  /** Confirmation label */
  private Label confirm;

  /** ActionListener object to close suggestions */
  private ActionListener closeSuggest;

  private List<User> users;

  public AdminPanel(JFrame frame, User currentUser) {
    super(frame);
    this.currentUser = currentUser;
    Panel panel = this;

    Label adminTitle = new Label(40, 40, "Admin Panel");
    adminTitle.setHeading();
    confirm = new Label(40,70, "");

    searchBack = new Panel();
    searchBack.setBounds(450, 0, getWidth() - 450, getHeight());
    searchBack.setBackground(MasterUI.accentCol);
    initSearchField();

    closeSuggest = e -> {
      searchBack.remove(sgscroll);
      sgscroll = null;
      searchAction(panel);
    };

    Label userQueryResult = new Label(searchField.getX(), searchField.getY() + 60, "");
    Button searchBtn = searchField.appendButton(MasterUI.searchIcon);
    searchBtn.setColor(MasterUI.lightColAlt);
    // searchBtn.addActionListener(closeSuggest);
    searchBtn.addActionListener(e -> {
      searchedUser = ViewModelHandler.searchUser(searchField, searchBack, userQueryResult);
      searchBack.remove(sgscroll);
      if ((searchedUser != null) && !(searchedUser.equals(currentUser))) {
        if (profileInfo != null) {
          panel.remove(profileInfo);
          panel.remove(confirm);
        }
        profileInfo = new ProfilePanelInfo(searchedUser, true);
        initDeleteBtn();
        panel.add(profileInfo);
        panel.repaint();
      }
    });

    searchBack.add(userQueryResult);
    searchBack.add(searchBtn);
    searchBack.setComponentZOrder(searchBtn, 0);
    add(adminTitle);

    MasterUI.setComponentStyles(this, "light");
  }

  private void searchAction(Panel panel) {
    searchedUser = ViewModelHandler.searchUser(searchField, searchBack, null);
      if ((searchedUser != null) && !(searchedUser.equals(currentUser))) {
        if (profileInfo != null) {
          panel.remove(profileInfo);
          panel.remove(confirm);
        }
        profileInfo = new ProfilePanelInfo(searchedUser, true);
        initDeleteBtn();
        panel.add(profileInfo);
        panel.repaint();
      }
  }

  /**
   * Initalise search field on admin panel.
   * The search field allows so look through the lists of
   * application users.
   */
  private void initSearchField() {
    Label searchTitle = new Label(50, 40, "<html><p>Search for a user and access their profile</p><html>");
    searchTitle.setVerticalTextPosition(SwingConstants.TOP);
    searchTitle.setHeading();
    searchTitle.setSize(450, 75);
    users = DatabaseAPI.getAllUsers();

    searchField = new TextField(50, 150);
    searchField.getDocument().addDocumentListener(new DocumentListener() {
      public void changedUpdate(DocumentEvent e) {
        updateSuggest();
      }
      public void removeUpdate(DocumentEvent e) {
        updateSuggest();
      }
      public void insertUpdate(DocumentEvent e) {
        updateSuggest();
      }
    });
    
    searchBack.add(searchField);
    searchBack.add(searchTitle);
    add(searchBack);
    MasterUI.setComponentStyles(searchBack, "light");
    
    searchField.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent f) {
        if (sgscroll == null) {
          updateSuggest();
        }
      }
      public void focusLost(FocusEvent f) {
        // searchBack.remove(sgscroll);
        // sgscroll = null;
      }
    });
  }
  

  /**
   * Method to trigger list of suggestions under user 
   * search field
   */
  private void updateSuggest() {
    if (DatabaseAPI.getUser(searchField.getText()) != null){
      return;
    }
    List<User> entries = new ArrayList<>(users);
    List<User> suggestions = new ArrayList<>(entries);
    if (sgscroll != null)
    searchBack.remove(sgscroll);
    suggestions.removeIf(e -> (!e.getUsername().toUpperCase().startsWith(searchField.getText().toUpperCase())));
    suggestions.removeIf(e -> e.getUsername().equals(currentUser.getUsername()));
    sgscroll = null;
    Component[] _comps = searchField.setDropdown(suggestions, sgscroll, searchBack, closeSuggest, suggestions.size());
    sgscroll = (JScrollPane) _comps[0];
    searchField.requestFocus();
  }


  /**
   * Initialise and create delete button to remove
   * any user from the application database
   */
  private void initDeleteBtn() {
    deleteBtn = new Button(40, 500, "Delete User", MasterUI.primaryCol);
    deleteBtn.setSize(310, 50);
    deleteBtn.setCornerRadius(Button.ROUND);
    deleteBtn.setForeground(MasterUI.lightCol);
    deleteBtn.addActionListener(confirm -> HomeUI.confirmDialog(deleteUser(searchedUser), "Delete User?"));
    add(deleteBtn);
  }

  /**
   * Deletes the user and all of his events
   * @param user user to be deleted
   * @return ActionListener object
   */
  private ActionListener deleteUser(User user) {
    return new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {

        /**
         * first get all events from user,
         * then deletes all user's hosted events (from list in events and in table Event),
         * then remove the user in all events where user is not the host (from the list in events)
         */
        ArrayList<Event> allEvents = DatabaseAPI.getEventsFromUser(user.getId());
        for (Event event : allEvents) {
          if(user.getId() == event.getHostId()) {
            user.deleteEvent(event);
          } else {
            //ArrayList<User> participants = event.getParticipants();
            DatabaseAPI.deleteUserEventBridge(user.getId(),event.getId());
            //event.updateParticipantList();
            event.removeParticipant(user);
          }
        }
        /**
         * delete user in database
         */
        DatabaseAPI.deleteUser(user.getId());

        remove(deleteBtn);
        remove(profileInfo);
        confirm.setText("User " + user.getUsername() + " successfully deleted");
        add(confirm);
        repaint();
        return;
      }
    };
  }
}