package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.IPFS.IPFS_SERVICE;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.modifyUser;
import com.mokelock.houseleasing.model.UserModel.record;
import com.mokelock.houseleasing.services.HouseService;
import com.mokelock.houseleasing.services.UserService;
//import org.springframework.stereotype.Service;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;



//@Service
public class UserServicesImpl implements UserService {

    private static final int User_Account_TYPE = 1;
    private static String path= "" ;

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
    public boolean register(String _username, String _password, String pay_password, String name, String phone, Image _profile_a, Image _profile_b, String _id, String _gender){return true;}

    @Override
    public int getBalance(String _username) {
        try
        {
            BlockChain bc = new BlockChain();
            String account = findAccount(_username);
            int balance = bc.getBalance(account).intValue();
            return balance;
        } catch (IOException e)
        {
            System.out.println("getBalance() is error");
        }
        finally
        {
            return -1;
        }
    }

    //根据用户名username获取目标用户的信息数据，返回信息存储在_one对象里，用户名存在，返回true，用户名不存在，返回值为false；
    //根据用户名查询系统以太坊账户的用户账户*以太坊账户的表
    //再从以太坊账户读取存储在区块链上的信息
    //存储在_one中
    @Override
    public boolean getUser(User _one, String _username)
    {
        try
        {
            String account = findAccount(_username);
            BlockChain bc = new BlockChain();
            String message = bc.getMessage(account);
            _one = readUser(message);
            _one.setUsername(_username);
            return true;
        }catch(IOException e)
        {
            System.out.println("getUser() is error!");
        }
        finally
        {
            return false;
        }


    }

    @Override
    public boolean postAccount(String _username, int _money) {
        return false;
    }

    @Override
    public ArrayList<record> getRecords(String _username) {
        ArrayList<record> alr = new ArrayList<record>();
        try
        {

            String account = findAccount(_username);
            BlockChain bc = new BlockChain();
            String message = bc.replayFilter(account);
            alr  = readRecords(message);


        }catch(IOException e)
        {
            System.out.println("getRecords() is error!");
        }finally
        {
            return alr;
        }
    }
/*
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
*/
    @Override
    public boolean postPhone(User _old,String _password,String _phone) {return true;}

    //修改一个用户的密码和电话号码，成功返回true，失败返回false，实际上调用的是这个函数的重载：boolean postUser(User _old, modifyUser _modified);
    public boolean postUser(User _old,String _password,String _phone){return true;}
/*
    @Override
    public boolean postUser(User _old, modifyUser _modified) {
        return false;
    }
*/
    //根据房子的哈希值获取一个房屋的信息，返回一个house对象
    public House getHouses(String _house_hash)
    {
        HouseService hs = new HouseServiceImpl();
        House house = new House();
        try
        {
            JSONObject job =(JSONObject) hs.speInfo(_house_hash);
            house = (House) JSONObject.toJavaObject(job,House.class);
        }catch (Exception e)
        {
            System.out.println("getHouses() is error!");
        }
        finally {
            return house;
        }
    }

    /*
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
*/
    @Override
    //修改一个房子的信息;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    public boolean postHouse(String _house_hash,int _state,boolean _elevator,int _lease,String _phone){return true;}


    /*
    *
    * 为毛这儿返回的是一个链表String数组
    *
    * */
    private String  findAccount(String _username) throws IOException
    {
        int where = 0;
        String hash = findUser_Account_hash();
        IPFS_SERVICE.download(path,hash);
        Table table = new TableImpl();
        String[] user_name = {"username"};
        String[] _user = {_username};
        String[] eth_id = {"eth_id"};
        ArrayList<String[]> result = table.query(user_name,_user,eth_id,path);
        String res = result.get(where)[where];

        return res;
    }


    private User readUsermessage(String _username) throws IOException
    {
        BlockChain bc = new BlockChain();
        String account = findAccount(_username);
        String _json = bc.getMessage(account);
        ArrayList<String> res = new ArrayList<String>();
        int begin = -1,end = 0;
        for(int i=0;i<_json.length();i++)
        {
            if(_json.substring(i,i+1) == "\"")
            {
                if(begin == -1)
                {
                    begin = i;
                }
                else
                    {
                        end = i;
                        res.add(_json.substring(begin,end));
                        begin = -1;
                        end = 0;
                    }
            }
        }

        User user = new User();
        for(int i=0;i<res.size();i++)
        {
            switch(res.get(i))
            {
                case "name":
                    user.setName(res.get(++i));
                    break;
                case "id":
                    user.setId(res.get(++i));
                    break;
                case "pay_password":
                    user.setPay_password(res.get(++i));
                    break;
                case "IPFS_hash":
                    user.setIPFS_hash(res.get(++i));
                    break;
            }
        }

        return user;
    }

