package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.Cipher.Ciphers;
import com.mokelock.houseleasing.Cipher.CiphersImpl.CiphersImpl;
import com.mokelock.houseleasing.IPFS.File_Read_Test;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.dao.UserDao;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.front_record;
import com.mokelock.houseleasing.model.UserModel.record;
import com.mokelock.houseleasing.services.HouseService;
import com.mokelock.houseleasing.services.UserService;
//import org.springframework.stereotype.Service;

import java.util.*;
import java.io.*;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

//import org.springframework.stereotype.Service;

@Service
public class UserServicesImpl implements UserService {

    private static final int User_Account_TYPE = 0;
    private static String path = System.getProperty("user.dir") + "\\src\\main\\file\\id";//身份证文件下载和上传路径
    private static String tablepath =  System.getProperty("user.dir") + "\\src\\main\\file\\table";
    //private static String SK = "";
    private static String profile_a = "profile_a.png";//身份证正面位置
    private static String profile_b = "profile_b.png";//身份证背面位置
    //private static String ipfs = "";//用户信息的位置
    private static String oneTable = "onetable.txt";//用户-账号表
    //private static String twoTable = "";//能租的房子
    //private static String threeTable = "";//不能租的房子
    //private static String contractAddress = "";//合约的地址
//    @Value("${BlockChain.root.Address}")
    private static String adminAccount = "0x7d8b423d21b1e682063665b4fa99df5d04874c48";
//    @Value("${BlockChain.root.Password}")
    private static String adminEthPassword = "123";
//    @Value("${BlockChain.root.File}")
    private String adminFilePath = "D:\\Geth\\data\\keystore\\UTC--2019-07-10T08-43-43.932731400Z--ed87e99eb4ec72678f1912c329d1c1cbf44f9c36";
//    private static String adminFilePath = "N:\\geth\\data\\keystore\\UTC--2019-07-06T05-37-21.279150600Z--1f3ff30f01ec45eb10a6c5613aaf33224b40d0b0";
    private static final int InitialCredit = 10;
    private static final int InitialGive = 10000000;
    @Resource
    private UserDao userDao;

    @Override
    //使用用户和密码进行登录，成功返回true,失败返回false；
    public boolean login(String _username, String _password) {
        if ( _password.equals(userDao.getPasswordByUsername(_username))) {
            return true;
        }
        return false;
    }

