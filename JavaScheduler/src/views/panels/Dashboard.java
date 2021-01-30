package views.panels;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import controllers.DatabaseAPI;
import controllers.FormatUtil;
import controllers.ViewModelHandler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import models.Event;
import models.Priority;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

public class Dashboard extends Panel implements CardModes {

  private enum SortModes{
    ALPHA, DATE_ADDED, DATE_EVENT, PARTICIPS;
  };

  private static final long serialVersionUID = 1L;
  private static Panel redpanel;
  private static Panel upSectionInner;
  private static Panel allSectionInner;
  private static JScrollPane scroller;
  private static Label eventData;
  private static SortModes SORT_MODE = SortModes.DATE_ADDED;
  private static String TITLE_KEY = "";
  private static String LOCATION_KEY = "";

  private static JFrame frame;
  private static User user;

  public Dashboard(JFrame frame, User user) {
    super(frame);
    Dashboard.frame = frame;
    Dashboard.user = user;
    redpanel = new Panel();
    redpanel.setPreferredSize(new Dimension(getWidth(), getHeight() * 2 - 150));
    redpanel.setBackground(MasterUI.lightCol);

    createDashboardTab(user);
    scroller = createScroller();

    Label scrollHintIcon = new Label();
    scrollHintIcon.setBounds(getWidth() / 2 - 50, 600, 24, 24);
    scrollHintIcon.setIcon(MasterUI.downIconDark);

    // add(scrollHintIcon);
    add(scroller);
  }

  /**
   * Create scroll pane and set properties
   * 
   * @return scroll pane object
   */
  private JScrollPane createScroller() {
    JScrollPane scroller = new JScrollPane(redpanel);
    scroller.setBounds(0, 0, getWidth(), getHeight());
    scroller.getVerticalScrollBar().setUnitIncrement(10);
    scroller.setBorder(BorderFactory.createEmptyBorder());

    return scroller;
  }

  /**
   * Create and initialise (default) dashboard panel
   */
  private void createDashboardTab(User user) {
    Label screenTitle = new Label(40, 40, "Upcoming Events");
    Label screenDate = new Label(10 + screenTitle.getX() + screenTitle.getWidth(), screenTitle.getY(), FormatUtil.readableDate(LocalDate.now()));
    screenTitle.setHeading();
    screenDate.setHeading();
    screenDate.setForeground(Color.LIGHT_GRAY);

    upSectionInner = new Panel();
    upSectionInner.setBackground(MasterUI.lightCol);
    upSectionInner.setBounds(40, 80, getWidth() - 380, getHeight() - 200);

    Label allEventsTitle = new Label(40, 600, "All Events");
    allEventsTitle.setHeading();
    allSectionInner = new Panel();
    allSectionInner.setBackground(MasterUI.lightCol);
    allSectionInner.setBounds(40, 650, upSectionInner.getWidth(), upSectionInner.getHeight());

    Label dashImage = new Label(600, 400, "");
    ImageIcon[] images = { MasterUI.dashImage1, MasterUI.dashImage1, MasterUI.dashImage2, MasterUI.dashImage3, MasterUI.dashImage4, MasterUI.dashImage5 };
    dashImage.setIcon(images[new Random().nextInt(6)]);
    dashImage.setSize(dashImage.getIcon().getIconWidth(), dashImage.getIcon().getIconHeight());
    redpanel.add(dashImage);

    Label notifLabel = new Label(650, 40, "<html>Events you've been added to:<html>");
    notifLabel.setSize(500, 40);
    notifLabel.setFont(MasterUI.monoFont);
    notifLabel.appendIcon(MasterUI.bellIcon);

    Label emptyNotif = new Label(683, 80, "You're all caught up!");
    emptyNotif.setFont(MasterUI.monoFont);
    emptyNotif.setForeground(Color.LIGHT_GRAY);

    redpanel.add(emptyNotif);
    redpanel.add(notifLabel);
    
    drawEventData(user);
    drawFilterSortSection();
    redpanel.add(screenTitle);
    redpanel.add(screenDate);
    redpanel.add(upSectionInner);
    redpanel.add(allEventsTitle);
    redpanel.add(allSectionInner);
  }
  
