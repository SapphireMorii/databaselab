package bookstore.util;

import java.sql.*;
import java.util.ResourceBundle;
public class JDBCutil {

    static Connection conn = null;

    static {
        ResourceBundle bundle = ResourceBundle.getBundle("jdbcConfig");
        String driver = bundle.getString("driverManager");
        String url = bundle.getString("url");
        String user = bundle.getString("user");
        String password = bundle.getString("password");
        try{
            Class.forName(driver);
        }catch (Exception e){
            e.printStackTrace();
        }

        try{
            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static Connection getConnection(){return conn;}

    public static void release(ResultSet set, Statement stmt, Connection connection) throws SQLException{
        if(set != null){
            set.close();
        }
        if(stmt!= null){
            stmt.close();
        }
        if(connection != null)
        {
            connection.close();
        }
    }
}
