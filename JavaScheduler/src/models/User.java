package models;

import java.util.ArrayList;
import java.util.UUID;

public class User {

  private String id;
  private String username;
  private String firstname;
  private String lastname;
  private String password;
  private String email;
  private ArrayList<Event> acceptedEvents = new ArrayList<Event>();
  private ArrayList<Event> pendingEvents = new ArrayList<Event>();
  private ArrayList<Location> customLocations = new ArrayList<Location>();
  private Boolean isAdmin;

  /**
   * Constructor for fetching user from database and creating model class from it
   */
  public User(String id, String username, String firstname, String lastname, String email,
      ArrayList<Event> acceptEvents, ArrayList<Event> pendingEvents, ArrayList<Location> customLocations) {
    this.id = id;
    this.username = username;
    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.acceptedEvents = acceptEvents;
    this.pendingEvents = pendingEvents;
    this.customLocations = customLocations;
    this.isAdmin = false;
  }

  /**
   * Constructor after account creation and before storing to database
   */
  public User(String username, String password, String email) {
    this.id = generateUUID();
    this.username = username;
    this.firstname = "";
    this.lastname = "";
    this.password = password;
    this.email = email;
    this.acceptedEvents = new ArrayList<Event>();
    this.pendingEvents = new ArrayList<Event>();
    this.customLocations = new ArrayList<Location>();
    this.isAdmin = false;
  }

  /**
   * Create new event
   * 
   * @param event - Newly created event
   */
  public void createEvent(Event event) {
    acceptedEvents.add(event);
  }

  /**
   * Get accepted events
   * 
   * @return List of accepted events from user
   */
  public ArrayList<Event> getAcceptedEvents() {
    return acceptedEvents;
  }

  /**
   * Get user ID
   * 
   * @return String user ID
   */
  public String getId() {
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

  /**
   * Get admin status
   * 
   * @return Boolean admin status
   */
  public Boolean getAdmin() {
    return isAdmin;
  }

  /**
   * Set user ID
   * 
   * @param id - String ID
   */
  public void setId(String id) {
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

  public ArrayList<Event> getPendingEvents() {
    return pendingEvents;
  }

  public void setPendingEvents(ArrayList<Event> pendingEvents) {
    this.pendingEvents = pendingEvents;
  }

  public ArrayList<Location> getCustomLocations() {
    return customLocations;
  }

  public void setCustomLocations(ArrayList<Location> customLocations) {
    this.customLocations = customLocations;
  }

  public String getFirstname() {
    return firstname;
  }

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
   * Generate a random UUID for a newly created user account
   * 
   * @return standard UUID with dashes removed
   */
  public static String generateUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  /**
   * Set accepted events
   * 
   * @param acceptedEvents
   */
  public void setAcceptedEvents(ArrayList<Event> acceptedEvents) {
    this.acceptedEvents = acceptedEvents;
  }

  @Override
  public boolean equals(Object other) {
    User that = (User) other;
    return this.id.equals(that.id);
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