  /**
   * Draw section for filtering and sorting of events. The sort section
   * contains radio buttons to change the ordering of drawn event cards.
   * The filter panel gives the user two textfields to search and filter
   * event titles or locations by names.
   */
  private static void drawFilterSortSection() {
    Panel filterPanel = new Panel();
    filterPanel.setBounds(680, 700, 280, 200);
    filterPanel.setBackground(MasterUI.primaryCol);
    filterPanel.setRounded(true);
    
    Label filterLabel = new Label(20, 20, "Filter");
    filterLabel.setHeading();
    filterLabel.setForeground(Color.WHITE);
    
    TextField filterQuery_1 = new TextField(20, 60, "filter event names...");
    filterQuery_1.setBackground(MasterUI.primaryColAlt);
    filterQuery_1.setSize(200, 40);
    
    
    TextField filterQuery_2 = new TextField(20, 110, "filter locations...");
    filterQuery_2.setBackground(MasterUI.primaryColAlt);
    filterQuery_2.setSize(200, 40);
    
    Button fqBtn_1 = filterQuery_1.appendButton(MasterUI.searchIconLight);
    Button fqBtn_2 = filterQuery_2.appendButton(MasterUI.searchIconLight);

    fqBtn_1.addActionListener(e -> { TITLE_KEY = filterQuery_1.getText(); drawEventData(user); });
    fqBtn_2.addActionListener(e -> { LOCATION_KEY = filterQuery_2.getText(); drawEventData(user); });

    filterPanel.add(filterLabel);
    filterPanel.add(filterQuery_1);
    filterPanel.add(fqBtn_1);
    filterPanel.add(filterQuery_2);
    filterPanel.add(fqBtn_2);

    Panel sortPanel = new Panel();
    sortPanel.setBounds(680, 700 + filterPanel.getHeight() + 20, 280, 200);
    sortPanel.setBackground(MasterUI.primaryColAlt);
    sortPanel.setRounded(true);
    
    Label sortLabel = new Label(20, 20, "Sort");
    sortLabel.setHeading();
    sortLabel.setForeground(Color.WHITE);
    sortPanel.add(sortLabel);

    int BTN_MRGN = 25;
    Button sortOpt0 = Button.createRadioButton(20, 70, "Alphabetically", false, sortPanel);
    Button sortOpt1 = Button.createRadioButton(20, sortOpt0.getY() + BTN_MRGN, "Date added", true, sortPanel);
    Button sortOpt2 = Button.createRadioButton(20, sortOpt1.getY() + BTN_MRGN, "Date event", false, sortPanel);
    Button sortOpt3 = Button.createRadioButton(20, sortOpt2.getY() + BTN_MRGN, "Number participants", false, sortPanel);

    sortOpt0.addActionListener(e -> { SORT_MODE = SortModes.ALPHA; drawEventData(user); });
    sortOpt1.addActionListener(e -> { SORT_MODE = SortModes.DATE_ADDED; drawEventData(user); });
    sortOpt2.addActionListener(e -> { SORT_MODE = SortModes.DATE_EVENT; drawEventData(user); });
    sortOpt3.addActionListener(e -> { SORT_MODE = SortModes.PARTICIPS; drawEventData(user); });

    redpanel.add(filterPanel);
    redpanel.add(sortPanel);
    MasterUI.setComponentStyles(sortPanel, "dark");
    MasterUI.setComponentStyles(filterPanel, "dark");
  }

