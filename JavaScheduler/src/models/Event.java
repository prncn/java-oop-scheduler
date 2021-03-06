package models;

import controllers.DatabaseAPI;
import controllers.EmailHandler;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

/**
 * The Event Class represents the events which a user creates
 */
public class Event implements Comparable<Event> {
  /** id of event */
  private int id;
  /** Name of event */
  private String name;
  /** Date of event */
  private LocalDate date;
  /** Start time of event */
  private LocalTime time;
  /** Duration in minutes of event */
  private int durationMinutes;
  /** Location object of event */
  private Location location;
  /** Host id of event */
  private int hostId;
  /** List of participants of an event */
  private ArrayList<User> participants;
  /** Priority of event */
  private Priority priority;
  /** List of attachment of an event */
  private ArrayList<File> attachments;
  /** Reminder of an event */
  private Reminder reminder;
  /** Description of event */
  private String description = "";

  /**
   * Constructor for creating event from users input
   * @param name - name of the event
   * @param date - date of the event
   * @param time - starting time of event
   * @param durationMinutes - duration of event in minutes
   * @param location - location of event
   * @param participants - list of participants
   * @param reminder - selected reminder for the event
   * @param priority - selected priority for the event
   * @param attachments - list of attachements
   * @param description - description of the event
   */
  public Event(String name, LocalDate date, LocalTime time, int durationMinutes, Location location,
      ArrayList<User> participants, Reminder reminder, Priority priority, ArrayList<File> attachments,
      String description) {
    this.name = name;
    this.date = date;
    this.time = time;
    this.durationMinutes = durationMinutes;
    this.location = location;
    this.participants = participants;
    this.reminder = reminder;
    this.priority = priority;
    this.attachments = attachments;
    this.description = description;
  }

  /**
   * Constructor for fetching event from database and creating model class from it
   * @param eventId - id of event
   * @param name - name of the event
   * @param date - date of the event
   * @param time - starting time of event
   * @param duration - duration of event in minutes
   * @param location - location of event
   * @param participants - list of participants
   * @param reminder - selected reminder for the event
   * @param priority - selected priority for the event
   * @param description - description of the event
   */
  public Event(int eventId, String name, String description, int duration, LocalDate date, LocalTime time,
      Location location, Priority priority, Reminder reminder, ArrayList<User> participants) {
    this.id = eventId;
    this.name = name;
    this.description = description;
    this.durationMinutes = duration;
    this.date = date;
    this.time = time;
    this.location = location;
    this.priority = priority;
    this.reminder = reminder;
    this.participants = participants;
  }

  /**
   * Copy constructor
   * 
   * @param other - Event to be copied from
   */
  public Event(Event other) {
    name = other.name;
    date = other.date;
    time = other.time;
    durationMinutes = other.durationMinutes;
    location = other.location;
    hostId = other.hostId;
    participants = other.participants;
    priority = other.priority;
    attachments = other.attachments;
    reminder = other.reminder;
    description = other.description;
  }

  /**
   * Overwrite all attributes of other event into current event. Used to update
   * edited event.
   * 
   * @param other - Event to be copied from
   */
  public void updateEvent(Event other) {
    name = other.name;
    date = other.date;
    time = other.time;
    durationMinutes = other.durationMinutes;
    location = other.location;
    participants = other.participants;
    priority = other.priority;
    attachments = other.attachments;
    reminder = other.reminder;
    description = other.description;

    DatabaseAPI.editEvent(this);
    if (!getParticipants().isEmpty()) {
      EmailHandler.sendEventMail(this, Status.EDITED);
    }
  }

  /**
   * Get event ID
   * 
   * @return Event ID
   */
  public int getId() {
    return id;
  }

  /**
   * Set event ID
   * 
   * @param id - Event ID
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * Get name
   * 
   * @return Title of event
   */
  public String getName() {
    return name;
  }

  /**
   * Set name
   * 
   * @param name - Title of event
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Get Priority
   * 
   * @return Enum level of priority
   */
  public Priority getPriority() {
    return this.priority;
  }

