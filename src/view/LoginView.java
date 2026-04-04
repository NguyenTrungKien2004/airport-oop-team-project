package view;

import javax.swing.*;
import java.awt.*;
import controller.AuthController;

public class LoginView extends JFrame {
    public JTextField txtUser = new JTextField(15);
    public JPasswordField txtPass = new JPasswordField(15);
    public JButton btnLogin = new JButton("Đăng nhập");
    public JButton btnGoRegister = new JButton("Đăng ký tài khoản khách hàng");

    public LoginView() {
        setTitle("HỆ THỐNG QUẢN LÝ SÂN BAY");
        setSize(400, 320);
        setResizable(false); 
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;

        // Dòng 1: Tài khoản
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lb1 = new JLabel("Tài khoản:"); 
        lb1.setPreferredSize(new Dimension(70, 25));
        p1.add(lb1); p1.add(txtUser);
        gbc.gridy = 0; add(p1, gbc);

        // Dòng 2: Mật khẩu
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel lb2 = new JLabel("Mật khẩu:"); 
        lb2.setPreferredSize(new Dimension(70, 25));
        p2.add(lb2); p2.add(txtPass);
        gbc.gridy = 1; add(p2, gbc);

        // Dòng 3: Nút Đăng nhập
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        btnLogin.setPreferredSize(new Dimension(120, 30));
        p3.add(btnLogin);
        gbc.gridy = 2; add(p3, gbc);

        // Dòng 4: Nút Đăng ký
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnGoRegister.setForeground(Color.BLUE);
        btnGoRegister.setBorderPainted(false);
        btnGoRegister.setContentAreaFilled(false);
        btnGoRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        p4.add(btnGoRegister);
        gbc.gridy = 3; add(p4, gbc);

        new AuthController(this); 
    }
}