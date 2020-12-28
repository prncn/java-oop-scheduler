package views;

import javax.swing.JFrame;
import javax.swing.SwingConstants;

import controllers.DataBaseAPI;
import models.UserAccount;
import models.Meeting.Priority;
import models.Event;
import models.Location;
import models.Meeting;

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

public class CreateMeetingPanel extends Panel {

  private static final long serialVersionUID = 1L;
  private Button addUserBtn;
  private Button confirmBtn;
  private Button remOption;
  private TextField searchUserField;
  private Label userQueryResult;
  private int participantListPosition = 335;
  private ArrayList<UserAccount> participants = new ArrayList<>();
  private Priority selectedPriority;
  public static Panel redpanel;
  public static TextField titleField;
  public static TextField dateField;
  public static TextField durationField;
  public static TextField locationField;
  public static TextField reminderField;

  public CreateMeetingPanel(JFrame frame, UserAccount user) {
    super(frame);
    participants.add(user);

    Label screenTitle = new Label(40, 40, "Schedule a Meeting");
    Point contentBox = new Point(40, 170);
    String[] lbStrings = { "Topic", "When", "Duration", "Where" };

    initDatePicker(frame);
    initPageButtons();
    initFormContent(contentBox, lbStrings);
    drawPrioritySection();
    initAddParticipant(user);
    drawReminderSection(frame);
    validateForm(frame, user);

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
        remOption = new Button(0, rmd_initialY, dueBefore, MasterUI.getColor("lightColAlt"));
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
   * Create and initialise text forms. This method implements a loop for developemental purposes.
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
        case "Duration":
          textfield = new TextField(contentBox.x, initialY + 20);
          textfield.setSize((textfield.getWidth() / 2) - 2, textfield.getHeight());
          durationField = textfield;

          secondField = new TextField(contentBox.x + textfield.getWidth() + 4, initialY + 20);
          secondField.setSize(textfield.getWidth(), textfield.getHeight());
          this.add(secondField);
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
        }
        public void focusLost(FocusEvent e) {
          //
        }
      });
    }
    for(Component c : this.getComponents()) {
      if(c instanceof TextField){
        
      }
    }
  }

  /**
   * Search a user to add them to participants
   */
  public void searchParticipant() {
    String username = searchUserField.getText();
    if (username.isEmpty())
      return;
    UserAccount user = DataBaseAPI.getUser(username);
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
  private void initAddParticipant(UserAccount user) {
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
   * Draw priority label and buttons
   */
  private void drawPrioritySection() {
    Label priorityLabel = new Label(40, 100, "Priority");
    Button loPrioBtn = new Button(40, 120, "LOW", new Color(171, 169, 239));
    Button midPrioBtn = new Button(140, 120, "MEDIUM", new Color(129, 109, 254));
    Button hiPrioBtn = new Button(240, 120, "HIGH", MasterUI.accentCol);

    loPrioBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loPrioBtn.setColor(MasterUI.secondaryCol);
        midPrioBtn.setColor(MasterUI.lightColAlt);
        hiPrioBtn.setColor(MasterUI.lightColAlt);
        loPrioBtn.setText("Casual");
        midPrioBtn.setText("MID");
        hiPrioBtn.setText("HIGH");
        selectedPriority = Priority.LOW;
      }
    });

    midPrioBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loPrioBtn.setColor(MasterUI.lightColAlt);
        midPrioBtn.setColor(new Color(219, 218, 149));
        hiPrioBtn.setColor(MasterUI.lightColAlt);
        loPrioBtn.setText("LOW");
        midPrioBtn.setText("Moderate");
        hiPrioBtn.setText("HIGH");
        selectedPriority = Priority.MEDIUM;
      }
    });

    hiPrioBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        loPrioBtn.setColor(MasterUI.lightColAlt);
        midPrioBtn.setColor(MasterUI.lightColAlt);
        hiPrioBtn.setColor(new Color(194, 21, 73));
        loPrioBtn.setText("LOW");
        midPrioBtn.setText("MID");
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

    redpanel = new CalendarPanel(frame, 40, true);
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
   * Valide creation form. If required fields are missing, the user gets an error
   * (on UI level).
   */
  private void validateForm(JFrame frame, UserAccount user) {
    Panel panel = this;
    Label errorMsg = new Label(40, 520, "");
    this.add(errorMsg);
    confirmBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        for (Component c : panel.getComponents()) {
          if (c instanceof TextField && ((TextField) c).getText().isEmpty() && c != searchUserField
              || selectedPriority == null || participants.size() < 2) {
            errorMsg.setText("Missing required fields.");
            return;
          }
        }

        panel.removeAll();
        String eventName = titleField.getText();
        LocalDate eventDate = LocalDate.parse(dateField.getText());
        int eventDuration = Integer.parseInt(durationField.getText());
        Location location = new Location();
        Event event = new Event(eventName, eventDate, eventDuration, location);

        Priority priority = selectedPriority;
        Meeting meeting = new Meeting(event, participants, priority);
        Panel createMeetingConfirm = new CreateMeetingConfirm(frame, user, meeting);
        HomeUI.switchPanel(createMeetingConfirm);
      }
    });
  }
}
