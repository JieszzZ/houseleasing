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
import java.nio.file.Files;
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
            //下载上线和下线概要房屋信息表
            isi.download("E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt","QmUUFbHapyxDVbjHbTQfbPaaD13vbmQSGZAbJFwA9WfqbM","");
            //isi.download("E:\\Git\\houseleasing\\src\\main\\path\\housetable2.txt",bc.getHash(2),"");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //从表中查询房源文件夹的hash
        ArrayList<String[]> HashToHouse = dhtable.query(key_for_search,value_to_search,hash_to_get,"E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt");
        //ArrayList<String[]> HashToHouse2 = dhtable.query(key_for_search,value_to_search,hash_to_get,"E:\\Git\\houseleasing\\src\\main\\path\\housetable2.txt");

        //确定house_hash
        if(HashToHouse.size() != 0){
            house_hash = HashToHouse.get(0)[0];
        //}else if(HashToHouse2.size() != 0){
            //house_hash = HashToHouse2.get(0)[0];
        }else;

        //根据house_hash下载房源文件夹
        try{
            isi.download("E:\\Git\\houseleasing\\src\\main\\path\\detailedHouse",house_hash,"");
            //isi.download("E:\\Git\\houseleasing\\src\\main\\path\\detailedHouse\\comment.txt",house_hash+"\\comment.txt","");
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
        ArrayList<String[]> detailedHouse = dhtable.query(key_for_search, value_to_search, key_to_get, "E:\\Git\\houseleasing\\src\\main\\path\\detailedHouse\\info.txt");
        ArrayList<String[]> houseComment = dhtable.get_all(key_to_getComment, "E:\\Git\\houseleasing\\src\\main\\path\\detailedHouse\\comment.txt");

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
        for(int i = 0; i < house_pic.length ; i++){

            String s = String.valueOf(i+1);

            house_pic[i] = house_hash + s + ".jpg";
        }

        System.out.println(detailedHouse.size());

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

        /*******************************************************************************************/
        //记录用户输入了几个查找条件，即用户调用该函数时的实参有几个是有具体数据的

        BlockChain bc = new BlockChain();

        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        LowLocation lowlocation = new LowLocation();
        lowlocation.StoLL(low_location);

        try {
            //下载在线房屋概要信息表
            isi.download("E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt","QmWoRCmj2wEoqg8FykTTyBDMyhuVQVucA9TbWLauGRimm8","");
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
        String[] key_to_get = {"provi","city","sector","commu_name","lease","house_type","lease_type","elevator","house_id_hash","house_hash"};
        /**/


        //查询满足要求的房子
        TableImpl tableforsearch = new TableImpl();
        ArrayList<String[]> returnedSH = tableforsearch.query(key_to_search,value_to_search,key_to_get,"E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt");

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
        String house_hash_single;

        int size = 0;

        if(returnedSH.size() > 6){

            size = 6;

        }else if(returnedSH.size() <= 6){

            size = returnedSH.size();

        }

        for(int i = 0;i < size;i++){
            provi_single = returnedSH.get(i)[0];
            city_single = returnedSH.get(i)[1];
            sector_single = returnedSH.get(i)[2];
            commu_name_single = returnedSH.get(i)[3];
            lease_single = returnedSH.get(i)[4];
            house_type_single = returnedSH.get(i)[5];
            lease_type_single = returnedSH.get(i)[6];
            elevator_single = Boolean.getBoolean(returnedSH.get(i)[7]);
            house_id_hash_single = returnedSH.get(i)[8];
            house_hash_single = returnedSH.get(i)[9];

            //String[] key = {"house_id_hash"};
            //String[] value = new String[1];
            //String[] get = {"house_hash"};

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

            //String house_pic = new String();

            //value[1] = house_id_hash_single;

            //ArrayList<String[]> forPic = tableforsearch.query(key,value,get,"/path/housetable1.txt");

            //try {
            //    isi.download("/path/detailedHouse/0.jpg",forPic.get(0)[0],"");
            //} catch (IOException e) {
            //    e.printStackTrace();
            //}
            sh = new SampleHouse(house_hash_single+"\\0.jpg",low_str_location,lease_single,house_type_single,lease_type_single,elevator_single,house_id_hash_single);
            allsh.add(sh.SHtoJson());
        }
        //Response resOfSearchSH = new Response(200,"success",allsh);

        return allsh;
    }

    @Override
    public JSONObject valuation(String user_id,String house_id_hash,String comment, String[] comment_pic) {
        /*
         * 获取用户账号、文字评论和图片评论、房屋等级
         * 根据房源hash找到该房源的详细信息并将房源评论添加上
         * 返回状态消息
         * */

        //String p="123";
        TableImpl t=new TableImpl();

        BlockChain b=new BlockChain();
        //String  hash= b.getHash(1);
        String hash="QmaREvaaa7MHzfadQBhf5UxgSBzv9KGjSkRWyRke57ht29";
        IPFS_SERVICE_IMPL i=new IPFS_SERVICE_IMPL();
        String filePathName="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\verified_house.txt";
        String filename="verified_house.txt";
        try {
            i.download(filePathName,hash,filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] key_for_search={"house_id_hash"};
        String[] value_for_search={house_id_hash};
        String[] key_to_get={"house_hash"};
        ArrayList<String[]> a1=t.query(key_for_search,value_for_search,key_to_get,filePathName);
        String[] hash1 = new String[a1.size()];
        for (int f=0;f<a1.size();f++){
            hash1[f]=a1.get(f)[0];
            System.out.println("123"+ hash1[f]);
        }
        String p=hash1[0];
        System.out.println(p);
        String filePathName1="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\"+p+"\\info.txt";
        String filename1="verified_house_all.txt";
        try {
            i.download(filePathName1,p,filePathName1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        t.insert_into_comment(user_id,comment,comment_pic,filePathName1);
        String filepath="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\"+p+"\\verified_house_all.txt";
        try {
            i.upload(filepath);
        } catch (IOException e) {
            e.printStackTrace();
        }

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
        TableImpl thouse=new TableImpl();
        IPFS_SERVICE_IMPL i=new IPFS_SERVICE_IMPL();
        String filePathName1="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\housetable1.txt";
        String filePathName2="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\housetable2.txt";
        String filename1="housetable1.txt";
        String filename2="housetable2.txt";


        String[] key_for_search={"house_id_hash"};
        BlockChain chain=new BlockChain();
        //String  verified_house= chain.getHash(1);
        String housetable1= "QmVXfrXfLc2dmqwYZmrictMhfdkBtg432Ha2Tu5c6KNY89";
        String housetable2="QmbFMke1KXqnYyBBWxB74N4c5SBnJMVAiMNRcGu6x1AwQH";
        //String non_verified_house="QmaREvaaa7MHzfadQBhf5UxgSBzv9KGjSkRWyRke57ht29";

        try {
            System.out.println("123");
            i.download(filePathName1,housetable1,filename1);
            i.download(filePathName2,housetable2,filename2);
            System.out.println("1234");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path_verified_house="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\housetable1.txt";
        String path_non_verified_house="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\housetable2.txt";

        ArrayList<String[]> a1=thouse.get_all(key_for_search,path_verified_house);
        ArrayList<String[]> a2=thouse.get_all(key_for_search,path_non_verified_house);
        String[] hash1 = new String[a1.size()];
        String[] hash2 = new String[a2.size()];


        //String[] hash1;
        for(int a=0;a<a1.size();a++){
            hash1[a]=a1.get(a)[0];
            //System.out.println(hash1[a]);
            // System.out.println("1"+a1.size());

        }
        for(int a=0;a<a2.size();a++){
            hash2[a]=a2.get(a)[0];
            //  System.out.println(hash2[a]);
            // System.out.println("1"+a1.size());

        }

        JSONArray verified = new JSONArray();
        JSONArray non_verified = new JSONArray();
        JSONObject data=new JSONObject();
        String[] key_to_get = {"provi","city","sector" ,"commu_name","lease", "house_type", "lease_type","elevator","house_hash","verify"};

        for (int h = 0;h < hash1.length; h++)
        {

            String[] value_for_search={hash1[h]};
            ArrayList<String[]> v1=thouse.query(key_for_search,value_for_search,key_to_get,path_verified_house);
            String h1="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file";

            for(int m=0;m<v1.size();m++){
                String s1="0";
                String s2="0";
                Boolean b=Boolean.valueOf(v1.get(m)[7]);

                String s=v1.get(m)[2];
                if(v1.get(m)[5].equals("0")){
                    s1="全部";
                }if (v1.get(m)[5].equals("1")){
                    s1="一室";
                }if (v1.get(m)[5].equals("2")){
                    s1="二室";
                }if (v1.get(m)[5].equals("3")){
                    s1="其他";
                }
                if (v1.get(m)[6].equals("0")){
                    s2="全部";
                }if (v1.get(m)[6].equals("1")){
                    s2="整租";
                }if (v1.get(m)[6].equals("2")){
                    s2="合租";
                }

                String photo_filename=v1.get(m)[8];
                String photo=photo_filename+"\\0.jpg";
                String s3=v1.get(m)[0]+v1.get(m)[1]+v1.get(m)[2]+v1.get(m)[3];
                String lease=v1.get(m)[4];
                if(v1.get(m)[9].equals("true")){
                    SampleHouse shouse1 = new SampleHouse(photo,s3,lease,s1,s2,b,hash1[h]);
                    verified.add(shouse1.SHtoJson());
                }if(v1.get(m)[9].equals("false")){
                    SampleHouse shouse1 = new SampleHouse(photo,s3,lease,s1,s2,b,hash1[h]);
                    non_verified.add(shouse1.SHtoJson());
                }


            }
            data.put("verified",verified);
            data.put("non_verified",non_verified);

        }
        for (int j=0;j<hash2.length;j++){
            String[] value_for_search={hash2[j]};
            ArrayList<String[]> v2=thouse.query(key_for_search,value_for_search,key_to_get,path_non_verified_house);
            System.out.println("1");
            for(int d=0;d<v2.size();d++){
                String s1="0";
                String s2="0";
                Boolean b=Boolean.valueOf(v2.get(d)[7]);
                String s=v2.get(d)[2];
                if(v2.get(d)[5].equals("0")){
                    s1="全部";
                }if (v2.get(d)[5].equals("1")){
                    s1="一室";
                }if (v2.get(d)[5].equals("2")){
                    s1="二室";
                }if (v2.get(d)[5].equals("3")){
                    s1="其他";
                }
                if (v2.get(d)[6].equals("0")){
                    s2="全部";
                }if (v2.get(d)[6].equals("1")){
                    s2="整租";
                }if (v2.get(d)[6].equals("2")){
                    s2="合租";
                }

                //String photo_path="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\non_verified_house\\v2.get(m)[6]";
                String photo_filename=v2.get(d)[8];
                String photo=photo_filename+"\\0.jpg";
                //String photo="1234";
                //String photo="c\\user\\1\\desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\non_verified_house\\v2.get(m)[6]\\1.jpg";
                String s3=v2.get(d)[0]+v2.get(d)[1]+v2.get(d)[2]+v2.get(d)[3];
                String lease=v2.get(d)[4];
                SampleHouse shouse2 = new SampleHouse(photo,s3,lease,s1,s2,b,hash2[j]);
                //System.out.println("12");

                non_verified.add(shouse2.SHtoJson());
                //System.out.println("123");

            }
            data.put("verified",verified);
            System.out.println("1234");

        }
        return data;

    }

    @Override
    public JSON myHouse(String house_id_hash, int state, boolean elevator, int lease) {

        TableImpl t=new TableImpl();
        String[]key_to_update1={"elevator","lease"};
        String[]key_to_update2={"state"};
        House h=new House();
        h.setHouse_id_hash(house_id_hash);
        String s1=String.valueOf(state);
        String s2=String.valueOf(elevator);
        String s3=String.valueOf(lease);
        String[] new_value1={s2,s3};
        String[] new_value2={s1};
        String hash="";

        IPFS_SERVICE_IMPL i=new IPFS_SERVICE_IMPL();
        String p1="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\verified_house.txt";
        String filename1="verified_house.txt";
        String[] key_for_search={"house_id_hash"};
        String[] value_for_search={house_id_hash};
        String[] key_to_get={"house_hash"};
        try {
            i.download(p1,hash,filename1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<String[]> v1=t.query(key_for_search,value_for_search,key_to_get,p1);
        String hash1[]=new String[v1.size()];
        for (int j=0;j<v1.size();j++){
            hash1[j]=v1.get(j)[0];
        }
        String hash_all=hash1[0];
        String p2="C:\\Users\\1\\Desktop\\新建文件夹\\houseleasing\\houseleasing\\src\\main\\file\\"+hash_all+"\\all.tct";
        String filename2="all.txt";
        try {
            i.download(p2,hash_all,filename2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        t.update(h,key_to_update1,new_value1,p1);
        t.update(h,key_to_update2,new_value2,p2);


        return null;
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
                            //isi.download("/path/housetable1",bc.getHash(1),"");
                            //isi.download("/path/housetable2",bc.getHash(2),"");
                            isi.download("E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt","QmVLCpEypACxpAexUnRvhedwY9DLCz4sUe5FujndcrckaA"
                                    ,"");
                            isi.download("E:\\Git\\houseleasing\\src\\main\\path\\housetable2.txt","QmbFMke1KXqnYyBBWxB74N4c5SBnJMVAiMNRcGu6x1AwQH"
                                    ,"");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        //ArrayList<String[]> id = forInsertHouse.query(key_for_search_get,value_for_search,key_for_search_get,"E:\Git\houseleasing\src\main\path\housetable1.txt");
                        //ArrayList<String[]> id2 = forInsertHouse.query(key_for_search_get,value_for_search,key_for_search_get,"E:\Git\houseleasing\src\main\path\housetable2.txt");


                        /*if(id.size() != 0 || id2.size() != 0){
                            //Response failRes = new Response(200,"fail");
                            //toReturn = failRes.RestoJson2();
                        }else if(id.size() == 0 && id2.size() == 0){*/

                            insertHouse.setHouse_id_hash(enId.encryHASH(house_id));
                            insertHouse.setOwner_id(user_id);
                            insertHouse.setOwner_name(user_name);
                            insertHouse.setOwner(user);
                            insertHouse.setRole(0);
                            insertHouse.setState(state);
                            insertHouse.setVerify(true);
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
                            File folder = new File("E:\\Git\\houseleasing\\src\\main\\path\\detailedHouse");

                            System.out.println("1");

                            //将房子的文字详细信息放入文件夹中
                            File houseInfo = new File(folder, "info.txt");
                            forInsertHouse.insert_into_more_info(insertHouse,houseInfo.getPath());

                            System.out.println(houseInfo.getPath());

                            String[] pic_hash = new String[house_pic.length];
                            try {
                                for(int i = 0;i < house_pic.length;i++){
                                    //将房子的图片放入文件夹并重新命名
                                    String s = String.valueOf(i);
                                    //house_pic[i].renameTo(new File(folder,s+".jpg"));
                                    //File pic = new File(folder, s+".jpg");
                                    System.out.println(house_pic[i].getPath());
                                    //Files.copy(house_pic[i].toPath(),pic.toPath());
                                    pic_hash[i] = isi.upload(house_pic[i].getPath());
                                    System.out.println(pic_hash[i]);
                                }
                                insertHouse.setHouse_pic(pic_hash);
                                //得到房屋详细信息文件夹的hash
                String house_hash = isi.upload(folder.getPath());
                //插入概要信息表
                if(state == 1) {
                    forInsertHouse.insert(insertHouse, house_hash, "E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt");
                    String newHash = isi.upload("E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt");
                    System.out.println("上线概要信息表的新hash值"+newHash);
                }
                else {
                    forInsertHouse.insert(insertHouse, house_hash, "E:\\Git\\houseleasing\\src\\main\\path\\housetable2.txt");
                    String newHash = isi.upload("E:\\Git\\houseleasing\\src\\main\\path\\housetable2.txt");
                    System.out.println("下线概要信息表的新hash值"+newHash);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        //}
        return insertHouse.HtoJson();
    }

    public static void main(String[] args){

        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        //String pic_hash = "";

//        LowLocation ll = new LowLocation("山东省","济南市","高新区","ffff小区");
//
//        File[] pic = new File[4];
//        pic[0] = new File("C:\\Users\\Administrator\\Desktop\\img\\0.jpg");
//        pic[1] = new File("C:\\Users\\Administrator\\Desktop\\img\\1.jpg");
//        pic[2] = new File("C:\\Users\\Administrator\\Desktop\\img\\2.jpg");
//        pic[3] = new File("C:\\Users\\Administrator\\Desktop\\img\\3.jpg");
//
//
//        HouseServiceImpl hsi = new HouseServiceImpl();

        //JSONObject house = hsi.setUpHouse("370784199811100112","王某","WEMZ",16,"HID0330",1,ll.LLtoJson(),"山东省济南市高新区ffff小区",7,false,200,0,1,"12.5","13.8","40",pic);
        //hsi.setUpHouse("370784199811100112","王某","WEMZ",16,"HID0330",1,ll.LLtoJson(),"山东省济南市高新区ffff小区",7,false,200,0,1,"12.5","13.8","40",pic);
        //hsi.setUpHouse("370784199811297777","张某","ZEMZ",16,"HID0331",1,ll.LLtoJson(),"山东省济南市高新区ZZZZ小区",3,true,200,0,1,"12.5","13.8","60",pic);
        //hsi.setUpHouse("370784199807100112","曲某","QEMZ",16,"HID0332",1,ll.LLtoJson(),"山东省济南市高新区QQQQ小区",4,false,200,0,1,"12.5","13.8","50",pic);
        //hsi.setUpHouse("370784199809100112","李某","LEMZ",16,"HID0333",1,ll.LLtoJson(),"山东省济南市高新区LLLL小区",5,true,200,0,1,"12.5","13.8","70",pic);


        //System.out.println(house.get("owner_id"));

        //try {
        //    pic_hash = isi.upload("E:\\Git\\houseleasing\\src\\main\\path\\123.txt");
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}
        //System.out.println(pic_hash);

        //JSONArray h = (JSONArray) hsi.search("","","","",true);

        //System.out.println(h.get(h.size()-1));

        //JSONObject j = (JSONObject) hsi.speInfo("lEj/IW4OvMJgYQbg3BynVA==");

        String s = "";

        try {

            isi.download("D:\\1.txt","QmWoRCmj2wEoqg8FykTTyBDMyhuVQVucA9TbWLauGRimm8","");

           // s = isi.upload("E:\\Git\\houseleasing\\src\\main\\path\\housetable1.txt");
        } catch (IOException e) {
//            e.printStackTrace();
        }

        //System.out.println(s);


        //File f = new File(

    }
}
