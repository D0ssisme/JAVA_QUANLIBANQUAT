package DTO;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/quanlycuahangquat";
    private static String USER = "root";      
    private static String PASS = "";          

    private static Connection conn;

    private DBConnection() { }


    public static void setCredentials(String user, String pass) {
        USER = user;
        PASS = pass;
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException ignored) { }
            conn = null;
        }
    }

    public static Connection getConnection() throws SQLException {
        if (conn == null || conn.isClosed()) {
            conn = DriverManager.getConnection(URL, USER, PASS);
        }
        return conn;
    }
     public static void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                conn = null; 
            }
        }
    }
    
}
