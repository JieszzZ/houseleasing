package com.mokelock.houseleasing.IPFS.TableImpl;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.alibaba.fastjson.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public void update(House house_obj,String new_hash,String path){
        File file=new File(path);
        List<String> list=new ArrayList<>();
        try{
            String str=null;
            BufferedReader br=new BufferedReader(new FileReader(path));
            while((str=br.readLine())!=null){
                JSONObject jsonObject=JSONObject.parseObject(str);
                if(jsonObject.getString("house_id").equals(house_obj.getHouse_id())){
                    jsonObject.put("house_hash",new_hash);
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
    public void insert(House house_obj,String path){
        String house_id=house_obj.getHouse_id();
        String lease_inter=String.valueOf(house_obj.getLease_inter());
        String house_type=String.valueOf(house_obj.getHouse_type());
        String lease_type=String.valueOf(house_obj.getLease_type());
        String house_hash=String.valueOf(house_obj.getHouse_hash());
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
            map.put("house_hash",house_hash);
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
        String path="C:\\Users\\徐宇钦\\Desktop\\teh3.txt";
        tml.create(path);
//        tml.insert("A","0X123456",path);
//        tml.insert("B","0X456",path);
//        tml.insert("C","0X123",path);
//        String [] a={"user_name"};
//        String [] b={"A"};
//        String [] d={"eth_id"};
//        String [] c=tml.query(a,b,d,path);
        House sample=new House();
        sample.setHouse_id("12345");
        sample.setLease_inter(500);
        sample.setHouse_type(1);
        sample.setLease_type(2);
        sample.setHouse_hash("klgjeklredad");
        Map map=new HashMap();
        map.put("provi","sh");
        map.put("city","pd");
        map.put("sector","qq");
        map.put("commu_name","xqq");
        JSONObject jsonObject=new JSONObject(map);
        sample.setLow_location(jsonObject);
        String[] key_for_search={"house_id"};
        String[] value_for_search={sample.getHouse_id()};
        String[] key_to_get={"provi"};
        ArrayList<String[]>v=tml.query(key_for_search,value_for_search,key_to_get,path);
        if(v.size()==0){
            System.out.println(v.size());
            tml.insert(sample,path);
        }
        else{
            System.out.println("重复");
        }
        tml.update(sample,"qwesafagqaw",path);
        House[] h={sample};
//        tml.delete(h,path);
        key_to_get=new String[]{"house_id"};
        for(int i=0;i<tml.get_all(key_to_get,path).size();i++){
            for(int j=0;j<tml.get_all(key_to_get,path).get(i).length;j++){
                System.out.println(tml.get_all(key_to_get,path).get(i)[j]);
            }
        }

    }
}
