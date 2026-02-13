package GetVillainNames;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            String url = ConfigLoader.get("db.url");
            String user = ConfigLoader.get("db.user");
            String password = ConfigLoader.get("db.password");
            connection = DriverManager.getConnection(url, user, password);
        }
        return connection;
    }
}
