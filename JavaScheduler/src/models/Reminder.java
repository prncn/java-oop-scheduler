package models;
/**
 * The Enum used to set the reminder time in min
 * @author ZuHyunLee97
 */

public enum Reminder {
  ONE_WEEK("One week before", 10080),
  THREE_DAYS("Three days before",4320 ),
  ONE_HOUR("One hour before", 60),
  TEN_MIN("Ten minutes before", 10),
  NONE("Don't remind me", 0 );

  private String name;
  private int minutes;

  /**
   * Constructor
   * @param name
   * @param minutes
   */
  private Reminder(String name, int minutes) {
    this.name = name;
    this.minutes = minutes;
  }

  /**
   * Get minutes
   * @return Int minutes of the reminder
   */
  public int getMinutes(){
    return minutes;
  }

  /**
   *Get name
   * @return String name of the reminder
   */
  public String toString() {
    return name;
  }

}
