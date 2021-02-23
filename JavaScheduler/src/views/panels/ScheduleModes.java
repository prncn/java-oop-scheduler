package views.panels;

/**
 * Interface for the different modes of the schedule panel. The
 * default mode is create, in which the standard creator is displayed.
 * Yes, this could simply have been an enum.
 */
public interface ScheduleModes {

  /** Schedule layout for creation of event */
  public static final int CREATE  = 0;

  /** Schedule layout for edit of event */
  public static final int EDIT    = 1;

  /** Schedule layout for viewing of event */
  public static final int VIEW    = 2;

}
