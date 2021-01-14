package views.panels;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import controllers.FormatUtil;
import controllers.ViewModelHandler;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import models.Event;
import models.User;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;

public class Dashboard extends Panel implements CardModes {

  private static final long serialVersionUID = 1L;
  private static Panel redpanel;
  private static Panel upSectionInner;
  private static Panel allSectionInner;
  private static Label eventData;

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
    JScrollPane scroller = createScroller();

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
    screenTitle.setHeading();

    upSectionInner = new Panel();
    upSectionInner.setBackground(MasterUI.lightCol);
    upSectionInner.setBounds(40, 80, getWidth() - 100, getHeight() - 200);

    Label allEventsTitle = new Label(40, 600, "All Events");
    allEventsTitle.setHeading();
    allSectionInner = new Panel();
    allSectionInner.setBackground(MasterUI.lightCol);
    allSectionInner.setBounds(40, 650, upSectionInner.getWidth(), upSectionInner.getHeight());

    Label dashImage = new Label(600, 400, "");
    dashImage.setSize(339, 242);
    dashImage.setIcon(MasterUI.dashImage);
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
    redpanel.add(screenTitle);
    redpanel.add(upSectionInner);
    redpanel.add(allEventsTitle);
    redpanel.add(allSectionInner);
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
    if (user.getAcceptedEvents().isEmpty()) {
      eventData = new Label(content.x, 40,
          "<html><p>No events :(<br>Schedule new events on the left</p><html>");
      eventData.setHeading();
      eventData.setSize(500, 120);
      eventData.setForeground(MasterUI.lightColAlt);
      upSectionInner.add(eventData);

      return;
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
    List<Event> upcomingEvents = new ArrayList<>(user.getAcceptedEvents());
    Collections.sort(upcomingEvents);
    upcomingEvents.removeIf(e -> e.hasPassed()); // filter passed events

    for (int i = 0; i < upcomingEvents.size(); i++) {
      Event event = upcomingEvents.get(i);
      if (i > 8)
        break; // slice list after 8 entries
      int mgn = 15; // margin in pixels
      Panel card = drawEventCard(content, event, upSectionInner, VIEW);

      content.y += card.getHeight() + mgn;
      if (i == 3)
        content.setLocation(310, mgn);
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
    List<Event> allEvents = new ArrayList<>(user.getAcceptedEvents());
    for (int i = 0; i < allEvents.size(); i++) {
      Event event = allEvents.get(i);
      int mgn = 15;
      Panel card = null;
      if(event.getHost().equals(user)){
        card = drawEventCard(content, event, allSectionInner, EDIT);
      } else {
        card = drawEventCard(content, event, allSectionInner, VIEW);
      }

      content.y += card.getHeight() + mgn;
      if (i == 3)
        content.setLocation(310, mgn);
    }
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
    Point c = new Point(15, 10);
    card.setRounded(true);
    card.setBounds(p.x, p.y, 300, 100);
    card.setBackground(MasterUI.lightColAlt);
    Button view = new Button(p.x, p.y + 40, "");
    view.setSize(300, 60);
    view.setOpaque(false);
    view.addActionListener(e -> {
      Panel editEvent = new ScheduleEvent(frame, user, event, ScheduleModes.VIEW);
      HomeUI.switchPanel(editEvent);
    });
    
    Label name = new Label(c.x, c.y, "<html><strong>" + event.getName() + "<strong><html>");
    Label location = new Label(c.x, c.y + 30, event.getLocation().getName());
    Label date = new Label(c.x, c.y + 55, FormatUtil.readableDate(event.getDate()));
    Label time = new Label(c.x + 60, c.y + 55, event.getTime().toString());
    
    Label prio = new Label(card.getWidth() - 34, 10, "");
    prio.setSize(24, 24);
    prio.setIcon(event.getPriority().getIcon());
    name.setFont(name.getFont().deriveFont(15f));
    
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
      remove.addActionListener(e -> {
        user.removeEvent(event);
        ViewModelHandler.updateDashboard(user);
        panel.repaint();
      });
      
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
