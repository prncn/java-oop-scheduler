import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
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
	String DB_PW = "SELECT * FROM UserAccount WHERE username='" + username + "'";
	Connection connection2 = connectDatabase();
    Statement statement2 = connection2.createStatement();
    ResultSet result2 = statement2.executeQuery(DB_PW);
    result2.next();
    String DB_PW2 = result2.getString("password");
	System.out.println(DB_PW2);
	String password_encrypted = PasswordEncryption.verify(password, DB_PW2);
    String sql = "SELECT * FROM UserAccount WHERE username='" + username + "' AND password='" + password_encrypted + "'";
    //Connection connection = connectDatabase();
    Statement statement = connection2.createStatement();
    ResultSet result = statement.executeQuery(sql);
    Boolean isUser = result.next();
    
    closeDatabase(connection2);
    return isUser;
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
}
