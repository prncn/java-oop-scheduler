package views;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;

import models.Meeting;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import models.UserAccount;

public class HomeUI extends MasterUI {

  private static final long serialVersionUID = -771654490802003766L;
  private JPanel sidebar = new JPanel();
  private UserAccount user;
  private static JPanel currentPanel = panel;
  private static JFrame frame;
  private Point tabsBox;

  private CreateMeetingPanel createPanel;
  private CalendarPanel calendarPanel;
  private ProfilePanel profilePanel;
  private Button exportTab;
  private Button logoutTab;
  private Button prevBtn;


  private static Button dashboardTab;
  private static Panel dashpanel;
  public static Label meetingName;
  public static Button createTab;

  public HomeUI(UserAccount user) {
    frame = this;
    this.user = user;
    this.add(panel);
    frame.setTitle("Meetings Scheduler");
    this.setSize(1200, 700);
    panel.setBounds(200, 0, frame.getWidth() - 200, frame.getHeight());
    panel.removeAll();
    panel.setBackground(lightCol);

    tabsBox = new Point(0, 200);
    createPanel = new CreateMeetingPanel(frame, user);
    calendarPanel = new CalendarPanel(frame, 95, false);
    profilePanel = new ProfilePanel(frame);

    styleSidebar();
    showAdminPanel();
    createSidebarTabs();
    initLogoutTab();

    this.setComponentStyles(sidebar, "dark");
    this.setComponentStyles(panel, "light");

    createDashboardTab();

    this.add(sidebar);
    this.setLocationRelativeTo(null);
  }

  /**
   * Create and initialise (default) dashboard panel
   */
  private void createDashboardTab() {
    userWelcome.setText("Upcoming Events");
    userWelcome.setBounds(40, 40, 10, 10);
    userWelcome.setHeading();

    dashpanel = new Panel();
    dashpanel.setBounds(40, 80, panel.getWidth() - 100, panel.getHeight() - 160);
    dashpanel.setBackground(lightCol);

    updateDashboard(user);
    panel.add(userWelcome);
    panel.add(dashpanel);
  }

  public static void updateDashboard(UserAccount user) {
    dashpanel.removeAll();
    Point p = new Point(40,80);
    if(user.getMeetings().size() == 0) {
      meetingName = new Label(40, 80, "No meetings planned");
      meetingName.setHeading();
      meetingName.setSize(800, 40);
      meetingName.setForeground(Color.LIGHT_GRAY);
      dashpanel.add(meetingName);
    }
    for (Meeting m : user.getMeetings()) {
      meetingName = new Label(p.x, p.y , m.getEvent().getName());
      Label locate = new Label(p.x + 180 , p.y , m.getEvent().getLocation().getWhere());
      Label date = new Label(p.x + 400 , p.y , m.getEvent().getDate().toString());

      switch (m.getPriority()) {
        case HIGH: {
          Label prio = new Label(p.x + 620 , p.y , "HIGH");
          prio.setForeground(new Color(194, 21, 73));
          dashpanel.add(prio);
          break;
        }
        case MEDIUM: {
          Label prio = new Label(p.x + 620 , p.y , "MEDIUM");
          prio.setForeground(new Color(219, 218, 149));
          dashpanel.add(prio);
          break;
        }
        case LOW: {
          Label prio = new Label(p.x + 620 , p.y , "LOW");
          prio.setForeground(MasterUI.secondaryCol);
          dashpanel.add(prio);
          break;
        }
      }

      meetingName.setSize(800, 30);
      meetingName.setFont(meetingName.getFont().deriveFont(Font.BOLD));
      dashpanel.add(meetingName);
      dashpanel.add(locate);
      dashpanel.add(date);
      p.y += 30;
    }
  }