  /**
   * Draw event data on dashboard
   * 
   * @param user - Currently logged in user
   */
  public static void drawEventData(User user) {
    upSectionInner.removeAll();
    allSectionInner.removeAll();
    Point content = new Point(0, 10);
    if (user.getEvents().isEmpty()) {
      eventData = new Label(content.x, 40,
          "<html><p>No events :(<br>Schedule new events on the left</p><html>");
      eventData.setHeading();
      eventData.setSize(500, 120);
      eventData.setForeground(MasterUI.lightColAlt);
      upSectionInner.add(eventData);
      return;
    }
    for (Event event : user.getEvents()) {
      drawEventCard(new Point(683, 80), event, redpanel, NOTIF);
    }
    sectionUpcomingEventsCards(user, new Point(0, 10));
    sectionAllEventsCards(user, new Point(0, 10));
  }

  /**
   * Draw all cards onto upcoming events section. This will filter for up to 8
   * coming events.
   * 
   * @param user
   * @param content
   */
  private static void sectionUpcomingEventsCards(User user, Point content) {
    List<Event> upcomingEvents = new ArrayList<>(user.getEvents());
    
    Collections.sort(upcomingEvents);
    upcomingEvents.removeIf(e -> e.hasPassed()); // filter passed events

    for (int i = 0; i < Math.min(8, upcomingEvents.size()); i++) {
      Event event = upcomingEvents.get(i);
      int mgn = 15; // margin in pixels
      Panel card = drawEventCard(content, event, upSectionInner, VIEW);

      content.y += card.getHeight() + mgn;
      if (i == 3) {
        content.x += card.getWidth() + mgn;
        content.y = 10;
      }
    }
  }

  /**
   * Draw all cards onto all events section. This will place all events. This
   * unlocks the option to edit and delete events. Deletion prompts confirmation
   * and edit directs the user to schedule form.
   * 
   * @param user    - User to be read events from
   * @param content - Point pixel coordinate to place the card
   */
  private static void sectionAllEventsCards(User user, Point content) {
    List<Event> allEvents = new ArrayList<>(user.getEvents());
    switch (SORT_MODE) {
      case ALPHA: Collections.sort(allEvents, (e1, e2) -> e1.getName().compareTo(e2.getName())); break;
      case DATE_ADDED: break;
      case DATE_EVENT: Collections.sort(allEvents); break;
      case PARTICIPS: Collections.sort(allEvents, (e1, e2) -> e1.getParticipants().size() - e2.getParticipants().size()); break;
      default: break;
    }
    if (!TITLE_KEY.isBlank()) {
      allEvents.removeIf(e -> !e.getName().equalsIgnoreCase(TITLE_KEY));
    }
    if (!LOCATION_KEY.isBlank()) {
      allEvents.removeIf(e -> !e.getLocation().getName().equalsIgnoreCase(LOCATION_KEY));
    }
    for (int i = 0; i < allEvents.size(); i++) {
      Event event = allEvents.get(i);
      int mgn = 15;
      Panel card = null;
      if(event.getHostId() == user.getId()){ //DatabaseAPI.getUser(event.getHostId()).equals(user)){
        card = drawEventCard(content, event, allSectionInner, EDIT);
      } else {
        card = drawEventCard(content, event, allSectionInner, VIEW);
      }
      
      if (i % 2 == 0) {
        content.x += card.getWidth() + mgn;
      } else {
        content.x = 0;
        content.y += card.getHeight() + mgn; 
      }  
    }
    if (allEvents.size() % 8 == 0) {
      allSectionInner.setSize(allSectionInner.getWidth(), (allEvents.size() * 115) * 2);
      redpanel.setSize(redpanel.getWidth(), (allEvents.size() * 115) * 2);
      redpanel.setPreferredSize(new Dimension(redpanel.getWidth(), (allEvents.size() * 115) * 2));
    }
    allSectionInner.repaint();
  }

