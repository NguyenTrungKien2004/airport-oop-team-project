package view;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class RegisterView extends JFrame {
    private JTextField txtUsername = new JTextField(15);
    private JTextField txtFullName = new JTextField(15);
    private JPasswordField txtPassword = new JPasswordField(15);
    private JPasswordField txtConfirm = new JPasswordField(15);
    private JButton btnRegister = new JButton("Đăng ký tài khoản Khách");
    private JButton btnBack = new JButton("Quay lại");

    public RegisterView() {
        setTitle("ĐĂNG KÝ THÀNH VIÊN MỚI");
        setSize(400, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Form nhập liệu
        addComponent("Họ và tên:", txtFullName, gbc, 0);
        addComponent("Tên đăng nhập:", txtUsername, gbc, 1);
        addComponent("Mật khẩu:", txtPassword, gbc, 2);
        addComponent("Xác nhận mật khẩu:", txtConfirm, gbc, 3);

        gbc.gridy = 4; gbc.gridx = 0; add(btnBack, gbc);
        gbc.gridx = 1; add(btnRegister, gbc);

        // Sự kiện quay lại
        btnBack.addActionListener(e -> dispose());
        
        // Sự kiện Đăng ký
        btnRegister.addActionListener(e -> {
            String user = txtUsername.getText().trim();
            String full = txtFullName.getText().trim();
            String pass = new String(txtPassword.getPassword());
            String conf = new String(txtConfirm.getPassword());

            if (user.isEmpty() || full.isEmpty() || pass.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                return;
            }
            if (!pass.equals(conf)) {
                JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
                return;
            }

            UserDAO dao = new UserDAO();
            // QUAN TRỌNG: Gán RoleID = 3 cho Khách hàng mới đăng ký
            User newUser = new User(0, user, pass, full, 3); 
            
            if (dao.addUser(newUser)) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công! Bạn có thể thử đăng nhập.");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Lỗi: Tài khoản đã tồn tại hoặc lỗi kết nối Database!");
            }
        });
    }

    private void addComponent(String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y;
        add(new JLabel(label), gbc);
        gbc.gridx = 1; add(field, gbc);
    }
}