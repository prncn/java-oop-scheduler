package views;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;

import views.components.Button;
import views.components.Label;
import views.components.Panel;
import models.Meeting;
import models.UserAccount;

public class HomeUI extends MasterUI {

  private static final long serialVersionUID = -771654490802003766L;
  private JPanel sidebar = new JPanel();
  private UserAccount user;
  private static JPanel currentPanel = panel;
  private static JFrame frame;
  private Point tabsBox;

  private CreateMeetingPanel createPanel;
  private static CalendarPanel calendarPanel;
  private ProfilePanel profilePanel;
  private Button exportTab;
  private Button logoutTab;
  private Button prevBtn;

  private static Panel dashpanel;
  public static Label meetingName;
  public static Button dashboardTab;
  public static Button createTab;
  public static Button calendarTab;
  public static Button profileTab;

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
    calendarPanel = new CalendarPanel(frame, 95, false, user);
    profilePanel = new ProfilePanel(frame);

    styleSidebar();
    showAdminPanel();
    createSidebarTabs();
    initLogoutTab();

    this.setComponentStyles(sidebar, "dark");
    this.setComponentStyles(panel, "light");

    createDashboardTab(user);

    this.add(sidebar);
    this.setLocationRelativeTo(null);
  }

  public static void updateCalendar() {
    LocalDate today = LocalDate.now();
    calendarPanel.initCalendarLayout(today);
    // this.repaint();
  }

  /**
   * Create and initialise (default) dashboard panel
   */
  private void createDashboardTab(UserAccount user) {
    userWelcome.setText("Upcoming Events");
    userWelcome.setBounds(40, 40, 10, 10);
    userWelcome.setHeading();

    dashpanel = new Panel();
    dashpanel.setBounds(40, 80, panel.getWidth() - 100, panel.getHeight() - 160);
    dashpanel.setBackground(lightCol);

    updateDashboad(user);
    panel.add(userWelcome);
    panel.add(dashpanel);
  }

  /**
   * Update meetings data on dashboard panel when the user's meetings change
   * 
   * @param user - Currently logged in user
   */
  public static void updateDashboad(UserAccount user) {
    dashpanel.removeAll();

    int initialY = 80;
    ArrayList<Meeting> meetings = user.getMeetings();
    if (meetings.isEmpty()) {
      Label emptyDash = new Label(0, initialY, "No meetings currently planned. :(");
      emptyDash.setHeading();
      emptyDash.setSize(800, 40);
      emptyDash.setForeground(Color.LIGHT_GRAY);
      dashpanel.add(emptyDash);
      return;
    }
    Collections.sort(meetings);
    for (Meeting meeting : meetings) {
      Label name = new Label(0, initialY, meeting.getEvent().getName());
      Label locate = new Label(180, initialY, meeting.getEvent().getLocation());
      Label date = new Label(450, initialY, meeting.getEvent().getDate().toString());
      name.setSize(800, 30);
      name.setFont(name.getFont().deriveFont(Font.BOLD));
      dashpanel.add(name);
      dashpanel.add(locate);
      dashpanel.add(date);
      initialY += 30;
    }
  }

  /**
   * Create tab buttons on sidebar
   */
  private void createSidebarTabs() {
    dashboardTab = new Button(tabsBox.x, tabsBox.y, "Dashboard", panel);
    createTab = new Button(tabsBox.x, tabsBox.y + 50, "Create Meeting", createPanel);
    calendarTab = new Button(tabsBox.x, tabsBox.y + 100, "View Calendar", calendarPanel);
    profileTab = new Button(tabsBox.x, tabsBox.y + 150, "Profile", profilePanel);
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

    ActionListener logoutAction = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        dispose();
        panel.removeAll();
        new LoginUI();
      }
    };

    logoutTab.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmDialog(logoutAction, "Are you sure you want to logout?");
      }
    });
  }

  /**
   * Open a dialog prompt asking the user to confirm their action
   * 
   * @param action - ActionListener object to be passed to "YES" button
   * @param prompt - String prompt the user is asked
   */
  public static void confirmDialog(ActionListener action, String prompt) {
    JDialog confirmDialog = new JDialog(frame, "Confirm action");
    frame.setEnabled(false);
    Label logoutlabel = new Label(20, 20, prompt);
    Button yes = new Button(30, 60, "Yes", secondaryCol);
    Button no = new Button(140, 60, "No", lightColAlt);
    no.setDark(false);

    JPanel logoutp = new JPanel();
    logoutp.setLayout(null);
    logoutp.setBackground(MasterUI.lightCol);
    logoutp.add(logoutlabel);
    logoutp.add(yes);
    logoutp.add(no);

    confirmDialog.add(logoutp);
    confirmDialog.setSize(300, 160);
    confirmDialog.setResizable(false);
    confirmDialog.setVisible(true);
    confirmDialog.setLocationRelativeTo(null);

    ((MasterUI) frame).setComponentStyles(logoutp, "light");

    ActionListener closeDialog = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        frame.setEnabled(true);
        confirmDialog.dispose();
      }
    };
    yes.addActionListener(action);
    yes.addActionListener(closeDialog);
    no.addActionListener(closeDialog);
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
