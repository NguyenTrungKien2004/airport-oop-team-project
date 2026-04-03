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
    // Tiến Bổ sung thêm Setter
    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }
    
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    
    public int getRoleID() { return roleID; }
    public void setRoleID(int roleID) { this.roleID = roleID; }
}