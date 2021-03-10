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

/**
 * The purpose of this class is to create a user-accessible event creation view.
 * This panel contains a form for selection of a event. This view is also used
 * for viewing and editing the event.
 */
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
  private int pcpIconPos = 330;
  private ArrayList<User> participants;
  private ArrayList<File> selectedAttachments;
  private Reminder selectedReminder;
  private Priority selectedPriority;
  private Location selectedLocation;
  private Point cb;
  private JScrollPane lcscroll;
  private JScrollPane rmscroll;
  public static Panel redpanel;
  public TextField titleField;
  public static TextField dateField;
  public TextField startField;
  public TextField endField;
  public TextField priorityField;
  public TextField locationField;
  public TextField reminderField;
  public TextField attachField;
  public JTextArea descField;

  /** Margin between text fields */
  private final int TF_MRGN = 70;

  /** Panel for the above section */
  private Panel PAGE_ONE;
  /** Panel for the below section */
  private Panel PAGE_TWO;
  /** Scroller */
  private JScrollPane scroller;
  /** Base Panel */
  private Panel mainpanel;

  /** Base Frame */
  private JFrame frame;
  /** Current User */
  private User user;
  /** Event which is viewed in mode == EDIT */
  private Event editEvent;
  /** Mode of this Panel, different layouts depending on mode */
  private int mode;

  /**
   * Construct panel of event creator. This is panel is created and placed on the
   * home panel.
   *
   * @param frame     - Main frame containing this panel
   * @param user      - Session logged in user
   * @param editEvent - <code>null</code> if not in edit mode, else event to be
   *                  edited
   * @param mode      - Mode depicting if the view should display, create or edit
   */
  public ScheduleEvent(JFrame frame, User user, Event editEvent, int mode) {
    super(frame);
    this.frame = frame;
    this.user = user;
    this.editEvent = editEvent;
    this.mode = mode;
    mainpanel = new Panel();
    mainpanel.setBackground(MasterUI.lightCol);
    mainpanel.setPreferredSize(new Dimension(getWidth(), 1000));

    scroller = new JScrollPane(mainpanel);
    scroller.setBounds(0, 0, getWidth(), getHeight());
    scroller.getVerticalScrollBar().setUnitIncrement(10);
    scroller.setBorder(BorderFactory.createEmptyBorder());
    scroller.setBackground(MasterUI.lightCol);
    add(scroller);

    cb = new Point(0, TF_MRGN);
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
      selectedReminder = editEvent.getReminder();
      participants = editEvent.getParticipants();
      selectedAttachments = editEvent.getAttachments();
    } else if (mode == VIEW) {
      screenTitle.setText(editEvent.getName());
      screenTitle.setForeground(MasterUI.accentCol);
      selectedReminder = editEvent.getReminder();
      selectedAttachments = editEvent.getAttachments();
      participants = editEvent.getParticipants();
    }

    PAGE_ONE = new Panel();
    PAGE_ONE.setBounds(100, 120, 730, 450);
    PAGE_ONE.setBackground(MasterUI.lightCol);
    mainpanel.add(PAGE_ONE);

    PAGE_TWO = new Panel();
    PAGE_TWO.setBounds(100, 570, 320, 400);
    PAGE_TWO.setBackground(MasterUI.lightCol);
    mainpanel.add(PAGE_TWO);

    initPageOneFormContent();
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

    mainpanel.add(screenTitle);
    MasterUI.setComponentStyles(this, "light");
    MasterUI.setComponentStyles(PAGE_ONE, "light");
    MasterUI.setComponentStyles(PAGE_TWO, "light");
    MasterUI.setComponentStyles(mainpanel, "light");
    setDefaultProperties();
  }

  /**
   * Create and initialise page buttons
   */
  private void initPageButtons() {
    confirmBtn = new Button(titleField.getX() + 520, titleField.getY() + 60, "Confirm",
        MasterUI.secondaryCol);
    switch (mode) {
      case VIEW:
        confirmBtn.setText("Back");
        break;
      case EDIT:
        confirmBtn.setText("Save Changes");
        break;
      default:
        confirmBtn.setText("Next");
    }
    Button cnclBtn = new Button(confirmBtn.getX() - 200, confirmBtn.getY(), "Cancel", MasterUI.primaryColAlt);
    cnclBtn.setTab();
    cnclBtn.centerText();
    cnclBtn.setCornerRadius(Button.ROUND);
    cnclBtn.addActionListener(e -> {
      removeAll();
    });
    PAGE_ONE.add(cnclBtn);

    confirmBtn.setTab();
    confirmBtn.centerText();
    confirmBtn.setCornerRadius(Button.ROUND);

    String pref = "";
    switch (mode) {
      case CREATE: pref = "Create"; break;
      case EDIT: pref = "Edit"; break;
      case VIEW: pref = "View"; break;
    }

    Label schedulehero = new Label(-10, -10, "");
    schedulehero.setIcon(FormatUtil.resizeImageIcon(MasterUI.scheduleFormImage, 0.5f));
    schedulehero.setSize(schedulehero.getIcon().getIconWidth(), schedulehero.getIcon().getIconHeight() - 70);
    Label scheduleheroText = new Label(15, 15, pref + " your event here.");
    scheduleheroText.setHeading();
    scheduleheroText.setFont(MasterUI.bodyFont.deriveFont(Font.BOLD, 28f));
    scheduleheroText.setForeground(Color.WHITE);
    Panel scheduleheroPanel = new Panel();
    scheduleheroPanel.setBounds(confirmBtn.getX() - 190, confirmBtn.getY() - 130, schedulehero.getWidth(), 200);
    scheduleheroPanel.setRounded(true);
    scheduleheroPanel.setBackground(MasterUI.secondaryCol);
    scheduleheroPanel.add(scheduleheroText);
    scheduleheroPanel.add(schedulehero);

    PAGE_ONE.add(confirmBtn);
    PAGE_ONE.add(scheduleheroPanel);
  }

  /**
   * Create and initialise text forms. This method was designed as a loop and not
   * statically for developemental purposes.
   *
   */
  private void initPageOneFormContent() {
    titleField = new TextField(cb.x, cb.y + 20);
    MasterUI.placeFieldLabel(titleField, "Title", PAGE_ONE);

    dateField = new TextField(cb.x, titleField.getY() + TF_MRGN);
    dateField.setSize(dateField.getWidth() - 55, dateField.getHeight());
    MasterUI.placeFieldLabel(dateField, "Date", PAGE_ONE);

    startField = new TextField(cb.x, dateField.getY() + TF_MRGN);
    startField.setSize(titleField.getWidth() / 2, titleField.getHeight());
    MasterUI.placeFieldLabel(startField, "Start time", PAGE_ONE);

    endField = new TextField(cb.x + startField.getWidth() + 5, dateField.getY() + TF_MRGN);
    endField.setSize(startField.getWidth() - 5, startField.getHeight());
    MasterUI.placeFieldLabel(endField, "End time", PAGE_ONE);

    locationField = new TextField(cb.x, endField.getY() + TF_MRGN);
    lc_dpdwn = locationField.appendButton(MasterUI.downIcon);
    lc_dpdwn.addActionListener(e -> locationDropdownSelection(locationField));
    if (mode != VIEW)
      PAGE_ONE.add(lc_dpdwn);
    MasterUI.placeFieldLabel(locationField, "Location", PAGE_ONE);

    ArrayList<TextField> fields = new ArrayList<>(
        Arrays.asList(titleField, dateField, startField, endField, locationField));
    fields.forEach(e -> PAGE_ONE.add(e));

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
          }
          public void focusLost(FocusEvent f) { }
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
      // titleField.setText("Proxy Networking");
      // locationField.setText("Communications department");
      dateField.setText(LocalDate.now().toString());
      startField.setText("09:00");
      endField.setText("10:35");
    } else {
      prioBtnAction(editEvent.getPriority());
      titleField.setText(editEvent.getName());
      locationField.setText(editEvent.getLocation().getName());
      dateField.setText(editEvent.getDate().toString());
      startField.setText(editEvent.getTime().toString());
      endField.setText(FormatUtil.getEndTime(editEvent).toString());
      descField.setText(editEvent.getDescription());
      reminderField.setText(editEvent.getReminder().toString());
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
        prioBtn.setForeground(Color.WHITE);
      }

      repaint();
    }
  }

  /**
   * Draw priority label and buttons
   */
  private void drawPrioritySection() {
    Label priorityLabel = new Label(0, -5, "Priority");
    priorityField = new TextField(0, 20);
    loPrioBtn = new Button(0, 20, "LOW", new Color(171, 169, 239));
    midPrioBtn = new Button(105, 20, "MEDIUM", new Color(129, 109, 254));
    hiPrioBtn = new Button(210, 20, "HIGH", MasterUI.accentCol);

    loPrioBtn.addActionListener(e -> prioBtnAction(Priority.LOW));
    midPrioBtn.addActionListener(e -> prioBtnAction(Priority.MEDIUM));
    hiPrioBtn.addActionListener(e -> prioBtnAction(Priority.HIGH));

    PAGE_ONE.add(priorityLabel);
    PAGE_ONE.add(loPrioBtn);
    PAGE_ONE.add(midPrioBtn);
    PAGE_ONE.add(hiPrioBtn);
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
    Button openDatePicker = new Button(dateField.getX() + dateField.getWidth() - 10, dateField.getY(), "",
        MasterUI.accentCol);
    openDatePicker.setIcon(MasterUI.calendarIcon);
    openDatePicker.setSize(65, 40);

    redpanel = new CalendarPanel(frame, 37, true, null);
    redpanel.setSize(0, 0);
    redpanel.setBackground(MasterUI.lightCol);
    redpanel.setLayout(null);
    redpanel.setBorder(BorderFactory.createLineBorder(Color.decode("#e8e8e8"), 1));
    ((CalendarPanel) redpanel).stripComponents();
    redpanel.isActive = false;
    openDatePicker.addActionListener(e -> {
      if (redpanel.isActive) {
        redpanel.setSize(0, 0);
        redpanel.isActive = false;
      } else {
        // redpanel.setBounds(openDatePicker.getX() + PAGE_ONE.getX(), openDatePicker.getY() + PAGE_ONE.getY(), 300, 310);
        redpanel.setBounds(openDatePicker.getX(), openDatePicker.getY(), 280, 290);
        redpanel.revalidate();
        redpanel.isActive = true;
      }
    });

    // mainpanel.add(redpanel);
    // mainpanel.setComponentZOrder(redpanel, 0);
    PAGE_ONE.add(openDatePicker);
    PAGE_ONE.add(redpanel);
    PAGE_ONE.setComponentZOrder(redpanel, 1);
    PAGE_ONE.setComponentZOrder(openDatePicker, 0);
  }

  /**
   * Build dropdown options as to which custom location to select
   *
   * @param textfield - Location TextField object to reference
   */
  @SuppressWarnings("unchecked")
  private void locationDropdownSelection(TextField textfield) {
    Panel panel = PAGE_ONE;
    List<Location> locations = user.getLocations();
    ActionListener action = e -> {
      Location location = ((DataButton<Location>) e.getSource()).getData();
      PAGE_ONE.remove(lcscroll);
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
    reminderField = new TextField(0, 25);
    MasterUI.placeFieldLabel(reminderField, "Remind me before event", PAGE_TWO);
    if(mode == CREATE) {
      reminderField.setText(Reminder.NONE.toString());
      selectedReminder = Reminder.NONE;
    } else {
      reminderField.setText(selectedReminder.toString());
    }
    reminderField.setEditable(false);
    Button dpdwn = reminderField.appendButton(MasterUI.downIcon);
    dpdwn.addActionListener(e -> reminderDropdownSelection());

    if (mode != VIEW)
      PAGE_TWO.add(dpdwn);
    PAGE_TWO.add(reminderField);
  }

  /**
   * Build dropdown options as to when to remind the user of their event
   */
  @SuppressWarnings("unchecked")
  private void reminderDropdownSelection() {
    List<Reminder> reminders = new ArrayList<>(EnumSet.allOf(Reminder.class));
    ActionListener action = e -> {
      Reminder reminder = ((DataButton<Reminder>) e.getSource()).getData();
      descField.requestFocus();
      PAGE_TWO.remove(rmscroll);
      rmscroll = null;
      selectedReminder = reminder;
    };
    Component[] comps = reminderField.setDropdown(reminders, rmscroll, PAGE_TWO, action, 3);
    rmscroll = (JScrollPane) comps[0];
    PAGE_TWO = (Panel) comps[1];
  }

  /**
   * Create and initialise attachment section
   */
  private void drawAttachmentSection() {
    Panel attachpanel = new Panel();
    attachpanel.setBackground(MasterUI.lightCol);
    attachField = new TextField(0, reminderField.getY() + TF_MRGN);
    attachpanel.setBounds(430, 600, 220, 200);
    if (mode == VIEW && !editEvent.getAttachments().isEmpty()) {
      MasterUI.placeFieldLabel(attachField, "Attachments", PAGE_TWO);
    } else if (mode == CREATE || mode == EDIT) {
      MasterUI.placeFieldLabel(attachField, "Attachments (optional)", PAGE_TWO);
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
      PAGE_TWO.add(attachBtn);
    PAGE_TWO.add(attachField);
    mainpanel.add(attachpanel);
    // attachpanel.setBackground(Color.RED);
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
      del.setColor(MasterUI.lightCol);
      Button open = new Button(0, 0, "");
      open.setBlank(true);
      open.setSize(fcard.getWidth() - 30, fcard.getHeight());
      open.addActionListener(e -> {
        try {
          Desktop.getDesktop().open(file);
        } catch (IOException exp) {
          exp.printStackTrace();
        }
      });
      if (file.getName().toLowerCase().endsWith(".pdf")) {
        ficon.setIcon(FormatUtil.resizeImageIcon(MasterUI.pdfIcon, 0.9f));
      }
      if (file.getName().toLowerCase().endsWith(".jpg")) {
        ficon.setIcon(FormatUtil.resizeImageIcon(MasterUI.jpgIcon, 0.9f));
      }
      if (file.getName().toLowerCase().endsWith(".png")) {
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
   * Create and initialise add-participant section. User icons are set
   * pre-emptively if they are already in the event participants list.
   */
  private void drawParticipantSection() {
    searchUserField = new TextField(0, attachField.getY() + TF_MRGN);
    if (mode == CREATE) {
      MasterUI.placeFieldLabel(searchUserField, "People to invite", PAGE_TWO);
    }

    addUserBtn = searchUserField.appendButton(MasterUI.addUserIcon);
    userQueryResult = new Label(0, 0, "");
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

    Label pcpIconLabel = new Label(pcpIconPos, 270, "Participants");
    PAGE_ONE.add(pcpIconLabel);

    for (User pcp : participants) {
      placeParticpantIcon(pcp);
    }

    if (mode != VIEW) {
      PAGE_TWO.add(addUserBtn);
      PAGE_TWO.add(searchUserField);
      PAGE_TWO.add(userQueryResult);
    }
  }

  /**
   * Search a user to add them to participants. If the database search fails, an
   * error label indicates the query failure.
   */
  public void searchParticipant() {
    User user = ViewModelHandler.searchUser(searchUserField, mainpanel, userQueryResult);
    if (user == null) {
    } else {
      if (participants.contains(user)) {
        userQueryResult.setText("User already added");
        return;
      }
      participants.add(user);
      placeParticpantIcon(user);
      repaint();
    }
  }

  /**
   * Place icon of particpant on schedule form. Partcipants added are visible
   * here, and can be removed from here.
   *
   * @param pcp - Participant user
   */
  public void placeParticpantIcon(User pcp) {
    Label pcpIcon = new Label(pcpIconPos, 300, "");
    pcpIcon.fillIcon(FormatUtil.resizeImageIcon(pcp.getAvatar(), 0.5f));
    pcpIcon.setVerticalTextPosition(SwingConstants.CENTER);
    pcpIcon.setToolTipText(pcp.getUsername());
    PAGE_ONE.add(pcpIcon);
    pcpIconPos += 40;
  }

  /**
   * Draw description text area for event description
   */
  private void drawDesciptionSection() {
    descField = new JTextArea();
    descField.setBackground(MasterUI.lightColAlt);
    descField.setFont(MasterUI.robotoFont);
    descField.setForeground(MasterUI.primaryColAlt);
    descField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    descField.setBounds(0, searchUserField.getY() + TF_MRGN, searchUserField.getWidth() + 40, 110);
    descField.setLineWrap(true);

    if (mode == CREATE || mode == EDIT) {
      MasterUI.placeFieldLabel(descField, "Description (optional)", PAGE_TWO);
    }
    if (mode == VIEW && !descField.getText().isBlank()) {
      MasterUI.placeFieldLabel(descField, "Description", PAGE_TWO);
      descField.setBorder(BorderFactory.createEmptyBorder());
      descField.setBackground(MasterUI.lightCol);
    }

    PAGE_TWO.add(descField);
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
      PAGE_ONE.add(error);
    }

    HashMap<String, TextField> FieldMap = new HashMap<String, TextField>();
    FieldMap.put("titleField", titleField);
    FieldMap.put("dateField", dateField);
    FieldMap.put("startField", startField);
    FieldMap.put("endField", endField);
    FieldMap.put("priorityField", priorityField);
    FieldMap.put("locationField", locationField);
    FieldMap.put("reminderField", reminderField);

/*
    System.out.println("in processConfirm\n"
            + titleField.getText() + "\n"
            + dateField.getText() + "\n"
            + startField.getText() + "\n"
            + endField.getText() + "\n"
            + locationField.getText() + "\n"
            + reminderField.getText() + "\n"
            + selectedReminder.toString() + "\n"
    );
 */

    ActionListener createAction = new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        panel.removeAll();

        /*
        System.out.println("in actionperformed\n"
                + titleField.getText() + "\n"
                + dateField.getText() + "\n"
                + startField.getText() + "\n"
                + endField.getText() + "\n"
                + locationField.getText() + "\n"
                + selectedPriority.toString() + "\n"
                + reminderField.getText() + "\n"
                + selectedReminder.toString() + "\n");
         */

        Event event = ViewModelHandler.consumeEventForm(FieldMap,
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
