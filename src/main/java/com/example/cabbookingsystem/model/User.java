package com.example.cabbookingsystem.model;

public class User {
    private int id;
    private String customerRegistrationNumber;
    private String fullName;
    private String address;
    private String nicNumber;
    private String email;
    private String password;
    private String role; // ✅ Role field added

    // ✅ Constructor for Employees (default role)
    public User(String customerRegistrationNumber, String fullName, String address, String nicNumber, String email, String password) {
        this.customerRegistrationNumber = customerRegistrationNumber;
        this.fullName = fullName;
        this.address = address;
        this.nicNumber = nicNumber;
        this.email = email;
        this.password = password;
        this.role = "employee"; // ✅ Default role set to "employee"
    }

    // ✅ Constructor for Admins & Employees (explicit role)
    public User(String customerRegistrationNumber, String fullName, String address, String nicNumber, String email, String password, String role) {
        this.customerRegistrationNumber = customerRegistrationNumber;
        this.fullName = fullName;
        this.address = address;
        this.nicNumber = nicNumber;
        this.email = email;
        this.password = password;
        this.role = role; // ✅ Allows specifying "admin" or "employee"
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCustomerRegistrationNumber() { return customerRegistrationNumber; }
    public void setCustomerRegistrationNumber(String customerRegistrationNumber) { this.customerRegistrationNumber = customerRegistrationNumber; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getNicNumber() { return nicNumber; }
    public void setNicNumber(String nicNumber) { this.nicNumber = nicNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }  // ✅ Getter for role
    public void setRole(String role) { this.role = role; } // ✅ Setter for role

    // ✅ Override toString for debugging
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", customerRegistrationNumber='" + customerRegistrationNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", address='" + address + '\'' +
                ", nicNumber='" + nicNumber + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
