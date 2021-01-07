package views.panels;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import controllers.FormatUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.Collections;
import java.util.List;

import models.Event;
import models.User;
import views.MasterUI;
import views.components.Label;
import views.components.Panel;

public class Dashboard extends Panel {

  private static final long serialVersionUID = 1L;
  private static Panel redpanel;
  private static Panel upSectionInner;
  private static Panel allSectionInner;
  public static Label eventData;

  public Dashboard(JFrame frame, User user) {
    super(frame);
    redpanel = new Panel();
    redpanel.setPreferredSize(new Dimension(getWidth(), getHeight() * 2 - 150));
    redpanel.setBackground(MasterUI.lightCol);
    createDashboardTab(user);
    JScrollPane scroller = createScroller();

    allSectionInner = new Panel();
    allSectionInner.setBackground(MasterUI.lightCol);
    allSectionInner.setBounds(40, 520, upSectionInner.getWidth(), upSectionInner.getHeight());
    redpanel.add(allSectionInner);

    Label allEventsTitle = new Label(0, 70, "All Events");
    allEventsTitle.setHeading();
    allSectionInner.add(allEventsTitle);

    Label scrollHintIcon = new Label();
    scrollHintIcon.setBounds(getWidth() / 2 - 50, 600, 24, 24);
    scrollHintIcon.setIcon(MasterUI.downIconDark);

    add(scrollHintIcon);
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
    upSectionInner.setBounds(40, 80, getWidth() - 100, getHeight() - 260);

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
  }

  /**
   * Draw event data on dashboard
   * 
   * @param user - Currently logged in user
   */
  public static void drawEventData(User user) {
    upSectionInner.removeAll();
    Point content = new Point(0, 10);
    if (user.getAcceptedEvents().isEmpty()) {
      eventData = new Label(content.x, 40,
          "<html><p>No events planned :( <br>Schedule a new event on the tab on the left</p><html>");
      eventData.setHeading();
      eventData.setSize(500, 120);
      eventData.setForeground(Color.LIGHT_GRAY);
      upSectionInner.add(eventData);

      return;
    }

    
    sectionUpcomingEventsCards(user, content);
    sectionAllEventsCards(user, content);
  }

  /**
   * Draw all cards onto upcoming events section. This will filter for up to 8
   * coming events.
   * 
   * @param user
   * @param content
   */
  private static void sectionUpcomingEventsCards(User user, Point content) {
    List<Event> upcomingEvents = user.getAcceptedEvents();
    Collections.sort(upcomingEvents);
    upcomingEvents.removeIf(e -> e.hasPassed()); // filter passed events

    for (int i = 0; i < upcomingEvents.size(); i++) {
      Event event = upcomingEvents.get(i);
      if (i > 8)
        break; // slice list after 8 entries
      int mgn = 15; // margin in pixels
      Panel card = drawEventCard(content, event, upSectionInner);

      content.y += card.getHeight() + mgn;
      if (i == 3)
        content.setLocation(310, mgn);
    }
  }

  /**
   * Draw all cards onto all events section. This will place all events.
   * 
   * @param user    - User to be read events from
   * @param content - Point pixel coordinate to place the card
   */
  private static void sectionAllEventsCards(User user, Point content) {
    List<Event> allEvents = user.getAcceptedEvents();
    for (int i = 0; i < allEvents.size(); i++) {
      Event event = allEvents.get(i);
      int mgn = 15;
      Panel card = drawEventCard(content, event, allSectionInner);

      content.y += card.getHeight() + mgn;
      if (i == 3)
        content.setLocation(310, mgn);
    }
  }

  /**
   * Draw card layout for a single event
   * 
   * @param p     - Point pixel coordinate to place the card
   * @param event - Event to be drawn
   * @param panel - Panel for the card to be placed on
   * @return Card as panel object
   */
  private static Panel drawEventCard(Point p, Event event, Panel panel) {
    Panel card = new Panel();
    Point c = new Point(15, 10);
    card.setRounded(true);
    card.setBounds(p.x, p.y, 300, 100);
    card.setBackground(MasterUI.lightColAlt);

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
    panel.add(card);

    return card;
  }
}
