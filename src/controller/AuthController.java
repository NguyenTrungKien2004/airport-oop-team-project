package controller;

import dao.UserDAO;
import model.User;
import view.LoginView;
import view.FlightsDashboard;
import view.AdminDashboard;
import javax.swing.JOptionPane;

public class AuthController {
    private LoginView view;
    private UserDAO dao;

    public AuthController(LoginView view) {
        this.view = view;
        this.dao = new UserDAO();

        this.view.btnLogin.addActionListener(e -> {
            String u = view.txtUser.getText();
            String p = new String(view.txtPass.getPassword());

            User user = dao.checkLogin(u, p);
            if (user != null) {
                view.dispose(); // Đóng Login
                if (user.getRoleID() == 1) { // Admin
                    new AdminDashboard().setVisible(true);
                } else if (user.getRoleID() == 2) { /// Bổ Sung của TIẾN
                    FlightsDashboard staffView = new FlightsDashboard(2); // Truyền roleID của Staff
                    new FlightController(staffView); /// Kết nối Controller của Tiến vào
                    staffView.setVisible(true); //
                } else { // Nhân viên hoặc Khách
                    JOptionPane.showMessageDialog(null,
                            "Đăng nhập thành công! Chức năng cho Nhân viên/Khách sẽ cập nhật sau.");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Sai tài khoản hoặc mật khẩu!");
            }
        });
    }
}