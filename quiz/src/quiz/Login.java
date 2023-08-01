package quiz;

import java.util.Scanner;
import java.sql.*;
public class Login {

    public static void main(String[] args) {
        
        String DB_URL = "jdbc:mysql://localhost:3306/quiz_application";
        String DB_USERNAME = "root";
        String DB_PASSWORD = "TIGER";
        
        try {
           
            Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
            System.out.println("\t \t <====================>Login<====================>");
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter your  username: ");
            String username = scanner.nextLine();
            System.out.print("Enter your  password: ");
            String password = scanner.nextLine();

         
            if (createAccount(conn, username, password)) {
                System.out.println("Account created successfully!");
            } else {
                System.out.println("Failed to create the account. Please try again.");
            }

  
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Quiz qz = new Quiz();
        qz.begin();
    }

    private static boolean createAccount(Connection conn, String username, String password) throws SQLException {
        String createAccountSQL = "INSERT INTO user_accounts (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(createAccountSQL)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        }      
    }
    
}
