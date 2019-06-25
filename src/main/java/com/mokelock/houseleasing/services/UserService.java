package com.mokelock.houseleasing.services;

import com.mokelock.houseleasing.model.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.modifyUser;
import com.mokelock.houseleasing.model.UserModel.record;

import java.util.ArrayList;

/*
* 编写人：袁虎
* 更新时间：2019/06/25
* 根据需求说明书完成了所提到的方法
* 将某些修改方法细化，方便调用；
*
*
* */


public interface UserService {


    //使用用户和密码进行登录，成功返回true,失败返回false；
    boolean login(String _username,String _password);

    //检测某用户名的账号是否已登录;
    boolean hasLoggedIn(String _username);

    //注销某用户名的账号
    boolean logout(String _username);

    //注册账号;注册的信息存储在_rUser里;
    boolean register(User _rUser);

    //获取目标用户账户的余额，查询失败返回-1
    int getBalance(String _username);

    //根据用户名username获取目标用户的信息数据，返回信息存储在_one对象里，用户名存在，返回true，用户名不存在，返回值为false；
    boolean getUser(User _one,String _username);

    //向目标账户进行充值，成功返回true，失败返回false；
    boolean postAccount(String _username,int _money);

    //根据用户名查询该用户的交易记录，存储在一个ArrayList<record>链表中
    ArrayList<record> getRecords(String _username);

    //根据房子的哈希值获取一个房屋的信息，返回一个house对象
    House getHouses(String _house_hash);


    //修改一个房子的状态；_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouseState(String _house_hash,int _state);

    //修改一个房子是否有电梯;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouseElevator(String _house_hash,boolean _elevator);

    //修改一个房子的租金;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouseLease(String _house_hash,int _lease);

    //修改一个房子房主的联系电话，_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHousePhone(String _house_hash,String _phone);

    //修改一个房子的信息;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouse(String _house_hash,int _state,boolean _elevator,int _lease,String _phone);

    //修改一个用户的密码和电话号码，成功返回true，失败返回false，实际上调用的是这个函数的重载：boolean postUser(User _old, modifyUser _modified);
    boolean postUser(User _old,String _password,String _phone);

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
