package views.panels;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import controllers.ControlHandler;
import controllers.DataBaseAPI;
import models.User;
import models.Priority;
import models.Location;
import models.Event;
import views.HomeUI;
import views.MasterUI;

import java.awt.Point;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.time.LocalDate;
import java.util.ArrayList;

import views.components.*;

public class ScheduleEvent extends Panel {

  private static final long serialVersionUID = 1L;
  private Button addUserBtn;
  private Button confirmBtn;
  private Button remOption;
  private TextField searchUserField;
  private Label userQueryResult;
  private int participantListPosition = 335;
  private ArrayList<User> participants = new ArrayList<>();
  private Priority selectedPriority;
  public static Panel redpanel;
  public static TextField titleField;
  public static TextField dateField;
  public static TextField durationField;
  public static TextField timeField;
  public static TextField locationField;
  public static TextField reminderField;

  public ScheduleEvent(JFrame frame, User user) {
    super(frame);
    participants.add(user);

    Label screenTitle = new Label(40, 40, "Schedule a Meeting");
    Point contentBox = new Point(40, 170);
    String[] lbStrings = { "Topic", "When", "Start Time", "Where" };

    initDatePicker(frame);
    initPageButtons();
    initFormContent(contentBox, lbStrings);
    drawPrioritySection();
    initAddParticipant(user);
    drawReminderSection(frame);
    processConfirm(frame, user);

    this.add(screenTitle);
    ((MasterUI) frame).setComponentStyles(this, "light");
    confirmBtn.setVerticalTextPosition(SwingConstants.BOTTOM);
    screenTitle.setHeading();
  }

