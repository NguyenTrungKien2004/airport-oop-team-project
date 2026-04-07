package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.User;
import util.DBContext;

public class UserDAO {
    // Hàm kiểm tra đăng nhập
    public User checkLogin(String user, String pass) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT * FROM Users WHERE Username = ? AND Password = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getInt("UserID"), rs.getString("Username"), 
                                rs.getString("Password"), rs.getString("FullName"), rs.getInt("RoleID"));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    // Hàm lấy danh sách cho Admin
    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Connection conn = DBContext.getConnection()) {
            String sql = "SELECT * FROM Users";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new User(rs.getInt("UserID"), rs.getString("Username"), 
                                  rs.getString("Password"), rs.getString("FullName"), rs.getInt("RoleID")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }
    // 1. Hàm Xóa User theo ID
    public boolean deleteUser(int id) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "DELETE FROM Users WHERE UserID = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Hàm Thêm User mới
    public boolean addUser(User u) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "INSERT INTO Users (Username, Password, FullName, RoleID) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setInt(4, u.getRoleID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(null, "Lỗi Database thực sự: " + e.getMessage(), "Lỗi Hệ thống", javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // 3. Hàm Sửa User
    public boolean updateUser(User u) {
        try (Connection conn = DBContext.getConnection()) {
            String sql = "UPDATE Users SET Username=?, Password=?, FullName=?, RoleID=? WHERE UserID=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPassword());
            ps.setString(3, u.getFullName());
            ps.setInt(4, u.getRoleID());
            ps.setInt(5, u.getUserID());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}