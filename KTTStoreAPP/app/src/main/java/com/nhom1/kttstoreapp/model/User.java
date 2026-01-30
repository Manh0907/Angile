package com.nhom1.kttstoreapp.model;

public class User {
    private String _id;
    private String name;
    private String email;
    private String phone;
    private String gender;
    private String role;

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isStaff() {
        return "staff".equals(role) || "admin".equals(role);
    }
}