  /**
   * Create tab buttons on sidebar
   */
  private void createSidebarTabs() {
    dashboardTab = new Button(tabsBox.x, tabsBox.y, "Dashboard", panel);
    createTab = new Button(tabsBox.x, tabsBox.y + 50, "Create Meeting", createPanel);
    Button calendarTab = new Button(tabsBox.x, tabsBox.y + 100, "View Calendar", calendarPanel);
    Button profileTab = new Button(tabsBox.x, tabsBox.y + 150, "Profile", profilePanel);
    exportTab = new Button(tabsBox.x, tabsBox.y + 250, "Export Schedule", primaryColAlt);
    dashboardTab.setIcon(dashboardIcon);
    createTab.setIcon(createMeetingIcon);
    calendarTab.setIcon(calendarIcon);
    exportTab.setIcon(exportIcon);
    profileTab.setIcon(profileIcon);
    exportTab.setTab();

    sidebar.add(dashboardTab);
    sidebar.add(createTab);
    sidebar.add(calendarTab);
    sidebar.add(exportTab);
    sidebar.add(profileTab);

    /**
     * Highlight active tab by color
     */
    dashboardTab.setColor(MasterUI.secondaryCol);
    prevBtn = dashboardTab;
    for (Component c : sidebar.getComponents()) {
      if (c instanceof Button) {
        ((AbstractButton) c).addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            if (prevBtn != null) {
              prevBtn.setColor(MasterUI.primaryColAlt);
            }
            ((Button) c).setColor(MasterUI.secondaryCol);
            prevBtn = ((Button) c);
          }
        });
      }
    }
  }

  /**
   * Create and initialise logout tab
   */
  private void initLogoutTab() {
    logoutTab = new Button(tabsBox.x, tabsBox.y + 300, "Logout", primaryColAlt);
    logoutTab.setIcon(logoutIcon);
    logoutTab.setTab();
    sidebar.add(logoutTab);

    logoutTab.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        JDialog logout = new JDialog(frame, "Logout Verification");

        JLabel logoutlabel = new JLabel("Are you sure ?");
        logoutlabel.setFont(new Font("Consolas", Font.PLAIN, 15));
        logoutlabel.setForeground(fontCol);
        JButton yes = new Button(300, 200, "Yes");
        JButton no = new Button(300, 220, "No");

        JPanel logoutp = new JPanel();
        logoutp.add(logoutlabel);
        logoutp.add(yes);
        logoutp.add(no);
        logoutp.setBackground(primaryColAlt);
        logout.add(logoutp);
        logout.setSize(300, 80);
        logout.setVisible(true);
        logout.setLocation(800, 500);
        yes.setFont(monoFont);
        yes.setForeground(Color.WHITE);
        yes.setBackground(secondaryCol);
        yes.setFocusPainted(false);
        yes.setContentAreaFilled(true);
        // yes.setMargin(new Insets(5, 5, 3, 3));
        yes.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            dispose();
            panel.removeAll();
            LoginUI login = new LoginUI();
            login.setVisible(true);
          }
        });
        no.setFont(monoFont);
        no.setForeground(Color.WHITE);
        no.setBackground(primaryColAlt);
        no.setFocusPainted(false);
        no.setContentAreaFilled(true);
        // no.setMargin(new Insets(5, 5, 3, 3));
        no.addActionListener(new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent actionEvent) {
            logout.dispose();

          }
        });
      }
    });
  }

  /**
   * Set styles for sidebar
   */
  private void styleSidebar() {
    Label headerinfoUser = new Label(20, 30, "Hi, " + user.getUsername());
    Label headerinfoEmail = new Label(20, 55, user.getEmail());
    headerinfoEmail.setFont(monoFont);
    headerinfoUser.setFont(monoFont);

    sidebar.setBackground(primaryColAlt);
    sidebar.setBounds(0, 0, 200, this.getHeight());
    sidebar.setLayout(null);
    sidebar.add(headerinfoUser);
    sidebar.add(headerinfoEmail);
  }

  /**
   * Switch current active panel to another
   * 
   * @param newPanel - Selected panel to be switched to
   */
  public static void switchPanel(JPanel newPanel) {
    frame.remove(currentPanel);
    currentPanel = newPanel;
    frame.add(currentPanel);
    frame.repaint();
  }

  /**
   * Make admin panel visible when current user is admin
   */
  private void showAdminPanel() {
    if (user.getUsername() == "admin") {
      AdminPanel adminPanel = new AdminPanel(frame);
      Button adminTab = new Button(tabsBox.x, tabsBox.y - 50, "ADMIN_PANEL", adminPanel);
      adminTab.setIcon(adminIcon);
      adminTab.setColor(accentCol);
      adminTab.setTab();
      sidebar.add(adminTab);
    }
  }

  public static void main(String[] args) {
    UserAccount guest = new UserAccount("admin", "root", "admin@mail.com");
    HomeUI homeFrame = new HomeUI(guest);
    homeFrame.setVisible(true);
  }

}
