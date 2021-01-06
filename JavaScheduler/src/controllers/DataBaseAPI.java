package controllers;

import models.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseAPI {

  private static final String SQL_CLOUD_URI = "jdbc:mysql://bqzknjzyoeidxu0hmqhq-mysql.services.clever-cloud.com:3306/bqzknjzyoeidxu0hmqhq?useSSL=false";
  private static final String SQL_CLOUD_USERNAME = "udpcghp8h7wkwbrg";
  private static final String SQL_CLOUD_PASSWORD = "mYe6S6puRrvcblEZPIWZ";

  /**
   * Build a connection to the application database
   * @return <code>null</code> on failed connection, else return {@link #Connection} object
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

    String sql = "SELECT * FROM UserAccount WHERE username = ? AND password = ?";

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
   * @param username - String of username
   * @return SQL result of data entry or <code>null</code> if user doesn't exist
   */
  private static ResultSet fetchUserData(Connection connection, String username) {
    String sql = "SELECT * FROM UserAccount WHERE username = ?";
    try {
      PreparedStatement statement = connection.prepareStatement(sql);
      statement.setString(1, username);
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
   * Check if username or email is already taken
   * @param user - User data
   * @return <code>true</code> if user data is available
   */
  public static boolean isAvailable(User user) {
    String sql = "SELECT * FROM UserAccount WHERE username = ? OR email=?";
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

  /**
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
      User user = new User(id, name, "", "", email, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
      closeDatabase(connection);
      return user;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
