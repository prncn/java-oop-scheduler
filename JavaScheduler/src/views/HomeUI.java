package views;

import javax.swing.AbstractButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Component;
import java.awt.Point;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalTime;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



import views.components.Button;
import views.components.Label;
import views.panels.AdminPanel;
import views.panels.CalendarPanel;
import views.panels.Dashboard;
import views.panels.ProfilePanel;
import views.panels.ScheduleEvent;
import models.User;



public class HomeUI extends MasterUI {
  private static final long serialVersionUID = -771654490802003766L;
  private JPanel sidebar = new JPanel();
  private User user;
  private static JPanel currentPanel;
  private static JFrame frame;
  private Point tabsBox;

  public static Dashboard dashPanel;
  private ScheduleEvent createPanel;
  public static CalendarPanel calendarPanel;
  private ProfilePanel profilePanel;
  private Button exportTab;
  private Button logoutTab;
  private Button prevBtn;

  private static Button dashboardTab;
  public static Button createTab;
  public static Button calendarTab;
  public static Button profileTab;

  public HomeUI(User user) {
    frame = this;
    this.user = user;
    frame.setTitle("Meetings Scheduler");
    this.setSize(1200, 700);
    this.remove(panel);

    tabsBox = new Point(0, 200);
    dashPanel = new Dashboard(frame, user);
    createPanel = new ScheduleEvent(frame, user, null);
    calendarPanel = new CalendarPanel(frame, 95, false, user);
    profilePanel = new ProfilePanel(frame, user);
    currentPanel = dashPanel;
    this.add(dashPanel);

    styleSidebar();
    showAdminPanel();
    createSidebarTabs();
    initLogoutTab();

    setComponentStyles(sidebar, "dark");
    setComponentStyles(panel, "light");

    add(sidebar);
    setVisible(true);

    setLocationRelativeTo(null);
    createTime();
  }

  /**
   * Create tab buttons on sidebar. Initialises the buttons 
   * and appends icons to them.
   */
  private void createSidebarTabs() {
    dashboardTab = new Button(tabsBox.x, tabsBox.y, "Dashboard", dashPanel);
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
   * Create and initialise logout tab. Logging out prompts the user for
   * confirmation and then directs them to login page.
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

    logoutTab.addActionListener(confirmDialogAction(logoutAction, "Are you sure you want to logout?"));
  }

  /**
   * Open a dialog prompt asking the user to confirm their action. This window
   * blocks action on the background until a selection (or exit) is given. On selecting
   * yes, an action listener will be triggered.
   * 
   * @param action - ActionListener object to be passed to "YES" button
   * @param prompt - String prompt the user is asked
   */
  public static void confirmDialog(ActionListener action, String prompt) {
    JDialog confirmDialog = new JDialog(frame, "Confirm action");
    frame.setEnabled(false);

    /**
     * Prevent app frame from staying disabled when user closes dialog
     */
    confirmDialog.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        frame.setEnabled(true);
      }
    });

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

    setComponentStyles(logoutp, "light");

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
   * Bind {@link #confirmDialog(ActionListener, String)} to action listener.
   * This design to wrap and bind methods into action listeners is later replaced with
   * lambda expressions, as they functionally make these wrappers obsolete.
   * I. e. <code>addActionListener(e -> confirmDialog())</code> could have replaced this wrapper.
   * 
   * @param action - Action to triggered on mouse event.
   * @param prompt - String prompt to be displayed.
   * @return ActionListener wrapping into single action
   */
  public static ActionListener confirmDialogAction(ActionListener action, String prompt) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        confirmDialog(action, prompt);
      }
    };
  }

  /**
   * Set styles for sidebar. Place header with user info and basic
   * styles and sizes for left sidebar.
   */
  private void styleSidebar() {
    Label headerinfoUser = new Label(20, 30, "Logged as " + user.getUsername());
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
   * Set time and date for sidebar, updating itself every Minute
   */
  private void createTime() {
    Calendar calendar;
    SimpleDateFormat timeFormat;
    SimpleDateFormat dateFormat;
    String time;
    String date;

    timeFormat = new SimpleDateFormat("hh:mm:ss"); // Zeit in 24h-layout und evtl sekunden raus
    dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    Label timelabel = new Label(115, 615, "");
    Label datelabel = new Label(100, 638, "");

    sidebar.add(timelabel);
    sidebar.add(datelabel);

    setComponentStyles(sidebar, "dark");

    while (true) {// while true ist eine endlos schleife die verhindert, dass die app startet
      time = timeFormat.format(Calendar.getInstance().getTime());
      timelabel.setText(time);
      date = dateFormat.format(Calendar.getInstance().getTime());
      datelabel.setText(date);

      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }

    }
  }

      /**
         * Switch current active panel to another. This is removes the currently
         * viewed panel and adds a new one.
         *
         * @param newPanel - Selected panel to be switched to
       * */
  public static void switchPanel(JPanel newPanel) {
    frame.remove(currentPanel);
    currentPanel = newPanel;
    frame.add(currentPanel);
    frame.repaint();
  }

  /**
   * Bind {@link #switchPanel(JPanel)} to action listener. @see {@link #confirmDialog(ActionListener, String)}
   * function. This could as well just been <code>addActionListener(e -> switchPanel())</code>.
   * 
   * @param newPanel - Selected panel to be switched to
   * @return ActionListener object triggering function
   * @see #switchPanel(JPanel)
   */
  public static ActionListener switchPanelAction(JPanel newPanel) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        switchPanel(newPanel);
      }
    };
  }

  /**
   * Make admin panel visible when current user is admin. By making 
   * the admin panel not show up to regular users, this method functionally
   * controls permissions for user "roles".
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
  }

}
