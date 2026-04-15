package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
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
        JButton btnEdit = new JButton("Sửa / Phân quyền");
        JButton btnDelete = new JButton("Xóa User");
        JButton btnReload = new JButton("Làm mới bảng");
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setForeground(Color.RED);

        pnlBtns.add(btnAdd);
        pnlBtns.add(btnEdit);
        pnlBtns.add(btnDelete);
        pnlBtns.add(btnReload);
        pnlBtns.add(btnLogout);

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

        // Nút Sửa / Phân quyền
        btnEdit.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 user để sửa!");
                return;
            }
            int id = (int) table.getValueAt(row, 0);
            String oldUser = table.getValueAt(row, 1).toString();
            String oldPass = table.getValueAt(row, 2).toString();
            String oldName = table.getValueAt(row, 3).toString();

            JTextField txtUser = new JTextField(oldUser);
            JTextField txtPass = new JTextField(oldPass);
            JTextField txtName = new JTextField(oldName);
            String[] roles = {"1 - Quản trị viên", "2 - Nhân viên", "3 - Hành khách"};
            JComboBox<String> cboRole = new JComboBox<>(roles);

            Object[] fields = {
                "Tài khoản:", txtUser,
                "Mật khẩu:", txtPass,
                "Họ tên:", txtName,
                "Phân quyền:", cboRole
            };

            int option = JOptionPane.showConfirmDialog(this, fields, "Sửa thông tin User ID: " + id, JOptionPane.OK_CANCEL_OPTION);
            if (option == JOptionPane.OK_OPTION) {
                int selectedRole = cboRole.getSelectedIndex() + 1;
                User u = new User(id, txtUser.getText(), txtPass.getText(), txtName.getText(), selectedRole);
                if (dao.updateUser(u)) {
                    JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                    loadData();
                } else {
                    JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
                }
            }
        });

        // Nút Đăng xuất
        btnLogout.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Đăng xuất", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                dispose();
                new LoginView().setVisible(true);
            }
        });

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