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
                        "Flight Number:", txtFlightNo,
                        "From:", txtFrom,
                        "To:", txtTo,
                        "Departure (yyyy-MM-dd HH:mm:ss):", txtDeparture,
                        "Arrival (yyyy-MM-dd HH:mm:ss):", txtArrival,
                        "Gate:", txtGate,
                        "Total Seats:", txtSeats,
                        "Status:", txtStatus
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Add Flight", JOptionPane.OK_CANCEL_OPTION);
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
                        JOptionPane.showMessageDialog(null, "Add successful!");
                        refreshTable(dao.getAllFlights());
                    } else {
                        JOptionPane.showMessageDialog(null, "Add failed!");
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input!");
            }
        });

        view.getBtnUpdate().addActionListener(e -> {
            int row = view.getFlightTable().getSelectedRow();

            if (row == -1) {
                JOptionPane.showMessageDialog(null, "Please select a flight!");
                return;
            }

            try {
                int id = (int) view.getTableModel().getValueAt(row, 0);

                JTextField txtDeparture = new JTextField(view.getTableModel().getValueAt(row, 4).toString());
                JTextField txtArrival = new JTextField(view.getTableModel().getValueAt(row, 5).toString());
                JTextField txtGate = new JTextField(view.getTableModel().getValueAt(row, 6).toString());
                JTextField txtStatus = new JTextField(view.getTableModel().getValueAt(row, 7).toString());

                Object[] message = {
                        "Departure (yyyy-MM-dd HH:mm:ss):", txtDeparture,
                        "Arrival (yyyy-MM-dd HH:mm:ss):", txtArrival,
                        "Gate:", txtGate,
                        "Status:", txtStatus
                };

                int option = JOptionPane.showConfirmDialog(null, message, "Reschedule Flight",
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
                        JOptionPane.showMessageDialog(null, "Update successful!");
                        refreshTable(dao.getAllFlights());
                    } else {
                        JOptionPane.showMessageDialog(null, "Update failed!");
                    }
                }

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Invalid input!");
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