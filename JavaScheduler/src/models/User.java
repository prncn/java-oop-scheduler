package models;

import controllers.DatabaseAPI;

import java.util.ArrayList;
import java.util.UUID;

public class User {

  private int id;
  private String username;
  private String firstname;
  private String lastname;
  private String password;
  private String email;
  private ArrayList<Event> events = new ArrayList<Event>();
  private ArrayList<Location> locations = new ArrayList<Location>();
  private Boolean isAdmin;

  /**
   * Constructor for fetching user from database and creating model class from it
   */
  public User(int id, String username, String firstname, String lastname, String email,
      ArrayList<Event> acceptEvents, ArrayList<Location> customLocations) {
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.events = acceptEvents;
    this.locations = customLocations;
    this.isAdmin = false;
  }

  /**
   * Constructor after account creation and before storing to database
   */
  public User(String username, String password, String email) {

    this.username = username;
    this.firstname = "";
    this.lastname = "";
    this.password = password;
    this.email = email;
    this.events = new ArrayList<Event>();
    this.locations = new ArrayList<Location>();
    this.isAdmin = false;
  }

  /**
   * Copy constructor
   * 
   * @param other - Other user to be copied from
   */
  public User(User other) {
    this.id = other.id;
    this.username = other.username;
    this.firstname = other.firstname;
    this.lastname = other.lastname;
    this.password = other.password;
    this.email = other.email;
    this.events = other.events;
    this.locations = other.locations;
    this.isAdmin = other.isAdmin;
  }

  /**
   * Set self to event host and add event
   * 
   * @param event - Newly created event
   */
  public void createEvent(Event event) {
    event.setHost(this);
    addEvent(event);

    for (User participant : event.getParticipants()) {
      if (participant == this) continue;
      participant.addEvent(event);
    }
    DatabaseAPI.createEvent(event);
  }

  /**
   * Add new event to event list
   * 
   * @param event - Newly created event
   */
  private void addEvent(Event event) {
    events.add(event);
  }

  /**
   * Remove event from user
   * 
   * @param event - Event to be removed
   */
  public void removeEvent(Event event) {
    events.remove(event);
  }

  /**
   * Get accepted events
   * 
   * @return List of accepted events from user
   */
  public ArrayList<Event> getAcceptedEvents() {
    return events;
  }

  /**
   * Get user ID
   *
   * @return String user ID
   */
  public int getId() {
    return this.id;
  }

  /**
   * Get username
   * 
   * @return String username
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Get user password
   * 
   * @return String user password (encrypted)
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Get user email
   * 
   * @return String user email
   */
  public String getEmail() {
    return this.email;
  }

  public void setId(int id) {
    this.id = id;
  }

  /**
   * Set username
   * 
   * @param username - String username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Set Admin
   * 
   * @param admin - Boolean isAdmin
   */
  public void setAdmin(Boolean admin) {
    isAdmin = admin;
  }

  /**
   * Set user password
   * 
   * @param password - String password (encrypted)
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Set user email
   * 
   * @param email - String email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Get custom locations
   * 
   * @return locations
   */
  public ArrayList<Location> getLocations() {
    return locations;
  }

  /**
   * Set locations
   * 
   * @param locations
   */
  public void setLocations(ArrayList<Location> locations) {
    this.locations = locations;
  }

  /**
   * Add single location
   * 
   * @param location - Location
   */
  public void addLocation(Location location) {
    locations.add(location);
  }

  /**
   * Get first name
   * 
   * @return firstname
   */
  public String getFirstname() {
    return firstname;
  }

  /**
   * Set first name
   * 
   * @param surname
   */
  public void setFirstname(String surname) {
    this.firstname = surname;
  }

  /**
   * Get last name
   * 
   * @return last name
   */
  public String getLastname() {
    return lastname;
  }

  /**
   * Set last name
   * 
   * @param name
   */
  public void setLastname(String name) {
    this.lastname = name;
  }

  /**
   * Set accepted events
   * 
   * @param events
   */
  public void setEvents(ArrayList<Event> events) {
    this.events = events;
  }

  /**
   * Generate a random UUID for a newly created user account
   * 
   * @return standard UUID with dashes removed
   */
  public static String generateUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  @Override
  public boolean equals(Object other) {
    User that = (User) other;
    return this.id == that.id;
  }

  /**
   * todo
   *
   * @param u
   * @return false on unsuccessful deletion, true on successful deletion
   */
  public boolean deleteUser(User u) {
    return false;
  }

  /**
   * todo
   *
   * @param u User input
   */
  public void accessUserProfile(User u) {
    return;
  }
}
