package controller;

import dao.TicketDAO;
import dao.FlightDAO;
import model.Flight;
import model.Ticket;
import view.PassengerFrame;
import view.LoginView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.stream.Collectors;

public class BookingController {
    private PassengerFrame view;
    private TicketDAO ticketDAO;
    private FlightDAO flightDAO;
    private List<Flight> allFlights; // Lưu toàn bộ danh sách để dùng khi lọc

    public BookingController(PassengerFrame view) {
        this.view = view;
        this.ticketDAO = new TicketDAO();
        this.flightDAO = new FlightDAO();
        allFlights = flightDAO.getAllFlights();
        loadData();

        // TÌM KIẾM CHUYẺN BAY
        this.view.btnSearch.addActionListener(e -> filterFlights());
        // Cho phép nhấn Enter ở ô tìm kiếm để trigger search
        this.view.txtSearch.addActionListener(e -> filterFlights());

        // XỬ LÝ ĐẶT VÉ + CHỌN GHẾ
        this.view.btnBook.addActionListener(e -> {
            int row = view.tblFlights.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn một chuyến bay!");
                return;
            }

            int flightID = (int) view.tblFlights.getValueAt(row, 0);

            // Hiện thông báo nhập số ghế
            String seat = JOptionPane.showInputDialog(view, "Nhập số ghế muốn chọn (Ví dụ: A01, B15):");

            if (seat != null && !seat.trim().isEmpty()) {
                if (ticketDAO.bookTicket(view.getCurrentUserID(), flightID, seat)) {
                    JOptionPane.showMessageDialog(view, "Đặt vé thành công! Ghế: " + seat);
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(view, "Đặt vé thất bại!");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Bạn chưa nhập số ghế!");
            }
        });

        // XỬ LÝ XÓA VÉ
        this.view.btnDelete.addActionListener(e -> {
            int row = view.tblMyTickets.getSelectedRow();
            if (row != -1) {
                int ticketID = (int) view.tblMyTickets.getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn hủy vé này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (ticketDAO.deleteTicket(ticketID)) {
                        JOptionPane.showMessageDialog(view, "Đã xóa vé!");
                        loadData();
                    }
                }
            }
        });

        this.view.btnRefresh.addActionListener(e -> loadData());

        // ĐĂNG XUẤT
        this.view.btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                view.dispose();
                new LoginView().setVisible(true);
            }
        });
    }

    // Lọc danh sách chuyến bay theo từ khoá
    private void filterFlights() {
        String keyword = view.txtSearch.getText().trim().toLowerCase();
        List<Flight> filtered;
        if (keyword.isEmpty()) {
            filtered = allFlights;
        } else {
            filtered = allFlights.stream()
                .filter(f -> f.getDepartureCity().toLowerCase().contains(keyword)
                          || f.getDestinationCity().toLowerCase().contains(keyword)
                          || f.getFlightNumber().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        }
        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Số hiệu", "Đi", "Đến", "Giờ đi", "Trạng thái"}, 0);
        for (Flight f : filtered) {
            m.addRow(new Object[]{f.getFlightID(), f.getFlightNumber(), f.getDepartureCity(), f.getDestinationCity(), f.getDepartureTime(), f.getStatus()});
        }
        view.tblFlights.setModel(m);
        // Ẩn cột ID
        view.tblFlights.getColumnModel().getColumn(0).setMinWidth(0);
        view.tblFlights.getColumnModel().getColumn(0).setMaxWidth(0);
    }

    private void loadData() {
        // Cập nhật allFlights để search luôn có dữ liệu mới
        allFlights = flightDAO.getAllFlights();

        // Load bảng Chuyến bay
        DefaultTableModel m1 = new DefaultTableModel(new String[]{"ID", "Số hiệu", "Đi", "Đến", "Giờ đi", "Trạng thái"}, 0);
        for (Flight f : allFlights) {
            m1.addRow(new Object[]{f.getFlightID(), f.getFlightNumber(), f.getDepartureCity(), f.getDestinationCity(), f.getDepartureTime(), f.getStatus()});
        }
        view.tblFlights.setModel(m1);
        // Ẩn cột ID khỏi màn hình nhưng vẫn dùng được để đặt vé
        view.tblFlights.getColumnModel().getColumn(0).setMinWidth(0);
        view.tblFlights.getColumnModel().getColumn(0).setMaxWidth(0);

        // Load bảng Vé của tôi (Có cột Số Ghế)
        DefaultTableModel m2 = new DefaultTableModel(new String[]{"Mã vé", "Số hiệu", "Số ghế", "Từ", "Đến", "Trạng thái"}, 0);
        for (Ticket t : ticketDAO.getTicketsByUserId(view.getCurrentUserID())) {
            m2.addRow(new Object[]{t.getTicketID(), t.getFlightNumber(), t.getSeatNumber(), t.getDepartureCity(), t.getDestinationCity(), t.getStatus()});
        }
        view.tblMyTickets.setModel(m2);
    }
}