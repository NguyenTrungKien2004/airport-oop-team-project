package controller;

import dao.FlightDAO;
import model.Flight;
import view.FlightsDashboard;
import view.LoginView;

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
                        "Thời gian tới dự định ", txtArrival,
                        "Cổng:", txtGate,
                        "Tổng số ghế:", txtSeats,
                        "Trạng thái(Scheduled, Delayed, và Boarding):", txtStatus
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
                        "Trạng thái(Scheduled, Delayed, và Boarding):", txtStatus
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

        // ĐĂNG XUẤT
        view.getBtnLogout().addActionListener(e -> {
            int confirm = javax.swing.JOptionPane.showConfirmDialog(view, "Bạn có chắc muốn đăng xuất?", "Đăng xuất", javax.swing.JOptionPane.YES_NO_OPTION);
            if (confirm == javax.swing.JOptionPane.YES_OPTION) {
                view.dispose();
                new LoginView().setVisible(true);
            }
        });

        view.getBtnCheckIn().addActionListener(e -> {
            int row = view.getFlightTable().getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Vui lòng chọn một chuyến bay để check-in!");
                return;
            }

            // Get the flight info
            int flightID = (int) view.getTableModel().getValueAt(row, 0);
            String flightNo = view.getTableModel().getValueAt(row, 1).toString();
            String from = view.getTableModel().getValueAt(row, 2).toString();
            String to = view.getTableModel().getValueAt(row, 3).toString();
            String status = view.getTableModel().getValueAt(row, 7).toString();

            if (!status.equalsIgnoreCase("Boarding") && !status.equalsIgnoreCase("Delayed")) {
                JOptionPane.showMessageDialog(null, "Từ chối check-in: Trạng thái chuyến bay không hợp lệ!\n(Chỉ cho phép Check-in khi Boarding hoặc Delayed)", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Construct currentFlight for CheckInView
            Flight currentFlight = new Flight();
            currentFlight.setFlightID(flightID);
            currentFlight.setFlightNumber(flightNo);
            currentFlight.setDepartureCity(from);
            currentFlight.setDestinationCity(to);

            // Open Check-in View
            view.CheckInView checkInView = new view.CheckInView(currentFlight);
            new controller.CheckInController(checkInView);
            checkInView.setVisible(true);
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