    /*
        @Override
        //检测某用户名的账号是否已登录;
        public boolean hasLoggedIn(String _username){return true;}

        @Override
        //注销某用户名的账号
        public boolean logout(String _username){return true;}
    */
    @Override
    //注册账号;注册的信息分别为用户名，密码，支付口令，姓名，电话，身份证照片正，反，身份证号，性别;
    //信誉值用初始化的值
    //以太坊钱包地址
    //已完成
    public boolean register(String _username, String _password, String pay_password, String name, String phone, File _profile_a, File _profile_b, String _id, byte _gender) {
        User user = new User(_username, _password, pay_password, name, phone, _profile_a, _profile_b, _id, _gender);
        System.out.println(user.toString());
        try {
            if (userDao.checkUser(_username) <= 0 && userDao.insertUser(_username, _password) > 0) {
                BlockChain bc = new BlockChain();
                Map map = bc.creatCredentials(pay_password);

                String old = findUser_Account_hash();
                IPFS_SERVICE_IMPL.download(tablepath + "\\" + oneTable,old,oneTable);
                //File table_one = new File(tablepath+"\\"+oneTable);
                String account = (String) map.get("ethAddress");
                String ethPath = (String) map.get("ethPath");
                Table table = new TableImpl();
                System.out.println("before insert " + _username + account + ethPath);
                String tempPath = System.getProperty("user.dir") + "\\src\\main\\file\\table\\onetable.txt";
                System.out.println("tempPath is " + tempPath);


                postAccount(account, InitialGive);

                //将身份证照片存储在IPFS上
                File pro_a = new File(path + "/" + profile_a);
                File pro_b = new File(path + "/" + profile_b);
                String pro_a_binary = "";
                String pro_b_binary = "";
                FileInputStream fis = new FileInputStream(_profile_a);
                FileOutputStream fos = new FileOutputStream(pro_a);
                System.out.println(1);
                int ch = fis.read();
                while (ch != -1) {
                    fos.write(ch);
                    ch = fis.read();
                }
                fis.close();
                fos.close();

                fis = new FileInputStream(_profile_b);
                fos = new FileOutputStream(pro_b);
                ch = fis.read();
                while (ch != -1) {
                    fos.write(ch);
                    ch = fis.read();
                }
                fis.close();
                fos.close();


                /*
                String _json =" { ";
                _json += "\""+"username"+"\""+":"+"\""+_username+"\", ";
                _json += "\""+"name"+"\""+":"+"\""+name+"\", ";
                _json += "\""+"profile_a"+"\""+":"+pro_a_binary+", ";
                _json += "\""+"profile_b"+"\""+"    :"+pro_b_binary+", ";
                _json += "\""+"gender"+"\""+":"+_gender+" ";

                _json += " } ";

                File json = new File(path +"/"+ ipfs);
                if(!json.exists())
                {
                    json.mkdir();
                }
                FileWriter fos = new FileWriter(json);
                fos.write(_json);
                fos.flush();
                fos.close();
                */

                System.out.println("length is " + new File("E:\\houseleasing\\houseleasing\\src\\main\\file\\table\\onetable.txt").length());

                System.out.println("length before is " + new File("E:\\houseleasing\\houseleasing\\src\\main\\file" +
                        "\\table\\onetable.txt").length());
                table.insert(_username, account, ethPath, tempPath);
                System.out.println("length after is " + new File("E:\\houseleasing\\houseleasing\\src\\main\\file" +
                        "\\table\\onetable.txt").length());

                //把身份证照片的文件夹传到IPFS
                String is = IPFS_SERVICE_IMPL.upload(path);
                String new_table = IPFS_SERVICE_IMPL.upload(tablepath+"\\"+oneTable);
                System.out.println("is is " + is);
                System.out.println("new_table is " + new_table);
                //把哈希值传给以太坊
                //bc.changeHashInfo(account,is);

                //木有在区块链添加id和name的方法
                //伪代码：在区块链添加id和name
                // {
                // 添加id和name
                //
                // }
                Ciphers ci = new CiphersImpl();

                String id_hash = ci.encryHASH(_id);
                bc.addUser(account, ethPath, pay_password, _username, id_hash, is, phone, _gender, InitialCredit);
                bc.changeTable(User_Account_TYPE,new_table);

                pro_a.delete();
                pro_b.delete();

                return true;
            } else {
                System.out.println("the user has exists");
            }

        } catch (IOException e) {
            System.out.println("register failed.");
        }
        return false;

    }

    @Override
    public int getBalance(String _username) {
        try {
            BlockChain bc = new BlockChain();
            String account = findAccount(_username);
            int balance = bc.getBalance(account).intValue();
            return balance;
        } catch (IOException e) {
            System.out.println("getBalance() is error");
        } finally {
            return -1;
        }
    }

