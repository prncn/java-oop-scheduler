package controllers;

import models.*;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class DatabaseAPI {
  private static final String SQL_CLOUD_URI = "jdbc:mysql://bda7xeloolcvse2aqa6l-mysql.services.clever-cloud.com:3306/bda7xeloolcvse2aqa6l?useSSL=false";
  private static final String SQL_CLOUD_USERNAME = "udpcghp8h7wkwbrg";
  private static final String SQL_CLOUD_PASSWORD = "mYe6S6puRrvcblEZPIWZ";
  private static Connection con = null;

  /**
   * Build a connection to the application database.
   * If a connection already resides, that connection is used instead.
   *
   * @return <code>null</code> on failed connection, else return connection object
   */
  private static Connection connectDatabase() {
    try {
      if (con != null) {
        return con;
      }
      Class.forName("com.mysql.jdbc.Driver");
      con = DriverManager.getConnection(SQL_CLOUD_URI, SQL_CLOUD_USERNAME, SQL_CLOUD_PASSWORD);

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
  public static void closeDatabase() {
    try {
      if (con != null) {
        con.close();
        con = null;
      }
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
    // closeDatabase();

    System.out.println("Verified user.");
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

      if (result.next()) {
        System.out.println("Fetching user data.");
        return result;
      }
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
  public static boolean storeUser(User user) {
    String sql = "INSERT INTO User (username, password, email, firstname, lastname, icon)" + " VALUES(?, ?, ?, ?, ?, ?)";
    Connection connection = connectDatabase();
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, user.getUsername());
      statement.setString(2, user.getPassword());
      statement.setString(3, user.getEmail());
      statement.setString(4, user.getFirstname());
      statement.setString(5, user.getLastname());
      statement.setBytes(6, FormatUtil.iconToBytes(user.getAvatar()));
      statement.executeUpdate();

      statement.close();
      closeDatabase();
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }

    System.out.println("Stored user.");
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
    String sql = "UPDATE User SET username = ?, email = ?, firstname = ?, lastname = ?, icon = ? WHERE user_id = ?";

    Connection connection = connectDatabase();
    try {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setString(1, user.getUsername());
      ps.setString(2, user.getEmail());
      ps.setString(3, user.getFirstname());
      ps.setString(4, user.getLastname());
      ps.setBytes(5, FormatUtil.iconToBytes(user.getAvatar()));
      ps.setInt(6, user.getId());
      ps.executeUpdate();
      ps.close();
      closeDatabase();
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }

    System.out.println("Updated user.");
    return true;
  }

  /**
   * todo remove password? Gets all users from DB
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
        String firstname = result.getString("firstname");
        String lastname = result.getString("lastname");
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
      while (result.next()) {
        if (result.getInt("user_id") != user.getId()) {
          return false;
        }
      }
      statement.close();
      closeDatabase();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }
  }

  /**
   * Query a username and return the corresponding User object from its table
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
      String firstname = result.getString("firstname");
      String lastname = result.getString("lastname");
      byte[] icon = result.getBytes("icon");

      ArrayList<Event> events = getEventsFromUser(id);
      User user = new User(id, name, firstname, lastname, email, events, new ArrayList<Location>());
      if (icon != null) {
        user.setAvatar(FormatUtil.byteToIcon(icon));
      }
      closeDatabase();
      System.out.println("Fetched user.");
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return null;
    }
  }

  /**
   * Gets all events from User with help of the userId.
   * 
   * @param userId is used to find the relative data
   * @return a list of all events a user is part of.
   */
  public static ArrayList<Event> getEventsFromUser(int userId) {
    String sql = "SELECT * FROM Event " + "LEFT JOIN User_Event " + "ON User_Event.event_id = Event.event_id "
        + "WHERE User_Event.user_id = ?";
    Connection connection = connectDatabase();
    ArrayList<Event> events = new ArrayList<Event>();

    try {
      PreparedStatement ps = connection.prepareStatement(sql);

      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();

      while (rs.next()) {
        int eventId = rs.getInt("event_id");
        String name = rs.getString("name");
        int duration = rs.getInt("duration_minutes");
        LocalDate date = rs.getDate("date").toLocalDate();
        LocalTime time = rs.getTime("time").toLocalTime();
        String description = rs.getString("description");

        Reminder reminder = Enum.valueOf(Reminder.class, rs.getString("reminder"));
        Priority priority = Enum.valueOf(Priority.class, rs.getString("priority"));
        int host_id = rs.getInt("host_id");
        Location location = fetchLocation(rs.getInt("location_id"));
        ArrayList<File> attachments = getAttachmentsFromEvent(eventId);

        Event event = new Event(eventId, name, description, duration, date, time, location, priority, reminder, getParticipants(eventId));

        event.setId(eventId);
        event.setHostId(host_id);
        event.setAttachments(attachments);
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
   * Gets a list of participants for an event
   * 
   * @param eventId
   * @return
   */
  private static ArrayList<User> getParticipants(int eventId) {
    String sql = "SELECT * FROM User " + "LEFT JOIN User_Event "
        + "ON User_Event.user_id = User.user_id WHERE User_Event.event_id = ? ";
        Connection connection = connectDatabase();
        ArrayList<User> participants = new ArrayList<>();
        
        try {
          PreparedStatement ps = connection.prepareStatement(sql);
          ps.setInt(1, eventId);
          
          ResultSet rs = ps.executeQuery();
          
          while (rs.next()) {
            User u = new User(rs.getInt("user_id"), rs.getString("username"), rs.getString("firstname"),
            rs.getString("lastName"), rs.getString("email"));
            byte[] icon = rs.getBytes("icon");
            if (icon != null) {
              u.setAvatar(FormatUtil.byteToIcon(icon));
            }
            participants.add(u);
          }
      rs.close();
      ps.close();
      return participants;
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
  public static boolean deleteEvent(int eventId) {
    String sql = "DELETE FROM Event WHERE event_id = ?";
    Connection connection = connectDatabase();
    
    try {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1, eventId);
      
      ps.executeUpdate();
      
      ps.close();
      
      closeDatabase();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }
  }
  
  /**
   * Creates an entry in the User_Event table in the Database.
   * 
   * @param userId
   * @param eventId
   * @return true when insertion was successful, false when insertion had an
   *         exception.
   */
  public static boolean createUserEventBridge(int userId, int eventId) {
    String sql = "INSERT INTO User_Event (user_id , event_id) " + "VALUES(?, ?)";
    Connection connection = connectDatabase();
    
    try {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1, userId);
      ps.setInt(2, eventId);
      
      ps.executeUpdate();
      
      ps.close();
      closeDatabase();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      closeDatabase();
      return false;
    }
  }
  
  /**
   * Delete table entry in the User_Event & therefore remove the connection
   * between the User & Event.
   *
   * @param userId  User that should be removed from the event
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
  public static int storeEvent(Event event) {
    String sql = "INSERT INTO Event (host_id, name, date, time, duration_minutes, location_id, reminder, priority, description)"
    + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
    Connection connection = connectDatabase();
    int eventId;
    
    try {
      PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      
      statement.setInt(1, event.getHostId());
      statement.setString(2, event.getName());
      statement.setDate(3, Date.valueOf(event.getDate()));
      statement.setTime(4, Time.valueOf(event.getTime()));
      statement.setInt(5, event.getDurationMinutes());
      statement.setInt(6, event.getLocation().getId());
      statement.setString(7, event.getReminder().name());
      statement.setString(8, event.getPriority().name());
      statement.setString(9, event.getDescription());
      statement.executeUpdate();
      
      ResultSet generatedKey = statement.getGeneratedKeys();
      
      if (generatedKey.next()) {
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
   *
   * @param event new event object which the Database should be adjusted for
   * @return
   */
  public static boolean editEvent(Event event) {
    String sql = "UPDATE Event SET reminder = ? , priority = ? , name = ? , date = ? , time = ? , duration_minutes = ? , description = ? ,  host_id = ? ,location_id = ? "
    + "WHERE event_id = ? ";
    
    Connection connection = connectDatabase();
    
    try {
      PreparedStatement ps = connection.prepareStatement(sql);
      
      ps.setString(1, event.getReminder().name());
      ps.setString(2, event.getPriority().name());
      ps.setString(3, event.getName());
      ps.setDate(4, Date.valueOf(event.getDate()));
      ps.setTime(5, Time.valueOf(event.getTime()));
      ps.setInt(6, event.getDurationMinutes());
      ps.setString(7, event.getDescription());
      ps.setInt(8, event.getHostId());
      ps.setInt(9, event.getLocation().getId());
      
      ps.setInt(10, event.getId());
      
      ps.executeUpdate();
      ps.close();
      closeDatabase();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      e.getErrorCode();
      closeDatabase();
      return false;
    }
  }
  
  /**
   * Creates an entry in Location table in the Database.
   * 
   * @param location Location that should be entered
   * @param userId User that the Location belongs to
   * @return ID of Location or -1
   */
  public static int storeLocation(Location location, int userId) {
    String sql = "INSERT INTO Location (name, city, zip, street, street_nr, building, room_nr, user_id) "
    + "VALUES( ? , ? , ? , ? , ? , ? , ? , ?)";
    Connection connection = connectDatabase();
    int locationId = -1;
    try {
      PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
      ps.setString(1, location.getName());
      ps.setString(2, location.getCity());
      ps.setString(3, location.getZip());
      ps.setString(4, location.getStreet());
      ps.setString(5, location.getStreetNr());
      ps.setString(6, location.getBuilding());
      ps.setString(7, location.getRoomNr());
      ps.setInt(8, userId);
      
      ps.executeUpdate();
      
      ResultSet genKey = ps.getGeneratedKeys();
      
      if (genKey.next()) {
        locationId = genKey.getInt(1);
      } else {
        throw new SQLException("Creating Location failed, no ID obtained.");
      }
      
      ps.close();
      genKey.close();
      
      return locationId;
    } catch (SQLException e) {
      e.printStackTrace();
      return -1;
    }
  }
  
  /**
   * Fetch a location object with its given location id
   * @param locationId Location ID to specify location
   * @return Location object
   */
  private static Location fetchLocation(int locationId) {
    String sql = "SELECT * FROM Location WHERE location_id = ?";
    Connection con = connectDatabase();

    try {
      PreparedStatement ps = con.prepareStatement(sql);
      ps.setInt(1, locationId);
      
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        Location lc = new Location(rs.getString("name"));
        lc.setCity(rs.getString("city"));
        lc.setZip(rs.getString("zip"));
        lc.setStreet(rs.getString("street"));
        lc.setStreetNr(rs.getString("street_nr"));
        lc.setBuilding(rs.getString("building"));
        lc.setRoomNr(rs.getString("room_nr"));
        return lc;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Edit the Location entry in the Database.
   * @param location Updated Location
   * @return true on successful edit, false on failed edit
   */
  public static boolean editLocation(Location location){
    String sql = "UPDATE Location SET name = ? , room_nr = ? , building = ? , city = ? , zip = ? , street = ? , street_nr = ? "
    + "WHERE location_id = ? ";
    Connection connection = connectDatabase();
    
    try{
      PreparedStatement ps = connection.prepareStatement(sql);

      ps.setString(1 , location.getName());
      ps.setString(2 , location.getRoomNr());
      ps.setString(3 , location.getBuilding());
      ps.setString(4 , location.getCity());
      ps.setString(5 , location.getZip());
      ps.setString(6 , location.getStreet());
      ps.setString(7 , location.getStreetNr());
      ps.setInt(8 , location.getId());

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

  public static ArrayList<Location> getLocationsFromUser(int userId){
    String sql = "SELECT * FROM Location WHERE user_id = ?";
    Connection connection = connectDatabase();
    ArrayList<Location> locations = new ArrayList<>();

    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1 , userId);

      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        Location location = new Location(
                rs.getString("name"),
                rs.getString("city") ,
                rs.getString("zip"),
                rs.getString("street"),
                rs.getString("street_nr"),
                rs.getString("building"),
                rs.getString("room_nr"));
        location.setId(rs.getInt("location_id"));
        locations.add(location);
      }
      return locations;
    } catch (SQLException e){
      e.printStackTrace();
      closeDatabase();
      return null;
    }
  }

  /**
   * Adds attachment entry into the Database
   *
   * @param file File to be uploaded into the database
   * @param event Event that the file belongs to
   * @return -1 on failed creation, ID on successful creation
   */
  public static int storeAttachment(File file, Event event) {
    String sql = "INSERT INTO Attachment (file, event_id, name) VALUES ( ? , ? , ? )";
    Connection connection = connectDatabase();
    int attachmentId = -1;
    try{
      PreparedStatement ps = connection.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);
      FileInputStream input = new FileInputStream(file);
      ps.setBinaryStream(1 , input);
      ps.setInt(2 , event.getId());
      ps.setString(3, file.getName());

      ps.executeUpdate();
      ResultSet generatedKey = ps.getGeneratedKeys();

      if (generatedKey.next()) {
        attachmentId = generatedKey.getInt(1);
      } else {
      throw new SQLException("Creating Attachment failed, no ID obtained.");
      }

      input.close();
      ps.close();
      closeDatabase();
      System.out.println("Attachment stored.");
      return attachmentId;

    } catch (SQLException e){
      e.printStackTrace();
      closeDatabase();
      return attachmentId;
    } catch (IOException e) {
      e.printStackTrace();
      return attachmentId;
    }
  }

  /**
   * Gets the Attachments out of the Database
   * @param eventId Id of an event from which the attachments should be returned
   * @return List of files
   */
  public static ArrayList<File> getAttachmentsFromEvent(int eventId){
    String sql = "SELECT * FROM Attachment WHERE event_id = ?";
    Connection connection = connectDatabase();
    ArrayList<File> files = new ArrayList<>();
    InputStream input = null;
    FileOutputStream output = null;

    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1 ,  eventId);

      ResultSet rs = ps.executeQuery();

      while(rs.next()){
        File tempFile = new File(rs.getString("name"));
        output = new FileOutputStream(tempFile);
        input = rs.getBinaryStream("file");

        byte[] buffer = new byte[1024];
        while (input.read(buffer) > 0){
          output.write(buffer);
        }

        files.add(tempFile);

        input.close();
        output.close();
      }


      return files;
    } catch (SQLException | IOException e) {
      e.printStackTrace();

      return null;
    }
  }

  /**
   * Delete all Attachment entries in the Database
   * @param eventId Event which the entries should be deleted from.
   */
  public static void deleteAllAttachments(int eventId){
    String sql = "DELETE FROM Attachment WHERE event_id = ?";

    Connection connection = connectDatabase();

    try{
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt( 1, eventId);

      ps.executeUpdate();

      ps.close();
      closeDatabase();
    } catch(SQLException e){
      e.printStackTrace();
      closeDatabase();
    }
  }

  /**
   * Delete user in the user table and user's corresponding entries
   * in table Location and table User_Event
   * @param userId - id of user to delete
   * @return true if deletion was successful
   */
  public static boolean deleteUser(int userId) {
    String sql = "DELETE FROM User WHERE user_id = ?";
    Connection connection = connectDatabase();
    try {
      PreparedStatement ps = connection.prepareStatement(sql);
      ps.setInt(1,userId);
      ps.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}
