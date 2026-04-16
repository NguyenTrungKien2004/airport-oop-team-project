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
    private List<Flight> allFlights;

    public BookingController(PassengerFrame view) {
        this.view = view;
        this.ticketDAO = new TicketDAO();
        this.flightDAO = new FlightDAO();
        allFlights = flightDAO.getAllFlights();
        loadData();

        // TÌM KIẾM
        this.view.btnSearch.addActionListener(e -> filterFlights());
        this.view.txtSearch.addActionListener(e -> filterFlights());

        // XỬ LÝ ĐẶT VÉ
        this.view.btnBook.addActionListener(e -> {
            int row = view.tblFlights.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn một chuyến bay!");
                return;
            }

            // Kiểm tra trạng thái Boarding trước
            String status = view.tblFlights.getValueAt(row, 5).toString();
            if ("Boarding".equalsIgnoreCase(status)) {
                JOptionPane.showMessageDialog(view, "Chuyến bay đang Boarding, không thể đặt thêm vé!", "Lỗi", JOptionPane.WARNING_MESSAGE);
                return; 
            }

            int flightID = (int) view.tblFlights.getValueAt(row, 0);
            String seat = JOptionPane.showInputDialog(view, "Nhập số ghế muốn chọn (1-100):");

            if (seat != null && !seat.trim().isEmpty()) {
                // Thực hiện đặt vé
                if (ticketDAO.bookTicket(view.getCurrentUserID(), flightID, seat.trim())) {
                    JOptionPane.showMessageDialog(view, "Đặt vé thành công! Số ghế: " + seat);
                    loadData();
                } else {
                    // Nếu thất bại ở đây, thông báo ghế đã bị trùng
                    JOptionPane.showMessageDialog(view, "Ghế số [" + seat + "] đã có người đặt. Vui lòng chọn ghế khác!", "Lỗi đặt vé", JOptionPane.ERROR_MESSAGE);
                }
            } else if (seat != null) {
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
                        JOptionPane.showMessageDialog(view, "Đã hủy vé thành công!");
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

    private void filterFlights() {
        String keyword = view.txtSearch.getText().trim().toLowerCase();
        List<Flight> filtered = keyword.isEmpty() ? allFlights : allFlights.stream()
                .filter(f -> f.getDepartureCity().toLowerCase().contains(keyword)
                          || f.getDestinationCity().toLowerCase().contains(keyword)
                          || f.getFlightNumber().toLowerCase().contains(keyword))
                .collect(Collectors.toList());
        updateFlightTable(filtered);
    }

    private void loadData() {
        allFlights = flightDAO.getAllFlights();
        updateFlightTable(allFlights);

        DefaultTableModel m2 = new DefaultTableModel(new String[]{"Mã vé", "Số hiệu", "Số ghế", "Từ", "Đến", "Trạng thái"}, 0);
        for (Ticket t : ticketDAO.getTicketsByUserId(view.getCurrentUserID())) {
            m2.addRow(new Object[]{t.getTicketID(), t.getFlightNumber(), t.getSeatNumber(), t.getDepartureCity(), t.getDestinationCity(), t.getStatus()});
        }
        view.tblMyTickets.setModel(m2);
    }

    private void updateFlightTable(List<Flight> flights) {
        DefaultTableModel m = new DefaultTableModel(new String[]{"ID", "Số hiệu", "Đi", "Đến", "Giờ đi", "Trạng thái"}, 0);
        for (Flight f : flights) {
            m.addRow(new Object[]{f.getFlightID(), f.getFlightNumber(), f.getDepartureCity(), f.getDestinationCity(), f.getDepartureTime(), f.getStatus()});
        }
        view.tblFlights.setModel(m);
        view.tblFlights.getColumnModel().getColumn(0).setMinWidth(0);
        view.tblFlights.getColumnModel().getColumn(0).setMaxWidth(0);
    }
}