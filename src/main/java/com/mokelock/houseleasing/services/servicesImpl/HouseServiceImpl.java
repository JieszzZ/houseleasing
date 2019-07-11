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
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * 李晓婷、张晨
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

        //根据house_id_hash查询简略信息表得到house_hash
        String[] key_for_search = {"house_id_hash"};
        String[] value_to_search = {house_id_hash};
        String[] hash_to_get = {"house_hash"};
        String house_hash = "";

        TableImpl dhtable = new TableImpl();
        BlockChain bc = new BlockChain();
        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        //新建文件用来存储概要信息表
        File fht1 = new File(System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt");
        File fht2 = new File(System.getProperty("user.dir")+"\\src\\main\\file\\housetable2.txt");
        //空表的hash："QmbFMke1KXqnYyBBWxB74N4c5SBnJMVAiMNRcGu6x1AwQH"
        try {
            //下载上线和下线概要房屋信息表
            isi.download(fht1.getPath(),bc.getHash(1),"");
            isi.download(fht2.getPath(),bc.getHash(2),"");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //从表中查询房源文件夹的hash
        ArrayList<String[]> HashToHouse1 = dhtable.query(key_for_search,value_to_search,hash_to_get,fht1.getPath());
        ArrayList<String[]> HashToHouse2 = dhtable.query(key_for_search,value_to_search,hash_to_get, fht2.getPath());

        //创建文件夹用于存储下载下来的房屋详细信息
        File dirdh=new File(System.getProperty("user.dir")+"\\src\\main\\file\\detailedHouse");
        //File dirdh=new File("C:\\Users\\Administrator\\Desktop\\img");
        if(!dirdh.exists()) {
            dirdh.mkdir();
        }

        //确定house_hash
        if(HashToHouse1.size() != 0){
            house_hash = HashToHouse1.get(0)[0];
        }else if(HashToHouse2.size() != 0){
            house_hash = HashToHouse2.get(0)[0];
        }else;

        //根据house_hash下载房源文件夹
        try{
            isi.download(dirdh.getPath(),house_hash,"");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //下载完成之后获取文件列表
        String[] dhl = dirdh.list();
        int piccount = 0;
        for (int i = 0; i < dhl.length; i++) {
            String s = dhl[i].substring(dhl[i].lastIndexOf("."));
            if(s.equals(".jpg"))
                piccount++;
            //System.out.println(dhl[i]);
        }

        String[] key_to_get = {"owner_id","verify", "owner","owner_name",
                "role","state", "area", "provi",
                "city", "sector","commu_name","specific_location",
                "floor", "lon", "lat",  "elevator",
                "lease", "house_type", "house_credit", "accessory"};
        String[] key_to_getComment = {"user_id","comment","comment_pic"};

        File info = new File(dirdh,"info.txt");
        File comment = new File(dirdh,"comment.txt");

        //得到满足条件的房子的信息
        JSONArray house_comment = new JSONArray();
        int commentpic = 0;
        ArrayList<String[]> detailedHouse = dhtable.query(key_for_search, value_to_search, key_to_get, info.getPath());
        if(dhl.length - piccount > 1){
            ArrayList<String[]> houseComment = dhtable.get_all(key_to_getComment, comment.getPath());
            //获得所有评论的正确形式
            HouseComment singleHouseComment;
            for(int i = 0 ;i < houseComment.size() ;i++){
                singleHouseComment = new HouseComment(houseComment.get(i)[0],houseComment.get(i)[1],
                        (houseComment.get(i)[2].substring(1,houseComment.get(i)[2].length()-1)).split(","));
                house_comment.add(singleHouseComment.HCtoJson());
                commentpic += (houseComment.get(i)[2].substring(1,houseComment.get(i)[2].length()-1)).split(",").length;
            }
            comment.delete();
        }

        info.delete();

        //存放得到的房子的图片
        String[] house_pic = new String[piccount-commentpic];
        for(int i = 0; i < piccount-commentpic ; i++){
            String s = String.valueOf(i);
            File pic = new File(dirdh,s+".jpg");
            house_pic[i] = house_hash + "/" + s + ".jpg";
            //pic.delete();
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
                Integer.parseInt(detailedHouse.get(0)[17]),Integer.parseInt(detailedHouse.get(0)[18]),detailedHouse.get(0)[19],house_comment);

        //dirdh.delete();
        //Response res = new Response(200,"success",);
        return theHouse.HtoJson();
    }

    @Override
    //搜索
    public JSON search(String low_location, String lease_inter, String house_type, String lease_type, boolean elevator, int page) {

        /*******************************************************************************************/
        //记录用户输入了几个查找条件，即用户调用该函数时的实参有几个是有具体数据的

        BlockChain bc = new BlockChain();
        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        LowLocation lowlocation = new LowLocation();
        lowlocation.StoLL(low_location);

        //只在在线房屋信息表中进行搜索
        File fht1 = new File(System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt");
        //"QmR1cRmDYbJL4zDGCBPojYDQaMpWinhiXVGeHNLTdarz6u"
        try {
            //下载在线房屋概要信息表
            isi.download(fht1.getPath(),bc.getHash(1),"");
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
        if(!lowlocation.getProvi().equals("山东省")){
            valued.add(0,1);
            count++;
        }else if(lowlocation.getProvi().equals("山东省")){
            valued.add(0,0);
        }
        if(!lowlocation.getCity().equals("济南市")){
            valued.add(1,1);
            count++;
        }else if(lowlocation.getCity().equals("济南市")){
            valued.add(1,0);
        }
        if(!lowlocation.getSector().equals("0")){
            valued.add(2,1);
            count++;
        }else if(lowlocation.getSector().equals("0")){
            valued.add(2,0);
        }
        if(!lowlocation.getCommu_name().equals("")){
            valued.add(3,1);
            count++;
        }else if(lowlocation.getCommu_name().equals("")){
            valued.add(3,0);
        }
        if(!lease_inter.equals("0")){
            valued.add(4,1);
            count++;
        }else if(lease_inter.equals("0")){
            valued.add(4,0);
        }
        if(!house_type.equals("0")){
            valued.add(5,1);
            count++;
        }else if(house_type.equals("0")){
            valued.add(5,0);
        }
        if(!lease_type.equals("0")){
            valued.add(6,1);
            count++;
        }else if(lease_type.equals("0")){
            valued.add(6,0);
        }
        if(!(String.valueOf(elevator)).equals("false")){
            valued.add(7,1);
            count++;
        }else if((String.valueOf(elevator)).equals("false")){
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
        for(int i = 0;i < key_to_search.length;i++){
            System.out.println(key_to_search[i]);
            System.out.println(value_to_search[i]);
        }

        System.out.println();
        ArrayList<String[]> returnedSH = tableforsearch.query(key_to_search,value_to_search,key_to_get,fht1.getPath());
        //ArrayList<String[]> returnedSH = tableforsearch.query(key_to_search,value_to_search,key_to_get,"C:\\Users\\Administrator\\Desktop\\housetable1.txt");

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

        int newPage = returnedSH.size()/6 + 1;

        if(page > newPage-1){
            size = 0;
        }else if(page == newPage-1){
            size = returnedSH.size()%6;
        }else if(page < newPage-1){
            size = 6;
        }

        for(int i = 6*page;i < 6*page + size;i++){
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

            sh = new SampleHouse(house_hash_single+"/0.jpg",low_str_location,lease_single,house_type_single,lease_type_single,elevator_single,house_id_hash_single);
            allsh.add(sh.SHtoJson());
        }

        JSONObject data = new JSONObject(true);
        data.put("houseList",allsh);
        data.put("page",newPage);
        //Response resOfSearchSH = new Response(200,"success",allsh);
        return data;
    }

    @Override
    public JSONObject setUpHouse(String user, String ethPassword, int house_owner_credit, String house_id, int state, JSONObject low_location, String specific_location, int floor, boolean elevator, int lease, int lease_type, int house_type, String lon, String lat, String area, String accessory, File[] house_pic) {

        //user_name要自己去区块链上查


        //添加一个房源
        TableImpl forInsertHouse = new TableImpl();

        String[] key_for_search_get = {"house_id"};
        String[] value_for_search = {house_id};

        House insertHouse = new House();
        BlockChain bc = new BlockChain();
        CiphersImpl enId = new CiphersImpl();
        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        //创建存放一个房子详细信息的文件夹
        File dirdh = new File(System.getProperty("user.dir")+"\\src\\main\\file\\detailedHouse");
        if(!dirdh.exists()) {
            dirdh.mkdir();
        }

        System.out.println(1);

        //定义三个表的路径
        File ueu = new File(System.getProperty("user.dir")+"\\src\\main\\file\\U_EthU.txt");
        File fht1 = new File(System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt");
        //if(!fht1.exists()) {
        //    try {
        //        fht1.createNewFile();
        //    } catch (IOException e) {
        // TODO: handle exception e.printStackTrace();
        //    }
        //}
        File fht2 = new File(System.getProperty("user.dir")+"\\src\\main\\file\\housetable2.txt");
        //"QmUeCWUpWwZSD7kYjEtRnKkATQ6ntaKX2k5ZF81QkifnkQ"
        try {
            isi.download(ueu.getPath(),bc.getHash(0),"");
            isi.download(fht1.getPath(),bc.getHash(1),"");
            isi.download(fht2.getPath(),bc.getHash(2),"");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(2);

        //查找用户姓名和身份证号所需要的
        String[] key_for_search_u = {"user_name"};
        String[] value_for_search_u = {user};
        String[] key_to_get_u = {"eth_id","SK"};
        ArrayList<String[]> fut= forInsertHouse.query(key_for_search_u,value_for_search_u,key_to_get_u,ueu.getPath());

        //判断该房源是否已经存在
        ArrayList<String[]> id1 = forInsertHouse.query(key_for_search_get,value_for_search,key_for_search_get,fht1.getPath());
        ArrayList<String[]> id2 = forInsertHouse.query(key_for_search_get,value_for_search,key_for_search_get,fht2.getPath());


        System.out.println(3);
        if(id1.size() != 0 || id2.size() != 0 || fut.size() == 0){
            //Response failRes = new Response(200,"fail");
            //toReturn = failRes.RestoJson2();
        }else if(id1.size() == 0 && id2.size() == 0 && fut.size() != 0){
            System.out.println("ownername is " + user);
            JSONObject u = bc.getMessage3(fut.get(0)[0],fut.get(0)[1],ethPassword);
            System.out.println(u.toJSONString());
            System.out.println("house_id in service is " + house_id);
            insertHouse.setHouse_id_hash(enId.encryHASH(house_id));
            insertHouse.setOwner_id(u.getString("id"));
            insertHouse.setOwner_name(u.getString("username"));
            System.out.println("username before insertHouse is " + insertHouse.getOwner_name());
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
            insertHouse.setAccessory(accessory);

            System.out.println("insertHosue is \n" + insertHouse.toString());

            System.out.println(4);
            int lease_inter;
            if(lease < 500){
                lease_inter = 1;
                insertHouse.setLease_inter(lease_inter);
            }else if(lease >= 500 && lease < 1000){
                lease_inter = 2;
                insertHouse.setLease_inter(lease_inter);
            }else if(lease >= 1000 && lease < 1500){
                lease_inter = 3;
                insertHouse.setLease_inter(lease_inter);
            }else if(lease >= 1500 && lease < 2000){
                lease_inter = 4;
                insertHouse.setLease_inter(lease_inter);
            }else if(lease >= 2000){
                lease_inter = 5;
                insertHouse.setLease_inter(lease_inter);
            }
            //insertHouse.setLease_inter(lease_inter);
            //System.out.println("1");
            File info = new File(dirdh, "info.txt");
            //将房子的文字详细信息放入文件夹中
            forInsertHouse.insert_into_more_info(insertHouse,info.getPath());
            //System.out.println(info.getPath());
            //将房子的图片放入文件夹中
            System.out.println(5);
            File[] pic = new File[house_pic.length];
            for(int i = 0; i< pic.length ; i++){
                String s = String.valueOf(i);
                pic[i] = new File(dirdh,s+".jpg");
                if(!pic[i].exists()) {
                    try {
                        pic[i].createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    BufferedImage image = ImageIO.read(new File(house_pic[i].getPath()));
                    ImageIO.write(image, "jpg", new File(pic[i].getPath()));
                } catch (Exception e)
                { // TODO: handle exception
                    e.printStackTrace();
                }
            }
            System.out.println(6);
            //存放所有图片的hash值
            String[] pic_hash = new String[house_pic.length];
            String[] pic_to_show = new String[house_pic.length];
            try {
                //得到房屋详细信息文件夹的hash
                String house_hash = isi.upload(dirdh.getPath());
                for(int i = 0;i < house_pic.length;i++){
                    String s = String.valueOf(i);
                    pic_hash[i] = isi.upload(pic[i].getPath());
                    pic_to_show[i] = house_hash + "/" + s + ".jpg";
                    //pic[i].delete();
                }
                System.out.println(7);
                insertHouse.setHouse_pic(pic_to_show);
                info.delete();
                for(int i = 0;i < pic.length;i++){
                    pic[i].delete();
                }
                //插入概要信息表
                if(state == 1) {
                    forInsertHouse.insert(insertHouse, house_hash, fht1.getPath());
                    String newHash = isi.upload(fht1.getPath());
                    System.out.println("上线概要信息表的新hash值"+newHash);
                    bc.changeTable(1,newHash);
                }
                else {
                    forInsertHouse.insert(insertHouse, house_hash, fht2.getPath());
                    String newHash = isi.upload(fht2.getPath());
                    System.out.println("下线概要信息表的新hash值"+newHash);
                    bc.changeTable(2,newHash);
                }
                //dirdh.delete();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(8);
        return insertHouse.HtoJson();
    }

    @Override
    //评价房子
    public String valuation(String user_id,String house_id_hash, String comment, File comment_pic[]) {
        /*
         * 获取用户账号、文字评论和图片评论、房屋等级
         * 根据房源hash找到该房源的详细信息并将房源评论添加上
         * 返回状态消息
         * */

        //String p="123";
        TableImpl t=new TableImpl();
        BlockChain b=new BlockChain();
        String  hash= b.getHash(1);

        //String hash="QmUeCWUpWwZSD7kYjEtRnKkATQ6ntaKX2k5ZF81QkifnkQ";
        IPFS_SERVICE_IMPL m=new IPFS_SERVICE_IMPL();
        String filePathName=System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt";
        String filename="housetable1.txt";
        try {
            m.download(filePathName,hash,filename);
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
        String filePathName1=System.getProperty("user.dir")+"\\src\\main\\file\\info";
        String filePathName2=System.getProperty("user.dir")+"\\src\\main\\file\\info\\comment.txt";
        //存放图片
        File dirdh = new File(System.getProperty("user.dir")+"\\src\\main\\file\\info");
        if(!dirdh.exists()) {
            try {
                dirdh.createNewFile();
            } catch (IOException e) {
                // TODO: handle exception e.printStackTrace();
            }
        }
        File[] pic = new File[comment_pic.length];
        String[]pic_hash=new String[comment_pic.length];
        for(int i = 0; i< pic.length ; i++){
            String s = String.valueOf(i);
            pic[i] = new File(dirdh,s+".jpg");
            if(!pic[i].exists()) {
                try {
                    pic[i].createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(pic[i].exists()){
                pic[i].delete();
                try {
                    pic[i].createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                BufferedImage image = ImageIO.read(new File(comment_pic[i].getPath()));
                System.out.println("路径为"+comment_pic[i].getPath());
                ImageIO.write(image, "jpg", new File(pic[i].getPath()));
                pic_hash[i]=m.upload(pic[i].getPath());
                System.out.println("路径为"+pic[i].getPath());
            } catch (Exception e)
            { // TODO: handle exception
                e.printStackTrace();
            }
        }

        String filename1="info.txt";
        try {

            m.download(filePathName1,p,filePathName1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        t.insert_into_comment(user_id,comment,pic_hash,filePathName2);
        String filepath=System.getProperty("user.dir")+"\\src\\main\\file\\info";
        try {
            String new_hash= m.upload(filepath);
            System.out.println("房子hash"+new_hash);
            House h=new House();
            h.setHouse_id_hash(house_id_hash);
            String[]key_to_update={"house_hash"};
            String[] new_value={new_hash};
            t.update(h,key_to_update,new_value,filePathName);
            String new_hash1=m.upload(filePathName);
            System.out.println("新表hash:"+new_hash1);


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
        String filePathName1=System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt";
        String filePathName2=System.getProperty("user.dir")+"\\src\\main\\file\\housetable2.txt";
        String filename1="housetable1.txt";
        String filename2="housetable2.txt";


        String[] key_for_search={"house_id_hash"};
        BlockChain chain=new BlockChain();
        String housetable1= chain.getHash(1);
        String housetable2=chain.getHash(2);
        //String  verified_house= chain.getHash(1);
        //String housetable1= "Qmf4Kui6PVPsMtRYphDxWR3jnNrjkdij8uvwZetYetHiSx";
        ///String housetable2="QmUVr4BMZUsbYgmtQhr9u6W6JxfyjPMkGDsExcWw9P6ACs";
        //String non_verified_house="QmaREvaaa7MHzfadQBhf5UxgSBzv9KGjSkRWyRke57ht29";

        try {
            System.out.println("123");
            i.download(filePathName1,housetable1,filename1);
            i.download(filePathName2,housetable2,filename2);
            System.out.println("1234");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path_verified_house=System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt";
        String path_non_verified_house=System.getProperty("user.dir")+"\\src\\main\\file\\housetable2.txt";

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
            String h1="src\\main\\file";

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
        BlockChain blockChain=new BlockChain();
        h.setHouse_id_hash(house_id_hash);
        String s1=String.valueOf(state);
        String s2=String.valueOf(elevator);
        String s3=String.valueOf(lease);
        String[] new_value1={s2,s3};
        String[] new_value2={s1};
        //String hash="QmUVr4BMZUsbYgmtQhr9u6W6JxfyjPMkGDsExcWw9P6ACs";
        String hash=blockChain.getHash(1);

        IPFS_SERVICE_IMPL i=new IPFS_SERVICE_IMPL();
        String p1=System.getProperty("user.dir")+"\\src\\main\\file\\housetable1.txt";
        String filename1="housetable1.txt";
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
        String p2=System.getProperty("user.dir")+"\\src\\main\\file\\info";
        String p3=System.getProperty("user.dir")+"\\src\\main\\file\\info\\info.txt";
        String filename2="info.txt";
        try {
            i.download(p2,hash_all,filename2);
        } catch (IOException e) {
            e.printStackTrace();
        }

        t.update(h,key_to_update1,new_value1,p1);
        t.update(h,key_to_update2,new_value2,p3);
        try {

            String new_hash2=i.upload(p2);
            House h1=new House();
            h1.setHouse_id_hash(house_id_hash);
            String[]key_to_update3={"house_hash"};
            String[] new_value3={new_hash2};

            t.update(h1,key_to_update3,new_value3,p1);
            String new_hash1= i.upload(p1);
            blockChain.changeTable(1,new_hash1);
            System.out.println(new_hash1);




        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    public static void main(String[] args){
        //IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        //String pic_hash = "";

        //LowLocation ll = new LowLocation("山东省","济南市","历城区","fr小区");
//
        //File[] pic = new File[4];
        //pic[0] = new File("C:\\Users\\Administrator\\Desktop\\img\\00.jpg");
        //pic[1] = new File("C:\\Users\\Administrator\\Desktop\\img\\00.jpg");
        //pic[2] = new File("C:\\Users\\Administrator\\Desktop\\img\\00.jpg");
        //pic[3] = new File("C:\\Users\\Administrator\\Desktop\\img\\00.jpg");


        //HouseServiceImpl hsi = new HouseServiceImpl();

        int size = 0;

        int newPage = 25/6 + 1;

        System.out.println(newPage);

        int page = 2;

        if(page > newPage-1){
            size = 0;
        }else if(page == newPage-1){
            size = 25%6;
        }else if(page < newPage-1){
            size = 6;
        }

        for(int i = 6*page;i<6*page+size;i++){
            System.out.println(i);

        }
        System.out.println(size);

        System.out.println(6*page);


        //JSONArray house = (JSONArray) hsi.search("山东省济南市高新区","1","1","1",false);

        //JSONObject house = (JSONObject) hsi.speInfo("lEj/IW4OvMJgYQbg3BynVA==");

        //System.out.println(house);

    }
}
