package com.mokelock.houseleasing.model.UserModel;

public class User extends modifyUser{
    private String username = "null";
    private String name;
    private String id;
    //private String password;
    //private String phone;
    //private short credit;
    //private byte gender;

    //不使用任何参数创建一个空的User对象
    public User(){}
    //依次使用用户名，密码，姓名，电话，身份证号，信誉值，性别创建User对象
    public User(String _username,String _name,String _id,String _password,String _phone,short _credit,byte _gender)
    {
        super(_password,_phone,_credit,_gender);
        username = _username;
        name = _name;
        id = _id;


    }
    //依次使用姓名，电话，身份证号，信誉值，性别创建User对象，用户名默认设置为null
    public User(String _name,String _id,String _password,String _phone,short _credit,byte _gender)
    {
        super(_password,_phone,_credit,_gender);
        name = _name;
        id =_id;
    }

    public User(String _username)
    {
        username = _username;
    }



    public void setUsername(String _usename)
    {
        username = _usename;
    }

    public String getUsername()
    {
        return username;
    }


    public void setName(String _name)
    {
        name = _name;
    }

    public String getName(String _name)
    {
        return name;
    }


    public void setId(String _id)
    {
        id = _id;
    }

    public String getId()
    {
        return id;
    }




}
