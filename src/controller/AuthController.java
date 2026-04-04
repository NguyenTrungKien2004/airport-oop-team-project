package controller;

import dao.UserDAO;
import model.User;
import view.LoginView;
import view.RegisterView;
import view.FlightsDashboard;
import view.AdminDashboard;
import javax.swing.JOptionPane;

public class AuthController {
    private LoginView view;
    private UserDAO dao;

    public AuthController(LoginView view) {
        this.view = view;
        this.dao = new UserDAO();

        // 1. Xử lý khi nhấn nút Đăng nhập
        this.view.btnLogin.addActionListener(e -> {
            String u = view.txtUser.getText();
            String p = new String(view.txtPass.getPassword());

            User user = dao.checkLogin(u, p);
            if (user != null) {
                // Kiểm tra quyền (RoleID)
                if (user.getRoleID() == 1) { // ADMIN
                    view.dispose();
                    new AdminDashboard().setVisible(true);
                } 
                else if (user.getRoleID() == 2) { // NHÂN VIÊN (Phần của Tiến)
                    view.dispose();
                    FlightsDashboard staffView = new FlightsDashboard();
                    new FlightController(staffView);
                    staffView.setVisible(true);
                } 
                else if (user.getRoleID() == 3) { // KHÁCH HÀNG (Phần của bạn)
                    // Vì chưa phát triển giao diện khách, ta chỉ thông báo thành công
                    JOptionPane.showMessageDialog(view, 
                        "Chào mừng Khách hàng: " + user.getFullName() + 
                        "\nHiện tại chức năng đặt vé dành cho Khách hàng đang được phát triển.");
                }
            } else {
                JOptionPane.showMessageDialog(view, "Tài khoản hoặc mật khẩu không chính xác!");
            }
        });

        // 2. Xử lý khi nhấn nút "Đăng ký tài khoản" trên giao diện Login
        this.view.btnGoRegister.addActionListener(e -> {
            new RegisterView().setVisible(true);
        });
    }
}