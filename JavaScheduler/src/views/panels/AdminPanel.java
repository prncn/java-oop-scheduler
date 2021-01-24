package views.panels;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

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

  public AdminPanel(JFrame frame) {
    super(frame);

    Label adminTitle = new Label(40, 40, "Admin Panel");
    adminTitle.setHeading();

    Panel searchBack = new Panel();
    searchBack.setBounds(450, 0, getWidth() - 450, getHeight());
    searchBack.setBackground(MasterUI.accentCol);

    Label searchTitle = new Label(50, 40, "<html><p>Search for a user and access their profile</p><html>");
    searchTitle.setVerticalTextPosition(SwingConstants.TOP);
    searchTitle.setHeading();
    searchTitle.setSize(450, 75);

    TextField searchField = new TextField(50, 150);
    Label userQueryResult = new Label(searchField.getX(), searchField.getY() + 60, "");
    Panel panel = this;
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
    searchBack.add(searchField);
    searchBack.add(searchTitle);
    add(adminTitle);
    add(searchBack);

    MasterUI.setComponentStyles(searchBack, "light");
    MasterUI.setComponentStyles(this, "light");
  }
}