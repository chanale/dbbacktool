package dbhistory.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * jdbc连接数据库
 */
public class JdbcUtil {
    public static final String DRIVER = "com.mysql.jdbc.Driver";

    public static Connection connected(String mysqlUrl, String username, String password) {
        Connection connection = null;
        try {
            Class.forName(DRIVER);
            DriverManager.setLoginTimeout(5);
            connection = DriverManager.getConnection(mysqlUrl, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }


}
