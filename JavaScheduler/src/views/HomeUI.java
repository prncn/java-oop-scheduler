package views;

import controllers.DatabaseAPI;
import controllers.EmailHandler;
import controllers.FormatUtil;
import controllers.PDFDocument;
import models.User;
import views.components.Button;
import views.components.Label;
import views.panels.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main view of application. The Home panel contains
 * all sub panels and application systems.
 */
public class HomeUI extends MasterUI {
  private static final long serialVersionUID = -771654490802003766L;
  private JPanel sidebar = new JPanel();
  private User user;
  private static JPanel currentPanel;
  private static JFrame frame;
  private Point tabsBox;

  public static Dashboard dashPanel;
  public static ScheduleEvent createPanel;
  public static CalendarPanel calendarPanel;
  private ProfilePanel profilePanel;
  private Button exportTab;
  private Button logoutTab;
  private Button prevBtn;
  public static Label sidebarAvatar;
  public static Label footerTime;

  private static Button dashboardTab;
  public static Button createTab;
  public static Button calendarTab;
  public static Button profileTab;
  private static Button adminTab;

  public HomeUI(User user) {
    frame = this;
    this.user = user;
    frame.setTitle("Meetings Scheduler");
    setSize(1200, 700);
    remove(panel);

    tabsBox = new Point(0, 260);
    dashPanel = new Dashboard(frame, user);
    createPanel = new ScheduleEvent(frame, user, null, ScheduleModes.CREATE);
    calendarPanel = new CalendarPanel(frame, 95, false, user);
    profilePanel = new ProfilePanel(frame, user);
    currentPanel = dashPanel;
    add(dashPanel);

    styleSidebar();
    showAdminPanel();
    createSidebarTabs();
    initLogoutTab();
    initExportTab();

    setComponentStyles(sidebar, "dark");
    setComponentStyles(panel, "light");

    add(sidebar);
    setLocationRelativeTo(null);

    EmailHandler.reminderMail(user);

    setVisible(true);
    createClock();
    runClock();
  }

  /**
   * Create tab buttons on sidebar. Initialises the buttons and appends icons to
   * them.
   */
  private void createSidebarTabs() {
    dashboardTab = new Button(tabsBox.x, tabsBox.y, "Dashboard", dashPanel);
    createTab = new Button(tabsBox.x, tabsBox.y + 50, "Schedule", createPanel);
    calendarTab = new Button(tabsBox.x, tabsBox.y + 100, "Calendar", calendarPanel);
    profileTab = new Button(tabsBox.x, tabsBox.y + 150, "Profile", profilePanel);
    exportTab = new Button(tabsBox.x, tabsBox.y + 300, "Export Schedule", primaryColAlt);
    dashboardTab.setIcon(FormatUtil.resizeImageIcon(dashboardIcon, 0.7f));
    createTab.setIcon(FormatUtil.resizeImageIcon(createMeetingIcon, 0.7f));
    calendarTab.setIcon(FormatUtil.resizeImageIcon(calendarIcon, 0.7f));
    exportTab.setIcon(FormatUtil.resizeImageIcon(exportIcon, 0.7f));
    profileTab.setIcon(FormatUtil.resizeImageIcon(profileIcon, 0.7f));
    exportTab.setTab();

    List<Button> tabs = new ArrayList<>(
        Arrays.asList(dashboardTab, createTab, calendarTab, profileTab, exportTab, adminTab));
    tabs.forEach(e -> {
      if (e != adminTab)
        sidebar.add(e);
    });

    /**
     * Highlight active tab by color
     */
    Color active = MasterUI.primaryColAlt;
    Color inactive = MasterUI.primaryColAlt.darker();
    dashboardTab.setColor(active);
    prevBtn = dashboardTab;
    tabs.forEach(tab -> {
      tab.setPrevColor(inactive);
      tab.addActionListener(e -> {
        prevBtn.setColor(prevBtn.getPrevColor());
        tab.setColor(active);
        prevBtn = tab;
      });
    });
  }