    //根据用户名username获取目标用户的信息数据，返回信息存储在_one对象里，用户名存在，返回true，用户名不存在，返回值为false；
    //根据用户名查询系统以太坊账户的用户账户*以太坊账户的表
    //再从以太坊账户读取存储在区块链上的信息
    //存储在_one中
    @Override
    public User getUser(String _username, String _pay_password) {
        User _one;
        try {
            System.out.println("getUser-1 " + _username + _pay_password);
            String account = findAccount(_username);
            String sk = findEthFile(_username);
            System.out.println("getUser " + account + sk);
            BlockChain bc = new BlockChain();
            JSONObject message = bc.getMessage3(account, sk, _pay_password);
            _one = readUser(message);
            _one.setUsername(_username);
            System.out.println("user info is " + _one.toString());
            /*
            IPFS_SERVICE.download(path,_one.getIPFS_hash(),ipfs);

            FileReader fr = new FileReader(path+"/"+ipfs);
            BufferedReader br = new BufferedReader(fr);

            String str = "";
            while((str += br.readLine())!= null)
            {
            }

            User user = (User)JSONObject.parseObject(str,User.class);
            _one.setName(user.getName());
            _one.setGender(user.getGender());
/*
            File pro_a = new File(path+"/"+profile_a);
            File pro_b = new File(path+"/"+profile_b);
            String pro_a_str = user.getProfile_a();
            String pro_b_str = user.getProfile_b();

            FileOutputStream fos = new FileOutputStream(pro_a);
            int ch;
            for(int i=0;i<pro_a_str.length();i++)
            {
                ch = Integer.parseInt(pro_a_str.substring(i,i+1));
                fos.write(ch);
            }
            fos.close();
            fos = new FileOutputStream(pro_b);
            for(int i=0;i<pro_b_str.length();i++)
            {
                ch = Integer.parseInt(pro_b_str.substring(i,i+1));
                fos.write(ch);
            }
            fos.close();

            _one.setProfile_a(path+"/"+profile_a);
            _one.setProfile_b(path+"/"+profile_b);
*/

            return _one;
        } catch (IOException e) {
            System.out.println("getUser() is error!");
        } finally {
            return null;
        }


    }

    @Override
    public ArrayList<User> getAllUser() {
        try {
            Table table = new TableImpl();
            BlockChain bc = new BlockChain();
            String one_hash = bc.getHash(User_Account_TYPE);
            IPFS_SERVICE.download(tablepath, one_hash, oneTable);
            String[] key = {"eth-id"};
            ArrayList<String[]> res = table.get_all(key, tablepath + "\\" + oneTable);
            ArrayList<User> ans = new ArrayList<User>();
            for (int i = 0; i < res.size(); i++) {
                User one;
                JSONObject message = bc.getMessage3(res.get(i)[0], adminFilePath, adminEthPassword);
                one = readUser(message);
                ans.add(one);
            }

            return ans;

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return null;
        }
    }


    //向目标账户进行充值
    @Override
    public boolean postAccount(String account, int _money) {
        //System.out.println("postAccount " + _username);
        try {
            BlockChain bc = new BlockChain();
            //String account = findAccount(_username);
            System.out.println(account);
            bc.transaction(adminEthPassword, adminFilePath, account);
            return true;

        } catch (Exception e) {
            System.out.println("postAccount() is error");
        } finally {
            return false;
        }

    }

    @Override
    public boolean postAccountByusername(String _username,int money)
    {
        try {
            BlockChain bc = new BlockChain();
            String account = findAccount(_username);
            System.out.println(account);
            bc.transaction(adminEthPassword, adminFilePath, account);
            return true;

        } catch (Exception e) {
            System.out.println("postAccount() is error");
        } finally {
            return false;
        }


    }


