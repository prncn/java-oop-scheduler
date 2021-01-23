package models;

public enum Reminder {
  ONE_WEEK("One week before"),
  THREE_DAYS("Three days before"),
  ONE_HOUR("One hour before"),
  TEN_MIN("Ten minutes before"),
  NONE("Don't remind me");

  private String name;

  private Reminder(String name) {
    this.name = name;
  }

  public String toString() {
    return name;
  }

}
