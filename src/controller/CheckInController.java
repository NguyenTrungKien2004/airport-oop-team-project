package controller;

import dao.CheckInDAO;
import model.Flight;
import model.Ticket;
import view.CheckInView;

import javax.swing.JOptionPane;
import java.util.List;

public class CheckInController {
    private CheckInView view;
    private CheckInDAO dao;

    public CheckInController(CheckInView view) {
        this.view = view;
        this.dao = new CheckInDAO();
        initController();
    }

    private void initController() {
        refreshTable();

        // [TÍNH NĂNG MỚI]: Khi bấm vào 1 khách hàng, tự động điền số ghế đã đặt vào ô Text để nhân viên khỏi phải gõ lại
        view.getTicketTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = view.getTicketTable().getSelectedRow();
                if (row != -1) {
                    String existingSeat = (String) view.getTableModel().getValueAt(row, 2);
                    if (existingSeat != null && !existingSeat.equals("Chưa gán")) {
                        view.getTxtSeatNumber().setText(existingSeat);
                    } else {
                        view.getTxtSeatNumber().setText("");
                    }
                }
            }
        });

        view.getBtnCheckIn().addActionListener(e -> {
            int selectedRow = view.getTicketTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, "Vui lòng chọn một hành khách để check-in!");
                return;
            }

            String currentStatus = (String) view.getTableModel().getValueAt(selectedRow, 3);
            if ("Checked-in".equalsIgnoreCase(currentStatus)) {
                JOptionPane.showMessageDialog(view, "Hành khách này đã check-in rồi!");
                return;
            }

            String seatNumber = view.getTxtSeatNumber().getText().trim();
            if (seatNumber.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Vui lòng nhập số ghế!");
                return;
            }

            int ticketID = (int) view.getTableModel().getValueAt(selectedRow, 0);

            // [VALIDATION DO USER YÊU CẦU]: Kiểm tra xem ghế nhập vào có bị trùng với người khác trên cùng chuyến bay không
            if (dao.isSeatOccupied(view.getCurrentFlight().getFlightID(), seatNumber, ticketID)) {
                JOptionPane.showMessageDialog(view, "Ghế [" + seatNumber + "] đã có người ngồi hoặc được đặt trước! Vui lòng chỉ định ghế khác.", "Lỗi trùng ghế", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (dao.confirmCheckIn(ticketID, seatNumber)) {
                JOptionPane.showMessageDialog(view, "Check-in thành công cho vé #" + ticketID);
                view.getTxtSeatNumber().setText("");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(view, "Có lỗi xảy ra khi check-in!");
            }
        });
    }

    private void refreshTable() {
        int flightID = view.getCurrentFlight().getFlightID();
        List<Ticket> tickets = dao.getTicketsForFlight(flightID);
        view.getTableModel().setRowCount(0);
        
        for (Ticket t : tickets) {
            view.getTableModel().addRow(new Object[]{
                t.getTicketID(),
                t.getPassengerName(),
                (t.getSeatNumber() == null) ? "Chưa gán" : t.getSeatNumber(),
                t.getStatus()
            });
        }
    }
}