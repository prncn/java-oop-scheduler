package views.panels;

import controllers.FormatUtil;
import controllers.ViewModelHandler;
import models.Event;
import models.*;
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
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class ScheduleEvent extends Panel implements ScheduleModes {

  private static final long serialVersionUID = 1L;
  private Button addUserBtn;
  private Button confirmBtn;
  private Button remOption;
  private Button hiPrioBtn;
  private Button midPrioBtn;
  private Button loPrioBtn;
  private Button dpdwn;
  private TextField searchUserField;
  private Label userQueryResult;
  private int participantListPosition;
  private ArrayList<User> participants;
  private ArrayList<File> selectedAttachments;
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
  private static Label WhereLabel;
  private JFrame frame;
  private User user;
  private Event editEvent;
  private int mode;

  public ScheduleEvent(JFrame frame, User user, Event editEvent, int mode) {
    super(frame);
    this.frame = frame;
    this.user = user;
    this.editEvent = editEvent;
    this.mode = mode;

    contentBox = new Point(40, 170);
    Label screenTitle = new Label(40, 40, "");
    screenTitle.setHeading();
    if (mode == CREATE) {
      screenTitle.setText("Schedule an Event");
      participants = new ArrayList<>();
      selectedAttachments = new ArrayList<>();
      participants.add(user);
    } else if (mode == EDIT) {
      screenTitle.setText("Edit event");
      screenTitle.setForeground(MasterUI.accentCol);
      participants = editEvent.getParticipants();
      selectedAttachments = editEvent.getAttachments();
    } else if (mode == VIEW) {
      screenTitle.setText(editEvent.getName());
      screenTitle.setForeground(MasterUI.accentCol);
      selectedAttachments = editEvent.getAttachments();
      participants = editEvent.getParticipants();
    }

    if (mode != VIEW) {
      initDatePicker();
    }
    initPageButtons();
    initFormContent();
    drawPrioritySection();
    drawReminderSection();
    drawAttachmentSection();
    drawParticipantSection();
    processConfirm();

    add(screenTitle);
    MasterUI.setComponentStyles(this, "light");
    setDefaultProperties();
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
    if (mode != VIEW) {
      add(dpdwn);
    }
    add(reminderField);
    add(reminderLabel);
  }

  /**
   * Create and initialise page buttons
   */
  private void initPageButtons() {
    confirmBtn = new Button(40, 550, "Confirm", MasterUI.secondaryCol);
    switch (mode) {
      case VIEW:
        confirmBtn.setText("Back");
        break;
      case EDIT:
        confirmBtn.setText("Save Changes");
        break;
      default:
        confirmBtn.setText("Confirm");
    }

    confirmBtn.setTab();
    confirmBtn.centerText();
    confirmBtn.setRounded(true);

    add(confirmBtn);
  }

  /**
   * Create and initialise text forms. This method is designed as a loop and not
   * statically for developemental purposes.
   *
   */
  private void initFormContent() {
    String[] lbStrings = { "Topic", "When", "Start Time", "Where" };
    int initialY = contentBox.y;
    for (String lbString : lbStrings) {
      Label label = new Label(contentBox.x, initialY, lbString);
      TextField textfield;
      TextField secondField;
      switch (lbString) {
        case "Topic":
          textfield = new TextField(contentBox.x, initialY + 20);
          textfield.setMaximumLength(25);
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
          textfield.setMaximumLength(5);
          startField = textfield;

          Label timelb = new Label(contentBox.x + textfield.getWidth() + 4, initialY, "End Time");
          secondField = new TextField(contentBox.x + textfield.getWidth() + 4, initialY + 20);
          secondField.setSize(textfield.getWidth(), textfield.getHeight());
          secondField.setMaximumLength(5);
          if (mode == VIEW) {
            Label memberLabel = new Label(400, timelb.getY(), "Participants: ");
            add(memberLabel);
          }
          add(timelb);
          add(secondField);
          endField = secondField;
          break;
        case "Where":
          textfield = new TextField(contentBox.x, initialY + 20);
          textfield.setSize(textfield.getWidth() - 40, textfield.getHeight());
          textfield.setMaximumLength(25);
          locationField = textfield;
          WhereLabel = label;

          dpdwn = new Button(contentBox.x + textfield.getWidth(), initialY + 20, "", MasterUI.lightColAlt);
          dpdwn.setIcon(MasterUI.downIcon);
          dpdwn.setSize(40, 40);
          dpdwn.addActionListener(e -> locationDropdownSelection(textfield));
          if (mode != VIEW)
            add(dpdwn);
          break;
        default:
          textfield = new TextField(contentBox.x, initialY + 20);
          break;
      }
      add(label);
      add(textfield);
      initialY += 70;
      /**
       * When clicking in on a text field, the panel of the date picker (redpanel)
       * closes, if it has been open, so that a user does not have to manually close
       * the panel.
       */
      if (mode != VIEW) {
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
  }

  /**
   * Set properties of event to defaul values. If editEvent exists the event to be
   * edited to filled into form.
   */
  public void setDefaultProperties() {
    if (editEvent == null) {
      titleField.setText("Proxy Networking");
      locationField.setText("Communications department");
      dateField.setText(LocalDate.now().toString());
      startField.setText("09:00");
      endField.setText("10:35");
    } else {
      prioBtnAction(editEvent.getPriority());
      titleField.setText(editEvent.getName());
      locationField.setText(editEvent.getLocation().getName());
      dateField.setText(editEvent.getDate().toString());
      startField.setText(editEvent.getTime().toString());
      endField.setText(FormatUtil.getEndTime(editEvent.getTime(), editEvent.getDurationMinutes()).toString());
      String attachStr = "";
      for (File f : editEvent.getAttachments()) {
        attachStr += f.getName() + " ";
      }

      System.out.println(attachStr);
      attachField.setText(attachStr);
    }

    if (mode == VIEW) {
      TextField[] fields = { titleField, locationField, dateField, startField, endField, attachField, reminderField };
      for (TextField field : fields) {
        field.setBackground(MasterUI.lightCol);
        field.setEditable(false);
        field.setEqualPadding(0);
        field.setFont(MasterUI.robotoFont.deriveFont(Font.BOLD, 15f));
      }
      dateField.setText(FormatUtil.readableDate(LocalDate.parse(dateField.getText())));
      Button[] prioBtns = { loPrioBtn, midPrioBtn, hiPrioBtn };
      for (Button prioBtn : prioBtns) {
        prioBtn.setEnabled(false);
      }

      repaint();
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
      add(participantLabel);
      repaint();
      participantListPosition += 35;
    }
  }

  /**
   * Create and initialise attachment section
   */
  private void drawAttachmentSection() {
    Panel attachpanel = new Panel();
    attachpanel.setBounds(750, 40, 220, 500);
    attachpanel.setBackground(MasterUI.lightCol);
    Label attachLabel = new Label(400, reminderField.getY() + 50, "Attachments (optional)");
    attachField = new TextField(attachLabel.getX(), attachLabel.getY() + 20);
    attachField.setEditable(false);
    Button attachBtn = new Button(attachField.getX() + attachField.getWidth(), attachField.getY(), "",
        MasterUI.lightColAlt);
    attachBtn.setIcon(MasterUI.folderIcon);
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
        File file = chooser.getSelectedFile();
        selectedAttachments.add(file);
        attachField.setText(selectedAttachments.size() + " File(s)");
        addAttachmentCard(attachpanel);
      }
    });

    addAttachmentCard(attachpanel);

    if (mode != VIEW)
      add(attachBtn);
    add(attachLabel);
    add(attachField);
    add(attachpanel);
  }

  /**
   * When the attachment form receives a new file, a card panel respresenting the
   * file is added to the right. The card allows the removal and opening of the
   * file.
   * 
   * @param panel - Panel that holds the file cards
   */
  private void addAttachmentCard(Panel panel) {
    panel.removeAll();
    int y = 0;
    for (File file : selectedAttachments) {
      Panel fcard = new Panel();
      fcard.setBounds(0, y, panel.getWidth(), 60);
      fcard.setBackground(MasterUI.lightCol);
      fcard.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
      Label ficon = new Label(5, 5, "");
      Label fname = new Label(55, 10, file.getName());
      Label fsize = new Label(fname.getX(), fname.getY() + 15, String.valueOf((int) file.length() / 1024) + " KB");
      Button del = new Button(fcard.getWidth() - 25, 10, "");
      Button open = new Button(0, 0, "");
      open.setSize(fcard.getWidth() - 30, fcard.getHeight());
      open.addActionListener(e -> {
        try {
          Desktop.getDesktop().open(file);
        } catch (IOException exp) {
          exp.printStackTrace();
        }
      });
      ficon.setIcon(MasterUI.pdfIcon);
      ficon.setSize(48, 48);
      fname.setFont(MasterUI.robotoFont.deriveFont(14f));
      fsize.setFont(MasterUI.robotoFont.deriveFont(11f));

      del.addActionListener(e -> {
        panel.remove(fcard);
        selectedAttachments.remove(file);
        attachField.setText(selectedAttachments.size() + " File(s)");
        panel.repaint();
      });
      del.setIcon(MasterUI.xIcon);
      del.setSize(15, 13);

      fcard.add(open);
      if (mode != VIEW)
        fcard.add(del);
      fcard.add(ficon);
      fcard.add(fname);
      fcard.add(fsize);

      panel.add(fcard);
      panel.repaint();
      y += fcard.getHeight() + 7;
    }
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
    participantListPosition = userQueryResult.getY();
    if (mode != VIEW)
      participantListPosition += 15;
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

    for (User part : participants) {
      Label partLabel = new Label(400, participantListPosition, part.getUsername());
      if (part.equals(user))
        partLabel.setText(partLabel.getText() + " (Me)");
      partLabel.setIcon(MasterUI.circleUserIcon);
      partLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
      add(partLabel);
      participantListPosition += 35;
    }

    if (mode != VIEW) {
      add(addUserBtn);
      add(searchUserLabel);
      add(searchUserField);
      add(userQueryResult);
    }
  }

  /**
   * Draw priority label and buttons
   */
  private void drawPrioritySection() {
    Label priorityLabel = new Label(40, 100, "Priority");
    loPrioBtn = new Button(40, 120, "LOW", new Color(171, 169, 239));
    midPrioBtn = new Button(140, 120, "MEDIUM", new Color(129, 109, 254));
    hiPrioBtn = new Button(240, 120, "HIGH", MasterUI.accentCol);

    loPrioBtn.addActionListener(e -> prioBtnAction(Priority.LOW));
    midPrioBtn.addActionListener(e -> prioBtnAction(Priority.MEDIUM));
    hiPrioBtn.addActionListener(e -> prioBtnAction(Priority.HIGH));

    add(priorityLabel);
    add(loPrioBtn);
    add(midPrioBtn);
    add(hiPrioBtn);
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
   */
  public void prioBtnAction(Priority prio) {
    Button btn;
    switch (prio) {
      case HIGH:
        btn = hiPrioBtn;
        break;
      case MEDIUM:
        btn = midPrioBtn;
        break;
      case LOW:
        btn = loPrioBtn;
        break;
      default:
        btn = loPrioBtn;
        break;
    }
    styleDefaultPriorityBtns();
    btn.setColor(prio.getColor());
    btn.setText(prio.toString());
    selectedPriority = prio;
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

    add(openDatePicker);
    add(redpanel);
    setComponentZOrder(openDatePicker, 0);
    setComponentZOrder(redpanel, 1);
  }

  /**
   * Validate input form
   * 
   * @param errorMsg - Label to display error message
   * @return Boolean whether form is valid or not
   */
  private boolean validateForm(Label errorMsg, Panel panel) {
    boolean valid = true;
    Border border = BorderFactory.createLineBorder(Color.RED, 1);

    if (selectedPriority == null) {
      errorPriority.setText("(Select Priority)");
      errorPriority.setForeground(Color.RED);
      errorPriority.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorPriority.setText("");
    }

    if (isBlankString(titleField.getText())) {
      titleField.setBorder(border);
      errorTitle.setText("*Title required");
      errorTitle.setForeground(Color.RED);
      errorTitle.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorTitle.setText("");
      titleField.setBorder(BorderFactory.createEmptyBorder());
    }

    if (isBlankString(dateField.getText())) {
      dateField.setBorder(border);
      errorDate.setText("(Select a date)");
      errorDate.setForeground(Color.RED);
      errorDate.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorDate.setText("");
      dateField.setBorder(BorderFactory.createEmptyBorder());
    }

    if (isBlankString(startField.getText()) || !isValidTime(startField.getText())) {
      startField.setBorder(border);
      errorStartTime.setText("(invalid time)");
      errorStartTime.setForeground(Color.RED);
      errorStartTime.setBounds(contentBox.x, contentBox.y + 132, startField.getWidth(), startField.getHeight());
      errorStartTime.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorStartTime.setText("");
      startField.setBorder(BorderFactory.createEmptyBorder());
    }

    if (isBlankString(endField.getText()) || !isValidTime(endField.getText())) {
      endField.setBorder(border);
      errorEndTime.setText("(invalid Time)");
      errorEndTime.setForeground(Color.RED);
      errorEndTime.setBounds(contentBox.x + endField.getWidth() + 4, contentBox.y + 132, endField.getWidth(),
          endField.getHeight());
      errorEndTime.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorEndTime.setText("");
      endField.setBorder(BorderFactory.createEmptyBorder());
    }

    if (isValidTime(startField.getText()) && isValidTime(endField.getText())) {
      if (LocalTime.parse(startField.getText()).isAfter(LocalTime.parse(endField.getText()))) {
        int y = 20;
        startField.setBorder(border);
        endField.setBorder(border);
        errorMsg.setLocation(contentBox.x, contentBox.y + 200);
        errorMsg.setForeground(Color.RED);
        errorMsg.setText("Start Time can't be after End Time");

        WhereLabel.setLocation(contentBox.x, contentBox.y + 210 + y);
        errorLocation.setLocation(contentBox.x + 50, contentBox.y + 210 + y);
        locationField.setLocation(contentBox.x, contentBox.y + 210 + 2 * y);
        dpdwn.setLocation(contentBox.x + locationField.getWidth(), contentBox.y + 210 + 2 * y);
        valid = false;
      } else {
        startField.setBorder(BorderFactory.createEmptyBorder());
        endField.setBorder(BorderFactory.createEmptyBorder());
        errorMsg.setText("");

        WhereLabel.setLocation(contentBox.x, contentBox.y + 210);
        errorLocation.setLocation(contentBox.x + 50, contentBox.y + 210);
        locationField.setLocation(contentBox.x, contentBox.y + 210 + 20);
        dpdwn.setLocation(contentBox.x + locationField.getWidth(), contentBox.y + 210 + 20);
      }
    }

    if (isBlankString(locationField.getText())) {
      locationField.setBorder(border);
      errorLocation.setText("(Location required)");
      errorLocation.setForeground(Color.RED);
      errorLocation.setHorizontalAlignment(SwingConstants.RIGHT);
      valid = false;
    } else {
      errorLocation.setText("");
      locationField.setBorder(BorderFactory.createEmptyBorder());
    }

    if (isBlankString(reminderField.getText())) {
      reminderField.setBorder(border);
      errorReminder.setText("(Select a reminder)");
      errorReminder.setForeground(Color.RED);
      valid = false;
    } else {
      errorReminder.setText("");
      reminderField.setBorder(BorderFactory.createEmptyBorder());
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
    add(errorMsg);

    errorPriority = new Label(contentBox.x + 50, 100, "");
    errorTitle = new Label(contentBox.x + 50, contentBox.y, "");
    errorDate = new Label(contentBox.x + 50, contentBox.y + 70, "");
    errorLocation = new Label(contentBox.x + 50, contentBox.y + 210, "");

    errorStartTime = new Label(contentBox.x + 50, contentBox.y + 140, "");
    errorEndTime = new Label(contentBox.x + 221, contentBox.y + 140, "");
    errorReminder = new Label(614, 100, "");

    add(errorTitle);
    add(errorDate);
    add(errorStartTime);
    add(errorEndTime);
    add(errorLocation);
    add(errorReminder);
    add(errorPriority);

    ActionListener createAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();

        Event event = ViewModelHandler.consumeEventForm(titleField, dateField, startField, endField, locationField,
            participants, selectedPriority, selectedAttachments);
        if (selectedLocation != null && selectedLocation.getName().equals(locationField.getText())) {
          event.setLocation(selectedLocation);
        }
        Panel createMeetingConfirm;
        switch (mode) {
          case CREATE:
            user.createEvent(event);
            createMeetingConfirm = new ScheduleEventConfirm(frame, user, event, CREATE);
            HomeUI.switchPanel(createMeetingConfirm);
            HomeUI.createTab.changeReferencePanel(createMeetingConfirm);
            break;
          case EDIT:
            editEvent.updateEvent(event);
            createMeetingConfirm = new ScheduleEventConfirm(frame, user, event, EDIT);
            HomeUI.switchPanel(HomeUI.dashPanel);
            HomeUI.createPanel = new ScheduleEvent(frame, user, null, ScheduleModes.CREATE);
            HomeUI.createTab.changeReferencePanel(HomeUI.createPanel);
            break;
          case VIEW:
            HomeUI.switchPanel(HomeUI.dashPanel);
            break;
        }

      }
    };

    confirmBtn.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (!validateForm(errorMsg, panel))
          return;
        if (mode == VIEW) {
          HomeUI.switchPanel(HomeUI.dashPanel);
          return;
        }
        HomeUI.confirmDialog(createAction, "Proceed with this event?");
      }
    });
  }
}
