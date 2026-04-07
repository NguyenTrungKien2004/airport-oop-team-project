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
        setTitle("ĐĂNG KÝ THÀNH VIÊN");
        setSize(400, 380);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        addComponent("Họ và tên:", txtFullName, gbc, 0);
        addComponent("Tên đăng nhập:", txtUsername, gbc, 1);
        addComponent("Mật khẩu:", txtPassword, gbc, 2);
        addComponent("Xác nhận:", txtConfirm, gbc, 3);

        gbc.gridy = 4; gbc.gridx = 0; add(btnBack, gbc);
        gbc.gridx = 1; add(btnRegister, gbc);

        btnBack.addActionListener(e -> dispose());
        btnRegister.addActionListener(e -> {
            String u = txtUsername.getText().trim();
            String f = txtFullName.getText().trim();
            String p = new String(txtPassword.getPassword());
            if (u.isEmpty() || f.isEmpty() || p.isEmpty() || !p.equals(new String(txtConfirm.getPassword()))) {
                JOptionPane.showMessageDialog(this, "Thông tin chưa đúng hoặc mật khẩu không khớp!");
                return;
            }
            if (new UserDAO().addUser(new User(0, u, p, f, 3))) {
                JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Tài khoản đã tồn tại!");
            }
        });
    }

    private void addComponent(String label, JTextField field, GridBagConstraints gbc, int y) {
        gbc.gridx = 0; gbc.gridy = y; add(new JLabel(label), gbc);
        gbc.gridx = 1; add(field, gbc);
    }
}