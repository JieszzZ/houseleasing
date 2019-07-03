package com.mokelock.houseleasing.services.servicesImpl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.Cipher.CiphersImpl.CiphersImpl;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.model.HouseModel.*;


import com.mokelock.houseleasing.services.HouseService;
import com.mysql.cj.xdevapi.JsonArray;
import jnr.ffi.annotations.In;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Int;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * 李晓婷
 * 20190625
 * 房屋相关处理接口
 */
@Service
public class HouseServiceImpl implements HouseService {
    @Override

    //获取房源详细信息
    public JSON speInfo(String house_id_hash) {

        //根据传进来的实参house_id_hash，去下载管理员的房屋表，得到这个房子所在文件夹的hash:house_hash
        //根据house_hash即可作为路径进行查询info和comment
        //house_pic则是直接表示为图片的路径即可

        String[] key_for_search = {"house_id_hash"};
        String[] value_to_search = {house_id_hash};

        String[] hash_to_get = {"house_hash"};
        String house_hash = "";

        TableImpl dhtable = new TableImpl();

        BlockChain bc = new BlockChain();

        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        try {
            isi.download("/path/housetable1",bc.getHash(1),"housetable1");
            isi.download("/path/housetable2",bc.getHash(2),"housetable2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String[]> HashToHouse = dhtable.query(key_for_search,value_to_search,hash_to_get,"/path/housetable1");
        ArrayList<String[]> HashToHouse2 = dhtable.query(key_for_search,value_to_search,hash_to_get,"/path/housetable2");

        if(HashToHouse.size() != 0){

            house_hash = HashToHouse.get(0)[0];

        }else if(HashToHouse2.size() != 0){

            house_hash = HashToHouse2.get(0)[0];

        }else;

        try{
            isi.download("/path/detailedHouse",house_hash,"dh");
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] key_to_get = {"owner_id","verify", "owner","owner_name",
                "role","state", "area", "provi",
                "city", "sector","commu_name","specific_location",
                "floor", "lon", "lat",  "elevator",
                "lease", "house_type", "house_credit"};
        String[] key_to_getComment = {"user_id","comment","comment_pic"};

        //得到满足条件的房子的信息
        ArrayList<String[]> detailedHouse = dhtable.query(key_for_search, value_to_search, key_to_get, "/path/detailedHouse/info.txt");
        ArrayList<String[]> houseComment = dhtable.get_all(key_to_getComment, "/path/detailedHouse/comment.txt");

        //获得所有评论的正确形式
        JSONArray house_comment = new JSONArray();
        HouseComment singleHouseComment;
        for(int i = 0 ;i < houseComment.size() ;i++){
            singleHouseComment = new HouseComment(houseComment.get(i)[0],houseComment.get(i)[1],
                    (houseComment.get(i)[2].substring(1,houseComment.get(i)[2].length())).split(","));
            house_comment.add(singleHouseComment.HCtoJson());
        }

        //存放得到的房子的图片
        String[] house_pic = new String[10];
        house_pic[0] = "/path/detailedHouse/2.jpg";
        house_pic[1] = "/path/detailedHouse/3.jpg";
        house_pic[2] = "/path/detailedHouse/4.jpg";

        //house_pic[0] = "C:\\Users\\Administrator\\Desktop\\pic.jpg";
        //house_pic[1] = "C:\\Users\\Administrator\\Desktop\\pic.jpg";
        //house_pic[2] = "C:\\Users\\Administrator\\Desktop\\pic.jpg";


        //获得符合要求的形式
        String low_str_location = detailedHouse.get(0)[7]+detailedHouse.get(0)[8]+detailedHouse.get(0)[9]+detailedHouse.get(0)[10];
        //创建一个LowLocation类型的对象
        LowLocation ll = new LowLocation();
        //使用字符串类型的low_str_location来初始化ll
        ll.StoLL(low_str_location);
        //获得JSON对象类型的low_location
        JSONObject low_location = ll.LLtoJson();

        House theHouse = new House(house_pic,house_id_hash,detailedHouse.get(0)[0],Boolean.parseBoolean(detailedHouse.get(0)[1]),
                detailedHouse.get(0)[2],detailedHouse.get(0)[3],Integer.parseInt(detailedHouse.get(0)[4]),
                Integer.parseInt(detailedHouse.get(0)[5]), detailedHouse.get(0)[6], low_location,low_str_location,detailedHouse.get(0)[11],
                Integer.parseInt(detailedHouse.get(0)[12]), detailedHouse.get(0)[13], detailedHouse.get(0)[14],
                Boolean.parseBoolean(detailedHouse.get(0)[15]),Integer.parseInt(detailedHouse.get(0)[16]),
                Integer.parseInt(detailedHouse.get(0)[17]),Integer.parseInt(detailedHouse.get(0)[18]),house_comment);

        //Response res = new Response(200,"success",);
        return theHouse.HtoJson();
    }

    @Override
    //搜索
    public JSON search(String low_location, String lease_inter, String house_type, String lease_type, boolean elevator) {

        //首先从IPFS中打开文件

        /*******************************************************************************************/
        //记录用户输入了几个查找条件，即用户调用该函数时的实参有几个是有具体数据的

        BlockChain bc = new BlockChain();

        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        LowLocation lowlocation = new LowLocation();
        lowlocation.StoLL(low_location);

        try {
            isi.download("/path/housetable1",bc.getHash(1),"housetable1");
        } catch (IOException e) {
            e.printStackTrace();
        }

        int count = 0;
        //将所有可能被用户输入的实参的变量名和变量值
        String[] allkey = {"provi","city","sector","commu_name",
                "lease_inter","house_type","lease_type","elevator"};
        String[] allvalue = {lowlocation.getProvi(),lowlocation.getCity(),lowlocation.getSector(),lowlocation.getCommu_name(),
                lease_inter,house_type,lease_type,String.valueOf(elevator)};

        //记录各实参索引的状态，1表示有具体值，0表示空或“”
        List<Integer> valued = new ArrayList<>();
        //如果实参有具体值，则记录该实参在
        if(!lowlocation.getProvi().equals("")){
            valued.add(0,1);
            count++;
        }else if(lowlocation.getProvi().equals("")){
            valued.add(0,0);
        }
        if(!lowlocation.getCity().equals("")){
            valued.add(1,1);
            count++;
        }else if(lowlocation.getCity().equals("")){
            valued.add(1,0);
        }
        if(!lowlocation.getSector().equals("")){
            valued.add(2,1);
            count++;
        }else if(lowlocation.getSector().equals("")){
            valued.add(2,0);
        }
        if(!lowlocation.getCommu_name().equals("")){
            valued.add(3,1);
            count++;
        }else if(lowlocation.getCommu_name().equals("")){
            valued.add(3,0);
        }
        if(!lease_inter.equals("")){
            valued.add(4,1);
            count++;
        }else if(lease_inter.equals("")){
            valued.add(4,0);
        }
        if(!house_type.equals("")){
            valued.add(5,1);
            count++;
        }else if(house_type.equals("")){
            valued.add(5,0);
        }
        if(!lease_type.equals("")){
            valued.add(6,1);
            count++;
        }else if(lease_type.equals("")){
            valued.add(6,0);
        }
        if(!(String.valueOf(elevator)).equals("")){
            valued.add(7,1);
            count++;
        }else if((String.valueOf(elevator)).equals("")){
            valued.add(7,0);
        }
        /*******************************************************************************************/

        /**/
        //定义要传入数据文件查询方法的实参
        String[] key_to_search = new String[count];
        String[] value_to_search = new String[count];
        int in = 0;
        for(int i = 0; i < valued.size(); i++){
            if(valued.get(i) == 1){
                key_to_search[in] = allkey[i];
                value_to_search[in] = allvalue[i];
                in++;
            }
        }
        //定义要得到的属性的集合
        String[] key_to_get = {"provi","city","sector","commu_name","lease","house_type","lease_type","elevator","house_id_hash"};
        /**/


        //查询满足要求的房子
        TableImpl tableforsearch = new TableImpl();
        ArrayList<String[]> returnedSH = tableforsearch.query(key_to_search,value_to_search,key_to_get,"/path/housetable1");

        //声明一个概要信息房子对象
        SampleHouse sh;

        //将所有房子想要输出的概要消息转化为json数组
        JSONArray allsh = new JSONArray();

        //String house_pic_single;
        String provi_single;
        String city_single;
        String sector_single;
        String commu_name_single;
        String lease_single;
        String house_type_single;
        String lease_type_single;
        String house_id_hash_single;
        boolean elevator_single;
        LowLocation llforStr;// = new LowLocation();
        String low_str_location;

        for(int i = 0;i < returnedSH.size();i++){
            provi_single = returnedSH.get(i)[0];
            city_single = returnedSH.get(i)[1];
            sector_single = returnedSH.get(i)[2];
            commu_name_single = returnedSH.get(i)[3];
            lease_single = returnedSH.get(i)[4];
            house_type_single = returnedSH.get(i)[5];
            lease_type_single = returnedSH.get(i)[6];
            elevator_single = Boolean.getBoolean(returnedSH.get(i)[7]);
            house_id_hash_single = returnedSH.get(i)[8];

            String[] key = {"house_id_hash"};
            String[] value = new String[1];
            String[] get = {"house_hash"};

            switch(house_type_single){
                case "0":
                    house_type_single = "全部";
                    break;
                case "1":
                    house_type_single = "一室";
                    break;
                case "2":
                    house_type_single = "二室";
                    break;
                case "3":
                    house_type_single = "其他";
                    break;
            }
            switch(lease_type_single){
                case "0":
                    lease_type_single = "全部";
                    break;
                case "1":
                    lease_type_single = "整租";
                    break;
                case "2":
                    lease_type_single = "合租";
                    break;
            }
            llforStr = new LowLocation(provi_single,city_single,sector_single,commu_name_single);
            low_str_location = llforStr.lltoStr();

            //从文件下载获得

            String house_pic = new String();

            value[1] = house_id_hash_single;

            ArrayList<String[]> forPic = tableforsearch.query(key,value,get,"/path/housetasble1");

            try {
                isi.download("/path/detailedHouse",forPic.get(0)[0],"dh");
            } catch (IOException e) {
                e.printStackTrace();
            }
            sh = new SampleHouse("/path/detailedHouse/1.jpg",low_str_location,lease_single,house_type_single,lease_type_single,elevator_single,house_id_hash_single);
            allsh.add(sh.SHtoJson());
        }
        //Response resOfSearchSH = new Response(200,"success",allsh);

        return allsh;
    }

    @Override
    //评价房子
    public JSONObject valuation(String user_id,String house_id_hash,String comment, String[] comment_pic) {
        /*
         * 获取用户账号、文字评论和图片评论、房屋等级
         * 根据房源hash找到该房源的详细信息并将房源评论添加上
         * 返回状态消息
         * */


        String h_level="house_level";
        //String p="123";
        TableImpl t=new TableImpl();

        BlockChain b=new BlockChain();
        String  hash= b.getHash(1);
        IPFS_SERVICE_IMPL i=new IPFS_SERVICE_IMPL();
        String filePathName="c\\user\\1\\desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\verified_house.txt";
        String filename="verified_house.txt";
        try {
            i.download(filePathName,hash,filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] key_for_search={"house_id_hash"};
        String[] value_for_search={"house_id_hash"};
        String[] key_to_get={"house_hash"};
        ArrayList<String[]> a1=t.query(key_for_search,value_for_search,key_to_get,filePathName);
        String[] hash1 = a1.toArray(new String[a1.size()]);
        String p="hash1[0]";
        String filePathName1="c\\user\\1\\desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\verified_house_all.txt";
        String filename1="verified_house_all.txt";
        try {
            i.download(filePathName1,p,filePathName1);
        } catch (IOException e) {
            e.printStackTrace();
        }


        t.insert_into_comment(user_id,comment,comment_pic,h_level,filePathName1);

        return null;
    }

    @Override

    //获取房屋列表
    public JSON allInfo() {
        /*
         * 创建JSONArray对象verified和non_verified
         * 遍历房源hash，每个都写为JSON的形式，并把verify=1的放入verified，verify=0的放入non_verified
         * 最后将整个json返回
         * */
        String[] hash = {"sdfwenk31345","sdfwenk31346"};
        String[] key_for_search={"house_hash"};

        JSONArray verified = new JSONArray();
        JSONArray non_verified = new JSONArray();
        //JSONArray all=new JSONArray();
        JSONObject data=new JSONObject();
        String path="C:\\Users\\Administrator\\Desktop\\demo.txt";
        TableImpl thouse=new TableImpl();
        String[] key_to_get = {"verify","house_pic", "provi","city","sector" ,"commu_name","lease", "house_type", "lease_type","elevator"};

        for (int i = 0; i < hash.length; i++)
        {

            String[] value_for_search={hash[i]};
            //thouse.query(key_for_search,value_for_search,key_to_get,path);
            ArrayList<String[]> v1=thouse.query(key_for_search,value_for_search,key_to_get,path);

            for(int m=0;m<v1.size();m++){
                String s1="0";
                String s2="0";
                Boolean b=Boolean.valueOf(v1.get(m)[9]);
                //String b=v1.get(m)[9];
                String s=v1.get(m)[2];
                if(v1.get(m)[7].equals("0")){
                    s1="全部";
                }if (v1.get(m)[7].equals("1")){
                    s1="一室";
                }if (v1.get(m)[7].equals("2")){
                    s1="二室";
                }if (v1.get(m)[7].equals("3")){
                    s1="其他";
                }
                if (v1.get(m)[8].equals("0")){
                    s2="全部";
                }if (v1.get(m)[8].equals("1")){
                    s2="整租";
                }if (v1.get(m)[8].equals("2")){
                    s2="合租";
                }
                /*
                if (v1.get(m)[9].equals("1")){
                    b=true;
                }if (v1.get(m)[9].equals("0")){
                    b=false;
                }*/
                String s3=v1.get(m)[2]+v1.get(m)[3]+v1.get(m)[4]+v1.get(m)[5];
                String lease=v1.get(m)[6];
                if(v1.get(m)[0].equals("1")){

                    SampleHouse shouse1 = new SampleHouse(v1.get(m)[1],s3,lease,s1,s2,b,hash[i]);
                    shouse1.SHtoJson();
                    verified.add(shouse1);
                    //System.out.print(v1.get(m)[b]+" ");
                }
                if(v1.get(m)[0].equals("0")){
                    SampleHouse shouse2=new SampleHouse(v1.get(m)[1],s3,lease,s1,s2,b,hash[i]);
                    shouse2.SHtoJson();
                    non_verified.add(shouse2);

                }
                System.out.println();


            }
            data.put("verified",verified);
            data.put("non_verified",non_verified);

        }
        //JSONObject data=new JSONObject(all);
        Response r=new Response(200,"success",data);

        return r.RestoJson3();
    }

    @Override
    public JSONObject setUpHouse(String user_id, String user_name, String user,int house_owner_credit, String house_id, int state, JSONObject low_location, String specific_location, int floor, boolean elevator, int lease, int lease_type, int house_type, String lon, String lat, String area, File[] house_pic) {

        //添加一个房源
        TableImpl forInsertHouse = new TableImpl();

        String[] key_for_search_get = {"house_id"};
        String[] value_for_search = {house_id};


        House insertHouse = new House();
        BlockChain bc = new BlockChain();
        CiphersImpl enId = new CiphersImpl();
        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        try {
            isi.download("/path/housetable1",bc.getHash(1),"housetable1");
            isi.download("/path/housetable2",bc.getHash(2),"housetable2");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String[]> id = forInsertHouse.query(key_for_search_get,value_for_search,key_for_search_get,"/path/housetable1");

        if(id.size() != 0){
            //Response failRes = new Response(200,"fail");
            //toReturn = failRes.RestoJson2();
        }else if(id.size() == 0){

            insertHouse.setHouse_id_hash(enId.encryHASH(house_id));
            insertHouse.setOwner_id(user_id);
            insertHouse.setOwner_name(user_name);
            insertHouse.setOwner(user);
            insertHouse.setRole(0);
            insertHouse.setState(state);
            insertHouse.setVerify(false);
            insertHouse.setLow_location(low_location);
            insertHouse.setLow_str_location(low_location.getString("provi")+low_location.getString("city")+low_location.getString("sector")+low_location.getString("commu_name"));
            insertHouse.setSpecific_location(specific_location);
            insertHouse.setFloor(floor);
            insertHouse.setElevator(elevator);
            insertHouse.setLease(lease);
            insertHouse.setLease_type(lease_type);
            insertHouse.setHouse_type(house_type);
            insertHouse.setHouse_owner_credit(house_owner_credit);
            insertHouse.setArea(area);
            insertHouse.setLat(lat);
            insertHouse.setLon(lon);

            //创建存放一个房子详细信息的文件夹
            File folder = new File("/path/theDetailedHouse");

            //将房子的文字详细信息放入文件夹中
            File houseInfo = new File(folder, "info.txt");
            forInsertHouse.insert_into_more_info(insertHouse,houseInfo.getPath());

            String[] pic_hash = new String[house_pic.length];
            try {
                for(int i = 0;i < house_pic.length;i++){
                    //将房子的图片放入文件夹并重新命名
                    String s = String.valueOf(i);
                    house_pic[i].renameTo(new File(folder.getPath(),s + ".jpg"));
                    pic_hash[i] = isi.upload(house_pic[i].getPath());
                }
                insertHouse.setHouse_pic(pic_hash);
                //得到房屋详细信息文件夹的hash
                String house_hash = isi.upload(folder.getPath());
                //插入概要信息表
                if(state == 1) {
                    forInsertHouse.insert(insertHouse, house_hash, "/path/housetable1");
                    //isi.upload("/path/housetable1");
                }
                else {
                    forInsertHouse.insert(insertHouse, house_hash, "/path/housetable2");
                    //isi.upload("/path/housetable2");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return insertHouse.HtoJson();
    }
}
