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

    // 1. Chức năng Đặt vé có kèm Số Ghế
    public boolean bookTicket(int userID, int flightID, String seatNumber) {
        String sql = "INSERT INTO Tickets (UserID, FlightID, SeatNumber, Status) VALUES (?, ?, ?, N'Confirmed')";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userID);
            ps.setInt(2, flightID);
            ps.setString(3, seatNumber);
            return ps.executeUpdate() > 0;
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

    // 3. Lấy danh sách vé (có kèm cột SeatNumber)
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