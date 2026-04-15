package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import model.Flight;

public class CheckInView extends JFrame {
    private JTable ticketTable;
    private DefaultTableModel tableModel;
    private JButton btnCheckIn;
    private JTextField txtSeatNumber;
    private Flight currentFlight;

    public CheckInView(Flight flight) {
        this.currentFlight = flight;
        setTitle("Check-In: Chuyến bay " + flight.getFlightNumber());
        setSize(800, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.add(new JLabel("Đang làm thủ tục cho chuyến bay: " + flight.getFlightNumber() + " (" + flight.getDepartureCity() + " ⟶ " + flight.getDestinationCity() + ")"));
        add(headerPanel, BorderLayout.NORTH);

        String[] columns = {"Ticket ID", "Hành Khách (Username)", "Số Ghế", "Trạng thái"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        ticketTable = new JTable(tableModel);
        add(new JScrollPane(ticketTable), BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(new JLabel("Nhập số ghế:"));
        txtSeatNumber = new JTextField(10);
        bottomPanel.add(txtSeatNumber);
        
        btnCheckIn = new JButton("Xác nhận Check-in");
        bottomPanel.add(btnCheckIn);
        
        add(bottomPanel, BorderLayout.SOUTH);
    }

    public DefaultTableModel getTableModel() { return tableModel; }
    public JTable getTicketTable() { return ticketTable; }
    public JButton getBtnCheckIn() { return btnCheckIn; }
    public JTextField getTxtSeatNumber() { return txtSeatNumber; }
    public Flight getCurrentFlight() { return currentFlight; }
}