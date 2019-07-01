package com.mokelock.houseleasing.services.servicesImpl;

import com.alibaba.fastjson.JSON;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

        //打开IPFS中存储房子详细信息的文件夹，每个房子的house_pic放在一个文件中，评论放在一个文件中
        // 除图片和文件之外的房子信息每个房子放在一个文件中

        TableImpl htable = new TableImpl();
        String[] key_to_search = {"house_hash"};
        String[] value_to_search = {house_hash};
        String[] key_to_get = {"house_pic","owner_id","verify",
                "owner","owner_name","role","state",
                "low_location","specific_location","floor",
                "elevator", "lease","house_type","house_owner_credit","house_level","user_id","comment","comment_pic"};

        ArrayList<String[]> dh = htable.query(key_to_search,value_to_search,key_to_get,"");

        String[] house_pic = new String[3];
        LowLocation ll = new LowLocation();
        ll.StoLL(dh.get(0)[7]);
        JSONObject low_location = ll.LLtoJson();

        //String[] comment_pic = new String[];



        //JSONObject house_comment_single = hc.HCtoJson();
        //JSONArray house_comment = new JSONArray();

        //for(int i=0;i<评论的数量;i++){
        // HouseComment hc = new HouseComment(dh.get(0)[15],dh.get(0)[16],comment_pic);
        //house_comment.add(hc);
        // }

        //House detailedHouse = new House(house_pic,house_hash,dh.get(0)[1],Integer.parseInt(dh.get(0)[2]),
        //        dh.get(0)[3],dh.get(0)[4],Integer.parseInt(dh.get(0)[5]),Integer.parseInt(dh.get(0)[6]),
        //        low_location,dh.get(0)[7],dh.get(0)[8],Integer.parseInt(dh.get(0)[9]),
        //        Boolean.parseBoolean(dh.get(0)[10]),Integer.parseInt(dh.get(0)[11]),
        //        Integer.parseInt(dh.get(0)[12]),Integer.parseInt(dh.get(0)[13]),
        //        Integer.parseInt(dh.get(0)[14]),house_comment);

        //Response res = new Response(200,"success",detailedHouse);

        //return res.RestoJson3();

        //房源的hash就是房源在IPFS中的索引，根据索引就可以得到该房源的其他相关信息
        //然后分层将房源的信息创建为json格式
        //最后使用第二个构造函数创建House对象outputhouse将房源信息录入，并转化为json格式
        //最后使用response来创建最终的返回信息
        return null;
    }

    @Override
    //搜索
    public JSON search(String low_location, String lease_inter, String house_type, String lease_type, boolean elevator) {

        //首先从IPFS中打开文件

        /*******************************************************************************************/
        //记录用户输入了几个查找条件，即用户调用该函数时的实参有几个是有具体数据的
        int count = 0;
        //将所有可能被用户输入的实参的变量名和变量值
        String[] allkey = {"low_location","lease_inter","house_type","lease_type","elevator"};
        String[] allvalue = {low_location,lease_inter,house_type,lease_type,String.valueOf(elevator)};

        //记录各实参索引的状态，1表示有具体值，0表示空或“”
        List<Integer> valued = new ArrayList<>();
        //如果实参有具体值，则记录该实参在
        if(!low_location.equals("")){
            valued.add(0,1);
            count++;
        }else if(low_location.equals("")){
            valued.add(0,0);
        }
        if(!lease_inter.equals("")){
            valued.add(1,1);
            count++;
        }else if(lease_inter.equals("")){
            valued.add(1,0);
        }
        if(!house_type.equals("")){
            valued.add(2,1);
            count++;
        }else if(house_type.equals("")){
            valued.add(2,0);
        }
        if(!lease_type.equals("")){
            valued.add(3,1);
            count++;
        }else if(lease_type.equals("")){
            valued.add(3,0);
        }
        if(!(String.valueOf(elevator)).equals("")){
            valued.add(4,1);
            count++;
        }else if((String.valueOf(elevator)).equals("")){
            valued.add(4,0);
        }
        /*******************************************************************************************/

        /**/
        //定义要传入数据文件查询方法的实参
        String[] key_to_search = new String[count];
        String[] value_to_search = new String[count];
        int in = 0;
        for(int i = 0; i < 5; i++){
            if(valued.indexOf(i) == 1){
                key_to_search[in] = allkey[i];
                value_to_search[in] = allvalue[i];
                in++;
            }
        }
        //定义要得到的属性的集合
        String[] key_to_get = {"house_pic","low_location","lease","house_type","lease_type","elevator"};
        /**/

        //查询满足要求的房子
        TableImpl tableforsearch = new TableImpl();
        ArrayList<String[]> returnedSH = tableforsearch.query(key_to_search,value_to_search,key_to_get,"");

        //声明一个概要信息房子对象
        SampleHouse sh;
        //将所有房子想要输出的概要消息转化为json数组
        JSONArray allsh = new JSONArray();
        String house_pic_single;
        String low_str_location_single;
        String lease_single;
        String house_type_single;
        String lease_type_single;
        boolean elevator_single;

        for(int i = 0;i < returnedSH.size();i++){
            house_pic_single = returnedSH.get(i)[0];
                low_str_location_single = returnedSH.get(i)[1];
                lease_single = returnedSH.get(i)[2];
                house_type_single = returnedSH.get(i)[3];
                lease_type_single = returnedSH.get(i)[4];
                elevator_single = Boolean.getBoolean(returnedSH.get(i)[5]);

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
                sh = new SampleHouse(house_pic_single,low_str_location_single,lease_single,house_type_single,lease_type_single,elevator_single);
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
                if(v1.get(m)[4]=="0"){
                    s1="全部";
                }if (v1.get(m)[4]=="1"){
                    s1="一室";
                }if (v1.get(m)[4]=="2"){
                    s1="二室";
                }if (v1.get(m)[4]=="3"){
                    s1="其他";
                }
                if (v1.get(m)[5]=="0"){
                    s2="全部";
                }if (v1.get(m)[5]=="1"){
                    s2="整租";
                }if (v1.get(m)[5]=="2"){
                    s2="合租";
                }if (v1.get(m)[6]=="1"){
                    b=true;
                }if (v1.get(m)[6]=="0"){
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
