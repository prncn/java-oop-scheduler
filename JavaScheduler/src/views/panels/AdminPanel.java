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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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
    Button searchBtn = new Button(searchField.getX() + 300, searchField.getY(), "");
    Label userQueryResult = new Label(searchField.getX(), searchField.getY() + 60, "");
    Panel panel = this;
    searchBtn.setSize(searchBtn.getHeight(), searchBtn.getHeight());
    searchBtn.setIcon(MasterUI.searchIcon);
    searchBtn.setColor(MasterUI.lightColAlt);

    searchBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        User user = ViewModelHandler.searchUser(searchField, searchBack, userQueryResult);
        if(user != null){
          if(profileInfo != null){
            panel.remove(profileInfo);
          }
          profileInfo = new ProfilePanelInfo(user);
          profileInfo.setEdit();
          panel.add(profileInfo);
          panel.repaint();
        }
      }
    });

    MasterUI.setComponentStyles(this, "light");
    
    searchBack.add(userQueryResult);
    searchBack.add(searchBtn);
    searchBack.add(searchField);
    searchBack.add(searchTitle);
    add(adminTitle);
    add(searchBack);
  }
}