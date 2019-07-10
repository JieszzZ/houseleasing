package com.mokelock.houseleasing.model.UserModel;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/*
 * 编写人：袁虎
 * 更新时间2019/06/25
 *
 * 把身份证照片属性加了进去，添加了支付密码属性
 * 根据需求说明书把需求里提到到关于所有的User的属性都加了进来
 * 继承了modifyUser类，为了方便以后添加和修改功能
 *
 * */


public class UserTemp {
    private String username = "null";
    private String name;
    private String id;
    private String pay_password;
    private MultipartFile profile_a; //本人身份证照片带脸，这是一个文件地址
    private MultipartFile profile_b; //本人身份证照片带国徽，这是一个文件地址
    private String IPFS_hash;
    private String password;
    private String phone;
    private String gender;

    public UserTemp() {
    }

    public UserTemp(String username, String name, String id, String pay_password, MultipartFile profile_a, MultipartFile profile_b, String IPFS_hash, String password, String phone, String gender) {
        this.username = username;
        this.name = name;
        this.id = id;
        this.pay_password = pay_password;
        this.profile_a = profile_a;
        this.profile_b = profile_b;
        this.IPFS_hash = IPFS_hash;
        this.password = password;
        this.phone = phone;
        this.gender = gender;
    }

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPay_password() {
        return pay_password;
    }

    public void setPay_password(String pay_password) {
        this.pay_password = pay_password;
    }

    public MultipartFile getProfile_a() {
        return profile_a;
    }

    public void setProfile_a(MultipartFile profile_a) {
        this.profile_a = profile_a;
    }

    public MultipartFile getProfile_b() {
        return profile_b;
    }

    public void setProfile_b(MultipartFile profile_b) {
        this.profile_b = profile_b;
    }

    public String getIPFS_hash() {
        return IPFS_hash;
    }

    public void setIPFS_hash(String IPFS_hash) {
        this.IPFS_hash = IPFS_hash;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "UserTemp{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", pay_password='" + pay_password + '\'' +
                ", profile_a=" + profile_a +
                ", profile_b=" + profile_b +
                ", IPFS_hash='" + IPFS_hash + '\'' +
                ", password='" + password + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                '}';
    }
}
