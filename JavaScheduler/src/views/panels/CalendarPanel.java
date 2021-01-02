package views.panels;

import java.awt.Color;

import controllers.FormatUtil;
import models.Event;
import models.User;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.Date;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;
import views.HomeUI;
import views.MasterUI;

public class CalendarPanel extends Panel {

  private static final long serialVersionUID = 1L;
  public int d_wdth; // 95
  final static int initialX = 10; // 20
  private final int initialY = 100; // 120

  private TextField yearField = new TextField(700, 70);
  private TextField monthField = new TextField(700, 120);
  private TextField dayField = new TextField(700, 170);

  private Panel redpanel = new Panel();
  private CalendarPanelWeeky weeklyDisplay;
  private Button prevActive = null;
  private Button highlightToday;
  public LocalDate today;
  private Label meetingsInfo;
  private String noMeetingStr = "No meetings on this day :)";

  public static Button dispModeWeek;
  public static Button dispModeMonth;
  private Button nextMonthBtn;
  private Button prevMonthBtn;

  private boolean isMinified;
  private JFrame frame;
  public User user;

  public CalendarPanel(JFrame frame, int d_wdth, boolean isMinified, User user) {
    super(frame);
    this.setLayout(null);
    this.d_wdth = d_wdth;
    this.isMinified = isMinified;
    this.frame = frame;
    this.user = user;

    drawDisplayModeBtns();
    
    meetingsInfo = new Label(700, 250, noMeetingStr);
    this.add(meetingsInfo);
    drawWeekDaysBar();
    today = LocalDate.now();
    initDateTextFields(today);
    setDisplayAction();
    
    LocalDate argDate = parseDateFromTextField();
    initCalendarLayout(argDate);
    initNavigationBtns();

    redpanel.setBounds(initialX, initialY, 7 * d_wdth, 5 * d_wdth);
    redpanel.setBackground(MasterUI.lightCol);
    this.add(redpanel);
    this.add(dispModeWeek);
    this.add(dispModeMonth);

    ((MasterUI) frame).setComponentStyles(redpanel, "light");
    ((MasterUI) frame).setComponentStyles(this, "light");
    styleDateTextFields();
    styleTopIcons();
  }

