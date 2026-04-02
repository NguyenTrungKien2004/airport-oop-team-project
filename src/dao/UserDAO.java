package dao;

import config.DatabaseConnection;
import model.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {
    // Hàm kiểm tra đăng nhập
    public User login(String user, String pass) {
        String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new User(rs.getInt("UserID"), rs.getString("Username"), 
                                rs.getString("FullName"), rs.getInt("RoleID"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return null;
    }

    // Hàm lấy danh sách 4 anh em cho Admin Kiên
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM Users";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                list.add(new User(rs.getInt("UserID"), rs.getString("Username"), 
                                  rs.getString("FullName"), rs.getInt("RoleID")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }
}