package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class FlightsDashboard extends JFrame {
    private JTable flightTable;
    private DefaultTableModel tableModel;
    private JButton btnAdd, btnUpdate, btnDelete, btnBackAdmin;
    private JComboBox<String> sortComboBox;

    public FlightsDashboard(int roleID) {
        setTitle("Flights Dashboard - Airport Management");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        String[] columns = { "ID", "Mã chuyến bay", "Điểm đi", "Điểm đến", "Thời gian khởi hành", "Thời gian đến",
                "Cổng", "Trạng thái" };
        tableModel = new DefaultTableModel(columns, 0);
        flightTable = new JTable(tableModel);

        // Không cho sửa trực tiếp trên bảng nếu muốn
        flightTable.setDefaultEditor(Object.class, null);

        // ẨN CỘT ID
        flightTable.getColumnModel().getColumn(0).setMinWidth(0);
        flightTable.getColumnModel().getColumn(0).setMaxWidth(0);
        flightTable.getColumnModel().getColumn(0).setWidth(0);
        flightTable.getColumnModel().getColumn(0).setPreferredWidth(0);

        // Tự co cột
        flightTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Set width từng cột còn lại
        flightTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        flightTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        flightTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        flightTable.getColumnModel().getColumn(4).setPreferredWidth(150);
        flightTable.getColumnModel().getColumn(5).setPreferredWidth(150);
        flightTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        flightTable.getColumnModel().getColumn(7).setPreferredWidth(100);

        JScrollPane scrollPane = new JScrollPane(flightTable);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnAdd = new JButton("Thêm chuyến bay");
        btnUpdate = new JButton("Chỉnh sửa chuyến bay");
        btnDelete = new JButton("Hủy chuyến bay");
        btnBackAdmin = new JButton("Quay về trang Admin");

        sortComboBox = new JComboBox<>(new String[] { "Thời gian", "Mã Chuyến Bay" });

        controlPanel.add(new JLabel("Sắp xếp"));
        controlPanel.add(sortComboBox);
        controlPanel.add(btnAdd);
        controlPanel.add(btnUpdate);
        controlPanel.add(btnDelete);

        // CHỈ HIỆN NÚT NẾU ĐI TỪ ADMIN
        if (roleID == 1) { // chỉ Admin mới thấy
            controlPanel.add(btnBackAdmin);
        }

        // Xử lý quay lại Admin
        btnBackAdmin.addActionListener(e -> {
            new AdminDashboard().setVisible(true);
            dispose();
        });

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

    public JButton getBtnBackAdmin() {
        return btnBackAdmin;
    }

    public JComboBox<String> getSortComboBox() {
        return sortComboBox;
    }

}
