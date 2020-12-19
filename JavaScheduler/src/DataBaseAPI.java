import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DataBaseAPI {

  private static final String SQL_CLOUD_URI = "jdbc:mysql://bqzknjzyoeidxu0hmqhq-mysql.services.clever-cloud.com:3306/bqzknjzyoeidxu0hmqhq?useSSL=false";
  private static final String SQL_CLOUD_USERNAME = "udpcghp8h7wkwbrg";
  private static final String SQL_CLOUD_PASSWORD = "mYe6S6puRrvcblEZPIWZ";

  private static Connection connect;

  public static ResultSet connectToDatabase(String sql) {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      connect = DriverManager.getConnection(SQL_CLOUD_URI, SQL_CLOUD_USERNAME, SQL_CLOUD_PASSWORD);
      System.out.println("Database connected..");

      Statement statement = connect.createStatement();
      ResultSet result = statement.executeQuery(sql);

      return result;

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }

    return null;
  }

  public static boolean verifyUser(String username, String password) throws SQLException {
    String sql = "SELECT * FROM UserAccount WHERE username='" + username + "' AND password='" + password + "'";
    if (connectToDatabase(sql).next()) {
      connect.close();
      return true;
    }
    connect.close();
    return false;
  }

  public static boolean createUser(UserAccount user) {
    String sql = "INSERT INTO `UserAccount` (`id`, `username`, `password`, `email`) VALUES ('" + user.getId() + "', '"
        + user.getUsername() + "', '" + user.getPassword() + "', '" + user.getEmail() + "')";
    ResultSet execute = connectToDatabase(sql);
    return (execute != null);
  }

  public static boolean userAvailable(String username, String email) throws SQLException {
    String sql = "SELECT * FROM UserAccount WHERE username='" + username + "' AND email='" + email + "'";
    if (connectToDatabase(sql).next()) {
      connect.close();
      return true;
    }
    connect.close();
    return false;
  }
}