    private ArrayList<record> readRecordmessage(String _json)
    {
        ArrayList<record> alr = new ArrayList<record>();
        ArrayList<String> als = new ArrayList<String>();
        int begin = -1,end = 0;
        final int NUMS = 14;


        for(int i=0;i<_json.length();i++)
        {
            if(_json.substring(i,i+1) == "\"")
            {
                if(begin == -1)
                {
                    begin = i;
                }
                else
                    {
                        end = i;
                        String str = _json.substring(begin,end);
                        als.add(str);
                        begin = -1;
                        end =0;
                    }
            }
        }

        int i=0,num = 0;
        record rd = new record();
        while(i<als.size())
        {
            if(num == 0)
            {
                rd = new record();
            }

            switch(als.get(i))
            {
                case "submiter":
                    rd.setTenant(als.get(++i));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "responder":
                    rd.setHomeowner(als.get(++i));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "submiterEthCoin":
                    rd.setSubmiterEthCoin(Integer.parseInt(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "aimerEthCoin":
                    rd.setAimerEthCoin(Integer.parseInt(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "subFirstSign":
                    rd.setSubFirstSign(tranlateSign(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "resFirstSign":
                    rd.setResFirstSign(tranlateSign(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "subSecondSign":
                    rd.setSubSecondSign(tranlateSign(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "resSecondSign":
                    rd.setResSecondSign(tranlateSign(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "sub_time":
                    rd.setSub_time(Integer.parseInt(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "effect_time":
                    rd.setEffect_time(Integer.parseInt(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "finish_time":
                    rd.setFinish_time(Integer.parseInt(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "role":
                    rd.setRole(tranlateRole(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "state":
                    rd.setState(tranlateState(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "money":
                    rd.setMoney(tranlateMoney(als.get(++i)));
                    num++;
                    if(num == NUMS)
                    {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
            }

            i++;
        }


        return alr;
    }


    //访问以太坊得到用户_账号表的哈希值
    private String findUser_Account_hash()
    {
        BlockChain bc = new BlockChain();
        return bc.getHash(User_Account_TYPE);
    }

    private record.Sign tranlateSign(String sign)
    {
        if(sign == "Sign.signed")
        {
            return record.Sign.signed;
        }
        else if(sign == "Sign.refused")
        {
            return record.Sign.refused;
        }
        else
            {
                return record.Sign.unsigned;
            }
    }

    private record.Role tranlateRole(String role)
    {
        if(role == "Role.submiter")
        {
            return record.Role.submiter;
        }
        else if(role == "Role.responder")
        {
            return  record.Role.responder;
        }
        return null;
    }

    private record.State tranlateState(String state)
    {
        if(state == "State.submiting")
        {
            return record.State.submiting;
        }
        else if(state == "State.effecting")
        {
            return record.State.effecting;
        }
        else if(state == "State.finished")
        {
            return record.State.finished;
        }
        else if(state == "State.refused")
        {
            return record.State.refused;
        }
        else if(state == "State.failed")
        {
            return record.State.failed;
        }
        return null;
    }

    private record.Money tranlateMoney(String money)
    {
        if(money == "Money.nosub")
        {
            return record.Money.nosub;
        }
        else if(money == "Money.subed")
        {
            return record.Money.subed;
        }
        else if(money == "Money.returned")
        {
            return record.Money.retruned;
        }
        else if(money == "Money.deduction")
        {
            return record.Money.deduction;
        }
        return null;
    }

    private User readUser(String userStr)
    {
        User user  =(User) JSONObject.parseObject(userStr,User.class);
        return user;
    }

    private ArrayList<record> readRecords(String records)
    {
        JSONArray ja = JSONArray.parseArray(records);
        ArrayList<record> alr = new ArrayList<record>();
        for(int i=0;i<ja.size();i++)
        {
            record record = (record)ja.get(i);
            alr.add(record);
        }
        return  alr;
    }
}
