package view;

import javax.swing.*;
import java.awt.*;
import controller.AuthController;

public class LoginView extends JFrame {
    public JTextField txtUser = new JTextField(15);
    public JPasswordField txtPass = new JPasswordField(15);
    public JButton btnLogin = new JButton("Đăng nhập");

    public LoginView() {
        setTitle("HỆ THỐNG QUẢN LÝ SÂN BAY");
        setSize(400, 250);
        setResizable(false); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Đổi sang GridBagLayout để các dòng không bị chia đều diện tích
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Tạo khoảng cách nhỏ 10px giữa các dòng
        gbc.gridx = 0;

        // Dòng 1: User
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lb1 = new JLabel("Tài khoản:"); 
        lb1.setPreferredSize(new Dimension(60, 25));
        p1.add(lb1); p1.add(txtUser);
        gbc.gridy = 0;
        add(p1, gbc);

        // Dòng 2: Pass
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lb2 = new JLabel("Mật khẩu:"); 
        lb2.setPreferredSize(new Dimension(60, 25));
        p2.add(lb2); p2.add(txtPass);
        gbc.gridy = 1;
        add(p2, gbc);

        // Dòng 3: Button
        JPanel p3 = new JPanel(); 
        btnLogin.setPreferredSize(new Dimension(100, 30));
        p3.add(btnLogin);
        gbc.gridy = 2;
        add(p3, gbc);

        new AuthController(this); 
    }
}