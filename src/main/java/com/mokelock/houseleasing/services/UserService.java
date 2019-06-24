package com.mokelock.houseleasing.services;

import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.modifyUser;
import com.mokelock.houseleasing.model.UserModel.record;

import java.util.ArrayList;

public interface UserService {

    boolean signIn();

    //获取目标用户账户的余额，查询失败返回-1
    int getBalance(String _username);

    //根据用户名username获取目标用户的信息数据，返回信息存储在_one对象里，用户名存在，返回true，用户名不存在，返回值为false；
    boolean getUser(User _one,String _username);

    //向目标账户进行充值，成功返回true，失败返回false；
    boolean postAccount(String _username,int _money);

    //根据用户名查询该用户的交易记录，存储在一个ArrayList<record>链表中
    ArrayList<record> getRecords(String _username);

    //根据用户名查询该用户名下的房子信息，存储在一个ArrayList<house>链表中
    ArrayList<house> getHouses(String _username);

    //修改一个用户的信誉值，得到的信誉值是_credit，成功返回true，失败返回false；实际上是调用了postUser
    boolean postCredit(User _old,short _credit);

    //修改一个用户的性别，成功返回true，失败返回false；实际上是调用了postUser
    boolean postGender(User _old,byte _gender);

    //修改一个用户的密码，成功返回true，失败返回false;实际上是调用了postUser
    boolean postPassword(User _old,String _password);

    //修改一个用户的电话号码，成功返回true，失败返回false;实际上是调用了postUser
    boolean postPhone(User _old,String _phone);

    //传入一个User对象和一个modifyUser对象，将_modified对象的信息覆盖old的信息,成功返回true，失败返回false;
    //该方法是其他修改方法的源方法，可以不用主动调用;
    boolean postUser(User _old, modifyUser _modified);


}
