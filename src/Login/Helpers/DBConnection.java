package Login.Helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public static final String URL = "jdbc:mysql://localhost/university?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String User = "root";
    public static final String password = "";

    private DBConnection() {
    }

    public static DBConnection getInstance() {
        return new DBConnection();
    }

    public Connection getConnection() {
        Connection connection = null;

        try {
            connection = connection = DriverManager.getConnection(URL,User,password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Connection problem!!");
        }
        return connection;
    }
}
