package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import controller.FlightController;

import java.awt.*;
import java.util.List;
import dao.UserDAO;
import model.User;

public class AdminDashboard extends JFrame {
    private JTable table;
    private DefaultTableModel model;
    private UserDAO dao = new UserDAO();

    public AdminDashboard() {
        setTitle("ADMIN DASHBOARD - Quản lý người dùng");
        setSize(900, 550);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // 1. Tiêu đề
        JLabel lbl = new JLabel("QUẢN LÝ NHÂN VIÊN & KHÁCH HÀNG", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 22));
        add(lbl, BorderLayout.NORTH);

        // 2. Bảng hiển thị
        String[] columns = { "ID", "Tài khoản", "Mật khẩu", "Họ tên", "Quyền" };
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        loadData();
        add(new JScrollPane(table), BorderLayout.CENTER);

        // 3. Các nút bấm
        JPanel pnlBtns = new JPanel();
        JButton btnAdd = new JButton("Thêm mới");
        JButton btnDelete = new JButton("Xóa User");
        JButton btnReload = new JButton("Làm mới bảng");

        pnlBtns.add(btnAdd);
        pnlBtns.add(btnDelete);
        pnlBtns.add(btnReload);

        add(pnlBtns, BorderLayout.SOUTH);

        // --- XỬ LÝ SỰ KIỆN ---

        // Nút Xóa
        btnDelete.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 dòng để xóa!");
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa User ID: " + id + "?");
            if (confirm == JOptionPane.YES_OPTION) {
                if (dao.deleteUser(id)) {
                    JOptionPane.showMessageDialog(this, "Đã xóa thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Lỗi: Không thể xóa (Có thể User đang có vé bay)!");
                }
            }
        });

        // Nút Thêm (Mở cửa sổ nhập nhanh)
        btnAdd.addActionListener(e -> {
            String user = JOptionPane.showInputDialog(this, "Nhập Username:");
            String pass = JOptionPane.showInputDialog(this, "Nhập Password:");
            String name = JOptionPane.showInputDialog(this, "Nhập Họ tên:");
            String role = JOptionPane.showInputDialog(this, "Nhập Quyền (1:Admin, 2:Staff, 3:Passenger):");

            if (user != null && pass != null) {
                User u = new User(0, user, pass, name, Integer.parseInt(role));
                if (dao.addUser(u)) {
                    JOptionPane.showMessageDialog(this, "Thêm thành công!");
                    loadData();
                }
            }
        });

        // Nút Làm mới
        btnReload.addActionListener(e -> loadData());

    }

    private void loadData() {
    model.setRowCount(0); // Xóa dữ liệu cũ trên bảng
    List<User> list = dao.getAllUsers();
    
    for (User u : list) {
        // --- BẮT ĐẦU ĐOẠN ĐỔI SỐ SANG CHỮ ---
        String tenQuyen = "";
        if (u.getRoleID() == 1) {
            tenQuyen = "Quản trị viên";
        } else if (u.getRoleID() == 2) {
            tenQuyen = "Nhân viên";
        } else if (u.getRoleID() == 3) {
            tenQuyen = "Hành khách";
        } else {
            tenQuyen = "Chưa xác định";
        }
        // ------------------------------------

        // Thêm dòng vào bảng (Thay u.getRoleID() bằng biến tenQuyen)
        model.addRow(new Object[]{
            u.getUserID(), 
            u.getUsername(), 
            u.getPassword(), 
            u.getFullName(), 
            tenQuyen // Chỗ này sẽ hiện chữ thay vì số 1, 2, 3
        });
    }
}
}