package view;

import javax.swing.*;
import java.awt.*;

public class PassengerFrame extends JFrame {
    public JTable tblFlights, tblMyTickets;
    public JButton btnBook, btnRefresh, btnDelete;
    private int currentUserID;

    public PassengerFrame(int userID) {
        this.currentUserID = userID;
        setTitle("Giao diện hành khách - ID: " + userID);
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JTabbedPane tabs = new JTabbedPane();

        // Tab đặt vé
        JPanel p1 = new JPanel(new BorderLayout());
        tblFlights = new JTable();
        btnBook = new JButton("Đặt vé & Chọn ghế");
        p1.add(new JScrollPane(tblFlights), BorderLayout.CENTER);
        p1.add(btnBook, BorderLayout.SOUTH);

        // Tab vé cá nhân
        JPanel p2 = new JPanel(new BorderLayout());
        tblMyTickets = new JTable();
        btnRefresh = new JButton("Làm mới");
        btnDelete = new JButton("Hủy vé đã chọn");
        
        JPanel p2Buttons = new JPanel();
        p2Buttons.add(btnRefresh);
        p2Buttons.add(btnDelete);
        
        p2.add(new JScrollPane(tblMyTickets), BorderLayout.CENTER);
        p2.add(p2Buttons, BorderLayout.SOUTH);

        tabs.addTab("Tìm & Đặt vé", p1);
        tabs.addTab("Vé của tôi", p2);

        add(tabs, BorderLayout.CENTER);
        setLocationRelativeTo(null);
    }

    public int getCurrentUserID() { return currentUserID; }
}