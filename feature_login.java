// File: model/User.java
package com.ptit.qlnv.model;

public abstract class User {
    private String username;
    private String password;
    private String role; // "ADMIN" hoặc "EMPLOYEE"

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // Getter và Setter
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}

// File: model/Employee.java
package com.ptit.qlnv.model;

public class Employee extends User {
    private String employeeId;
    private double salary;

    public Employee(String username, String password, String employeeId, double salary) {
        super(username, password, "EMPLOYEE"); // Gọi constructor cha
        this.employeeId = employeeId;
        this.salary = salary;
    }

    @Override
    public String toString() {
        return "ID: " + employeeId + " | Name: " + getUsername() + " | Salary: " + salary;
    }
}