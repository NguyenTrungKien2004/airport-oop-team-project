package model;

public class User {
    private int userID;
    private String username;
    private String fullName;
    private int roleID; // 1: Admin, 2: Nhân viên, 3: Hành khách

    public User(int userID, String username, String fullName, int roleID) {
        this.userID = userID;
        this.username = username;
        this.fullName = fullName;
        this.roleID = roleID;
    }

    // Getters để lấy dữ liệu hiển thị lên bảng
    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public int getRoleID() { return roleID; }
}