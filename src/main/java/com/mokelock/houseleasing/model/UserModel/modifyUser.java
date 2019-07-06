package com.mokelock.houseleasing.model.UserModel;

public class modifyUser {
    private String password;
    private String phone;
    private int credit;
    private int gender;

    public modifyUser(){}
    public modifyUser(String _password,String _phone,short _credit,int _gender)
    {
        password = _password;
        phone = _phone;
        credit = _credit;
        gender = _gender;
    }


    public void setPassword(String _password){password = _password;}
    public String getPassword(){return password;}
    public void setPhone(String _phone){phone = _phone;}
    public String getPhone(){return phone;}
    public void setCredit(short _credit){credit = _credit;}
    public int getCredit(){return credit;}
    public void setGender(int _gender){gender = _gender;}
    public int getGender(){return gender;}

}
