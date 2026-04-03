package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FlightsDashboard extends JFrame {
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete;
    private JComboBox<String> sortComboBox;

    public FlightsDashboard() {
        setTitle("Flights Dashboard - Airport Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = { "ID", "Flight No", "From", "To", "Departure", "Arrival", "Gate", "Status" };
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(flightTable);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdd = new JButton("Add Flight");
        btnUpdate = new JButton("Reschedule");
        btnDelete = new JButton("Cancel Flight");

        sortComboBox = new JComboBox<>(new String[] { "Time", "Flight Number", "Destination" });

        controlPanel.add(new JLabel("Sort by: "));
        controlPanel.add(sortComboBox);
        controlPanel.add(btnAdd);
        controlPanel.add(btnUpdate);
        controlPanel.add(btnDelete);

        add(controlPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    public JTable getFlightTable() {
        return flightTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public JComboBox<String> getSortComboBox() {
        return sortComboBox;
    }
}
