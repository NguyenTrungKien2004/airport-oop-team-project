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
            this.conn = new DBContext().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Chức năng Đặt vé: Có kiểm tra trạng thái Boarding trước khi chèn
    public boolean bookTicket(int userID, int flightID, String seatNumber) {
        // 1. Kiểm tra trạng thái chuyến bay
        String sqlCheck = "SELECT Status FROM Flights WHERE FlightID = ?";
        // 2. Lệnh chèn vé
        String sqlInsert = "INSERT INTO Tickets (UserID, FlightID, SeatNumber, Status) VALUES (?, ?, ?, N'Confirmed')";

        try {
            // Bước 1: Check trạng thái từ Database
            try (PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
                psCheck.setInt(1, flightID);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next()) {
                    String currentStatus = rs.getString("Status");
                    // Nếu là Boarding thì trả về false ngay, không thực hiện Insert
                    if ("Boarding".equalsIgnoreCase(currentStatus)) {
                        return false; 
                    }
                }
            }

            // Bước 2: Thực hiện đặt vé nếu trạng thái hợp lệ
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
                t.setDepartureTime(rs.getTimestamp("DepartureTime").toString());
                t.setStatus(rs.getString("Status"));
                list.add(t);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}