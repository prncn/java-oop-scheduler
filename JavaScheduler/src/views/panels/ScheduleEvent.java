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
import views.components.*;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class ScheduleEvent extends Panel implements ScheduleModes {

  private static final long serialVersionUID = 1L;
  private Button addUserBtn;
  private Button confirmBtn;
  private Button hiPrioBtn;
  private Button midPrioBtn;
  private Button loPrioBtn;
  private Button lc_dpdwn;
  private TextField searchUserField;
  private Label userQueryResult;
  private int participantListPosition;
  private ArrayList<User> participants;
  private ArrayList<File> selectedAttachments;
  private Reminder selectedReminder;
  private Priority selectedPriority;
  private Location selectedLocation;
  private Point cb;
  private JScrollPane lcscroll;
  private JScrollPane rmscroll;
  public static Panel redpanel;
  public static TextField titleField;
  public static TextField dateField;
  public static TextField startField;
  public static TextField endField;
  public static TextField priorityField;
  public static TextField locationField;
  public static TextField reminderField;
  public static TextField attachField;
  public static JTextArea descField;

  /** Margin between text fields */
  final int TF_MRGN = 70;

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

    cb = new Point(40, 170);
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

    initLeftFormContent();
    if (mode != VIEW) {
      initDatePicker();
    }
    initPageButtons();
    drawPrioritySection();
    drawReminderSection();
    drawAttachmentSection();
    drawParticipantSection();
    drawDesciptionSection();
    processConfirm();

    add(screenTitle);
    MasterUI.setComponentStyles(this, "light");
    setDefaultProperties();
  }

  /**
   * Build dropdown options as to when to remind the user of their event
   */
  @SuppressWarnings("unchecked")
  private void reminderDropdownSelection() {
    List<Reminder> reminders = new ArrayList<>(EnumSet.allOf(Reminder.class));
    Panel panel = this;
    ActionListener action = e -> {
      Reminder reminder = ((DataButton<Reminder>) e.getSource()).getData();
      descField.requestFocus();
      remove(rmscroll);
      rmscroll = null;
      selectedReminder = reminder;
    };
    Component[] comps = reminderField.setDropdown(reminders, rmscroll, panel, action, 3);
    rmscroll = (JScrollPane) comps[0];
    panel = (Panel) comps[1];
  }

  /**
   * Build dropdown options as to which custom location to select
   * 
   * @param textfield - Location TextField object to reference
   */
  @SuppressWarnings("unchecked")
  private void locationDropdownSelection(TextField textfield) {
    Panel panel = this;
    List<Location> locations = user.getLocations();
    ActionListener action = e -> {
      Location location = ((DataButton<Location>) e.getSource()).getData();
      remove(lcscroll);
      lcscroll = null;
      selectedLocation = location;
    };
    Component[] comps = locationField.setDropdown(locations, lcscroll, panel, action);
    lcscroll = (JScrollPane) comps[0];
    panel = (Panel) comps[1];
  }

  /**
   * Draw reminder section section that then prompts drop down options
   */
  private void drawReminderSection() {
    reminderField = new TextField(400, 120);
    MasterUI.placeFieldLabel(reminderField, "Remind me before event", this);
    reminderField.setText(Reminder.NONE.toString());
    selectedReminder = Reminder.NONE;
    reminderField.setEditable(false);
    Button dpdwn = reminderField.appendButton(MasterUI.downIcon);
    dpdwn.addActionListener(e -> reminderDropdownSelection());

    if (mode != VIEW)
      add(dpdwn);
    add(reminderField);
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
    confirmBtn.setCornerRadius(Button.ROUND);

    add(confirmBtn);
  }

  /**
   * Create and initialise text forms. This method was designed as a loop and not
   * statically for developemental purposes.
   *
   */
  private void initLeftFormContent() {
    titleField = new TextField(cb.x, cb.y + 20);
    MasterUI.placeFieldLabel(titleField, "Title", this);

    dateField = new TextField(cb.x, titleField.getY() + TF_MRGN);
    dateField.setSize(dateField.getWidth() - 60, dateField.getHeight());
    MasterUI.placeFieldLabel(dateField, "Date", this);

    startField = new TextField(cb.x, dateField.getY() + TF_MRGN);
    startField.setSize(titleField.getWidth() / 2, titleField.getHeight());
    MasterUI.placeFieldLabel(startField, "Start time", this);

    endField = new TextField(cb.x + startField.getWidth() + 5, dateField.getY() + TF_MRGN);
    endField.setSize(startField.getWidth() - 5, startField.getHeight());
    MasterUI.placeFieldLabel(endField, "End time", this);

    locationField = new TextField(cb.x, endField.getY() + TF_MRGN);
    locationField.setSize(locationField.getWidth() - 40, dateField.getHeight());
    lc_dpdwn = locationField.appendButton(MasterUI.downIcon);
    lc_dpdwn.addActionListener(e -> locationDropdownSelection(locationField));
    if (mode != VIEW ) add(lc_dpdwn);
    MasterUI.placeFieldLabel(locationField, "Location", this);

    ArrayList<TextField> fields = new ArrayList<>(
        Arrays.asList(titleField, dateField, startField, endField, locationField));
    fields.forEach(e -> add(e));

    /**
     * When clicking in on a text field, the panel of the date picker (redpanel)
     * closes, if it has been open, so that a user does not have to manually close
     * the panel.
     */
    fields.forEach(e -> {
      if (mode != VIEW) {
        e.addFocusListener(new FocusListener() {
          public void focusGained(FocusEvent f) {
            redpanel.setSize(0, 0);
            redpanel.isActive = false;
            e.setText("");
          }
          public void focusLost(FocusEvent f) {}
        });
      }
    });

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
      descField.setText(editEvent.getDescription());
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
        field.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 14f));
      }
      descField.setBackground(MasterUI.lightCol);
      descField.setEditable(false);
      descField.setBorder(BorderFactory.createEmptyBorder());
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
      Label participantLabel = new Label(750, participantListPosition, "");
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
    attachpanel.setBounds(750, 40, 220, 200);
    attachpanel.setBackground(MasterUI.lightCol);
    attachField = new TextField(400, reminderField.getY() + TF_MRGN);
    if (mode == VIEW) {
      MasterUI.placeFieldLabel(attachField, "Attachments", this);
    } else {
      MasterUI.placeFieldLabel(attachField, "Attachments (optional)", this);
    }
    attachField.setEditable(false);
    Button attachBtn = attachField.appendButton(MasterUI.folderIcon);
    attachBtn.setDark(false);
    attachBtn.setForeground(MasterUI.accentCol);
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
      if(file.getName().toLowerCase().endsWith(".pdf")) {
        ficon.setIcon(FormatUtil.resizeImageIcon(MasterUI.pdfIcon, 0.9f));
      }
      if(file.getName().toLowerCase().endsWith(".jpg")) {
        ficon.setIcon(FormatUtil.resizeImageIcon(MasterUI.jpgIcon, 0.9f));
      }
      if(file.getName().toLowerCase().endsWith(".png")) {
        ficon.setIcon(FormatUtil.resizeImageIcon(MasterUI.pngIcon, 0.9f));
      }
      ficon.setSize(48, 48);
      ficon.setHorizontalAlignment(SwingConstants.CENTER);
      fname.setSize(200, 25);
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
    searchUserField = new TextField(400, 260);
    MasterUI.placeFieldLabel(searchUserField, "People to invite", this);

    addUserBtn = searchUserField.appendButton(MasterUI.addUserIcon);
    userQueryResult = new Label(0, 0, "");
    participantListPosition = searchUserField.getY();
    addUserBtn.addActionListener(e -> searchParticipant());

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
      Label partLabel = new Label(750, participantListPosition, part.getUsername());
      if (part.equals(user))
        partLabel.setText(partLabel.getText() + " (Me)");
      partLabel.setIcon(MasterUI.circleUserIcon);
      partLabel.setVerticalTextPosition(SwingConstants.CENTER);
      add(partLabel);
      participantListPosition += 35;
    }

    if (mode != VIEW) {
      add(addUserBtn);
      add(searchUserField);
      add(userQueryResult);
    }
  }

  /**
   * Draw priority label and buttons
   */
  private void drawPrioritySection() {
    Label priorityLabel = new Label(40, 95, "Priority");
    priorityField = new TextField(40, 120);
    loPrioBtn = new Button(40, 120, "LOW", new Color(171, 169, 239));
    midPrioBtn = new Button(145, 120, "MEDIUM", new Color(129, 109, 254));
    hiPrioBtn = new Button(250, 120, "HIGH", MasterUI.accentCol);

    loPrioBtn.addActionListener(e -> prioBtnAction(Priority.LOW));
    midPrioBtn.addActionListener(e -> prioBtnAction(Priority.MEDIUM));
    hiPrioBtn.addActionListener(e -> prioBtnAction(Priority.HIGH));

    add(priorityLabel);
    add(loPrioBtn);
    add(midPrioBtn);
    add(hiPrioBtn);
  }

  private void drawDesciptionSection() {
    descField = new JTextArea();
    descField.setBackground(MasterUI.lightColAlt);
    descField.setFont(MasterUI.robotoFont);
    descField.setForeground(MasterUI.primaryColAlt);
    descField.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
    descField.setBounds(400, 330, 340, 110);
    descField.setLineWrap(true);

    Label descLabel = new Label(400, descField.getY() - 25, "Description (optional)");
    if (mode == VIEW)
      descLabel.setText("Description");

    add(descLabel);
    add(descField);
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
    priorityField.setText("TRUE");
    selectedPriority = prio;
  }

  /**
   * Create and initialise date picker
   */
  private void initDatePicker() {
    Button openDatePicker = new Button(285, dateField.getY(), "", MasterUI.accentCol);
    openDatePicker.setIcon(MasterUI.calendarIcon);
    openDatePicker.setSize(65, 40);

    redpanel = new CalendarPanel(frame, 40, true, null);
    redpanel.setSize(0, 0);
    redpanel.setBackground(MasterUI.lightCol);
    redpanel.setLayout(null);
    ((CalendarPanel) redpanel).stripComponents();
    redpanel.isActive = false;
    openDatePicker.addActionListener(e -> {
      if (redpanel.isActive) {
        redpanel.setSize(0, 0);
        redpanel.isActive = false;
        add(lc_dpdwn);
      } else {
        redpanel.setBounds(openDatePicker.getX(), openDatePicker.getY(), 300, 310);
        redpanel.isActive = true;
        remove(lc_dpdwn);
      }
    });

    add(openDatePicker);
    add(redpanel);
    setComponentZOrder(openDatePicker, 0);
    setComponentZOrder(redpanel, 1);
  }

  /**
   * Confirm creation form. Feed form data to new meeting object and proceed to
   * success screen.
   */
  private void processConfirm() {
    Panel panel = this;
    Label errorPriority = priorityField.createErrorLabel("Priority");
    Label errorTitle = titleField.createErrorLabel("Title");
    Label errorDate = dateField.createErrorLabel("Date");
    Label errorLocation = locationField.createErrorLabel("Location");
    Label errorStartTime = startField.createErrorLabel("Start Time");
    Label errorEndTime = endField.createErrorLabel("End Time");
    Label errorReminder = reminderField.createErrorLabel("Reminder");

    Label[] errorLabels = { errorPriority, errorTitle, errorDate, errorLocation, errorStartTime, errorEndTime,
        errorReminder };
    for (Label error : errorLabels) {
      add(error);
    }

    HashMap<String, TextField> FieldMap = new HashMap<String, TextField>();
    FieldMap.put("titleField", titleField);
    FieldMap.put("dateField", dateField);
    FieldMap.put("startField", startField);
    FieldMap.put("endField", endField);
    FieldMap.put("priorityField", priorityField);
    FieldMap.put("locationField", locationField);
    FieldMap.put("reminderField", reminderField);

    ActionListener createAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();

        Event event = ViewModelHandler.consumeEventForm(titleField, dateField, startField, endField, locationField,
            participants, selectedReminder, selectedPriority, selectedAttachments, descField);
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

    confirmBtn.addActionListener(e -> {
      if (!ViewModelHandler.validateForm(FieldMap, selectedPriority))
        return;
      if (mode == VIEW) {
        HomeUI.switchPanel(HomeUI.dashPanel);
        return;
      }
      HomeUI.confirmDialog(createAction, "Proceed with this event?");
    });
  }
}
