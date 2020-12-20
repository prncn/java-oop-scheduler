package models;

import java.util.UUID;

public class UserAccount {

  private String id;
  private String username;
  private String password;
  private String email;

  public UserAccount(String username, String password, String email) {
    this.id = generateUUID();
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public String getId() {
    return this.id;
  }

  public String getUsername() {
    return this.username;
  }

  public String getPassword() {
    return this.password;
  }

  public String getEmail() {
    return this.email;
  }

  public static String generateUUID() {
    return UUID.randomUUID().toString().replace("-", "");
  }
}
