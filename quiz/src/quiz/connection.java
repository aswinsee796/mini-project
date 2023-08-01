package quiz;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connection {
 private static Connection con;

 public static Connection getConnection() {
     try {
         if (con == null || con.isClosed()) {
             String mysqlJDBCDriver = "com.mysql.cj.jdbc.Driver";
             String url = "jdbc:mysql://localhost:3306/quiz_application";
             String user = "root";
             String pass = "TIGER";
             Class.forName(mysqlJDBCDriver);
             con = DriverManager.getConnection(url, user, pass);
         }
     } catch (ClassNotFoundException | SQLException e) {
         e.printStackTrace();
     }

     return con;
 }
}