  /**
   * Create and initialise logout tab. Logging out prompts the user for
   * confirmation and then directs them to login page.
   */
  private void initLogoutTab() {
    logoutTab = new Button(0, exportTab.getY() + 50, "Logout", primaryColAlt);
    logoutTab.setIcon(FormatUtil.resizeImageIcon(logoutIcon, 0.7f));
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

  private void initExportTab() {
    ActionListener exportAction = new ActionListener() {
      public void actionPerformed(ActionEvent actionEvent) {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        String dest;
        if (chooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
          dest = chooser.getSelectedFile().toPath().toString();
          if (isValidPath(dest)) {
            CalendarPanelWeekly cpw = calendarPanel.createPDFWeekly();
            LocalDate date = cpw.getDate();
            dest += "\\weekly_schedule_" + date.with(DayOfWeek.MONDAY).toString() + ".pdf";
            File file = PDFDocument.create(user, CalendarPanel.getWeekly(), dest, date);
            try {
              Desktop.getDesktop().open(file);
            } catch (IOException exp) {
              exp.printStackTrace();
            }
            System.out.println("Weekly report saved to " + dest);
          } else
            System.out.println("Path is invalid");
        }
      }
    };

    exportTab.addActionListener(confirmDialogAction(exportAction, "Export week selected on calendar?"));
  }

  /**
   * Checks if the used path is valid.
   *
   * @param path the path that is to be checked
   * @return true if path exists, false if not
   */
  public static boolean isValidPath(String path) {
    try {
      Paths.get(path);
    } catch (InvalidPathException | NullPointerException ex) {
      return false;
    }
    return true;
  }

  
  /** 
   * @param prompt
   */
  public static void confirmDialog(String prompt) {
    confirmDialog(null, null, prompt);
  }

  /**
   * Overload confirmDialog, for prompts that have no fail action. A fail action
   * is the action to be triggered if the user does no confirm.c
   * 
   * @param action
   * @param prompt
   * @see #confirmDialog(ActionListener, ActionListener, String)
   */
  public static void confirmDialog(ActionListener action, String prompt) {
    confirmDialog(action, null, prompt);
  }

  /**
   * Open a dialog prompt asking the user to confirm their action. This window
   * blocks action on the background until a selection (or exit) is given. On
   * selecting yes, an action listener will be triggered.
   * 
   * @param action - ActionListener object to be passed to "YES" button
   * @param prompt - String prompt the user is asked
   */
  public static void confirmDialog(ActionListener action, ActionListener failAction, String prompt) {
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
    if (action != null) {
      logoutp.add(no);
    } else {
      yes.setLocation(85, 60);
      yes.setText("OK");
    }
    logoutp.add(yes);

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
    if (failAction != null)
      no.addActionListener(failAction);
    yes.addActionListener(closeDialog);
    no.addActionListener(closeDialog);
  }

  /**
   * Bind {@link #confirmDialog(ActionListener, String)} to action listener. This
   * design to wrap and bind methods into action listeners is later replaced with
   * lambda expressions, as they functionally make these wrappers obsolete. I. e.
   * <code>addActionListener(e -> confirmDialog())</code> could have replaced this
   * wrapper.
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
   * Set time and date for sidebar, updating itself every Minute. This creates the
   * initial static clock component.
   */
  private void createClock() {
    DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm:ss");
    footerTime = new Label(90, 5, LocalTime.now().format(timeformat));

    footerTime.setForeground(Color.white);
    footerTime.setSize(100, 25);
    footerTime.setFont(MasterUI.robotoFont);
    footerTime.setHorizontalAlignment(SwingConstants.RIGHT);
    sidebar.add(footerTime);
  }

  /**
   * Create a UI thread timer to periodically update the clock every few
   * milliseconds. This prevents the application from stopping execution while the
   * timer thread is running.
   * 
   * @see javax.swing.Timer
   */
  public void runClock() {
    DateTimeFormatter timeformat = DateTimeFormatter.ofPattern("HH:mm:ss");

    Timer timer = new Timer(100, e -> {
      footerTime.setText(LocalTime.now().format(timeformat));
    });
    timer.start();
  }

  /**
   * Set styles for sidebar. Place header with user info and basic styles and
   * sizes for left sidebar.
   */
  private void styleSidebar() {
    sidebarAvatar = new Label(10, 30, "");
    sidebarAvatar.fillIcon(FormatUtil.resizeImageIcon(user.getAvatar(), 0.7f));

    Label headerinfoUser = new Label(10, 140, user.getFirstname() + " " + user.getLastname());
    Label headerinfoEmail = new Label(headerinfoUser.getX(), headerinfoUser.getY() + 20, user.getUsername());
    headerinfoUser.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 16f));
    headerinfoUser.setForeground(Color.WHITE);
    headerinfoUser.setUnset(true);
    headerinfoEmail.setSize(70, 24);

    sidebar.setBackground(primaryColAlt.darker());
    sidebar.setBounds(0, 0, 200, this.getHeight());
    sidebar.setLayout(null);
    sidebar.add(headerinfoUser);
    sidebar.add(headerinfoEmail);
    sidebar.add(sidebarAvatar);
  }

  /**
   * Switch current active panel to another. This is removes the currently viewed
   * panel and adds a new one.
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
   * Bind {@link #switchPanel(JPanel)} to action listener. @see
   * {@link #confirmDialog(ActionListener, String)} function. This could as well
   * just been <code>addActionListener(e -> switchPanel())</code>.
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
   * Make admin panel visible when current user is admin. By making the admin
   * panel not show up to regular users, this method functionally controls
   * permissions for user "roles".
   */
  private void showAdminPanel() {
    AdminPanel adminPanel = new AdminPanel(frame, user);
    adminTab = new Button(tabsBox.x, tabsBox.y - 50, "Admin Panel", adminPanel);
    adminTab.setIcon(FormatUtil.resizeImageIcon(adminIcon, 0.7f));
    adminTab.setTab();
    if (user.getUsername().equals("admin")) {
      sidebar.add(adminTab);
    }
  }

  /**
   * dispose the HomeUI
   */
  public static void disposeFrame() {
    frame.dispose();
  }

  
  /** 
   * @param args
   */
  public static void main(String[] args) {
    User guest = DatabaseAPI.getUser("admin");
    new HomeUI(guest);
  }

}
