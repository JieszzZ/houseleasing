package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.modifyUser;
import com.mokelock.houseleasing.model.UserModel.record;
import com.mokelock.houseleasing.services.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServicesImpl implements UserService {

    @Override
    //使用用户和密码进行登录，成功返回true,失败返回false；
    public boolean login(String _username,String _password){return true;}

    @Override
    //检测某用户名的账号是否已登录;
    public boolean hasLoggedIn(String _username){return true;}

    @Override
    //注销某用户名的账号
    public boolean logout(String _username){return true;}

    @Override
    //注册账号;注册的信息存储在_rUser里;
    public boolean register(User _rUser){return true;}

    @Override
    public int getBalance(String _username) {
        return 0;
    }

    @Override
    public boolean getUser(User _one, String _username) {
        return false;
    }

    @Override
    public boolean postAccount(String _username, int _money) {
        return false;
    }

    @Override
    public ArrayList<record> getRecords(String _username) {
        return null;
    }

    @Override
    public boolean postCredit(User _old, short _credit) {
        return false;
    }

    @Override
    public boolean postGender(User _old, byte _gender) {
        return false;
    }

    @Override
    public boolean postPassword(User _old, String _password) {
        return false;
    }

    @Override
    public boolean postPhone(User _old, String _phone) {return true;}

    //修改一个用户的密码和电话号码，成功返回true，失败返回false，实际上调用的是这个函数的重载：boolean postUser(User _old, modifyUser _modified);
    public boolean postUser(User _old,String _password,String _phone){return true;}

    @Override
    public boolean postUser(User _old, modifyUser _modified) {
        return false;
    }

    //根据房子的哈希值获取一个房屋的信息，返回一个house对象
    public House getHouses(String _house_hash){return null;}

    @Override
    //修改一个房子的状态；_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    public boolean postHouseState(String _house_hash,int _state){return true;}

    @Override
    //修改一个房子是否有电梯;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    public boolean postHouseElevator(String _house_hash,boolean _elevator){return true;}

    @Override
    //修改一个房子的租金;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    public boolean postHouseLease(String _house_hash,int _lease){return true;}

    @Override
    //修改一个房子房主的联系电话，_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    public boolean postHousePhone(String _house_hash,String _phone){return true;}

    @Override
    //修改一个房子的信息;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    public boolean postHouse(String _house_hash,int _state,boolean _elevator,int _lease,String _phone){return true;}
}
