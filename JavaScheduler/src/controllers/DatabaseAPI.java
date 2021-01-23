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

  /**
   * Build a connection to the application database
   * @return <code>null</code> on failed connection, else return connection object
   */
  private static Connection connectDatabase() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      Connection connection = DriverManager.getConnection(SQL_CLOUD_URI, SQL_CLOUD_USERNAME, SQL_CLOUD_PASSWORD);
      System.out.println("Database connected..");

      return connection;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  /**
   * Verify if given username and pass correspond to a user in the database
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
    closeDatabase(connection);
    return isUser;
  }

  /**
   * Fetch user data from database. This is only called by other user related DB functions.
   * @param connection - SQL jdbc connection object, connection to DB 
   * @param t - ???
   * @return SQL result of data entry or <code>null</code> if user doesn't exist
   */
  private static <T> ResultSet fetchUserData(Connection connection, T t) {
    String sqlColumn = "";

    if(t instanceof String) sqlColumn = "username";
      else sqlColumn = "user_id";

    String sql = "SELECT * FROM User WHERE " + sqlColumn + " = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      if(t instanceof String){
        String username = t.toString();
        statement.setString(1, username );
      } else if (t instanceof Integer){
        Integer userId = ((Integer) t).intValue();
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

  private static ResultSet fetchEventData(Connection connection, String eventID){
    String sql = "SELECT * FROM Event WHERE eventID = ?";
    try{
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1 , eventID);
      ResultSet result = statement.executeQuery();

      if(result.next()) return result;
      else return null;

    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e);
      return null;
    }
  }

  /**
   * Create table entry of a new user in database. Used for account creation.
   * @param user - User object of new user
   * @return <code>true</code> on successful user creation
   */
  public static boolean createUser(User user) {
    String sql = "INSERT INTO UserAccount (id, username, password, email)" + " VALUES(?, ?, ?, ?)";
    Connection connection = connectDatabase();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, user.getId());
      statement.setString(2, user.getUsername());
      statement.setString(3, user.getPassword());
      statement.setString(4, user.getEmail());
      statement.executeUpdate();

      statement.close();
      closeDatabase(connection);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }
  
  /**
   * Edits a user in the database with the parameter user.
   *
   * @param user This user's attribute values are taken to edit the user in the DB with the same id 
   * @return true, if successful
   */
  public static boolean editUser(User user) {
	  String sql = "UPDATE UserAccount SET username = ?, password = ?, email = ? WHERE id = ?";
	  Connection connection = connectDatabase();
	  try {
		  PreparedStatement statement = connection.prepareStatement(sql);
	      statement.setString(1, user.getUsername());
	      statement.setString(2, user.getPassword());
	      statement.setString(3, user.getEmail());
	      statement.setString(4, user.getId());
	      statement.executeUpdate();
	      statement.close();
	      closeDatabase(connection);
	  } catch(SQLException e) {
		  e.printStackTrace();
		  closeDatabase(connection);
		  return false;
	  }
	  return true;
  }
  
  /**
   * Gets all users from DB
   *
   * @return all users as an arraylist
   */
  public static ArrayList<User> getAllUsers() {
	  String sql = "SELECT * FROM UserAccount";
	  Connection connection = connectDatabase();
	  try {
		  PreparedStatement statement = connection.prepareStatement(sql);
	      
		  ResultSet result = statement.executeQuery();
		  ArrayList<User> allUsers = new ArrayList<User>();
		  while(result.next()) {
			  String username = result.getString("username");
			  String password = result.getString("password");
			  String email = result.getString("email");
			  allUsers.add(new User(username,password,email));
			  
			  statement.close();
		      closeDatabase(connection);
		  }

		  return allUsers;
		  
	      
	  } catch(SQLException e) {
		  e.printStackTrace();
		  closeDatabase(connection);
	  }
	return null;
  }

  /**
   * Check if username or email is already taken
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
      closeDatabase(connection);
      return !isTaken;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Close an existing connection to the database. This function should be used after 
   * every other database API function, as multiple unused connection may reach cloud traffic
   * limit.
   * @param connection - SQL connection object of existing connection
   */
  private static void closeDatabase(Connection connection) {
    System.out.println("Connection closed.");
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /** TODO
   * Query a username and return the corresponding User object from its table entry.
   * Used to search the user table.
   * @param username - String of username
   * @return User object on successful query, else <code>null</code>
   */
  public static User getUser(String username) {
    Connection connection = connectDatabase();
    ResultSet result = fetchUserData(connection, username);
    if (result == null) {
      closeDatabase(connection);
      return null;
    }
    try {
      String id = result.getString("id");
      String name = result.getString("username");
      String email = result.getString("email");
      String firstname = result.getString("firstName");
      String lastname = result.getString("lastName");
      // TODO succesfully create the new user
      User user = new User(id, name, firstname, lastname, email, new ArrayList<Event>(), new ArrayList<Location>());
      closeDatabase(connection);
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

/**  public static User getUser(int userId){
    userId = sql
            getUser(username);
  }**/

  /**
   *
   * @param userId is used to find the relative data
   * @return a list of all events a user is part of.
   */
  public static ArrayList<Event> getEventsFromUser(int userId){
    String sql = "SELECT * FROM UserEvent WHERE UserID = ?";
    ArrayList<Event> events = new ArrayList<Event>();

    Connection connection = connectDatabase();
    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1 , userId);
      ResultSet rs = ps.executeQuery();

      while(rs.next()){

      }

      return events;
    } catch (SQLException e){
      e.printStackTrace();
      return null;
    }

  }

  /**
   * Query a event_id and return the corresponding Event object
   * @param id - SQL id
   * @return Event object on successful query, else null
   */
  /*
  public static Event getEvent(int id){
    Location PLATZHALTER = new Location("W", "w", "2", "2", "2", "2", "2");

    String sql = "SELECT * FROM Event WHERE event_id = ?";
    Connection connection = connectDatabase();

    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1, id);
      ResultSet rs = ps.executeQuery();

      if(rs.next()){
        int eventId = rs.getInt("user_id");
        // TODO List of participants?
        Reminder reminder = Enum.valueOf(Reminder.class, rs.getString("reminder"));
        Priority priority = Enum.valueOf(Priority.class, rs.getString("priority"));
        String name = rs.getString("name");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        int duration = rs.getInt("durationMinutes");
        String description = rs.getString("description");
        int host_id = rs.getInt("host_id");
        int location_id = rs.getInt("location_id");

        Event event = new Event(eventId, date, time, duration, PLATZHALTER, new ArrayList<User>(), priority, new ArrayList<File>());

        closeDatabase(connection);
        return event;
      }
    } catch (SQLException e){

      closeDatabase(connection);
      return null;
    }
    return event;
  }*/

  /**
     * Create table entry of new event in database.
     * @param event Object of new entry.
     * @return true on successful creation, return false on failed creation
     */
  public static boolean createEvent(Event event){

      String sql = "INSERT INTO Event (id, reminder, priority, name, date, time, durationMinutes, description, hostId, location)" + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      Connection connection = connectDatabase();

      try{
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1 , event.getId());
        statement.setString(2 , event.getReminder().name());
        statement.setString(3 , event.getPriority().name());
        statement.setString(4 , event.getName());
        statement.setDate(5 , Date.valueOf(event.getDate() ));
        statement.setTime(6 , Time.valueOf(event.getTime() ));
        statement.setInt(7 , event.getDurationMinutes());
        statement.setString(8 , event.getDescription());
        statement.setString(9 , event.getHost().getId());
        statement.setString(10 , event.getLocation().getId());


        statement.executeUpdate();

        statement.close();
        closeDatabase(connection);
        return true;

      } catch (SQLException e) {
          e.printStackTrace();
          return false;
      }
  }
}