  /**
   * Create dropdown options as to when to remind the user of their event
   * 
   * @param dropdown - Panel object of reminder section section
   * @param frame    - JFrame of current instance
   */
  private void createReminderDropdown(Panel dropdown, JFrame frame, Panel panel) {
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
      ((MasterUI) frame).setComponentStyles(dropdown, "dark");
    }
  }

  /**
   * Draw reminder section section that then prompts drop down options
   * 
   * @param frame - JFrame of current instance
   */
  private void drawReminderSection(JFrame frame) {
    Panel panel = this;
    Label reminderLabel = new Label(400, 100, "Remind me before event");
    reminderField = new TextField(400, 120);
    reminderField.setText("Don't remind me");
    Button dpdwn = new Button(705, 120, "", MasterUI.accentCol);
    dpdwn.setIcon(MasterUI.downIcon);
    dpdwn.setSize(40, 40);
    reminderField.setEditable(false);

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
        createReminderDropdown(rp, frame, panel);
      }
    });

    this.add(rp);
    this.add(dpdwn);
    this.add(reminderField);
    this.add(reminderLabel);
  }

  /**
   * Create and initialise page buttons
   */
  private void initPageButtons() {
    confirmBtn = new Button(40, 550, "Confirm", MasterUI.secondaryCol);
    addUserBtn = new Button(705, 190, "", MasterUI.accentCol);
    addUserBtn.setSize(40, 40);
    addUserBtn.setIcon(MasterUI.addUserIcon);
    confirmBtn.setTab();
    confirmBtn.setHorizontalAlignment(SwingConstants.CENTER);
    confirmBtn.setVerticalAlignment(SwingConstants.CENTER);

    this.add(confirmBtn);
    this.add(addUserBtn);
  }

  /**
   * Create and initialise text forms. This method implements a loop for
   * developemental purposes.
   * 
   * @param contentBox - Border box and bounds for forms
   * @param lbStrings  - Names of forms to create
   */
  private void initFormContent(Point contentBox, String[] lbStrings) {
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
          durationField = textfield;

          Label timelb = new Label(contentBox.x + textfield.getWidth() + 4, initialY, "End Time");
          secondField = new TextField(contentBox.x + textfield.getWidth() + 4, initialY + 20);
          secondField.setSize(textfield.getWidth(), textfield.getHeight());
          this.add(timelb);
          this.add(secondField);
          timeField = secondField;
          break;
        case "Where":
          textfield = new TextField(contentBox.x, initialY + 20);
          locationField = textfield;
          break;
        default:
          textfield = new TextField(contentBox.x, initialY + 20);
          break;
      }
      this.add(label);
      this.add(textfield);
      initialY += 70;
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
    locationField.setText("Communications department");
    dateField.setText("2022-01-01");
    titleField.setText("Proxy Networking");
    durationField.setText("95");
    timeField.setText("9:00");
  }

  /**
   * Search a user to add them to participants
   */
  public void searchParticipant() {
    String username = searchUserField.getText();
    if (username.isEmpty())
      return;
    User user = DataBaseAPI.getUser(username);
    if (user == null) {
      userQueryResult.setText("User not found");
    } else {
      searchUserField.setText("");

      if (participants.contains(user)) {
        userQueryResult.setText("User already added");
        return;
      }

      userQueryResult.setText("Added user");
      userQueryResult.setForeground(MasterUI.secondaryCol);
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
   * Create and initialise add-participant section
   */
  private void initAddParticipant(User user) {
    Label searchUserLabel = new Label(400, 170, "People to invite");
    searchUserField = new TextField(400, 190);

    userQueryResult = new Label(400, 250, "");
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

    Label hostUser = new Label(400, 300, user.getUsername() + " (Me)");
    hostUser.setIcon(MasterUI.circleUserIcon);
    hostUser.setVerticalTextPosition(SwingConstants.BOTTOM);

    this.add(hostUser);
    this.add(searchUserLabel);
    this.add(searchUserField);
    this.add(userQueryResult);
  }

  /**
   * Set default unselected priority button styles
   * @param lo - Low priority button
   * @param mid - Medium priority button
   * @param hi - Hight priority button
   */
  private void styleDefaultPriorityBtns(Button lo, Button mid, Button hi) {
    lo.setColor(MasterUI.lightColAlt);
    mid.setColor(MasterUI.lightColAlt);
    hi.setColor(MasterUI.lightColAlt);
    lo.setText("LOW");
    mid.setText("MID");
    hi.setText("HIGH");
  }

  /**
   * Draw priority label and buttons
   */
  private void drawPrioritySection() {
    Label priorityLabel = new Label(40, 100, "Priority");
    Button loPrioBtn = new Button(40, 120, "LOW", new Color(171, 169, 239));
    Button midPrioBtn = new Button(140, 120, "MEDIUM", new Color(129, 109, 254));
    Button hiPrioBtn = new Button(240, 120, "HIGH", MasterUI.accentCol);

    loPrioBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        styleDefaultPriorityBtns(loPrioBtn, midPrioBtn, hiPrioBtn);
        loPrioBtn.setColor(MasterUI.secondaryCol);
        loPrioBtn.setText("Casual");
        selectedPriority = Priority.LOW;
      }
    });

    midPrioBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        styleDefaultPriorityBtns(loPrioBtn, midPrioBtn, hiPrioBtn);
        midPrioBtn.setColor(new Color(219, 218, 149));
        midPrioBtn.setText("Moderate");
        selectedPriority = Priority.MEDIUM;
      }
    });

    hiPrioBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        styleDefaultPriorityBtns(loPrioBtn, midPrioBtn, hiPrioBtn);
        hiPrioBtn.setColor(new Color(194, 21, 73));
        hiPrioBtn.setText("Urgent");
        selectedPriority = Priority.HIGH;
      }
    });

    this.add(priorityLabel);
    this.add(loPrioBtn);
    this.add(midPrioBtn);
    this.add(hiPrioBtn);
  }

  /**
   * Create and initialise date picker
   * 
   * @param frame - Frame of current instance
   */
  private void initDatePicker(JFrame frame) {
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
   * @return Boolean wether form is valid or not
   */
  private boolean validateForm(Label errorMsg, Panel panel) {
    for (Component c : panel.getComponents()) {
      if (c instanceof TextField && ((TextField) c).getText().isEmpty() && c != searchUserField
          || selectedPriority == null) {
        errorMsg.setText("Missing required fields.");
        return false;
      }
    }
    return true;
  }

  /**
   * Confirm creation form. Feed form data to new meeting object and proceed to
   * success screen.
   */
  private void processConfirm(JFrame frame, User user) {
    Panel panel = this;
    Label errorMsg = new Label(40, 520, "");
    this.add(errorMsg);
    ActionListener createAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();

        Event event = ControlHandler.consumeEventForm(titleField, dateField, durationField, locationField);
        event.setParticipants(participants);
        event.setPriority(selectedPriority);
        user.addEvent(event);

        Panel createMeetingConfirm = new ScheduleEventConfirm(frame, user, event);
        HomeUI.switchPanel(createMeetingConfirm);
        HomeUI.createTab.changeReferencePanel(createMeetingConfirm);
      }
    };

    confirmBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!validateForm(errorMsg, panel))
          return;
        HomeUI.confirmDialog(createAction, "Do you wish to continue?");
      }
    });
  }
}
