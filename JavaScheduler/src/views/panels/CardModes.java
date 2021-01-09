package views.panels;

public interface CardModes {

  /** Card layout for upcoming events */
  public static final int UPCOMING  = 0;

  /** Card layout for all events. Unlocks edit and delete options */
  public static final int ALL       = 1;

  /** Card layout for notifaction panel. Minimises content. */
  public static final int NOTIF     = 2;

  /** Card layout for calendar side bar. Truncates card size. */
  public static final int CALENDAR  = 3;
}
