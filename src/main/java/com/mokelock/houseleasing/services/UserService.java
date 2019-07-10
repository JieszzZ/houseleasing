package com.mokelock.houseleasing.services;

import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.front_record;

import java.io.File;
import java.util.ArrayList;

/*
 * 编写人：袁虎
 * 更新时间：2019/06/25
 * 根据需求说明书完成了所提到的方法
 * 将某些修改方法细化，方便调用；
 *
 *
 * 更新时间：2019/06/28
 * 修改了record类，将其适应合约的数据内容
 * 实现了读取用户信息的方法，但是修改的方法还未完成
 * 实现了读取JSON格式的方法，能读取用户信息，但是订单信息还不能解决
 *
 *更新时间：2019/06/30
 *根据需求文档修改了一下方法输入的参数
 * 注掉了多余的方法
 *实现了读取交易记录，根据用户名查询以太坊账号等方法，注册，登录一切需要密码的操作均未实现：无数据库接口
 *一切修改的操作均未实现，如修改房源信息，修改电话号码：因为没有此类接口
 *顺便一提，我觉得今天应该完成不了第一版···
 *
 * */


public interface UserService {


    //使用用户和密码进行登录，成功返回true,失败返回false；
    //通过输入的用户名和密码，检索数据库里的账号和密码匹配，则登录成功
    boolean login(String _username, String _password);


    //注册账号;注册的信息存储在_rUser里;
    //根据_rUser里的信息注册账户
    //注册需要在以太坊上申请一个以太坊账户，并且在系统以太坊账户的用户*以太账户对应表上添加该数据
    //在数据库上添加账号*密码信息
    //在以太坊账户上存储该用户的个人信息
    boolean register(String _username, String _password, String pay_password, String name, String phone, File _profile_a, File _profile_b, String _id, byte _gender);

    //获取目标用户账户的余额，查询失败返回-1
    //从以太坊返回账户的余额
    //先从系统以太坊账户取出用户账号*以太坊账户的表
    //根据用户账户寻找以太坊账户，查询以太坊账户的余额
    int getBalance(String _username);

    //根据用户名username获取目标用户的信息数据，返回信息存储在_one对象里，用户名存在，返回true，用户名不存在，返回值为false；
    //根据用户名查询系统以太坊账户的用户账户*以太坊账户的表
    //再从以太坊账户读取存储在区块链上的信息
    //存储在_one中
    User getUser(String _username, String _pay_password);

    //向目标账户进行充值，成功返回true，失败返回false；
    //先从系统账户寻找用户账户*以太坊账户的表，找到用户的以太坊账户
    //再从系统账户向用户账户转账_money
    boolean postAccount(String _username, int _money);

    //根据用户名查询该用户的交易记录，存储在一个ArrayList<record>链表中
    //先根据用户名查询系统账户里的用户账户*以太坊账户表
    //再从以太坊账户访问智能合约查询用户的订单
    ArrayList<front_record> getRecords(String _username, String pay_password);

    //根据房子的哈希值获取一个房屋的信息，返回一个house对象
    House getHouses(String _house_hash);

    //获取所有用户的信息
    ArrayList<User> getAllUser();
/*
    //修改一个房子的状态；_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouseState(String _house_hash,int _state);

    //修改一个房子是否有电梯;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouseElevator(String _house_hash,boolean _elevator);

    //修改一个房子的租金;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHouseLease(String _house_hash,int _lease);

    //修改一个房子房主的联系电话，_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    boolean postHousePhone(String _house_hash,String _phone);
*/


    //修改一个房子的信息;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
//    boolean postHouse(String _house_hash,int _state,boolean _elevator,int _lease,String _phone);


/*
    //修改一个用户的密码和电话号码，成功返回true，失败返回false，实际上调用的是这个函数的重载：boolean postUser(User _old, User _modified);
    boolean postUser(User _old,String _password,String _phone);

    //修改一个用户的信誉值，得到的信誉值是_credit，成功返回true，失败返回false；实际上是调用了postUser
    boolean postCredit(User _old,short _credit);

    //修改一个用户的性别，成功返回true，失败返回false；实际上是调用了postUser
    boolean postGender(User _old,byte _gender);

    //修改一个用户的密码，成功返回true，失败返回false;实际上是调用了postUser
    boolean postPassword(User _old,String _password);
*/


    //修改一个用户的电话号码，成功返回true，失败返回false;需要输入用户密码，密码正确才能修改，密码错误则返回false;
    boolean postPhone(String _username, String _password, String _pay_password, String _phone);

    /*
    //传入一个User对象和一个modifyUser对象，将_modified对象的信息覆盖old的信息,成功返回true，失败返回false;
    //该方法是其他修改方法的源方法，可以不用主动调用;
    boolean postUser(User _old, User _modified);
*/

}
