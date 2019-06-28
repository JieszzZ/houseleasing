package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.IPFS.IPFS_SERVICE;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.modifyUser;
import com.mokelock.houseleasing.model.UserModel.record;
import com.mokelock.houseleasing.services.UserService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Service
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
    public boolean register(User _rUser){return true;}

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
            _one = readUsermessage(_username);
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
        try
        {
            String account = findAccount(_username);
            //交易信息存储在合约上，现在的方法是返回一个hash值



        }catch(IOException e)
        {

        }finally
        {

        }

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
        int begin = 0,end = 0,index = 0;
        for(int i=0;i<_json.length();i++)
        {
            if(_json.substring(i,i+1) == "\"")
            {
                if(begin == 0)
                {
                    begin = i;
                }
                else
                    {
                        end = i;
                        res.add(_json.substring(begin,end));
                        begin = 0;
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
                    user.setHash(res.get(++i));
                    break;
            }
        }

        return user;
    }

    private ArrayList<record> readRecordmessage(String _json)
    {
        ArrayList<record> ar = new ArrayList<record>();


        return null;
    }


    //访问以太坊得到用户_账号表的哈希值
    private String findUser_Account_hash()
    {
        BlockChain bc = new BlockChain();
        return bc.getHash(User_Account_TYPE);
    }
}
