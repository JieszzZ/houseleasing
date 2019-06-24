package com.mokelock.houseleasing.model.UserModel;

public class modifyUser {
    private String password;
    private String phone;
    private short credit;
    private byte gender;

    public modifyUser(){}
    public modifyUser(String _password,String _phone,short _credit,byte _gender)
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
    public short getCredit(){return credit;}
    public void setGender(byte _gender){gender = _gender;}
    public byte getGender(){return gender;}

}
