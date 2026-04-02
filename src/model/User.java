package model;

public class User {
    private int userID;
    private String username;
    private String password;
    private String fullName;
    private int roleID;

    public User() {}

    public User(int userID, String username, String password, String fullName, int roleID) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.roleID = roleID;
    }

    // Các hàm lấy dữ liệu (Getters)
    public int getUserID() { return userID; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getFullName() { return fullName; }
    public int getRoleID() { return roleID; }
}