    //*********
    //已完成
    //*********
    @Override
    public ArrayList<front_record> getRecords(String _username, String pay_password) {
        ArrayList<front_record> alr = new ArrayList<front_record>();
        try {

            String account = findAccount(_username);
            String ethFile = findEthFile(_username);
            BlockChain bc = new BlockChain();


            JSONArray orders = bc.findOrders(account, ethFile, pay_password);
            ArrayList<record> ars = readRecords(orders);
            for (int i = 0; i < ars.size(); i++) {
                alr.add(new front_record(ars.get(i)));
            }
            // String message = bc.replayFilter(account);
            //  alr  = readRecords(message);


        } catch (IOException e) {
            System.out.println("getRecords() is error!");
        } finally {
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
    //需要比对密码,_phone 为修改后的电话号码
    //************************************
    //待完善！！！
    //等BlockChain那里修改号码的的方法修改后我再把这里完善
    //************************************
    //已完善；
    //************************************
    public boolean postPhone(String _username, String _password, String _pay_password, String _phone) {
        BlockChain bc = new BlockChain();
        String account, ethFile;
        if (!_password.equals(userDao.getPasswordByUsername(_username))) {
            return false;
        }
        try {

            account = findAccount(_username);
            ethFile = findEthFile(_username);
            //User user = readUser(bc.getMessage(account,ethFile,_pay_password));
            bc.changeTelInfo(account, ethFile, _pay_password, _phone);
            return true;
        } catch (IOException e) {
            System.out.println("postPhone() is error;");
        } finally {
            return false;
        }


    }

    //修改一个用户的密码和电话号码，成功返回true，失败返回false，实际上调用的是这个函数的重载：boolean postUser(User _old, User _modified);
    // public boolean postUser(User _old,String _password,String _phone){return true;}
/*
    @Override
    public boolean postUser(User _old, User _modified) {
        return false;
    }
*/
    //根据房子的哈希值获取一个房屋的信息，返回一个house对象
    public House getHouses(String _house_hash) {
        HouseService hs = new HouseServiceImpl();
        House house = new House();
        try {
            JSONObject job = (JSONObject) hs.speInfo(_house_hash);
            house = (House) JSONObject.toJavaObject(job, House.class);
        } catch (Exception e) {
            System.out.println("getHouses() is error!");
        } finally {
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
    //@Override
    //修改一个房子的信息;_house_hash为需要修改的房子的哈希地址；成功返回true，失败返回false；
    //需要等到李晓婷把这部分接口完成我才能修改房源
    //不需要我写
    // public boolean postHouse(String _house_hash,int _state,boolean _elevator,int _lease,String _phone){return true;}


    /*
     *
     * 已完成
     *
     * */
    public String findAccount(String _username) throws IOException {
        int where = 0;
        String hash = findUser_Account_hash();
        System.out.println("findAccount_hash is " + hash);

        IPFS_SERVICE_IMPL.download(tablepath + "\\" + oneTable, hash, oneTable);

        System.out.println("1111111111111" + _username);
        Table table = new TableImpl();
        String[] user_name = {"user_name"};
        String[] _user = {_username};
        String[] eth_id = {"eth_id"};
        ArrayList<String[]> result = table.query(user_name, _user, eth_id, tablepath + "\\" + oneTable);
        String res = result.get(where)[where];

        return res;
    }


    private User readUsermessage(String _username, String _pay_password) throws IOException {
        BlockChain bc = new BlockChain();
        String account = findAccount(_username);
        String ethFile = findEthFile(_username);
        String _json = bc.getMessage(account, ethFile, _pay_password);
        ArrayList<String> res = new ArrayList<String>();
        int begin = -1, end = 0;
        for (int i = 0; i < _json.length() - 1; i++) {
            if (_json.charAt(i) == '\"' || Character.isDigit(_json.charAt(i))) {
                if (begin == -1) {
                    begin = i;
                } else {
                    end = i;
                    res.add(_json.substring(begin, end));
                    begin = -1;
                    end = 0;
                }
            }
        }

        User user = new User();
        for (int i = 0; i < res.size(); i++) {
            switch (res.get(i)) {
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
                case "phone":
                    user.setPhone(res.get(++i));
                    break;
                case "gender":
                    user.setGender(Byte.parseByte(res.get(++i)));
                    break;
                case "credit":
                    user.setCredit(Short.parseShort(res.get(++i)));
                    break;
            }
        }

        return user;
    }

    private ArrayList<record> readRecordmessage(String _json) {
        ArrayList<record> alr = new ArrayList<record>();
        ArrayList<String> als = new ArrayList<String>();
        int begin = -1, end = 0;
        final int NUMS = 14;


        for (int i = 0; i < _json.length(); i++) {
            if (_json.charAt(i) == '\"' || Character.isDigit(_json.charAt(i))) {
                if (begin == -1) {
                    begin = i;
                } else {
                    end = i;
                    String str = _json.substring(begin, end);
                    als.add(str);
                    begin = -1;
                    end = 0;
                }
            }
        }

        int i = 0, num = 0;
        record rd = new record();
        while (i < als.size()) {
            if (num == 0) {
                rd = new record();
            }

            switch (als.get(i)) {
                case "submiter":
                    rd.setTenant(als.get(++i));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "responder":
                    rd.setHomeowner(als.get(++i));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "submiterEthCoin":
                    rd.setSubmiterEthCoin(Integer.parseInt(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "aimerEthCoin":
                    rd.setAimerEthCoin(Integer.parseInt(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "subFirstSign":
                    rd.setSubFirstSign(tranlateSign(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "resFirstSign":
                    rd.setResFirstSign(tranlateSign(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "subSecondSign":
                    rd.setSubSecondSign(tranlateSign(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "resSecondSign":
                    rd.setResSecondSign(tranlateSign(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "sub_time":
                    rd.setSub_time(Integer.parseInt(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "effect_time":
                    rd.setEffect_time(Integer.parseInt(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "finish_time":
                    rd.setFinish_time(Integer.parseInt(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "role":
                    rd.setRole(tranlateRole(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "state":
                    rd.setState(tranlateState(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;
                case "money":
                    rd.setMoney(tranlateMoney(als.get(++i)));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
                    break;

                case "house_hash":
                    rd.setHouse_hash(als.get(++i));
                    num++;
                    if (num == NUMS) {
                        num = 0;
                        alr.add(rd);
                    }
            }

            i++;
        }


        return alr;
    }


    //访问以太坊得到用户_账号表的哈希值
    private String findUser_Account_hash() {
        BlockChain bc = new BlockChain();
        return bc.getHash(User_Account_TYPE);
    }

    public String findEthFile(String _username) throws IOException {
        System.out.println("000000" + _username);
        int where = 0;
        String hash = findUser_Account_hash();
        IPFS_SERVICE.download(tablepath+"\\"+oneTable, hash, oneTable);
        Table table = new TableImpl();
        String[] user_name = {"user_name"};
        String[] _user = {_username};
        String[] SK = {"SK"};
        ArrayList<String[]> result = table.query(user_name, _user, SK, tablepath + "\\" + oneTable);
        String res = result.get(where)[where];
        System.out.println("0000000000000000" + res);
        return res;

    }

    private int tranlateSign(String sign) {
        if (sign == "1") {
            return 1;
        } else if (sign == "2") {
            return 2;
        } else {
            return 0;
        }
    }

    private int tranlateRole(String role) {
        if (role == "0") {
            return 0;
        } else if (role == "1") {
            return 1;
        }
        return -1;
    }

    private int tranlateState(String state) {
        if (state == "0") {
            return 0;
        } else if (state == "1") {
            return 1;
        } else if (state == "2") {
            return 2;
        } else if (state == "3") {
            return 3;
        } else if (state == "4") {
            return 4;
        }
        return -1;
    }

    private int tranlateMoney(String money) {
        if (money == "0") {
            return 1;
        } else if (money == "1") {
            return 1;
        } else if (money == "2") {
            return 2;
        } else if (money == "3") {
            return 3;
        }
        return -1;
    }

    public User readUser(JSONObject userStr) {
        User user = new User();
        user.setCredit(Integer.parseInt(userStr.getString("credit")));
        user.setGender(Integer.parseInt(userStr.getString("gender")));
        user.setId(userStr.getString("id"));
        user.setIPFS_hash(userStr.getString("IPFS_hash"));
        user.setPhone(userStr.getString("phone"));
        user.setName(userStr.getString("username"));

        return user;
    }

    private ArrayList<record> readRecords(JSONArray records) {
        //JSONArray ja = JSONArray.parseArray(records);
        ArrayList<record> alr = new ArrayList<record>();
        for (int i = 0; i < records.size(); i++) {
            record record = (record) records.get(i);
            alr.add(record);
        }
        return alr;
    }


}
