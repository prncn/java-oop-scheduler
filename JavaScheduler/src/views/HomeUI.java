package views;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controllers.FormatUtil;

import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import models.Event;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.panels.AdminPanel;
import views.panels.CalendarPanel;
import views.panels.ProfilePanel;
import views.panels.ScheduleEvent;
import models.User;

public class HomeUI extends MasterUI {
  private static final long serialVersionUID = -771654490802003766L;
  private JPanel sidebar = new JPanel();
  private User user;
  private static JPanel currentPanel = panel;
  private static JFrame frame;
  private Point tabsBox;

  private ScheduleEvent createPanel;
  public static CalendarPanel calendarPanel;
  private ProfilePanel profilePanel;
  private Button exportTab;
  private Button logoutTab;
  private Button prevBtn;

  private static Button dashboardTab;
  private static Panel dashpanel;
  public static Label eventData;
  public static Button createTab;
  public static Button calendarTab;
  public static Button profileTab;

  public HomeUI(User user) {
    frame = this;
    this.user = user;
    this.add(panel);
    frame.setTitle("Meetings Scheduler");
    this.setSize(1200, 700);
    panel.setBounds(200, 0, frame.getWidth() - 200, frame.getHeight());
    panel.removeAll();
    panel.setBackground(lightCol);

    tabsBox = new Point(0, 200);
    createPanel = new ScheduleEvent(frame, user);
    calendarPanel = new CalendarPanel(frame, 95, false, user);
    profilePanel = new ProfilePanel(frame, user);

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

  /**
   * Create and initialise (default) dashboard panel
   */
  private void createDashboardTab(User user) {
    Label screenTitle = new Label(40, 40, "Upcoming events");
    screenTitle.setHeading();

    dashpanel = new Panel();
    dashpanel.setBackground(lightCol);
    dashpanel.setBounds(40, 80, panel.getWidth() - 100, panel.getHeight() - 160);

    Label dashImage = new Label(600, 400, "");
    dashImage.setSize(339, 242);
    dashImage.setIcon(MasterUI.dashImage);
    panel.add(dashImage);

    Label notifLabel = new Label(650, 40, "<html>Events you've been added to:<html>");
    notifLabel.setSize(500, 40);
    notifLabel.setFont(monoFont);
    notifLabel.appendIcon(bellIcon);

    Label emptyNotif = new Label(683, 80, "You're all caught up!");
    emptyNotif.setFont(monoFont);
    emptyNotif.setForeground(Color.LIGHT_GRAY);

    panel.add(emptyNotif);
    panel.add(notifLabel);

    drawEventData(user);
    panel.add(screenTitle);
    panel.add(dashpanel);
  }

  /**
   * Draw event data on dashboard
   * 
   * @param user - Currently logged in user
   */
  public static void drawEventData(User user) {
    dashpanel.removeAll();
    Point p = new Point(0, 10);
    if (user.getAcceptedEvents().isEmpty()) {
      eventData = new Label(p.x, 40, "<html><p>No events planned :( <br>Schedule a new event on the tab on the left</p><html>");
      eventData.setHeading();
      eventData.setSize(700, 120);
      eventData.setForeground(Color.LIGHT_GRAY);
      dashpanel.add(eventData);

      return;
    }

    List<Event> events = user.getAcceptedEvents();
    Collections.sort(events);
    events.removeIf(e -> e.hasPassed()); // filter passed events

    for (int i = 0; i < events.size(); i++) {
      Event event = events.get(i);
      if (i > 8) break; // slice list after 8 entries

      Panel card = new Panel();
      int mgn = 15; // margin in pixels
      Point c = new Point(mgn, 10);
      card.setRounded(true);
      card.setBounds(p.x, p.y, 300, 100);
      card.setBackground(lightColAlt);

      Label name = new Label(c.x, c.y, "<html><strong>" + event.getName() + "<strong><html>");
      Label location = new Label(c.x, c.y + 30, event.getLocation().getName());
      Label date = new Label(c.x, c.y + 55, FormatUtil.readableDate(event.getDate()));
      Label time = new Label(c.x + 60, c.y + 55, event.getTime().toString());

      Label prio = new Label(card.getWidth() - 34, 10, "");
      prio.setSize(24, 24);
      prio.setIcon(event.getPriority().getIcon());

      card.add(prio);
      card.add(name);
      card.add(location);
      card.add(date);
      card.add(time);
      dashpanel.add(card);
      p.y += card.getHeight() + mgn;

      if (i == 3) p.setLocation(310, mgn);
    }
  }

  /**
   * Create tab buttons on sidebar
   */
  private void createSidebarTabs() {
    dashboardTab = new Button(tabsBox.x, tabsBox.y, "Dashboard", panel);
    createTab = new Button(tabsBox.x, tabsBox.y + 50, "Schedule Event", createPanel);
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
    User guest = new User("admin", "root", "admin@mail.com");
    HomeUI homeFrame = new HomeUI(guest);
    homeFrame.setVisible(true);
  }

}
