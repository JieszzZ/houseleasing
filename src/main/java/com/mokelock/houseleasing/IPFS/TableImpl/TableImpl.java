package com.mokelock.houseleasing.IPFS.TableImpl;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.alibaba.fastjson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.io.*;

public class TableImpl implements  Table{
    public void create(String path){
        File table_file=new File(path);
    }
    public void insert(String user_name,String eth_id,String path){
        File file=new File(path);
        Map map=new HashMap();
        map.put("user_name",user_name);
        map.put("eth_id",eth_id);
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
                for (int i = 0; i < key_to_get.length; i++) {
                    element[i] = "init";
                }
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
        }
        String[] error={"not found"};
        return value;
    }
    public void insert(House house_obj,String eth_id,String path){
        String house_id=house_obj.getHouse_id();
        String lease_inter=String.valueOf(house_obj.getLease_inter());
        String house_type=String.valueOf(house_obj.getHouse_type());
        String lease_type=String.valueOf(house_obj.getLease_type());
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
            map.put("eth_id",eth_id);
            map.put("lease_inter",lease_inter);
            map.put("house_type",house_type);
            map.put("lease_type",lease_type);
            map.put("provi",provi);
            map.put("city",city);
            map.put("sector",sector);
            map.put("commu_name",commu_name);
            String msg=JSON.toJSONString(map);
            String template=msg+"\r\n";
            fos.write( template.getBytes("utf-8") );
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String args[]){
        TableImpl tml=new TableImpl();
        String path="C:\\Users\\徐宇钦\\Desktop\\teh.txt";
        tml.create(path);
////        tml.insert("A","0X123456",path);
////        tml.insert("B","0X456",path);
////        tml.insert("C","0X123",path);
//        String [] a={"user_name"};
//        String [] b={"A"};
//        String [] d={"eth_id"};
//        String [] c=tml.query(a,b,d,path);
        House sample=new House();
        sample.setHouse_id("65745234");
        sample.setLease_inter(500);
        sample.setHouse_type(1);
        sample.setLease_type(2);
        Map map=new HashMap();
        map.put("provi","sdd");
        map.put("city","jnn");
        map.put("sector","qq");
        map.put("commu_name","xqq");
        JSONObject jsonObject=new JSONObject(map);
        sample.setLow_location(jsonObject);
        tml.insert(sample,"0x56789",path);

        String[] key_for_search={"house_type","lease_type"};
        String[] value_for_search={"1","2"};
        String[] key_to_get={"house_id","eth_id","lease_inter"};
        ArrayList<String[]>v=tml.query(key_for_search,value_for_search,key_to_get,path);
        for(int i=0;i<v.size();i++){
            for(int j=0;j<v.get(i).length;j++){
                System.out.print(v.get(i)[j]+" ");
            }
            System.out.println();
        }
//        String[] key_for_search={"house_id"};
//        String[] value_for_search={"6574523"};
//        String[] key_to_get={"eth_id"};
//        ArrayList<String[]>v=tml.query(key_for_search,value_for_search,key_to_get,path);
//        System.out.println(v.size());
//        for(int i=0;i<v.size();i++){
//            for(int j=0;j<v.get(i).length;j++){
//                System.out.print(v.get(i)[j]+" ");
//            }
//            System.out.println();
//        }
    }
}
