package views.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import controllers.FormatUtil;
import models.Event;
import models.User;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;

public class CalendarPanelWeekly extends Panel {

  private static final long serialVersionUID = -8947171452870363548L;
  private Button dispModeWeek;
  private Button dispModeMonth;

  private final static int initialX = 10;
  private final static int initialY = 50;
  private final static int d_wdth = 125;

  private static Label dayNum;
  private static Label dayName;
  private static Panel scrollpanel;
  private static Panel greenpanel;
  private Color background;
  private Panel dayNums;

  public LocalDate date;
  private CalendarPanel origin;
  private User user;

  public CalendarPanelWeekly(JFrame frame, CalendarPanel origin, User user) {
    super(frame);
    this.origin = origin;
    this.user = user;
    background = MasterUI.lightCol;
    date = ((CalendarPanel) origin).parseDateFromTextField();

    drawWeekDaysBar();
    drawDisplayModeBtns(frame);

    JScrollPane scroller = createScrollPane();
    createDynamicPanel();
    scroller.getViewport().add(scrollpanel);
    createStaticPanel();
    updateWeekDaysBar(date);
    replaceStaticPanel(scroller);
    add(scroller);
    
    MasterUI.setComponentStyles(this, "light");
  }

  public CalendarPanelWeekly(JFrame frame, LocalDate date, User user) {
    super(frame);
    this.date = date;
    this.user = user;
    background = Color.WHITE;
    setBackground(background);

    drawWeekDaysBar();
    createStaticPanel();
    greenpanel.setSize(getWidth() - 40, getHeight() + 80);
    updateWeekDaysBar(date);

    MasterUI.setComponentStyles(this, "light");
  }

  /**
   * Replace static calendar panel with the dynamic panel when the user scrolls
   */
  private void replaceStaticPanel(JScrollPane scroller) {
    Panel panel = this;
    JScrollBar bar = scroller.getVerticalScrollBar();
    bar.addAdjustmentListener(new AdjustmentListener() {
      public void adjustmentValueChanged(AdjustmentEvent e) {
        panel.remove(greenpanel);
      }
    });
  }

  /**
   * Create scroll panel that will hold the scrollable
   * calendar grid
   * 
   * @return JScrollPane object
   */
  private JScrollPane createScrollPane() {
    JScrollPane scroller = new JScrollPane();
    scroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scroller.getVerticalScrollBar().setUnitIncrement(10);
    scroller.setBounds(10, 120, this.getWidth() - 40, this.getHeight() - 170);
    scroller.setPreferredSize(new Dimension(this.getWidth() - 40, this.getHeight() - 170));
    scroller.setBorder(BorderFactory.createEmptyBorder());
    return scroller;
  }

  /**
   * Create dynamic panel to which the scroll pane will be set to
   */
  private void createDynamicPanel() {
    scrollpanel = new Panel();
    scrollpanel.setPreferredSize(new Dimension(this.getWidth() - 80, 30 * 26));
    scrollpanel.setBounds(10, 120, this.getWidth() - 40, this.getHeight() - 170);
    scrollpanel.setBackground(background);
    drawGrid(scrollpanel);
  }

  /**
   * Create static panel which only shows before the user starts scrolling
   */
  private void createStaticPanel() {
    greenpanel = new Panel();
    greenpanel.setBounds(10, 120, this.getWidth() - 40, this.getHeight() - 170);
    greenpanel.setBackground(background);
    add(greenpanel);
    drawGrid(greenpanel);
  }

  /**
   * Work-around for java swing limitations. Wrapping this function fixes
   * visibility bugs in scroll component.
   * 
   * @see placeEventDriver
   * @param scrollpanel - Panel containg calendar grid
   * @param time        - Time of event
   */
  private static void placeEvent(Panel panel, Event event) {
    LocalDate date = event.getDate();
    LocalTime time = event.getTime();
    Color eventCol;

    int weekDay = date.getDayOfWeek().getValue();
    Panel bluepanel = new Panel();
    int xOffset = (int) LocalTime.parse("08:00").until(time, ChronoUnit.MINUTES);
    int yOffset = d_wdth * (weekDay - 1);
    eventCol = event.getPriority().getColor();

    bluepanel.setBounds(90 + yOffset, xOffset, d_wdth - 2, Math.max(event.getDurationMinutes(), 45));
    bluepanel.setBackground(FormatUtil.colorLowerAlpha(eventCol, 80));
    bluepanel.setAlpha(0.93f);

    Label eventInfo = new Label(2, 2,
        "<html><p><strong>" + event.getName() + "</strong><br>" + event.getTime() + "</p><html>");
    eventInfo.setSize(d_wdth - 10, bluepanel.getHeight());
    eventInfo.setFont(MasterUI.robotoFont);
    eventInfo.setVerticalAlignment(SwingConstants.TOP);
    eventInfo.setHorizontalAlignment(SwingConstants.LEFT);
    eventInfo.setForeground(eventCol.darker());
    bluepanel.add(eventInfo);

    panel.add(bluepanel);
    panel.setComponentZOrder(bluepanel, 0);

    panel.revalidate();
    panel.repaint();
  }

