package models;

/**
 * The Reminder enum is used to determine when to remind the user
 * of an upcoming event in which he is partaking
 */
public enum Reminder {
  ONE_WEEK("One week before", 10080),
  THREE_DAYS("Three days before",4320 ),
  ONE_HOUR("One hour before", 60),
  TEN_MINUTES("Ten minutes before", 10),
  NONE("Don't remind me", 0 );

  private String name;
  private int minutes;

  /**
   * Constructor for reminder enum
   * @param name - name of the enum
   * @param minutes - minutes before start of en event when the user should be remind
   */
  private Reminder(String name, int minutes) {
    this.name = name;
    this.minutes = minutes;
  }

  /**
   * Get minutes of the reminder
   * @return
   */
  public int getMinutes(){
    return minutes;
  }

  /**
   * Get the name of the reminder as a string
   * @return name of the reminder as a string
   */
  public String toString() {
    return name;
  }

}
