package com.mokelock.houseleasing.model;

import java.util.ArrayList;
import java.util.Map;

public class UserModelTest {

    private String userType;
    private String username;
    private String name;
    private String gender;
    private String phone;
    private String ID;
    private String credit;
    private ArrayList<Map> myHouse;

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public ArrayList<Map> getMyHouse() {
        return myHouse;
    }

    public void setMyHouse(ArrayList<Map> myHouse) {
        this.myHouse = myHouse;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "userType='" + userType + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", phone='" + phone + '\'' +
                ", ID='" + ID + '\'' +
                ", credit='" + credit + '\'' +
                ", myHouse=" + myHouse +
                '}';
    }

    public UserModelTest() {
    }

    public UserModelTest(String userType, String username, String name, String gender, String phone, String ID, String credit, ArrayList<Map> myHouse) {
        this.userType = userType;
        this.username = username;
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.ID = ID;
        this.credit = credit;
        this.myHouse = myHouse;
    }
}