  /**
   * Place event graphic onto weekly calendar. Wrapper for
   * {@link #placeEvent(Panel, Event)}
   * 
   * @see placeEvent
   * @param time - Time of event
   * @param date - Date of event
   */
  public void placeEventDriver(Event event) {
    placeEvent(scrollpanel, event);
    placeEvent(greenpanel, event);

    scrollpanel.repaint();
    greenpanel.repaint();
  }

  /**
   * Remove event panels from weekly calendar grid
   */
  public void clearEventPanels() {
    scrollpanel.removeAll();
    greenpanel.removeAll();
  }

  /**
   * Draw weekly calendar layout grid. The cells of the grid
   * correspond to a 30-minute time frame. Event panel will be placed
   * on the grid.
   * 
   * @param redpanel - Panel the grid is placed on
   */
  private void drawGrid(Panel redpanel) {
    int h = 28;
    Point p = new Point(0, -(h + 1));
    LocalTime time = LocalTime.parse("08:00");
    for (int i = 0; i < 208; i++) {
      Panel bluepanel = new Panel();
      bluepanel.setBackground(MasterUI.lightColAlt);
      if (i % 8 == 0) {
        p.x = 0;
        p.y += h + 2;
        bluepanel.setBounds(p.x, p.y, 88, h);
        if (i % 16 == 0) {
          Label label = new Label(10, 2, time.toString());
          bluepanel.add(label);
          time = time.plusHours(1);
        }
        p.x += 90;
      } else {
        bluepanel.setBounds(p.x, p.y, d_wdth - 2, h);
        if (i % 8 == 6 || i % 8 == 7) {
          bluepanel.setBackground(new Color(230, 230, 235));
        }
        p.x += d_wdth;
      }
      redpanel.add(bluepanel);
    }
  }

  /**
   * Draw buttons to switch display modes.
   * <code>Week</code> shows weekly calendar, <code>Month</code> show regular monthly calendar.
   */
  private void drawDisplayModeBtns(JFrame frame) {
    dispModeWeek = new Button(initialX, 10, "Week", MasterUI.secondaryCol);
    dispModeMonth = new Button(70, 10, "Month", MasterUI.lightColAlt);

    dispModeMonth.addActionListener(CalendarPanel.getDisplayAction(origin));
    dispModeWeek.setDark(true);
    dispModeWeek.setSize(60, 35);
    dispModeMonth.setDark(false);
    dispModeMonth.setSize(60, 35);

    add(dispModeMonth);
    add(dispModeWeek);
  }
  
  /**
   * Draw initial week day bar
   */
  private void drawWeekDaysBar() {
    dayNums = new Panel();
    dayNums.setBackground(background);
    dayNums.setAlpha(1f);
    dayNums.setBounds(initialX + 90, initialY, (d_wdth - 2) * 7, 70);
    add(dayNums);
  }

  /**
   * Update the data on the week day bar, depending on currently
   * selected day on the monthly calendar
   * @param date
   */
  public void updateWeekDaysBar(LocalDate date) {
    String[] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    dayNums.removeAll();
    int incremX = 0;

    date = date.with(DayOfWeek.MONDAY);

    for (String day : days) {
      dayNum = new Label(incremX, 0, String.valueOf(date.getDayOfMonth()));
      dayName = new Label(incremX, 40, day);
      dayNum.setSize(d_wdth - 2, 40);
      dayNum.setHeading();
      if (date.equals(LocalDate.now())) {
        dayNum.setForeground(MasterUI.accentCol);
      }
      dayName.setSize(d_wdth - 2, 20);

      dayNums.add(dayName);
      dayNums.add(dayNum);
      incremX += d_wdth;

      if (user != null) {
        for (Event event : user.getEvents()) {
          if (event.getDate().equals(date)) {
            placeEventDriver(event);
          }
        }
      }

      date = date.plusDays(1);
    }
  }

  /**
   * Get origin panel, monthly calendar, of 
   * current weekly panel
   * @return CalendarPanel object of calendar
   */
  public CalendarPanel getOrigin() {
    return origin;
  }

  /**
   * Get date currently selected in calendar
   * @return LocalDate object of date
   */
  public LocalDate getDate() {
    return date;
  }
}
