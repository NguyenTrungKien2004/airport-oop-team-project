package dao;

import model.Flight;
import util.DBContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO extends DBContext {

    // 1. Lấy toàn bộ danh sách chuyến bay
    public List<Flight> getAllFlights() {
        List<Flight> list = new ArrayList<>();
        // Câu lệnh SQL đã cập nhật tên cột: FlightNumber, DepartureCity, DestinationCity...
        String sql = "SELECT * FROM Flights";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Flight(
                    rs.getInt("FlightID"),
                    rs.getString("FlightNumber"), // Đã đổi từ FlightCode
                    rs.getString("DepartureCity"), // Đã đổi từ Departure
                    rs.getString("DestinationCity"), // Đã đổi từ Arrival
                    rs.getTimestamp("DepartureTime"),
                    rs.getTimestamp("ArrivalTime"),
                    rs.getString("Gate"),
                    rs.getInt("TotalSeats"),
                    rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // 2. Thêm chuyến bay mới (Sử dụng cho nút Add Flight)
    public boolean addFlight(Flight f) {
        String sql = "INSERT INTO Flights (FlightNumber, DepartureCity, DestinationCity, DepartureTime, ArrivalTime, Gate, TotalSeats, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, f.getFlightNumber());
            ps.setString(2, f.getDepartureCity());
            ps.setString(3, f.getDestinationCity());
            ps.setTimestamp(4, f.getDepartureTime());
            ps.setTimestamp(5, f.getArrivalTime());
            ps.setString(6, f.getGate());
            ps.setInt(7, f.getTotalSeats());
            ps.setString(8, f.getStatus());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 3. Cập nhật giờ bay và trạng thái (Sử dụng cho nút Reschedule)
    public boolean updateFlight(Flight f) {
        // Cập nhật dựa trên FlightID
        String sql = "UPDATE Flights SET DepartureTime = ?, ArrivalTime = ?, Status = ?, Gate = ? WHERE FlightID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setTimestamp(1, f.getDepartureTime());
            ps.setTimestamp(2, f.getArrivalTime());
            ps.setString(3, f.getStatus());
            ps.setString(4, f.getGate());
            ps.setInt(5, f.getFlightID());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 4. Hủy chuyến bay (Sử dụng cho nút Cancel Flight)
    public boolean deleteFlight(int id) {
        String sql = "DELETE FROM Flights WHERE FlightID = ?";
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Lưu ý: Nếu chuyến bay đã có người đặt vé, SQL sẽ chặn không cho xóa (lỗi khóa ngoại)
            e.printStackTrace();
        }
        return false;
    }

    // 5. Sắp xếp danh sách (Sử dụng cho JComboBox sắp xếp)
    public List<Flight> getFlightsSorted(String criteria) {
        List<Flight> list = new ArrayList<>();
        String column = "DepartureTime"; // Mặc định sắp xếp theo giờ đi
        
        // Chuyển đổi lựa chọn từ giao diện sang tên cột SQL
        if (criteria.equals("Flight Number")) column = "FlightNumber";
        else if (criteria.equals("Destination")) column = "DestinationCity";

        String sql = "SELECT * FROM Flights ORDER BY " + column;
        try {
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Flight(
                    rs.getInt("FlightID"),
                    rs.getString("FlightNumber"),
                    rs.getString("DepartureCity"),
                    rs.getString("DestinationCity"),
                    rs.getTimestamp("DepartureTime"),
                    rs.getTimestamp("ArrivalTime"),
                    rs.getString("Gate"),
                    rs.getInt("TotalSeats"),
                    rs.getString("Status")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}