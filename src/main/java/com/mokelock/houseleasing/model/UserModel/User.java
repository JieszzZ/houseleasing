package com.mokelock.houseleasing.model.UserModel;
import java.awt.Image;
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


public class User extends modifyUser{
    private String username = "null";
    private String name;
    private String id;
    private String pay_password;
    private File profile_a; //本人身份证照片带脸，这是一个文件地址
    private File profile_b; //本人身份证照片带国徽，这是一个文件地址
    private String IPFS_hash;
    //private String password;
    //private String phone;
    //private short credit;
    //private byte gender;

    //不使用任何参数创建一个空的User对象
    public User(){}
    //依次使用用户名，姓名，身份证号，支付密码，密码，电话，信誉值，性别创建User对象
    public User(String _username,String _name,String _id,String _pay_password,String _password,String _phone,
                short _credit,int _gender)
    {
        super(_password,_phone,_credit,_gender);
        username = _username;
        name = _name;
        id = _id;
        pay_password = _pay_password;
    }
    public User(String _username, String _password, String pay_password, String name, String phone, File _profile_a,
                File _profile_b, String _id, int _gender)
    {
        username = _username;
        super.setPassword(_password);
        this.pay_password = pay_password;
        this.name = name;
        super.setPhone(phone);
        profile_a = _profile_a;
        profile_b = _profile_b;
        id = _id;
        super.setGender(_gender);
    }

    //依次使用姓名，身份证号，密码，电话，信誉值，性别创建User对象，用户名默认设置为null,暂时不用
    /*
    public User(String _name,String _id,String _password,String _phone,short _credit,byte _gender)
    {
        super(_password,_phone,_credit,_gender);
        name = _name;
        id =_id;
    }
    */

    public User(String _username)
    {
        username = _username;
    }



    public void setUsername(String _username)
    {
        username = _username;
    }

    public String getUsername()
    {
        return username;
    }


    public void setName(String _name)
    {
        name = _name;
    }

    public String getName()
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

    public void setPay_password(String _pay_password)
    {
        pay_password = _pay_password;
    }
    public String getPay_password(){ return pay_password;}

    public void setProfile_a(File _pro_a)
    {
        profile_a = _pro_a;
    }
    public File getProfile_a(){return profile_a;}

    public void setProfile_b(File _pro_b)
    {
        profile_b = _pro_b;
    }
    public File getProfile_b(){return profile_b;}

    public void setIPFS_hash(String _hash)
    {
        IPFS_hash = _hash;
    }
    public String getIPFS_hash()
    {
        return IPFS_hash;
    }


}
