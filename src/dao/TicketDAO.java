package dao;

import util.DBContext;
import model.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TicketDAO {
    private Connection conn;

    public TicketDAO() {
        try {
            this.conn = DBContext.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1. Chức năng Đặt vé: CHẶN TRÙNG GHẾ TUYỆT ĐỐI
    public boolean bookTicket(int userID, int flightID, String seatNumber) {
        // Kiểm tra xem ghế đã có ai đặt trên chuyến bay này chưa
        String sqlCheck = "SELECT COUNT(*) FROM Tickets WHERE FlightID = ? AND SeatNumber = ?";
        // Lệnh chèn vé mới
        String sqlInsert = "INSERT INTO Tickets (UserID, FlightID, SeatNumber, Status) VALUES (?, ?, ?, N'Confirmed')";

        try {
            // Bước A: Kiểm tra tính khả dụng của ghế
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, flightID);
                psCheck.setString(2, seatNumber);
                try (ResultSet rs = psCheck.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return false; // Ghế đã có người đặt, trả về thất bại
                    }
                }
            }

            // Bước B: Nếu ghế trống, tiến hành chèn vé
            try (PreparedStatement psInsert = conn.prepareStatement(sqlInsert)) {
                psInsert.setInt(1, userID);
                psInsert.setInt(2, flightID);
                psInsert.setString(3, seatNumber);
                return psInsert.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 2. Chức năng Xóa vé
    public boolean deleteTicket(int ticketID) {
        String sql = "DELETE FROM Tickets WHERE TicketID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, ticketID);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. Lấy danh sách vé của người dùng
    public List<Ticket> getTicketsByUserId(int userID) {
        List<Ticket> list = new ArrayList<>();
        String sql = "SELECT t.TicketID, t.SeatNumber, f.FlightNumber, f.DepartureCity, f.DestinationCity, f.DepartureTime, t.Status " +
                     "FROM Tickets t JOIN Flights f ON t.FlightID = f.FlightID WHERE t.UserID = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Ticket t = new Ticket();
                t.setTicketID(rs.getInt("TicketID"));
                t.setSeatNumber(rs.getString("SeatNumber"));
                t.setFlightNumber(rs.getString("FlightNumber"));
                t.setDepartureCity(rs.getString("DepartureCity"));
                t.setDestinationCity(rs.getString("DestinationCity"));
                
                Timestamp ts = rs.getTimestamp("DepartureTime");
                if (ts != null) t.setDepartureTime(ts.toString());
                
                t.setStatus(rs.getString("Status"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}