package com.mokelock.houseleasing.model;

public class UserRegisterModel {

    private String username;
    private String name;
    private String password;
    private String pay_password;
    private String phone;
    private String profile_a;
    private String profile_b;
    private String id;
    private String gender;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getProfile_a() {
        return profile_a;
    }

    public void setProfile_a(String profile_a) {
        this.profile_a = profile_a;
    }

    public String getProfile_b() {
        return profile_b;
    }

    public void setProfile_b(String profile_b) {
        this.profile_b = profile_b;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserRegisterModel() {
    }

    public UserRegisterModel(String username, String name, String password, String pay_password, String phone, String profile_a, String profile_b, String id, String gender) {
        this.username = username;
        this.name = name;
        this.password = password;
        this.pay_password = pay_password;
        this.phone = phone;
        this.profile_a = profile_a;
        this.profile_b = profile_b;
        this.id = id;
        this.gender = gender;
    }
}
