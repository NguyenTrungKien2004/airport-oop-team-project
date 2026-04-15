package dao;

import model.Ticket;
import util.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CheckInDAO extends DBContext {
    
    // Lấy danh sách hành khách cho 1 chuyến bay cụ thể
    public List<Ticket> getTicketsForFlight(int flightID) {
        List<Ticket> list = new ArrayList<>();
        // JOIN bảng Users để lấy Username hiển thị làm tên Hành khách
        String sql = "SELECT t.TicketID, t.UserID, u.Username, t.SeatNumber, t.Status, t.FlightID " +
                     "FROM Tickets t " +
                     "JOIN Users u ON t.UserID = u.UserID " +
                     "WHERE t.FlightID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, flightID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setUserID(rs.getInt("UserID"));
                t.setPassengerName(rs.getString("Username"));
                t.setSeatNumber(rs.getString("SeatNumber"));
                t.setStatus(rs.getString("Status"));
                t.setFlightID(rs.getInt("FlightID"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // Hoàn tất check-in và gán ghế
    public boolean confirmCheckIn(int ticketID, String seatNumber) {
        String sql = "UPDATE Tickets SET Status = ?, SeatNumber = ? WHERE TicketID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, "Checked-in");
            ps.setString(2, seatNumber);
            ps.setInt(3, ticketID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Kiểm tra xem ghế đã bị người khác chọn trước đó (thuộc cùng 1 chuyến bay) hay chưa?
    public boolean isSeatOccupied(int flightID, String seatNumber, int currentTicketID) {
        String sql = "SELECT COUNT(*) FROM Tickets WHERE FlightID = ? AND SeatNumber = ? AND TicketID != ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, flightID);
            ps.setString(2, seatNumber);
            ps.setInt(3, currentTicketID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu lớn hơn 0 nghĩa là đã có người ngồi
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}