  /**
   * Minify panel and shrink/strip components
   */
  public void stripComponents() {
    this.remove(yearField);
    this.remove(dayField);
    this.remove(dispModeWeek);
    this.remove(dispModeMonth);

    monthField.setBounds(120, 15, 70, 30);
    monthField.setHorizontalAlignment(SwingConstants.CENTER);
    monthField.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 18f));
    monthField.setText(monthField.getText().substring(0, 3));
    prevMonthBtn.setPosition(60, 5);
    nextMonthBtn.setPosition(200, 5);

    this.repaint();
  }

  /**
   * Draw buttons to switch display modes
   */
  private static void drawDisplayModeBtns() {
    dispModeWeek = new Button(initialX, 10, "Week", MasterUI.lightColAlt);
    dispModeMonth = new Button(70, 10, "Month", MasterUI.lightColAlt);

    dispModeWeek.setDark(false);
    dispModeWeek.setSize(60, 35);
    dispModeMonth.setDark(true);
    dispModeMonth.setSize(60, 35);
  }

  /**
   * Get corresponding display button action depending on current display
   * @param display - Currently displayed calendar (monthly/weekly)
   * @return ActionListener for display button
   */
  public static ActionListener getDisplayAction(Panel display) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        HomeUI.switchPanel(display);
        HomeUI.calendarTab.changeReferencePanel(display);
      }
    };
  }

  /**
   * Set button action on display modes
   */
  private void setDisplayAction() {
    dispModeMonth.setColor(MasterUI.secondaryCol);
    CalendarPanel origin = this;
    // weeklyDisplay = new CalendarPanelWeeky(frame, origin);

    // dispModeWeek.addActionListener(getDisplayAction(weeklyDisplay));
    dispModeWeek.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        weeklyDisplay = new CalendarPanelWeeky(frame, origin, user);
        HomeUI.switchPanel(weeklyDisplay);
        HomeUI.calendarTab.changeReferencePanel(weeklyDisplay);
      }
    });
  }

  /**
   * Draw bar containing week day names on calendar
   */
  private void drawWeekDaysBar() {
    String[] daysFull = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday" };
    String[] daysShort = { "M", "T", "W", "T", "F", "S", "S" };
    String[] days;
    if (isMinified) {
      days = daysShort;
    } else {
      days = daysFull;
    }
    int incremX = initialX;
    for (String day : days) {
      Label dayLabel = new Label(incremX, initialY - 50, day);
      dayLabel.setHorizontalAlignment(SwingConstants.CENTER);
      dayLabel.setBackground(MasterUI.lightColAlt);
      dayLabel.setSize(d_wdth - 2, 40);
      dayLabel.setOpaque(true);
      this.add(dayLabel);
      incremX += d_wdth;
    }
  }

  /**
   * Parse strings of text fields to LocalDate objects to be passed onto date
   * changer
   * 
   * @return LocalDate object of converted strings
   */
  public LocalDate parseDateFromTextField() {
    String yearStr = yearField.getText();
    String monthStr = monthField.getText();
    String dayStr = dayField.getText().replaceAll("\\D+", "");
    String parseDate = yearStr + "-" + monthStr + "-" + dayStr;

    SimpleDateFormat parser = new SimpleDateFormat("yyyy-MMM-dd", Locale.US);
    String formattedDate = "";
    try {
      Date date = parser.parse(parseDate);
      SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
      formattedDate = formatter.format(date);
    } catch (ParseException e1) {
      e1.printStackTrace();
    }

    return LocalDate.parse(formattedDate);
  }

  /**
   * Navigate to next or to previous month. Change the Year accordingly
   * (breakpoint)
   * 
   * @param direction - String either next or prev
   * @param frame     - JFrame of current instance
   */
  private void navigateMonth(String direction, JFrame frame) {
    LocalDate date = parseDateFromTextField();
    int breakpoint;
    int addremove;
    if (direction == "next") {
      date = date.plusMonths(1);
      breakpoint = 1;
      addremove = 1;
    } else if (direction == "prev") {
      date = date.minusMonths(1);
      breakpoint = 12;
      addremove = -1;
    } else {
      throw new IllegalArgumentException("Invalid calendar navigation direction");
    }
    Month nextMonth = date.getMonth();
    if (nextMonth.getValue() == breakpoint) {
      yearField.setText(Integer.toString(Integer.parseInt(yearField.getText()) + addremove));
    }
    dayField.setText(FormatUtil.formatOrdinal(1));
    if (isMinified) {
      monthField.setText(nextMonth.toString().substring(0, 3));
    } else {
      monthField.setText(nextMonth.toString());
    }
    changeDateFromTextField(frame);
  }

  /**
   * Create and initialise navigation buttons
   * 
   * @param frame - JFrame of current instance
   */
  private void initNavigationBtns() {
    nextMonthBtn = new Button(740, 15, "", MasterUI.lightColAlt);
    prevMonthBtn = new Button(680, 15, "", MasterUI.lightColAlt);
    nextMonthBtn.setIcon(MasterUI.nextIcon);
    prevMonthBtn.setIcon(MasterUI.prevIcon);

    prevMonthBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        navigateMonth("prev", frame);
      }
    });

    nextMonthBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        navigateMonth("next", frame);
      }
    });

    this.add(nextMonthBtn);
    this.add(prevMonthBtn);
  }

  /**
   * Function to change date of calendar according to input from text fields
   * 
   * @param frame - JFrame of current instance (used for setComponentStyles cast)
   */
  public void changeDateFromTextField(JFrame frame) {
    redpanel.removeAll();
    redpanel.repaint();

    LocalDate argDate = parseDateFromTextField();
    initCalendarLayout(argDate);

    redpanel.setLayout(null);
    ((MasterUI) frame).setComponentStyles(redpanel, "light");
    redpanel.repaint();
  }

  /**
   * Set event info label when day button is clicked
   * 
   * @param str - String the label to be set to
   * @return ActionListener to be passed to day button
   */
  private ActionListener setInfoAction(String str) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        meetingsInfo.setText(str);
      }
    };
  }

  /**
   * Fetch event info on day button click of that day
   * 
   * @param currentLocalDate - LocalDate of calendar day
   * @param dayBtn           - Calendar day button
   */
  private void fetchEventInfo(LocalDate currentLocalDate, Button dayBtn) {
    if (isMinified)
      return;
    for (Event event : user.getAcceptedEvents()) {
      if (currentLocalDate.equals(event.getDate())) {
        dayBtn.setColor(MasterUI.accentCol);
        dayBtn.addActionListener(setInfoAction(event.getName()));
        dayBtn.setText("<html><p>" + event.getName() + "</p><html>");
        dayBtn.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD));
        dayBtn.setVerticalAlignment(SwingConstants.TOP);
        dayBtn.setDark(true);
        
        // dayBtn.setIcon(MasterUI.profileIcon);
      } else {
        dayBtn.addActionListener(setInfoAction(noMeetingStr));
      }
    }
  }

  /**
   * Make days that are in the past unclickable
   * 
   * @param dayBtn           - Calendar day button
   * @param currentLocalDate - Current date object
   */
  private void disablePassedDates(Button dayBtn, LocalDate currentLocalDate) {
    if (isMinified && currentLocalDate.isBefore(today)) {
      dayBtn.setForeground(Color.LIGHT_GRAY);
      dayBtn.setEnabled(false);
    }
  }

  /**
   * Send selected day's date to ScheduleEvent form field
   */
  private void sendToScheduleForm() {
    if (isMinified) {
      ScheduleEvent.dateField.setText(parseDateFromTextField().toString());
      ScheduleEvent.redpanel.setSize(0, 0);
      ScheduleEvent.redpanel.isActive = false;
    }
  }

  /**
   * Set style for active selected day button
   * 
   * @param dayNum          - Integer of current day of the month
   * @param activeSelectDay - Integer of active selection (day that is currently clicked)
   * @param dayBtn          - Calendar day button
   */
  private void styleActiveSelectDay(int dayNum, int activeSelectDay, Button dayBtn) {
    if (dayNum == activeSelectDay) {
      dayBtn.setColor(MasterUI.secondaryCol);
      dayBtn.setDark(true);
      prevActive = dayBtn;
    }
  }

  /**
   * Initialise calendar layout
   * 
   * @param date - LocalDate object describing any date
   */
  public void initCalendarLayout(LocalDate date) {
    Month currentMonth = date.getMonth();
    int dayOfMonth = date.getDayOfMonth();
    int currentYear = date.getYear();
    YearMonth yearMonth = YearMonth.of(currentYear, currentMonth);
    int firstWeekday = yearMonth.atDay(1).getDayOfWeek().getValue();
    int daysInMonth = yearMonth.lengthOfMonth();
    int activeSelectDay = dayOfMonth; // default active selection is today's day button
    int firstDay = firstWeekday - 1;
    int incremX = 0;
    int incremY = 0;
    incremX += firstDay * d_wdth;

    for (int i = firstDay; i < firstDay + daysInMonth; i++) {
      int dayNum = i - firstDay + 1;
      if (i % 7 == 0 && i != 0) {
        incremX = 0;
        incremY += d_wdth;
      }
      Button dayBtn = new Button(incremX, incremY, Integer.toString(dayNum), MasterUI.lightColAlt);
      LocalDate currentLocalDate = FormatUtil.parseDate(currentYear, currentMonth.getValue(), dayNum);

      styleDayBtn(dayBtn);
      disablePassedDates(dayBtn, currentLocalDate);
      fetchEventInfo(currentLocalDate, dayBtn);
      dayBtn.setPrevColor(dayBtn.getColor());
      styleActiveSelectDay(dayNum, activeSelectDay, dayBtn);

      if(currentLocalDate.equals(LocalDate.now())){
        highlightToday = dayBtn;
        dayBtn.setForeground(Color.RED);
      }

      dayBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          dayBtn.setDark(true);
          switchActiveDayBtn(dayBtn, currentYear, currentMonth, dayNum);
          sendToScheduleForm();
          if(!isMinified)
            weeklyDisplay.drawWeekDaysBar(FormatUtil.parseDate(currentYear, currentMonth.getValue(), dayNum));
        }
      });

      redpanel.add(dayBtn);
      incremX += d_wdth;
    }
  }

  /**
   * Create and initialise date text fields
   * 
   * @param date  - LocalDate object of any date
   * @param frame - JFrame of current instance
   */
  private void initDateTextFields(LocalDate date) {
    yearField = new TextField(700, 70);
    monthField = new TextField(700, 120);
    dayField = new TextField(700, 170);
    yearField.setText(Integer.toString(date.getYear()));
    if (isMinified) {
      monthField.setText(date.getMonth().toString().substring(0, 3));
    } else {
      monthField.setText(date.getMonth().toString());
    }
    dayField.setText(FormatUtil.formatOrdinal(date.getDayOfMonth()));

    monthField.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        changeDateFromTextField(frame);
      }
    });

    this.add(yearField);
    this.add(monthField);
    this.add(dayField);
  }

  /**
   * Set styles for display mode buttons and navigation buttons
   */
  private void styleTopIcons() {
    nextMonthBtn.setSize(60, 35);
    nextMonthBtn.setColor(MasterUI.lightCol);
    prevMonthBtn.setSize(60, 35);
    prevMonthBtn.setColor(MasterUI.lightCol);
  }

  /**
   * Set styles for date text fields
   */
  private void styleDateTextFields() {
    yearField.setSize(100, 40);
    yearField.setBackground(MasterUI.lightCol);
    yearField.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 30f));
    monthField.setSize(200, 40);
    monthField.setBackground(MasterUI.lightCol);
    monthField.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 30f));
    dayField.setSize(200, 40);
    dayField.setBackground(MasterUI.lightCol);
    dayField.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 30f));
  }

  /**
   * Set styles for day buttons inside calendar
   * 
   * @param dayBtn - Button object depicting day inside calendar
   */
  private void styleDayBtn(Button dayBtn) {
    dayBtn.setDark(false);
    dayBtn.setForeground(Color.BLACK);
    dayBtn.setVerticalAlignment(SwingConstants.BOTTOM);
    dayBtn.setHorizontalAlignment(SwingConstants.RIGHT);
    dayBtn.setSize(d_wdth - 2, d_wdth - 2);
  }

  /**
   * Switch the current active day button (calendar day currently clicked on) and
   * set its styles and active state (infos on the active day are displayed on the
   * right)
   * 
   * @param dayBtn       - Button object depicting day inside calendar
   * @param currentYear  - Integer of any year
   * @param currentMonth - Month object of any month
   * @param dayNum       - Integer of any day (of the month)
   */
  private void switchActiveDayBtn(Button dayBtn, int currentYear, Month currentMonth, int dayNum) {
    String yearStr = Integer.toString(currentYear);
    String monthStr = currentMonth.toString();

    yearField.setText(yearStr);
    if (isMinified) {
      monthField.setText(monthStr.substring(0, 3));
    } else {
      monthField.setText(monthStr);
    }
    dayField.setText(FormatUtil.formatOrdinal(dayNum));

    if (prevActive != null) {
      prevActive.setColor(prevActive.getPrevColor());
      if(prevActive.getPrevColor().equals(MasterUI.lightColAlt)){
        prevActive.setDark(false);
      } 
    }
    if(prevActive.equals(highlightToday)){
      prevActive.setForeground(Color.RED);
    }

    dayBtn.setColor(MasterUI.secondaryCol);
    dayBtn.setDark(true);

    prevActive = dayBtn;
  }
}
