package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    public static Connection getConnection() {
        // Địa chỉ kết nối: server là localhost, port 1433, tên DB là AirportManagement
        String url = "jdbc:sqlserver://localhost:1433;databaseName=AirportManagement;encrypt=true;trustServerCertificate=true;";
        String user = "sa"; 
        String pass = "123"; // Dũng kiểm tra xem pass sa của bạn là gì nhé

        try {
            // Đăng ký driver (bắt buộc với một số bản Java cũ)
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, pass);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Loi ket noi SQL: " + e.getMessage());
            return null;
        }
    }

    // Hàm main để Dũng chạy thử xem kết nối được chưa
    public static void main(String[] args) {
        if (getConnection() != null) {
            System.out.println("Ngon, ket noi thanh cong!");
        } else {
            System.out.println("Chua ket noi");
        }
    }
}