package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import util.DBContext;

public class CheckInDAO extends DBContext {

    // Tìm UserID bằng Username
    public int getUserIDByUsername(String username) {
        String sql = "SELECT UserID FROM Users WHERE Username = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("UserID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // Kiểm tra xem vé của hành khách này đã Check-in chưa
    public boolean hasCheckedIn(int flightID, int userID) {
        String sql = "SELECT IsCheckedIn FROM Tickets WHERE FlightID = ? AND UserID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, flightID);
            ps.setInt(2, userID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBoolean("IsCheckedIn");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Mặc định nếu không tìm thấy vé
    }

    // Kiểm tra chuyến bay có hợp lệ không (Phải là Boarding hoặc Delayed)
    public boolean isFlightValidForCheckIn(int flightID) {
        String sql = "SELECT Status FROM Flights WHERE FlightID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, flightID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String status = rs.getString("Status");
                return status.equalsIgnoreCase("Boarding") || status.equalsIgnoreCase("Delayed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Kiểm tra xem ghế đã bị người khác chọn chưa
    public boolean isSeatAvailable(int flightID, String seatNumber) {
        String sql = "SELECT * FROM Tickets WHERE FlightID = ? AND SeatNumber = ? AND IsCheckedIn = 1";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, flightID);
            ps.setString(2, seatNumber);
            ResultSet rs = ps.executeQuery();
            return !rs.next(); // Trả về true nếu ghế chưa ai check-in
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật trạng thái Check-in (Xác nhận lên máy bay và update ghế)
    public boolean performCheckIn(int flightID, int userID, String seatNumber) {
        String sql = "UPDATE Tickets SET IsCheckedIn = 1, SeatNumber = ? WHERE FlightID = ? AND UserID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, seatNumber);
            ps.setInt(2, flightID);
            ps.setInt(3, userID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