  /**
   * Set Priority
   * 
   * @param level - Enum of priority
   */
  public void setPriority(Priority level) {
    this.priority = level;
  }

  /**
   * Get reminder
   * 
   * @return Enum time of reminder
   */
  public Reminder getReminder() {
    return reminder;
  }

  /**
   * Set reminder
   * 
   * @param reminder Enum time of reminder
   */
  public void setReminder(Reminder reminder) {
    this.reminder = reminder;
  }

  /**
   * Get date of event
   * 
   * @return - LocalDate object of event
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * Set date of event
   * 
   * @param date - LocalDate object of event
   */
  public void setDate(LocalDate date) {
    this.date = date;
  }

  /**
   * Get starting time of event
   * 
   * @return LocalTime object of event
   */
  public LocalTime getTime() {
    return time;
  }

  /**
   * Set starting time of event
   * 
   * @param time LocalTime object of event
   */
  public void setTime(LocalTime time) {
    this.time = time;
  }

  /**
   * Get duration of event in minutes
   * 
   * @return - Integer of duration
   */
  public int getDurationMinutes() {
    return durationMinutes;
  }

  /**
   * Set duration of event in minutes
   * 
   * @param durationMinutes - Integer of duration
   */
  public void setDurationMinutes(int durationMinutes) {
    this.durationMinutes = durationMinutes;
  }

  /**
   * Get description
   * 
   * @return Description of event
   */
  public String getDescription() {
    return description;
  }

  /**
   * Set description
   * 
   * @param description - Description of event
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Set location
   * 
   * @return - Location of event
   */
  public Location getLocation() {
    return location;
  }

  /**
   * Set location
   * 
   * @param location - Location of event
   */
  public void setLocation(Location location) {
    this.location = location;
  }

  /**
   * Get host
   * 
   * @return - User host
   */
  public int getHostId() {
    return hostId;
  }

  /**
   * Set host
   * 
   * @param hostId - User to be set to host
   */
  public void setHostId(int hostId) {
    this.hostId = hostId;
  }

  /**
   * Check if event is in the past
   * 
   * @return true if it was in the past, else false.
   */
  public boolean hasPassed() {
    return date.isBefore(LocalDate.now());
  }

  /**
   * Get attachments
   * 
   * @return attachments list
   */
  public ArrayList<File> getAttachments() {
    return attachments;
  }

  /**
   * Set attachments
   * 
   * @param attachments attachments list
   */
  public void setAttachments(ArrayList<File> attachments) {
    this.attachments = attachments;
  }

  /**
   * Get participants as User Objects
   * 
   * @return participants
   */
  public ArrayList<User> getParticipants() {
    return participants;
  }

  /**
   * Set participants
   * 
   * @param participants - List of participants
   */
  public void setParticipants(ArrayList<User> participants) {
    this.participants = participants;
  }

  /**
   * todo
   *
   * @param user - User to be added
   * @return <code>true</code> on successful addition
   */
  public boolean addParticipant(User user) {
    return false;
  }

  /**
   * Remove participants
   *
   * @param user - User to be removed
   * @return <code>true</code> on successful removal
   */
  public boolean removeParticipant(User user) {
    if(participants.contains(user)) {
      participants.remove(user);
      return true;
    } else
      return false;
  }

  /**
   * Get a list of all participants' full names as text
   * 
   * @return String of participant names
   */
  public String participantsToString() {
    String list = "";
    for (User participant : getParticipants()) {
      list += participant.getFirstname() + " " + participant.getLastname() + "<br>";
    }
    return "<html> " + list + "<html>";
  }

  /**
   * Compare the dates of two Event objects
   *
   * @param other - event that is compared
   * @return negative integer if date is before, positive integer if date is after
   */
  @Override
  public int compareTo(Event other) {
    if (getDate() == null || other.getDate() == null) {
      return 0;
    }
    return getDate().compareTo(other.getDate());
  }

}
