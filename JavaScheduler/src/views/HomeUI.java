package views;

import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Point;

import views.components.Button;
import views.components.Label;
import views.components.Panel;
import models.UserAccount;

public class HomeUI extends MasterUI {
  private static final long serialVersionUID = -771654490802003766L;
  private JPanel sidebar = new JPanel();
  private Panel createPanel;
  private Panel calendarPanel;
  private Panel exportPanel;
  private Panel settingsPanel;
  private static JPanel currentPanel = panel;
  private static JFrame frame;

  public HomeUI(UserAccount user) {
    frame = this;
    this.add(panel);
    frame.setTitle("Meetings Scheduler");
    this.setSize(1200, 700);
    panel.setBounds(200, 0, frame.getWidth() - 200, frame.getHeight());
    panel.removeAll();
    panel.setBackground(MasterUI.getColor("lightCol"));
    userWelcome.setText("Upcoming Events");
    Point tbsBox = new Point(0, 200);

    Label headerinfoUser = new Label(20, 30, "Hi, " + user.getUsername());
    Label headerinfoEmail = new Label(20, 55, user.getEmail());

    createPanel = new createMeetingPanel(frame);
    calendarPanel = new calendarPanel(frame);
    exportPanel = new exportPanel(frame);
    settingsPanel = new settingsPanel(frame);

    Button dashboardTab = new Button(tbsBox.x, tbsBox.y, "Dashboard", panel);
    Button createTab = new Button(tbsBox.x, tbsBox.y + 50, "Create Meeting", createPanel);
    Button calendarTab = new Button(tbsBox.x, tbsBox.y + 100, "View Calendar", calendarPanel);
    Button exportTab = new Button(tbsBox.x, tbsBox.y + 150, "Export Schedule", exportPanel);
    Button settingsTab = new Button(tbsBox.x, tbsBox.y + 250, "Settings", settingsPanel);
    Button logoutTab = new Button(tbsBox.x, tbsBox.y + 300, "Logout", primaryColAlt);
    dashboardTab.setIcon(dashboardIcon);
    createTab.setIcon(createMeetingIcon);
    calendarTab.setIcon(calendarIcon);
    exportTab.setIcon(exportIcon);
    settingsTab.setIcon(settingsIcon);
    logoutTab.setIcon(logoutIcon);
    logoutTab.setTab();

    logoutTab.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        dispose();
        panel.removeAll();
        LoginUI login = new LoginUI();
        login.setVisible(true);
      }
    });

    sidebar.setBackground(primaryColAlt);
    sidebar.setBounds(0, 0, 200, this.getHeight());
    sidebar.setLayout(null);
    sidebar.add(headerinfoUser);
    sidebar.add(headerinfoEmail);
    sidebar.add(dashboardTab);
    sidebar.add(createTab);
    sidebar.add(calendarTab);
    sidebar.add(exportTab);
    sidebar.add(settingsTab);
    sidebar.add(logoutTab);

    panel.add(userWelcome);
    this.setComponentStyles(sidebar, "dark");
    this.setComponentStyles(panel, "light");

    userWelcome.setBounds(40, 40, 10, 10);
    userWelcome.setHeading();
    // userWelcome.setForeground(MasterUI.getColor("accentCol"));
    headerinfoEmail.setFont(monoFont);
    headerinfoUser.setFont(monoFont);

    this.add(sidebar);
    this.setLocationRelativeTo(null);
  }

  public static void switchPanel(JPanel newPanel) {
    frame.remove(currentPanel);
    currentPanel = newPanel;
    frame.add(currentPanel);
    frame.repaint();
  }
  
  public static void main(String[] args) {
    UserAccount guest = new UserAccount("quaintguest", "guestPassword", "guest@mail.com");
    HomeUI homeFrame = new HomeUI(guest);
    homeFrame.setVisible(true);
  }
  
}