  /**
   * Draw card layout for a single event. The layout of the event card is
   * specified with the card mode paramater. Depending on panel, the card should
   * look different or give access to different features.
   * 
   * @param p        - Point pixel coordinate to place the card
   * @param event    - Event to be drawn
   * @param panel    - Panel for the card to be placed on
   * @param cardMode - Layout mode for card. <code>UPCOMING</code>,
   *                 <code>ALL</code>, <code>NOTIF</code> or <code>CALENDAR</code>
   * @return Card as panel object
   */
  private static Panel drawEventCard(Point p, Event event, Panel panel, int cardMode) {
    Panel card = new Panel();
    Point c = new Point(15, 15);
    card.setRounded(true);
    card.setBounds(p.x, p.y, 300, 100);
    card.setBackground(MasterUI.lightColAlt);
    Button view = new Button(p.x, p.y + 40, "");
    view.setSize(300, 60);
    view.setOpaque(false);
    view.setContentAreaFilled(false);
    view.setBorderPainted(false);
    view.addActionListener(e -> {
      Panel editEvent = new ScheduleEvent(frame, user, event, ScheduleModes.VIEW);
      HomeUI.switchPanel(editEvent);
    });
    
    Label name = new Label(c.x, c.y, event.getName());
    Label location = new Label(c.x, c.y + 20, event.getLocation().getName());
    Label date = new Label(c.x, c.y + 45, FormatUtil.readableDate(event.getDate()));
    Label time = new Label(c.x + 60, c.y + 45, event.getTime().toString());
    
    Label prio = new Label(card.getWidth() - 34, 10, "");
    prio.setSize(24, 24);
    prio.setIcon(event.getPriority().getIcon());
    name.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 14f));
    name.setUnset(true);
    
    int margin = 6;
    
    if (checkCardModeKey(cardMode) == EDIT) {
      Button edit = new Button(prio.getX() - (prio.getWidth() + margin), prio.getY(), "");
      edit.setSmall();
      edit.setSize(24, 24);
      edit.setColor(MasterUI.lightColAlt);
      edit.setIcon(MasterUI.editIcon);
      edit.addActionListener(e -> {
        Panel editEvent = new ScheduleEvent(frame, user, event, ScheduleModes.EDIT);
        HomeUI.switchPanel(editEvent);
      });
      
      Button remove = new Button(edit.getX() - (edit.getWidth() + margin), edit.getY(), "");
      remove.setSmall();
      remove.setSize(24, 24);
      remove.setColor(MasterUI.lightColAlt);
      remove.setIcon(MasterUI.removeIcon);
      ActionListener removeAction = new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          user.deleteEvent(event);
          ViewModelHandler.updateDashboard(user);
          panel.repaint();
        }
      };
      remove.addActionListener(e -> HomeUI.confirmDialog(removeAction, "Remove this event?"));
      
      card.add(remove);
      card.add(edit);
    }

    card.add(prio);
    card.add(name);
    card.add(location);
    card.add(date);
    card.add(time);
    panel.add(view);
    panel.add(card);
    
    MasterUI.setComponentStyles(card, "light");
    
    if (checkCardModeKey(cardMode) == VIEW && event.getPriority() == Priority.HIGH) {
      card.setBackground(MasterUI.hiPrioCol);
      Label[] labels = { name, location, date, time };
      for (Label label : labels) {
        label.setForeground(MasterUI.lightColAlt);
      }
    }

    if (checkCardModeKey(cardMode) == NOTIF) {
      // location.setText("By " + DatabaseAPI.getUser(event.getHostId()).getUsername());
      redpanel.setComponentZOrder(card, 0);
      card.setSize(250, 100);
      card.setBackground(MasterUI.primaryCol.brighter());
      name.setForeground(Color.WHITE);
      location.setForeground(Color.WHITE);
      date.setForeground(Color.WHITE);
      time.setForeground(Color.WHITE);
      prio.setLocation(card.getWidth() - 34, 10);
    }

    card.repaint();
    
    return card;
  }

  /**
   * Check if card layout mode key is valid.
   * 
   * @param key - Card layout mode
   * @return Returns <code>key</code> on valid card layout.
   * @throws IllegalArgumentException on incorrect layout mode.
   */
  private static int checkCardModeKey(int key) {
    if (key == VIEW || key == EDIT || key == NOTIF || key == CALENDAR) {
      return key;
    } else {
      throw new IllegalArgumentException("Invalid layout mode for event card.");
    }
  }
}
