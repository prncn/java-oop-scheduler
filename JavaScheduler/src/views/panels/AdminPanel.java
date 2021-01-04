package views.panels;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

import controllers.ControlHandler;
import models.User;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

public class AdminPanel extends Panel {

  private static final long serialVersionUID = 1L;
  private Panel profilePanel;
  private User user;

  public AdminPanel(JFrame frame) {
    super(frame);

    Label screenTitle = new Label(40, 40, "Admin Panel");
    screenTitle.setHeading();

    Label searchTitle = new Label(500, 40, "Search User");
    searchTitle.setHeading();

    TextField searchField = new TextField(500, 100);
    Button searchBtn = new Button(805, 100, "Search", MasterUI.secondaryCol);

    Panel panel = this;
    Label userQueryResult = new Label(0, 0, "");
    searchBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        user = ControlHandler.searchUser(searchField, panel, userQueryResult);
        if(user != null){
          if(profilePanel != null){
            panel.remove(profilePanel);
          }
          ProfilePanel.createEditButton(panel, user);
          profilePanel = ProfilePanel.createMainPanel(user, true, panel);
          panel.add(profilePanel);
        }
      }
    });
    
    this.add(userQueryResult);
    this.add(searchBtn);
    this.add(searchField);
    this.add(searchTitle);
    this.add(screenTitle);

    ((MasterUI) frame).setComponentStyles(this, "light");
  }
}
