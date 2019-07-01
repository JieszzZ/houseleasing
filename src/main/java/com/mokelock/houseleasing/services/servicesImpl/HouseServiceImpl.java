package com.mokelock.houseleasing.services.servicesImpl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.model.HouseModel.*;


import com.mokelock.houseleasing.services.HouseService;
import com.mysql.cj.xdevapi.JsonArray;
import jnr.ffi.annotations.In;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.Int;

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
    public JSON speInfo(String house_hash) {
        //根据传进来的实参house_hash，去下载管理员的房屋表，得到这个房子所在文件夹的hash
        // 然后根据这个hash下载房屋文件夹，然后即可访问里面的文件

        //接口文档中说明：house_hash表示的是房产证的hash
        //house_pic直接从txt中下载

        IPFS_SERVICE_IMPL isi = new IPFS_SERVICE_IMPL();

        //isi.download("这个房子文件夹的hash，假设是thehousefilehash");
        //d:\thehousefilehash\info.txt    表示这个房屋的信息的TXT
        //d:\thehousefilehash\comment.txt    表示这个房屋的评论的TXT
        //d:\thehousefilehash\pic.txt    表示这个房屋的图片的TXT

        TableImpl dhtable = new TableImpl();

        String[] key_for_search = {"house_hash"};
        String[] value_to_search = {house_hash};
        String[] key_to_get = {"owner_id","verify", "owner","owner_name",
                "role","state", "provi","city",
                "sector","commu_name","specific_location", "floor",
                "lon", "lat", "elevator", "lease",
                "house_type", "house_credit", "house_level"};
        String[] key_to_getComment = {"user_id","comment","comment_pic"};

        //得到满足条件的房子的信息
        ArrayList<String[]> detailedHouse = dhtable.query(key_for_search, value_to_search, key_to_get, "");
        ArrayList<String[]> houseComment = dhtable.get_all(key_to_getComment, "");

        //获得所有评论的正确形式
        JSONArray house_comment = new JSONArray();
        HouseComment singleHouseComment;
        for(int i = 0 ;i < houseComment.size() ;i++){
            singleHouseComment = new HouseComment(houseComment.get(i)[0],houseComment.get(i)[1],
                    (houseComment.get(i)[2].substring(1,houseComment.get(i)[2].length())).split(","));
            house_comment.add(singleHouseComment.HCtoJson());
        }

        //存放得到的房子的图片
        String[] house_pic = new String[3];//

        //获得符合要求的形式
        String low_str_location = detailedHouse.get(0)[6]+detailedHouse.get(0)[7]+detailedHouse.get(0)[8]+detailedHouse.get(0)[9];
        //创建一个LowLocation类型的对象
        LowLocation ll = new LowLocation();
        //使用字符串类型的low_str_location来初始化ll
        ll.StoLL(low_str_location);
        //获得JSON对象类型的low_location
        JSONObject low_location = ll.LLtoJson();

        House theHouse = new House(house_pic,house_hash,detailedHouse.get(0)[0],Boolean.parseBoolean(detailedHouse.get(0)[1]),
                detailedHouse.get(0)[2],detailedHouse.get(0)[3],Integer.parseInt(detailedHouse.get(0)[4]),
                Integer.parseInt(detailedHouse.get(0)[5]), low_location,low_str_location,detailedHouse.get(0)[10],
                Integer.parseInt(detailedHouse.get(0)[11]), detailedHouse.get(0)[12], detailedHouse.get(0)[13],
                Boolean.parseBoolean(detailedHouse.get(0)[14]),Integer.parseInt(detailedHouse.get(0)[15]),
                Integer.parseInt(detailedHouse.get(0)[16]),Integer.parseInt(detailedHouse.get(0)[17]),
                Integer.parseInt(detailedHouse.get(0)[18]),house_comment);

        Response res = new Response(200,"success",theHouse.HtoJson());
        return res.RestoJson3();
    }

    @Override
    //搜索
    public JSON search(String low_location, String lease_inter, String house_type, String lease_type, boolean elevator) {

        //首先从IPFS中打开文件

        /*******************************************************************************************/
        //记录用户输入了几个查找条件，即用户调用该函数时的实参有几个是有具体数据的

        LowLocation lowlocation = new LowLocation();
        lowlocation.StoLL(low_location);

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
        String[] key_to_get = {"provi","city","sector","commu_name","lease","house_type","lease_type","elevator"};
        /**/

        //从文件下载获得
        String house_pic = new String();

        //查询满足要求的房子
        TableImpl tableforsearch = new TableImpl();
        ArrayList<String[]> returnedSH = tableforsearch.query(key_to_search,value_to_search,key_to_get,"");

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
        boolean elevator_single;

        for(int i = 0;i < returnedSH.size();i++){
            provi_single = returnedSH.get(i)[0];
            city_single = returnedSH.get(i)[1];
            sector_single = returnedSH.get(i)[2];
            commu_name_single = returnedSH.get(i)[3];
            lease_single = returnedSH.get(i)[4];
            house_type_single = returnedSH.get(i)[5];
            lease_type_single = returnedSH.get(i)[6];
            elevator_single = Boolean.getBoolean(returnedSH.get(i)[7]);

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
                sh = new SampleHouse(house_pic,provi_single+city_single+sector_single+commu_name_single,lease_single,house_type_single,lease_type_single,elevator_single);
                allsh.add(sh.SHtoJson());
        }
        Response resOfSearchSH = new Response(200,"success",allsh);

        return resOfSearchSH.RestoJson3();
    }

    @Override
    //评价房子
    public JSONObject valuation(String house_hash, int house_level, String comment, String[] comment_pic) {
        /*
         * 获取用户账号、文字评论和图片评论、房屋等级
         * 根据房源hash找到该房源的详细信息并将房源评论添加上
         * 返回状态消息
         * */
        String user_ID="123456";
        String p="123";
        TableImpl t=new TableImpl();
        t.insert_into_comment(user_ID,comment,comment_pic,p);

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
        String[] hash = {"123", "456"};
        String[] key_for_search={"house_hash"};

        JSONArray verified = new JSONArray();
        JSONArray non_verified = new JSONArray();
        JSONArray all=new JSONArray();
        JSONObject j=new JSONObject();
        String path="123";
        TableImpl thouse=new TableImpl();
        String[] key_to_get = {"verify","photo", "provi","city","sector" ,"commu_name","lease", "house_type", "lease_type","elevator"};

        for (int i = 0; i < hash.length; i++)
        {

            String[] value_for_search={hash[i]};
            //thouse.query(key_for_search,value_for_search,key_to_get,path);
            ArrayList<String[]> v1=thouse.query(key_for_search,value_for_search,key_to_get,path);

            for(int m=0;m<v1.size();m++){
                String s1="0";
                String s2="0";
                Boolean b=true;
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
                }if (v1.get(m)[9].equals("1")){
                    b=true;
                }if (v1.get(m)[9].equals("0")){
                    b=false;
                }
                String s3=v1.get(m)[2]+v1.get(m)[3]+v1.get(m)[4]+v1.get(m)[5];
                String lease=v1.get(m)[3];
                if(v1.get(m)[0]=="1"){

                    SampleHouse shouse1 = new SampleHouse(v1.get(m)[1],s3,lease,s1,s2,b);
                    shouse1.SHtoJson();
                    verified.add(shouse1);
                    //System.out.print(v1.get(m)[b]+" ");
                }
                if(v1.get(m)[0]=="0"){
                    SampleHouse shouse2=new SampleHouse(v1.get(m)[1],s3,lease,s1,s2,b);
                    shouse2.SHtoJson();
                    non_verified.add(shouse2);

                }
                System.out.println();
            }

            all.add( verified);
            all.add(non_verified);
            j.put("status",200);
            j.put("message","success");
            j.put("data",all);

        }

        return j;
    }
}
