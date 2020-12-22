package controllers;

import models.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseAPI {

  private static final String SQL_CLOUD_URI = "jdbc:mysql://bqzknjzyoeidxu0hmqhq-mysql.services.clever-cloud.com:3306/bqzknjzyoeidxu0hmqhq?useSSL=false";
  private static final String SQL_CLOUD_USERNAME = "udpcghp8h7wkwbrg";
  private static final String SQL_CLOUD_PASSWORD = "mYe6S6puRrvcblEZPIWZ";

  public static Connection connectDatabase() {
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

  public static boolean verifyUser(String username, String password) throws SQLException {
    Connection connection = connectDatabase();
    ResultSet userData = fetchUserData(connection, username);
    String saltHash = userData.getString("password");
    String password_encrypted = PasswordEncryption.verify(password, saltHash);
    
    String sql = "SELECT * FROM UserAccount WHERE username='" + username + "' AND password='" + password_encrypted + "'";
    Statement statement = connection.createStatement();
    ResultSet result = statement.executeQuery(sql);
    Boolean isUser = result.next();
    
    closeDatabase(connection);
    return isUser;
  }

  private static ResultSet fetchUserData(Connection connection, String username) {
	  String sql = "SELECT * FROM UserAccount WHERE username='" + username + "'";
    try {
      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(sql);
      result.next();
      return result;
    } catch (SQLException e) {
      e.printStackTrace();
      System.out.println(e);
      return null;
    }
  }

  public static boolean createUser(UserAccount user) {
    String sql = "INSERT INTO `UserAccount` (`id`, `username`, `password`, `email`) VALUES ('" + user.getId() + "', '"
        + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getEmail() + "')";
    Connection connection = connectDatabase();
    Statement statement;
    try {
      statement = connection.createStatement();
      statement.execute(sql);
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  public static boolean isAvailable(UserAccount user) {
    String sql = "SELECT * FROM UserAccount WHERE username='" + user.getUsername() + "' OR email='" + user.getEmail() + "'";
    Connection connection = connectDatabase();
    try {
      Statement statement = connection.createStatement();
      ResultSet result = statement.executeQuery(sql);
      Boolean isTaken = result.next();
      closeDatabase(connection);
      return !isTaken;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    
  }

  private static void closeDatabase(Connection connection) throws SQLException {
    System.out.println("Connection closed.");
    connection.close();
  }

  public static UserAccount getUser(String username) {
    Connection connection = connectDatabase();
    ResultSet result = fetchUserData(connection, username);

    try {
      String _id = result.getString("id");
      String _username = result.getString("username");
      String _password = result.getString("password");
      String _email = result.getString("email");
      closeDatabase(connection);
      return new UserAccount(_username, _password, _email);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

}
