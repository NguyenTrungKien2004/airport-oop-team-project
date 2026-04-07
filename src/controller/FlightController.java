package controller;

import dao.FlightDAO;
import model.Flight;
import view.FlightsDashboard;

import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import java.sql.Timestamp;
import java.util.List;

public class FlightController {
    private FlightsDashboard view;
    private FlightDAO dao;

    public FlightController(FlightsDashboard view) {
        this.view = view;
        this.dao = new FlightDAO();
        initController();
    }

    private void initController() {
        refreshTable(dao.getAllFlights());

        view.getSortComboBox().addActionListener(e -> {
            String selected = (String) view.getSortComboBox().getSelectedItem();
            refreshTable(dao.getFlightsSorted(selected));
        });

        view.getBtnDelete().addActionListener(e -> {
            int row = view.getFlightTable().getSelectedRow();
            if (row != -1) {
                int id = (int) view.getTableModel().getValueAt(row, 0);
                if (dao.deleteFlight(id)) {
                    refreshTable(dao.getAllFlights());
                }
            }
        });

        // THỰC HIỆN CHECK-IN & GÁN GHẾ
        view.getBtnCheckIn().addActionListener(e -> {
            int row = view.getFlightTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn 1 chuyến bay để Check-in!");
                return;
            }

            int flightID = (int) view.getTableModel().getValueAt(row, 0);
            String status = view.getTableModel().getValueAt(row, 7).toString();
            String flightNo = view.getTableModel().getValueAt(row, 1).toString();

            // 1. Kiểm tra trạng thái chuyến bay (Rule)
            if (!status.equalsIgnoreCase("Boarding") && !status.equalsIgnoreCase("Delayed")) {
                JOptionPane.showMessageDialog(null, "Chuyến bay " + flightNo + " hiện đang '" + status + "'. Không thể mở quầy Check-in!", "Từ chối Check-in", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // 2. Hiển thị Popup nhập User và Gán ghế
            JTextField txtUser = new JTextField();
            JTextField txtSeat = new JTextField("12A");
            Object[] message = {
                "Nhập tên đăng nhập Hành khách (Đã mua vé):", txtUser,
                "Chỉ định Ghế (Ví dụ: 12A, 14F):", txtSeat
            };

            int option = JOptionPane.showConfirmDialog(null, message, "Check-in cho chuyến " + flightNo, JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                String username = txtUser.getText().trim();
                String seat = txtSeat.getText().trim();
                
                if (username.isEmpty() || seat.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Tên hành khách và Số ghế không được để trống!");
                    return;
                }

                dao.CheckInDAO checkInDAO = new dao.CheckInDAO();
                int userID = checkInDAO.getUserIDByUsername(username);

                if (userID == -1) {
                    JOptionPane.showMessageDialog(null, "Không tìm thấy Hành khách nào tên '" + username + "' trong hệ thống!", "Lỗi xác thực", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (checkInDAO.hasCheckedIn(flightID, userID)) {
                    JOptionPane.showMessageDialog(null, "Hành khách '" + username + "' đã được Check-in rồi!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!checkInDAO.isSeatAvailable(flightID, seat)) {
                    JOptionPane.showMessageDialog(null, "Ghế '" + seat + "' đã có hành khách khác chiếm chỗ! Vui lòng chỉ định ghế khác.", "Lỗi gán ghế", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                // 3. Thực hiện Gán ghế & Cập nhật Check-in
                if (checkInDAO.performCheckIn(flightID, userID, seat)) {
                    JOptionPane.showMessageDialog(null, "✅ Check-in Thành công!\nKhách: " + username + "\nGhế: " + seat + "\nLên chuyến bay: " + flightNo);
                } else {
                    JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật ghế hoặc Hành khách này chưa mua vé cho chuyến bay này (Dữ liệu vé rỗng).", "Lỗi CSDL", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Add Logic for Add and Update buttons similarly using Dialogs for input

        // ADD FLIGHT
        view.getBtnAdd().addActionListener(e -> {
            try {
                JTextField txtFlightNo = new JTextField();
                JTextField txtFrom = new JTextField();
                JTextField txtTo = new JTextField();
                JTextField txtDeparture = new JTextField("2026-01-01 10:00:00");
                JTextField txtArrival = new JTextField("2026-01-01 12:00:00");
                JTextField txtGate = new JTextField();
                JTextField txtSeats = new JTextField();
                JTextField txtStatus = new JTextField();

                Object[] message = {
                        "Mã số chuyến bay:", txtFlightNo,
                        "Điểm đi:", txtFrom,
                        "Điểm đến:", txtTo,
                        "Thời gian khởi hành", txtDeparture,
                        "Thời gian ", txtArrival,
                        "Cổng:", txtGate,
                        "Tổng số ghế:", txtSeats,
                        "Trạng thái:", txtStatus
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Thêm chuyến bay",
                        JOptionPane.OK_CANCEL_OPTION);
                if (option == JOptionPane.OK_OPTION) {
                    Flight f = new Flight(
                            0,
                            txtFlightNo.getText(),
                            txtFrom.getText(),
                            txtTo.getText(),
                            Timestamp.valueOf(txtDeparture.getText()),
                            Timestamp.valueOf(txtArrival.getText()),
                            txtGate.getText(),
                            Integer.parseInt(txtSeats.getText()),
                            txtStatus.getText());

                    if (dao.addFlight(f)) {
                        JOptionPane.showMessageDialog(null, "Thêm thành công!");
                        refreshTable(dao.getAllFlights());
                    } else {
                        JOptionPane.showMessageDialog(null, "Thêm thất bại!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Giá trị nhập vào không hợp lệ!");
            }
        });

        view.getBtnUpdate().addActionListener(e -> {
            int row = view.getFlightTable().getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một chuyến bay!");
                return;
            }

            try {
                int id = (int) view.getTableModel().getValueAt(row, 0);

                JTextField txtDeparture = new JTextField(view.getTableModel().getValueAt(row, 4).toString());
                JTextField txtArrival = new JTextField(view.getTableModel().getValueAt(row, 5).toString());
                JTextField txtGate = new JTextField(view.getTableModel().getValueAt(row, 6).toString());
                JTextField txtStatus = new JTextField(view.getTableModel().getValueAt(row, 7).toString());

                Object[] message = {
                        "Thời gian khởi hành:", txtDeparture,
                        "Thời gian đến:", txtArrival,
                        "Cổng:", txtGate,
                        "Trạng thái:", txtStatus
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Chỉnh sửa chuyến bay",
                        JOptionPane.OK_CANCEL_OPTION);

                if (option == JOptionPane.OK_OPTION) {
                    Flight f = new Flight(
                            id,
                            "", "", "", // không cần update mấy field này
                            Timestamp.valueOf(txtDeparture.getText()),
                            Timestamp.valueOf(txtArrival.getText()),
                            txtGate.getText(),
                            0,
                            txtStatus.getText());

                    if (dao.updateFlight(f)) {
                        JOptionPane.showMessageDialog(null, "Cập nhật thành công!");
                        refreshTable(dao.getAllFlights());
                    } else {
                        JOptionPane.showMessageDialog(null, "Cập nhật thất bại!");
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Giá trị nhập vào không hợp lệ!");
            }
        });
    }

    private void refreshTable(List<Flight> flights) {
        DefaultTableModel model = view.getTableModel();
        model.setRowCount(0);
        for (Flight f : flights) {
            model.addRow(new Object[] {
                    f.getFlightID(),
                    f.getFlightNumber(),
                    f.getDepartureCity(),
                    f.getDestinationCity(),
                    f.getDepartureTime(),
                    f.getArrivalTime(),
                    f.getGate(),
                    f.getStatus()
            });
        }
    }
}