package views.panels;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import controllers.ViewModelHandler;
import models.User;
import models.Priority;
import models.Reminder;
import models.Event;
import models.Location;
import views.HomeUI;
import views.MasterUI;
import views.components.Button;
import views.components.Label;
import views.components.Panel;
import views.components.TextField;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;

import java.awt.Point;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleEvent extends Panel {

  private static final long serialVersionUID = 1L;
  private Button addUserBtn;
  private Button confirmBtn;
  private Button remOption;
  private Button hiPrioBtn;
  private Button midPrioBtn;
  private Button loPrioBtn;
  private TextField searchUserField;
  private Label userQueryResult;
  private int participantListPosition;
  private ArrayList<User> participants = new ArrayList<>();
  private Priority selectedPriority;
  private Location selectedLocation;
  private Point contentBox;
  public static Panel redpanel;
  public JScrollPane dpscroll;
  public static TextField titleField;
  public static TextField dateField;
  public static TextField startField;
  public static TextField endField;
  public static TextField locationField;
  public static TextField reminderField;
  public static TextField attachField;
  private static Label errorPriority;
  private static Label errorTitle;
  private static Label errorDate;
  private static Label errorStartTime;
  private static Label errorEndTime;
  private static Label errorLocation;
  private static Label errorReminder;
  private JFrame frame;
  private User user;
  private Event editEvent;

  public ScheduleEvent(JFrame frame, User user, Event editEvent) {
    super(frame);
    this.frame = frame;
    this.user = user;
    this.editEvent = editEvent;
    participants.add(user);

    contentBox = new Point(40, 170);
    Label screenTitle = new Label(40, 40, "Schedule an Event");
    screenTitle.setHeading();
    if (editEvent != null) {
      screenTitle.setText("Edit event");
      screenTitle.setForeground(MasterUI.accentCol);
    }
    String[] lbStrings = { "Topic", "When", "Start Time", "Where" };

    initDatePicker();
    initPageButtons();
    initFormContent(lbStrings);
    setDefaultProperties();
    drawPrioritySection();
    drawReminderSection();
    drawAttachmentSection();
    drawParticipantSection();
    processConfirm();

    add(screenTitle);
    MasterUI.setComponentStyles(this, "light");
  }

  /**
   * Build dropdown options as to when to remind the user of their event
   * 
   * @param dropdown - Panel object of reminder section section
   * @param frame    - JFrame of current instance
   */
  private void reminderDropdownSelection(Panel dropdown, JFrame frame, Panel panel) {
    if (dropdown.isActive) {
      dropdown.setSize(0, 0);
      dropdown.isActive = false;
    } else {
      dropdown.setSize(300, 200);
      String[] dueBefores = { "One week before", "Three days before", "One hour before", "Ten minutes before",
          "Don't remind me" };
      int rmd_initialY = 0;
      for (String dueBefore : dueBefores) {
        remOption = new Button(0, rmd_initialY, dueBefore, MasterUI.lightColAlt);
        remOption.setSize(dropdown.getWidth(), 40);
        remOption.setDark(false);
        remOption.setForeground(Color.BLACK);
        remOption.setHorizontalAlignment(SwingConstants.LEFT);
        dropdown.add(remOption);
        rmd_initialY += 40;

        remOption.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            reminderField.setText(dueBefore);
            dropdown.setSize(0, 0);
            dropdown.isActive = false;

            panel.add(searchUserField);
          }
        });
      }
      dropdown.isActive = true;
      MasterUI.setComponentStyles(dropdown, "dark");
    }
  }

  /**
   * Build dropdown options as to which custom location to select
   * 
   * @param textfield - Location TextField object to reference
   */
  private void locationDropdownSelection(TextField textfield) {
    if (dpscroll != null) {
      remove(dpscroll);
      dpscroll = null;
      repaint();
      return;
    }
    Panel dppanel = new Panel();
    List<Location> locations = user.getLocations();
    dppanel.setBounds(0, 0, textfield.getWidth() + 30, 40 * locations.size());
    dppanel.setPreferredSize(new Dimension(textfield.getWidth(), 40 * locations.size()));
    int y = 0;
    for (Location location : locations) {
      Button lcBtn = new Button(0, y, location.getName(), MasterUI.lightColAlt);
      lcBtn.setSize(dppanel.getWidth(), 40);
      lcBtn.setDark(false);
      lcBtn.setHorizontalAlignment(SwingConstants.LEFT);
      lcBtn.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          textfield.setText(location.getName());
          remove(dpscroll);
          dpscroll = null;
          repaint();
          selectedLocation = location;
        }
      });
      dppanel.add(lcBtn);
      y += lcBtn.getHeight();
    }
    dpscroll = new JScrollPane(dppanel);
    dpscroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    dpscroll.getVerticalScrollBar().setUnitIncrement(15);
    dpscroll.setBorder(BorderFactory.createEmptyBorder());
    dpscroll.setBounds(textfield.getX(), textfield.getY() + textfield.getHeight(), textfield.getWidth() + 40, 40 * 2);
    MasterUI.setComponentStyles(dppanel, "light");
    add(dpscroll);
    revalidate();
    repaint();
  }

  /**
   * Draw reminder section section that then prompts drop down options
   */
  private void drawReminderSection() {
    Panel panel = this;
    Label reminderLabel = new Label(400, 100, "Remind me before event");
    reminderField = new TextField(400, 120);
    reminderField.setText(Reminder.NONE.toString());
    reminderField.setEditable(false);
    Button dpdwn = new Button(700, 120, "", MasterUI.lightColAlt);
    dpdwn.setIcon(MasterUI.downIcon);
    dpdwn.setSize(40, 40);

    Panel rp = new Panel();
    rp.setBounds(400, 160, 0, 0);
    rp.setBackground(Color.RED);
    rp.isActive = false;
    dpdwn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (rp.isActive) {
          panel.add(searchUserField);
        } else {
          panel.remove(searchUserField);
        }
        reminderDropdownSelection(rp, frame, panel);
      }
    });

    add(rp);
    add(dpdwn);
    add(reminderField);
    add(reminderLabel);
  }

  /**
   * Create and initialise page buttons
   */
  private void initPageButtons() {
    confirmBtn = new Button(40, 550, "Confirm", MasterUI.secondaryCol);

    confirmBtn.setTab();
    confirmBtn.centerText();
    confirmBtn.setRounded(true);

    add(confirmBtn);
  }

  /**
   * Create and initialise text forms. This method is designed as a loop instead
   * of statically for developemental purposes.
   *
   */
  private void initFormContent(String[] lbStrings) {
    int initialY = contentBox.y;
    for (String lbString : lbStrings) {
      Label label = new Label(contentBox.x, initialY, lbString);
      TextField textfield;
      TextField secondField;
      switch (lbString) {
        case "Topic":
          textfield = new TextField(contentBox.x, initialY + 20);
          titleField = textfield;
          break;
        case "When":
          textfield = new TextField(contentBox.x, initialY + 20);
          textfield.setSize(textfield.getWidth() - 60, textfield.getHeight());
          textfield.setEditable(false);
          dateField = textfield;
          break;
        case "Start Time":
          textfield = new TextField(contentBox.x, initialY + 20);
          textfield.setSize((textfield.getWidth() / 2) - 2, textfield.getHeight());
          startField = textfield;

          Label timelb = new Label(contentBox.x + textfield.getWidth() + 4, initialY, "End Time");
          secondField = new TextField(contentBox.x + textfield.getWidth() + 4, initialY + 20);
          secondField.setSize(textfield.getWidth(), textfield.getHeight());
          this.add(timelb);
          this.add(secondField);
          endField = secondField;
          break;
        case "Where":
          textfield = new TextField(contentBox.x, initialY + 20);
          textfield.setSize(textfield.getWidth() - 40, textfield.getHeight());
          locationField = textfield;

          Button dpdwn = new Button(contentBox.x + textfield.getWidth(), initialY + 20, "", MasterUI.lightColAlt);
          dpdwn.setIcon(MasterUI.downIcon);
          dpdwn.setSize(40, 40);
          dpdwn.addActionListener(e -> locationDropdownSelection(textfield));
          this.add(dpdwn);
          break;
        default:
          textfield = new TextField(contentBox.x, initialY + 20);
          break;
      }
      this.add(label);
      this.add(textfield);
      initialY += 70;
      /**
       * When clicking in on a text field, the panel of the date picker (redpanel)
       * closes, if it has been open, so that a user does not have to manually close
       * the panel.
       */
      textfield.addFocusListener(new FocusListener() {
        public void focusGained(FocusEvent e) {
          redpanel.setSize(0, 0);
          redpanel.isActive = false;
          textfield.setText("");
        }

        public void focusLost(FocusEvent e) {
          // unchanged
        }
      });
    }
  }

  /**
   * Set properties of event to defaul values. If editEvent exists the event to be
   * edited to filled into form.
   */
  public void setDefaultProperties() {
    if (editEvent == null) {
      titleField.setText("Proxy Networking");
      locationField.setText("Communications department");
      dateField.setText("2021-01-12");
      startField.setText("09:00");
      endField.setText("10:35");
    } else {
      titleField.setText(editEvent.getName());
      locationField.setText(editEvent.getLocation().getName());
      dateField.setText(editEvent.getDate().toString());
      startField.setText(editEvent.getTime().toString());
    }
  }

  /**
   * Search a user to add them to participants. If the database search fails, an
   * error label indicates the query failure.
   */
  public void searchParticipant() {
    Panel panel = this;
    User user = ViewModelHandler.searchUser(searchUserField, panel, userQueryResult);
    if (user == null) {
    } else {
      if (participants.contains(user)) {
        userQueryResult.setText("User already added");
        return;
      }
      participants.add(user);
      Label participantLabel = new Label(400, participantListPosition, "");
      participantLabel.setText(user.getUsername());
      participantLabel.setIcon(MasterUI.circleUserIcon);
      this.add(participantLabel);
      this.repaint();
      participantListPosition += 35;
    }
  }

  /**
   * Create and initialise attachment section
   */
  private void drawAttachmentSection() {
    Label attachLabel = new Label(400, reminderField.getY() + 50, "Attachment (optional)");
    attachField = new TextField(attachLabel.getX(), attachLabel.getY() + 20);
    Button attachBtn = new Button(attachField.getX() + attachField.getWidth(), attachField.getY(), "...",
        MasterUI.lightColAlt);
    attachBtn.setDark(false);
    attachBtn.setForeground(MasterUI.accentCol);
    attachBtn.setSize(attachField.getHeight(), attachField.getHeight());
    attachBtn.addActionListener(e -> {
      JFileChooser chooser = new JFileChooser();
      chooser.setAcceptAllFileFilterUsed(false);
      chooser.setFileFilter(new FileFilter() {
        public String getDescription() {
          return "PDF files and image files";
        }

        public boolean accept(File f) {
          if (f.isDirectory()) {
            return true;
          } else {
            String filename = f.getName().toLowerCase();
            return filename.endsWith(".pdf") || filename.endsWith(".jpg") || filename.endsWith(".jpeg")
                || filename.endsWith(".png");
          }
        }
      });
      if (chooser.showOpenDialog(frame) == JFileChooser.APPROVE_OPTION) {
        attachField.setText(chooser.getSelectedFile().toPath().toString());
      }
    });

    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
        | UnsupportedLookAndFeelException ex) {
    }

    add(attachBtn);
    add(attachLabel);
    add(attachField);
  }

  /**
   * Create and initialise add-participant section
   */
  private void drawParticipantSection() {
    Label searchUserLabel = new Label(400, 240, "People to invite");
    searchUserField = new TextField(searchUserLabel.getX(), searchUserLabel.getY() + 20);

    addUserBtn = new Button(700, searchUserField.getY(), "", MasterUI.lightColAlt);
    addUserBtn.setSize(40, 40);
    addUserBtn.setIcon(MasterUI.addUserIcon);

    userQueryResult = new Label(400, searchUserField.getY() + 100, "");
    participantListPosition = userQueryResult.getY() + 50;
    addUserBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        searchParticipant();
      }
    });

    searchUserField.addFocusListener(new FocusListener() {
      public void focusGained(FocusEvent e) {
        redpanel.setSize(0, 0);
        redpanel.isActive = false;
      }

      public void focusLost(FocusEvent e) {
        // System.out.println("focus lost");
      }
    });

    Label hostUser = new Label(400, participantListPosition - 35, user.getUsername() + " (Me)");
    hostUser.setIcon(MasterUI.circleUserIcon);
    hostUser.setVerticalTextPosition(SwingConstants.BOTTOM);

    add(addUserBtn);
    add(hostUser);
    add(searchUserLabel);
    add(searchUserField);
    add(userQueryResult);
  }

  /**
   * Draw priority label and buttons
   */
  private void drawPrioritySection() {
    Label priorityLabel = new Label(40, 100, "Priority");
    loPrioBtn = new Button(40, 120, "LOW", new Color(171, 169, 239));
    midPrioBtn = new Button(140, 120, "MEDIUM", new Color(129, 109, 254));
    hiPrioBtn = new Button(240, 120, "HIGH", MasterUI.accentCol);

    loPrioBtn.addActionListener(prioBtnAction(Priority.LOW, loPrioBtn));
    midPrioBtn.addActionListener(prioBtnAction(Priority.MEDIUM, midPrioBtn));
    hiPrioBtn.addActionListener(prioBtnAction(Priority.HIGH, hiPrioBtn));

    this.add(priorityLabel);
    this.add(loPrioBtn);
    this.add(midPrioBtn);
    this.add(hiPrioBtn);
  }

  /**
   * Set default unselected priority button styles
   */
  private void styleDefaultPriorityBtns() {
    loPrioBtn.setColor(MasterUI.lightColAlt);
    midPrioBtn.setColor(MasterUI.lightColAlt);
    hiPrioBtn.setColor(MasterUI.lightColAlt);
    loPrioBtn.setText("LOW");
    midPrioBtn.setText("MID");
    hiPrioBtn.setText("HIGH");
  }

  /**
   * Get button action for priority buttons
   * 
   * @param prio - Priority enum
   * @param btn  - Button for priority
   * @return ActionListener styling priority button
   */
  public ActionListener prioBtnAction(Priority prio, Button btn) {
    return new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        styleDefaultPriorityBtns();
        btn.setColor(prio.getColor());
        btn.setText(prio.toString());
        selectedPriority = prio;
      }
    };
  }

  /**
   * Create and initialise date picker
   */
  private void initDatePicker() {
    Button openDatePicker = new Button(285, 260, "", MasterUI.accentCol);
    openDatePicker.setIcon(MasterUI.calendarIcon);
    openDatePicker.setSize(55, 40);

    redpanel = new CalendarPanel(frame, 40, true, null);
    redpanel.setSize(0, 0);
    redpanel.setBackground(MasterUI.lightCol);
    redpanel.setLayout(null);
    ((CalendarPanel) redpanel).stripComponents();
    redpanel.isActive = false;
    openDatePicker.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (redpanel.isActive) {
          redpanel.setSize(0, 0);
          redpanel.isActive = false;
        } else {
          redpanel.setBounds(285, 260, 300, 310);
          redpanel.isActive = true;
        }
      }
    });

    this.add(openDatePicker);
    this.add(redpanel);
    this.setComponentZOrder(redpanel, 0);
  }

  /**
   * Validate input form
   * 
   * @param errorMsg - Label to display error message
   * @return Boolean whether form is valid or not
   */
  private boolean validateForm(Label errorMsg, Panel panel) {
    boolean valid = true;
    Border border = BorderFactory.createLineBorder(MasterUI.hiPrioCol, 1);
    for (Component c : panel.getComponents()) {

      if (c instanceof TextField && isBlankString(((TextField) c).getText()) && c != searchUserField) {
        ((TextField) c).setBorder(border);
        errorMsg.setText("Missing required fields.");
        valid = false;
      }

      if (selectedPriority == null) {
        errorPriority.setText("(Select Priority)");
        errorPriority.setForeground(Color.RED);
        errorPriority.setHorizontalAlignment(SwingConstants.RIGHT);
        valid = false;
      } else {
        errorPriority.setText("");
      }

      if ((c == startField || c == endField) && !isValidTime(((TextField) c).getText())) {
        ((TextField) c).setBorder(border);
        valid = false;
      }

      if (c == titleField) {
        if (isBlankString(((TextField) c).getText())) {
          errorTitle.setText("(Topic required)");
          errorTitle.setForeground(Color.RED);
          errorTitle.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
          errorTitle.setText("");
        }
      }

      if (c == dateField) {
        if (isBlankString(((TextField) c).getText())) {
          errorDate.setText("(Select a date)");
          errorDate.setForeground(Color.RED);
          errorDate.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
          errorDate.setText("");
        }
      }

      if (c == startField) {
        if (isBlankString(((TextField) c).getText()) || !isValidTime(((TextField) c).getText())) {
          errorStartTime.setText("(invalid)");
          errorStartTime.setForeground(Color.RED);
          errorStartTime.setBounds(contentBox.x, contentBox.y + 132, startField.getWidth(), startField.getHeight());
          errorStartTime.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
          errorStartTime.setText("");
        }
      }

      if (c == endField) {
        if (isBlankString(((TextField) c).getText()) || !isValidTime(((TextField) c).getText())) {
          errorEndTime.setText("(invalid)");
          errorEndTime.setForeground(Color.RED);
          errorEndTime.setBounds(contentBox.x + endField.getWidth() + 4, contentBox.y + 132, endField.getWidth(),
              endField.getHeight());
          errorEndTime.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
          errorEndTime.setText("");
        }
      }

      if (c == locationField) {
        if (isBlankString(((TextField) c).getText())) {
          errorLocation.setText("(Location required)");
          errorLocation.setForeground(Color.RED);
          errorLocation.setHorizontalAlignment(SwingConstants.RIGHT);
        } else {
          errorLocation.setText("");
        }
      }

      if (c == reminderField) {
        if (isBlankString(((TextField) c).getText())) {
          errorReminder.setText("(Select a reminder)");
          errorReminder.setForeground(Color.RED);
          // errorReminder.setOpaque(true);
        } else {
          errorReminder.setText("");
        }
      }

    }
    return valid;
  }

  /**
   * Validate if given string is of pattern "H:mm"
   * 
   * @param time - the string to be checked
   * @return Boolean whether form is valid or not
   */
  private boolean isValidTime(String time) {
    try {
      LocalTime.parse(time);
    } catch (DateTimeParseException e) {
      return false;
    }
    return true;
  }

  /**
   * Check if a string is blank or not
   * 
   * @param string - the string to be checked
   * @return Boolean whether string is blanked or not
   */
  boolean isBlankString(String string) {
    return string == null || string.trim().isEmpty();
  }

  /**
   * Confirm creation form. Feed form data to new meeting object and proceed to
   * success screen.
   */
  private void processConfirm() {
    Panel panel = this;
    Label errorMsg = new Label(40, 520, "");
    this.add(errorMsg);

    errorPriority = new Label(contentBox.x + 50, 100, "");
    errorTitle = new Label(contentBox.x + 50, contentBox.y, "");
    errorDate = new Label(contentBox.x + 50, contentBox.y + 70, "");
    errorLocation = new Label(contentBox.x + 50, contentBox.y + 210, "");

    errorStartTime = new Label(contentBox.x + 50, contentBox.y + 140, "");
    errorEndTime = new Label(contentBox.x + 221, contentBox.y + 140, "");
    errorReminder = new Label(614, 100, "");

    this.add(errorTitle);
    this.add(errorDate);
    this.add(errorStartTime);
    this.add(errorEndTime);
    this.add(errorLocation);
    this.add(errorReminder);
    this.add(errorPriority);

    ActionListener createAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();

        Event event = ViewModelHandler.consumeEventForm(titleField, dateField, startField, endField, locationField,
            participants, selectedPriority, attachField);
        if (selectedLocation != null && selectedLocation.getName().equals(locationField.getText())) {
          event.setLocation(selectedLocation);
        }
        Panel createMeetingConfirm;
        if (editEvent != null) {
          editEvent.updateEvent(event);
          createMeetingConfirm = new ScheduleEventConfirm(frame, user, event, 1);
        } else {
          user.createEvent(event);
          createMeetingConfirm = new ScheduleEventConfirm(frame, user, event, 0);
        }

        HomeUI.switchPanel(createMeetingConfirm);
        HomeUI.createTab.changeReferencePanel(createMeetingConfirm);
      }
    };

    confirmBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!validateForm(errorMsg, panel)) {
          return;
        }
        HomeUI.confirmDialog(createAction, "Proceed with this event?");
      }
    });
  }
}
