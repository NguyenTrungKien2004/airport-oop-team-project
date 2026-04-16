package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBContext {
    protected Connection connection; // Bổ sung này để các DAO kế thừa dùng được

    public static Connection getConnection() throws Exception {
        // Kiểm tra đúng tên Database là AirportManagement
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AirportManagement;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String pass = "123"; 
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, user, pass);
    }

    /// Hàm khởi tạo để các DAO kế thừa có thể tự động kết nối khi tạo instance
    public DBContext() {
        try {
            String url = "jdbc:sqlserver://localhost:1433;databaseName=AirportManagement;encrypt=true;trustServerCertificate=true";
            String user = "sa";
            String pass = "123";
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //
    public static void main(String[] args) {
        try {
            Connection conn = getConnection();
            if (conn != null) {
                System.out.println("KET NOI THANH CONG.");
            }
        } catch (Exception e) {
            System.err.println("KET NOI THAT BAI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}