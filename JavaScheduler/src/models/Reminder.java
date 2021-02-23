package models;

public enum Reminder {
  ONE_WEEK("One week before", 10080),
  THREE_DAYS("Three days before",4320 ),
  ONE_HOUR("One hour before", 60),
  TEN_MINUTES("Ten minutes before", 10),
  NONE("Don't remind me", 0),
  SENT("Reminder has been sent", -1);

  private String name;
  private int minutes;

  private Reminder(String name, int minutes) {
    this.name = name;
    this.minutes = minutes;
  }

  public int getMinutes(){
    return minutes;
  }

  public String toString() {
    return name;
  }

}
