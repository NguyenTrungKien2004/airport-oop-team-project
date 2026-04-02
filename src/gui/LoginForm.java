package gui;

import javax.swing.*;
import java.awt.*;
import dao.UserDAO;
import model.User;

public class LoginForm extends JFrame {
    private JTextField txtUsername = new JTextField(15);
    private JPasswordField txtPassword = new JPasswordField(15);
    private JButton btnLogin = new JButton("Đăng nhập");
    
    // ĐÂY LÀ DÒNG THÔNG BÁO TÍCH HỢP
    private JLabel lblMessage = new JLabel(" "); // Để dấu cách để giữ chỗ

    public LoginForm() {
        setTitle("HỆ THỐNG QUẢN LÝ SÂN BAY");
        setSize(600, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Thiết kế Label thông báo cho đẹp
        lblMessage.setFont(new Font("Arial", Font.ITALIC, 12));
        lblMessage.setHorizontalAlignment(SwingConstants.CENTER);

        // --- Vẽ Giao Diện ---
        gbc.gridx = 0; gbc.gridy = 0; add(new JLabel("Tài khoản:"), gbc);
        gbc.gridx = 1; add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; add(new JLabel("Mật khẩu:"), gbc);
        gbc.gridx = 1; add(txtPassword, gbc);

        gbc.gridx = 1; gbc.gridy = 2; add(btnLogin, gbc);

        // Dòng thông báo nằm ở dưới cùng, chiếm 2 cột
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(lblMessage, gbc);

        // --- Xử lý sự kiện ---
        btnLogin.addActionListener(e -> {
            String u = txtUsername.getText();
            String p = new String(txtPassword.getPassword());
            User user = new UserDAO().login(u, p);

            if (user != null) {
                if (user.getRoleID() == 1) { // ADMIN KIÊN
                    lblMessage.setForeground(new Color(0, 150, 0)); // Màu xanh lá
                    lblMessage.setText("Chào Admin! Đang chuyển hướng...");
                    
                    // Đợi 1 giây cho Kiên kịp nhìn thông báo rồi mới chuyển trang
                    Timer timer = new Timer(1000, event -> {
                        this.dispose();
                        new UserManagementForm().setVisible(true);
                    });
                    timer.setRepeats(false);
                    timer.start();
                    
                } else { 
                    // TIẾN, NAM, DUY
                    lblMessage.setForeground(Color.BLUE);
                    lblMessage.setText("Chào " + user.getFullName() + ". Chức năng đang phát triển, vui lòng chờ hoàn thiện!");
                }
            } else {
                lblMessage.setForeground(Color.RED);
                lblMessage.setText("Sai tài khoản hoặc mật khẩu rồi!");
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginForm().setVisible(true));
    }
}