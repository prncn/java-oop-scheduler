package models;

import java.util.ArrayList;
import java.util.UUID;

public class UserAccount {

  private String id;
  private String username;
  private String password;
  private String email;
  private ArrayList<Meeting> meetings;

  public UserAccount() {
    //
  }

  public UserAccount(String username, String password, String email) {
    this.id = generateUUID();
    this.username = username;
    this.password = password;
    this.email = email;
    meetings = new ArrayList<>();
  }

  public void addMeeting(Meeting meeting) {
    meetings.add(meeting);
  }

  public ArrayList<Meeting> getMeetings() {
    return meetings;
  }

  /**
   * Get user ID
   * @return String user ID
   */
  public String getId() {
    return this.id;
  }

  /**
   * Get username
   * @return String username
   */
  public String getUsername() {
    return this.username;
  }

  /**
   * Get user password
   * @return String user password (encrypted)
   */
  public String getPassword() {
    return this.password;
  }

  /**
   * Get user email
   * @return String user email
   */
  public String getEmail() {
    return this.email;
  }

  /**
   * Set user ID
   * @param id - String ID
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Set username
   * @param username - String username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Set user password
   * @param password - String password (encrypted)
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Set user email
   * @param email - String email
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Generate a random UUID for a newly created user account
   * @return standard UUID with dashes removed
   */
  public static String generateUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }

  @Override
  public boolean equals(Object other) {
    UserAccount that = (UserAccount) other;
    return this.id.equals(that.id);
  }
}
