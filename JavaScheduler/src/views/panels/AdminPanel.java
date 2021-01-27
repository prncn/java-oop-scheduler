package views.panels;

import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentListener;
import javax.swing.event.DocumentEvent;

import controllers.DatabaseAPI;
import controllers.ViewModelHandler;
import models.User;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

public class AdminPanel extends Panel {

  private static final long serialVersionUID = 542069743291702880L;
  private ProfilePanelInfo profileInfo;
  private JScrollPane sgscroll;
  private Panel searchBack;
  private TextField searchField;

  public AdminPanel(JFrame frame) {
    super(frame);
    Panel panel = this;

    Label adminTitle = new Label(40, 40, "Admin Panel");
    adminTitle.setHeading();

    searchBack = new Panel();
    searchBack.setBounds(450, 0, getWidth() - 450, getHeight());
    searchBack.setBackground(MasterUI.accentCol);
    initSearchField();

    Label userQueryResult = new Label(searchField.getX(), searchField.getY() + 60, "");
    Button searchBtn = searchField.appendButton(MasterUI.searchIcon);
    searchBtn.setColor(MasterUI.lightColAlt);
    searchBtn.addActionListener(e -> {
      User user = ViewModelHandler.searchUser(searchField, searchBack, userQueryResult);
      if (user != null) {
        if (profileInfo != null) {
          panel.remove(profileInfo);
        }
        profileInfo = new ProfilePanelInfo(user, true);
        panel.add(profileInfo);
        panel.repaint();
      }
    });

    searchBack.add(searchBtn);
    searchBack.add(userQueryResult);
    add(adminTitle);
    
    MasterUI.setComponentStyles(this, "light");
  }
  
  private void initSearchField() {
    Label searchTitle = new Label(50, 40, "<html><p>Search for a user and access their profile</p><html>");
    searchTitle.setVerticalTextPosition(SwingConstants.TOP);
    searchTitle.setHeading();
    searchTitle.setSize(450, 75);
    
    ActionListener action = e -> {};
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

      public void updateSuggest() {
        if (!searchField.getText().isBlank()) {
          if (sgscroll != null) searchBack.remove(sgscroll);
          List<User> entries = DatabaseAPI.getAllUsers();
          entries.removeIf(e -> !e.getUsername().startsWith(searchField.getText()));
          sgscroll = null;
          Component[] _comps = searchField.setDropdown(entries, sgscroll, searchBack, action, entries.size());
          sgscroll = (JScrollPane) _comps[0];
        }
        searchField.requestFocus();
      }
    });
    
    searchBack.add(searchField);
    searchBack.add(searchTitle);
    add(searchBack);
    MasterUI.setComponentStyles(searchBack, "light");
  }
}