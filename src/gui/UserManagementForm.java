package gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import dao.UserDAO;
import model.User;

public class UserManagementForm extends JFrame {
    public UserManagementForm() {
        setTitle("BẢNG QUẢN TRỊ NHÂN VIÊN");
        setSize(800, 500);

        setResizable(false); //cố định kích thước để

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo bảng
        String[] header = {"ID", "Tài khoản", "Họ tên", "Vai trò"};
        DefaultTableModel model = new DefaultTableModel(header, 0);
        JTable table = new JTable(model);

        // Đổ dữ liệu 4 anh em từ SQL lên
        List<User> users = new UserDAO().getAllUsers();
        for (User u : users) {
            String roleStr = (u.getRoleID() == 1) ? "Quản trị viên" : (u.getRoleID() == 2 ? "Nhân viên" : "Hành khách");
            model.addRow(new Object[]{u.getUserID(), u.getUsername(), u.getFullName(), roleStr});
        }

        add(new JScrollPane(table), BorderLayout.CENTER);
        
        // Khu vực nút bấm cho Leader
        JPanel pnlAction = new JPanel();
        pnlAction.add(new JButton("Thêm Nhân Viên"));
        pnlAction.add(new JButton("Xóa Nhân Viên"));
        add(pnlAction, BorderLayout.SOUTH);
    }
}