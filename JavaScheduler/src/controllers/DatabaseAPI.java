package controllers;

import models.*;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DatabaseAPI {
  private static final String SQL_CLOUD_URI = "jdbc:mysql://bqzknjzyoeidxu0hmqhq-mysql.services.clever-cloud.com:3306/bqzknjzyoeidxu0hmqhq?useSSL=false";
  private static final String SQL_CLOUD_USERNAME = "udpcghp8h7wkwbrg";
  private static final String SQL_CLOUD_PASSWORD = "mYe6S6puRrvcblEZPIWZ";
  private static Connection con = null;

  /**
   * Build a connection to the application database
   *
   * @return <code>null</code> on failed connection, else return connection object
   */
  private static Connection connectDatabase() {
    try {
      if(con != null){
        return con;
      }
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(SQL_CLOUD_URI, SQL_CLOUD_USERNAME, SQL_CLOUD_PASSWORD);
      System.out.println("Database connected..");

      return con;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  /**
   * Close an existing connection to the database. This function should be used
   * after every other database API function, as multiple unused connection may
   * reach cloud traffic limit.
   */
  private static void closeDatabase() {
    try {
      con.close();
      con = null;
      System.out.println("Connection closed.");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Verify if given username and pass correspond to a user in the database
   *
   * @param username - String of username
   * @param password - String of <i>hashed<i> password
   * @return <code>true</code> if user exists
   * @throws SQLException
   */
  public static boolean verifyUser(String username, String password) throws SQLException {
    Connection connection = connectDatabase();
    ResultSet userData = fetchUserData(connection, username);
    if (userData == null) {
      System.out.println("EMPTY ELEM");
      return false;
    }
    String saltHash = userData.getString("password");
    String password_encrypted = PasswordEncryption.verify(password, saltHash);

    String sql = "SELECT * FROM User WHERE username = ? AND password = ?";

    PreparedStatement statement = connection.prepareStatement(sql);
    statement.setString(1, username);
    statement.setString(2, password_encrypted);

    ResultSet result = statement.executeQuery();

    Boolean isUser = result.next();
    statement.close();
    closeDatabase();
    return isUser;
  }

  /**
   * Fetch user data from database. This is only called by other user related DB
   * functions.
   *
   * @param connection - SQL jdbc connection object, connection to DB
   * @param key        - ???
   * @return SQL result of data entry or <code>null</code> if user doesn't exist
   */
  private static <T> ResultSet fetchUserData(Connection connection, T key) {
    String sqlColumn = "";

    if (key instanceof String)
      sqlColumn = "username";
    else
      sqlColumn = "user_id";

    String sql = "SELECT * FROM User WHERE " + sqlColumn + " = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      if (key instanceof String) {
        String username = key.toString();
        statement.setString(1, username);
      } else if (key instanceof Integer) {
        Integer userId = ((Integer) key).intValue();
        statement.setInt(1, userId);
      }

      ResultSet result = statement.executeQuery();

      if (result.next())
        return result;
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e);
      return null;
    }
  }

  private static ResultSet fetchEventData(Connection connection, String eventID) {
    String sql = "SELECT * FROM Event WHERE eventID = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, eventID);
      ResultSet result = statement.executeQuery();

      if (result.next())
        return result;
      else
        return null;

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e);
      return null;
    }
  }

  /**
   * Create table entry of a new user in database. Used for account creation.
   *
   * @param user - User object of new user
   * @return <code>true</code> on successful user creation
   */
  public static boolean createUser(User user) {
    String sql = "INSERT INTO User (username, password, email, firstName, lastName)" + " VALUES(?, ?, ?, ?, ?)";
    Connection connection = connectDatabase();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getFirstname());
      statement.setString(5, user.getLastname());
      statement.executeUpdate();

      statement.close();
      closeDatabase();
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }

    return true;
  }

  /**
   * Edits a user in the database with the parameter user.
   *
   * @param user This user's attribute values are taken to edit the user in the DB
   *             with the same id
   *
   * @return <code>true</code>, if successful
   */
  public static boolean editUser(User user) {
    String sql = "UPDATE User SET username = ?, password = ?, email = ?, firstName = ?, lastName = ? WHERE user_id = ?";

    Connection connection = connectDatabase();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getFirstname());
      statement.setString(5, user.getLastname());
      statement.setInt(6, user.getId());
      statement.executeUpdate();
      statement.close();
      closeDatabase();
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }
    return true;
  }

  /** todo remove password?
   * Gets all users from DB
   *
   * @return all users as an arraylist
   */
  public static ArrayList<User> getAllUsers() {
    String sql = "SELECT * FROM User";
    Connection connection = connectDatabase();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);

      ResultSet result = statement.executeQuery();
      ArrayList<User> allUsers = new ArrayList<User>();
      while (result.next()) {
        String username = result.getString("username");
        String password = result.getString("password");
        String firstname = result.getString("firstName");
        String lastname = result.getString("lastName");
        String email = result.getString("email");
        allUsers.add(new User(username, password, firstname, lastname, email));
      }

      statement.close();
      closeDatabase();
      return allUsers;

    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
    }
    return null;
  }

  /**
   * Check if username or email is already taken
   *
   * @param user - User data
   * @return <code>true</code> if user data is available
   */
  public static boolean isAvailable(User user) {
    String sql = "SELECT * FROM User WHERE username = ? OR email=?";

    Connection connection = connectDatabase();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getEmail());

      ResultSet result = statement.executeQuery();
      Boolean isTaken = result.next();
      statement.close();
      closeDatabase();
      return !isTaken;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * TODO Query a username and return the corresponding User object from its table
   * entry. Used to search the user table.
   * 
   * @param key - String of username or Int of userid
   * @return User object on successful query, else <code>null</code>
   */
  public static <T> User getUser(T key) {
    Connection connection = connectDatabase();

    ResultSet result = fetchUserData(connection, key);
    if (result == null) {
      closeDatabase();
      return null;
    }
    try {

      int id = result.getInt("user_id");
      String name = result.getString("username");
      String email = result.getString("email");
      String firstname = result.getString("firstName");
      String lastname = result.getString("lastName");

      closeDatabase();

      ArrayList<Event> events = getEventsFromUser(id);

      User user = new User(id, name, firstname, lastname, email, events, new ArrayList<Location>());
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return null;
    }
  }

  /**
   * public static User getUser(int userId){ userId = sql getUser(username); }
   **/

  /**
   * Gets all events from User with help of the userId.
   * 
   * @param userId is used to find the relative data
   * @return a list of all events a user is part of.
   */
  public static ArrayList<Event> getEventsFromUser(int userId) {
    String sql =  "SELECT * FROM Event " +
                  "LEFT JOIN User_Event " +
                  "ON User_Event.event_id = Event.event_id " +
                  "WHERE User_Event.user_id = ?";
    Connection connection = connectDatabase();
    ArrayList<Event> events = new ArrayList<Event>();

    try {
      PreparedStatement ps = connection.prepareStatement(sql);

      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int eventId = rs.getInt("event_id");
        String name = rs.getString("name");
        int duration = rs.getInt("durationMinutes");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        String description = rs.getString("description");

        Reminder reminder = Enum.valueOf(Reminder.class, rs.getString("reminder"));
        Priority priority = Enum.valueOf(Priority.class, rs.getString("priority"));
        int host_id = rs.getInt("host_id");
        int location_id = rs.getInt("location_id");

        Event event = new Event(eventId, name, description, duration, date, time, new Location("PLACEHOLDER"), priority, reminder,
                new ArrayList<User>(), new ArrayList<File>());

        event.setId(eventId);
        event.setHostId(host_id);
        events.add(event);

      }

      rs.close();
      ps.close();
      return events;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

  }

  /**
   * Delete table entry in Event
   *
   * @param eventId
   * @return true when deletion is successful, false when deletion is unsuccessful
   */
  public static boolean deleteEvent(int eventId){
    String sql = "DELETE FROM Event WHERE event_id = ?";
    Connection connection = connectDatabase();

    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1 , eventId);

      ps.executeUpdate();

      ps.close();

      closeDatabase();
      return true;
    } catch (SQLException e){
      e.printStackTrace();
      closeDatabase();
      return false;
    }
  }

  /**
   * Delete table entry in the User_Event & therefore remove the connection between the User & Event.
   *
   * @param userId User that should be removed from the event
   * @param eventId Event that the User should be removed from
   * @return true on success, false on failure.
   */
  public static boolean deleteUserEventBridge(int userId, int eventId) {
    String sql;
    Connection connection = connectDatabase();
    sql = "DELETE FROM User_Event WHERE user_id = ? AND event_id = ?";

    try {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1, userId);
      ps.setInt(2, eventId);

      ps.executeUpdate();
      closeDatabase();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }
  }

  /**
   * Create table entry of new event in database.
   * 
   * @param event Object of new entry.
   * @return ID on successful creation, return -1 on failed creation
   */
  public static int createEvent(Event event) {

    String sql = "INSERT INTO Event (reminder, priority, name, date, time, durationMinutes, description, host_id, location_id)"
        + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    Connection connection = connectDatabase();
    int eventId;

    try {
      PreparedStatement statement = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);

      statement.setString(1, event.getReminder().name());
      statement.setString(2, event.getPriority().name());
      statement.setString(3, event.getName());
      statement.setDate(4, Date.valueOf(event.getDate()));
      statement.setTime(5, Time.valueOf(event.getTime()));
      statement.setInt(6, event.getDurationMinutes());
      statement.setString(7, event.getDescription());

      statement.setInt(8, event.getHostId());
      statement.setString(9, event.getLocation().getId());

      statement.executeUpdate();

      ResultSet generatedKey = statement.getGeneratedKeys();

      if(generatedKey.next()) {
        eventId = generatedKey.getInt(1);
      } else {
        throw new SQLException("Creating user failed, no ID obtained.");
      }


      statement.close();
      closeDatabase();

      return eventId;

    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return -1;
    }
  }

  /**
   * Creates an entry in the User_Event table in the Database.
   * @param userId
   * @param eventId
   * @return true when insertion was successful, false when insertion had an exception.
   */
  public static boolean createUserEventBridge(int userId, int eventId){
    String sql = "INSERT INTO User_Event (user_id , event_id) " + "VALUES(?, ?)";
    Connection connection = connectDatabase();

    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1 , userId);
      ps.setInt(2 , eventId);

      ps.executeUpdate();

      ps.close();
      closeDatabase();
      return true;
    } catch (SQLException e){
      e.printStackTrace();
      closeDatabase();
      return false;
    }
  }

  /** TODO
   * Adds attachment entry into the Database
   * @param file
   * @param event
   * @return
   */
  public static boolean createAttachment(File file, Event event){
    return false;
  }
}
