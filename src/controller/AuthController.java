package controller;

import dao.UserDAO;
import model.User;
import view.LoginView;
import view.PassengerFrame;
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
                if (user.getRoleID() == 1) { // ADMIN
                    view.dispose(); 
                    new AdminDashboard().setVisible(true);
                } 
                else if (user.getRoleID() == 2) { // NHÂN VIÊN 
                    view.dispose();
                    // ĐÃ SỬA: Truyền RoleID vào để hết lỗi đỏ
                    FlightsDashboard staffView = new FlightsDashboard(user.getRoleID());
                    new FlightController(staffView);
                    staffView.setVisible(true);
                } 
                else if (user.getRoleID() == 3) { // KHÁCH HÀNG
                    view.dispose();
                    PassengerFrame pFrame = new PassengerFrame(user.getUserID());
                     new BookingController(pFrame); 
                    pFrame.setVisible(true); 
    
    // 4. Hiển thị cửa sổ lên
    pFrame.setVisible(true);
                }
            } else {
                JOptionPane.showMessageDialog(view, "Tài khoản hoặc mật khẩu không chính xác!");
            }
        });

        // 2. Mở giao diện Đăng ký
        this.view.btnGoRegister.addActionListener(e -> {
            new RegisterView().setVisible(true);
        });
    }
}