package com.mokelock.houseleasing.IPFS.TableImpl;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.alibaba.fastjson.*;

import java.util.*;

import java.io.*;

public class TableImpl implements  Table{
    public void create(String path){
        File table_file=new File(path);
    }
    public void insert(String user_name,String eth_id,String SK,String path){
        File file=new File(path);
        Map map=new HashMap();
        map.put("user_name",user_name);
        map.put("eth_id",eth_id);
        map.put("SK",SK);
        String msg=JSON.toJSONString(map);
        try{
            FileOutputStream fos=new FileOutputStream(path,true);
            String template=msg+"\r\n";
            fos.write( template.getBytes("utf-8") );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void delete(House[] house_obj,String path){
        File file=new File(path);
//        String house_id=house_obj.getHouse_id();
        List<String> list=new ArrayList();
        try{
            String str=null;
            BufferedReader br=new BufferedReader(new FileReader(path));
            while((str=br.readLine())!=null){
                JSONObject jsonObject=JSONObject.parseObject(str);
                for(int i=0;i<house_obj.length;i++){
                    if(!jsonObject.getString("house_id").equals(house_obj[i].getHouse_id())) {
                        if(!list.contains(str))
                            list.add(str);
                    }
                }
            }
            FileOutputStream fos=new FileOutputStream(path,false);
//            String template=msg+"\r\n";
            for(int i=0;i<list.size();i++){
                String template=list.get(i)+"\r\n";
                fos.write( template.getBytes("utf-8") );
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void update(House house_obj,String[]key_to_update,String[] new_value,String path){
        File file=new File(path);
        List<String> list=new ArrayList<>();
        try{
            String str=null;
            BufferedReader br=new BufferedReader(new FileReader(path));
            while((str=br.readLine())!=null){
                JSONObject jsonObject=JSONObject.parseObject(str);
                if(jsonObject.getString("house_id_hash").equals(house_obj.getHouse_id_hash())){
                    for(int i=0;i<key_to_update.length;i++){
                        jsonObject.put(key_to_update[i],new_value[i]);
                    }
                    str=jsonObject.toJSONString();
                }
                list.add(str);
            }
            FileOutputStream fos=new FileOutputStream(path,false);
//            String template=msg+"\r\n";
            for(int i=0;i<list.size();i++){
                String template=list.get(i)+"\r\n";
                fos.write( template.getBytes("utf-8") );
            }
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public ArrayList<String[]> get_all(String[] key_to_get,String path){
        ArrayList<String[]> value=new ArrayList<String[]>();
        try{
            String str  = null;
            BufferedReader br=new BufferedReader(new FileReader(path));
            while((str = br.readLine()) != null){
                JSONObject jsonObject=JSONObject.parseObject(str);
                JSONArray jsonArray=null;
                int counter=0;
                String[]element=new String[key_to_get.length];
                for(int i=0;i<key_to_get.length;i++){
                    element[i]=jsonObject.getString(key_to_get[i]);
                }
                value.add(element);
            }
            return value;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            System.out.println("不存在的关键字");
        }
        String[] error={"not found"};
        return value;
    }

    public ArrayList<String[]> query(String[] key_for_search, String[] value_for_search, String[] key_to_get, String path){
        ArrayList<String[]> value=new ArrayList<String[]>();
//        String[] element=new String[key_to_get.length];
        try{
            String str  = null;
            BufferedReader br=new BufferedReader(new FileReader(path));
            while((str = br.readLine()) != null){
                JSONObject jsonObject=JSONObject.parseObject(str);
//                System.out.print(result[1]+" "+result[2]+"\n");
                int counter=0;
                for(int i=0;i<key_for_search.length;i++){
                    String v=jsonObject.getString(key_for_search[i]);
                    if(!v.equals(value_for_search[i])){
                        break;
                    }
                    counter++;

                }
                String[] element=new String[key_to_get.length];
//                for (int i = 0; i < key_to_get.length; i++) {
//                    element[i] = "init";
//                }
                if(counter==key_for_search.length) {
                    for (int i = 0; i < key_to_get.length; i++) {
                        element[i] = jsonObject.getString(key_to_get[i]);
                    }
                     value.add(element);
                }
            }
            return value;
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch (NullPointerException e){
            System.out.println("不存在的关键字");
        }
        String[] error={"not found"};
        return value;
    }
    public void insert(House house_obj,String house_hash,String path){
        String house_id=house_obj.getHouse_id();
        String house_id_hash=house_obj.getHouse_id_hash();
        String lease_inter=String.valueOf(house_obj.getLease_inter());
        String house_type=String.valueOf(house_obj.getHouse_type());
        String lease_type=String.valueOf(house_obj.getLease_type());
        String lease=String.valueOf(house_obj.getLease());
        String elevator=String.valueOf(house_obj.isElevator());
        String househash=String.valueOf(house_obj.getHouse_id_hash());
        JSONObject loc=house_obj.getLow_location();
        String provi=loc.getString("provi");
        String city=loc.getString("city");
        String sector=loc.getString("sector");
        String commu_name=loc.getString("commu_name");
        File file=new File(path);
        try{
            FileOutputStream fos=new FileOutputStream(path,true);
            Map map=new HashMap();
            map.put("house_id",house_id);
            map.put("house_id_hash",house_id_hash);
            map.put("house_hash",house_hash);
            map.put("lease_inter",lease_inter);
            map.put("house_type",house_type);
            map.put("lease_type",lease_type);
            map.put("provi",provi);
            map.put("city",city);
            map.put("sector",sector);
            map.put("commu_name",commu_name);
            map.put("lease",lease);
            map.put("elevator",elevator);
            String msg=JSON.toJSONString(map);
            String template=msg+"\r\n";
            fos.write( template.getBytes("utf-8") );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public void insert_into_more_info(House house_obj,String path){
        String house_id=house_obj.getHouse_id();
        String house_id_hash= house_obj.getHouse_id_hash();
        String owner_id=house_obj.getOwner_id();
        String owner_name=house_obj.getOwner_name();
        String owner=house_obj.getOwner();
        String role=String.valueOf(house_obj.getRole());
        String state=String.valueOf(house_obj.getState());
        String verify=String.valueOf(house_obj.isVerify());
        JSONObject loc=house_obj.getLow_location();
        String provi=loc.getString("provi");
        String city=loc.getString("city");
        String sector=loc.getString("sector");
        String commu_name=loc.getString("commu_name");
        String spe_loc=house_obj.getSpecific_location();
        String floor=String.valueOf(house_obj.getFloor());
        String elevator=String.valueOf(house_obj.isElevator());
        String lease=String.valueOf(house_obj.getLease());
        String house_type=String.valueOf(house_obj.getHouse_type());
        String house_credit=String.valueOf(house_obj.getHouse_owner_credit());
        String lon=String.valueOf(house_obj.getLon());
        String lat=String.valueOf(house_obj.getLat());
        String area=String.valueOf(house_obj.getArea());
        Map<String,String>map=new HashMap<>();
        map.put("house_id",house_id);
        map.put("house_id_hash",house_id_hash);
        map.put("owner_id",owner_id);
        map.put("verify",verify);
        map.put("owner_name",owner_name);
        map.put("owner",owner);
        map.put("role",role);
        map.put("state",state);
        map.put("provi",provi);
        map.put("city",city);
        map.put("sector",sector);
        map.put("commu_name",commu_name);
        map.put("specific_location",spe_loc);
        map.put("floor",floor);
        map.put("elevator",elevator);
        map.put("lease",lease);
        map.put("house_type",house_type);
        map.put("house_credit",house_credit);
        map.put("lon",lon);
        map.put("lat",lat);
        map.put("area",area);
        String msg=JSON.toJSONString(map);
        String template=msg+"\r\n";
        try{
            FileOutputStream fos=new FileOutputStream(path,true);
            fos.write( template.getBytes("utf-8") );
        }catch (FileNotFoundException e){
            System.out.println("输入正确的文件路径");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void insert_into_comment(String user_id,String comment ,String []comment_pic,String path){
        Map<Object,Object>map=new HashMap<Object, Object>();
        map.put("user_id",user_id);
        map.put("comment",comment);
        map.put("comment_pic",comment_pic);
        String msg=JSON.toJSONString(map);
        String template=msg+"\r\n";
        try{
            FileOutputStream fos=new FileOutputStream(path,true);
            fos.write( template.getBytes("utf-8") );
        }catch (FileNotFoundException e){
            System.out.println("输入正确的文件路径");
        }catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        TableImpl tml=new TableImpl();
        String path1="C:\\Users\\徐宇钦\\Desktop\\fortest1.txt";
        String path2="C:\\Users\\徐宇钦\\Desktop\\fortest2.txt";
        House house=new House();
        house.setHouse_id_hash("asfasdas");
        house.setHouse_id("123456");
        house.setLease_type(1);
        house.setLease(3500);
        house.setHouse_type(1);
        house.setLease_inter(1);
        house.setElevator(false);
        house.setFloor(11);
        house.setHouse_owner_credit(5);
        house.setLat("111");
        house.setLon("333");
        house.setOwner("abc");
        house.setOwner_id("123");
        house.setOwner_name("ppp");
        Map<String,Object > map=new HashMap<>();
        map.put("provi","山东");
        map.put("city","济南");
        map.put("sector","历下区");
        map.put("commu_name","奥龙官邸");
        JSONObject jsonObject=new JSONObject(map);
        house.setLow_location(jsonObject);
//        tml.insert(house,"qwewqeqw",path1);
//        tml.insert_into_more_info(house,path2);
        String[] key_for_search={"provi","city"};
        String[] value_for_search={"山东","济南"};
        String[] key_to_get={"house_hash","lease","lon"};
        String[] key_to_update={"lease","elevator"};
        String[] new_value={"100","true"};
        House[] houses={house};
        tml.update(house,key_to_update,new_value,path2);
        tml.delete(houses,path1);
        tml.delete(houses,path2);
        ArrayList<String[]>v=tml.query(key_for_search,value_for_search,key_to_get,path2);
        for(int i=0;i<v.size();i++){
            for(int j=0;j<v.get(i).length;j++){
                System.out.println(v.get(i)[j]);
            }
        }



    }
}
