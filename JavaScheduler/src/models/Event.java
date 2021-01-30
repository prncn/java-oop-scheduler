package models;

import controllers.DatabaseAPI;
import controllers.EmailHandler;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Event implements Comparable<Event> {
  private int id;
  private String name;
  private LocalDate date;
  private LocalTime time;
  private int durationMinutes;
  private Location location;
  private int hostId;
  private ArrayList<User> participants;
  private Priority priority;
  private ArrayList<File> attachments;
  private Reminder reminder;
  private String description = "";

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

  public Event(int eventId, String name, String description, int duration,
               LocalDate date, LocalTime time, Location location,
               Priority priority, Reminder reminder,
               ArrayList<User> participants, ArrayList<File> attachments) {
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
    this.attachments = attachments;
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
    EmailHandler.sendEventMail(this, Status.EDITED);
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
   * @return
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
  public void setParticipants(ArrayList<Integer> participants) {
    this.participants = this.participants;
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
   * todo
   *
   * @param user - User to be removed
   * @return <code>true</code> on successful removal
   */
  public boolean removeParticipant(User user) {
    return false;
  }

  @Override
  public int compareTo(Event other) {
    if (getDate() == null || other.getDate() == null) {
      return 0;
    }
    return getDate().compareTo(other.getDate());
  }

  public String participantsToString() {
    String list ="";
    for(User participant : getParticipants()){
      list += participant.getFirstname() + " " + participant.getLastname() + "<br>";
    }
    return "<html> " + list + "<html>";
  }
}
