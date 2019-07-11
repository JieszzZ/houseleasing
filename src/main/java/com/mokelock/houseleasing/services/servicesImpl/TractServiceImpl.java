package com.mokelock.houseleasing.services.servicesImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.services.TractService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class TractServiceImpl implements TractService {
    BlockChain bc=new BlockChain();
    Table table=new TableImpl();
    String path=System.getProperty("user.dir") + "\\src\\main\\file\\";
    BigInteger coins= new BigInteger("10000");
    String role_user="0";
    String role_owner="1";
    private String download(){
        String hash=bc.getHash(0);
        try{
            File f=new File(path+"\\"+hash);
            if(!f.exists())
                f.mkdirs();
            IPFS_SERVICE_IMPL.download(path+"\\"+hash+"\\"+"user.txt",hash,"null");
        }catch (Exception e){
            e.printStackTrace();
        }
        return hash;
    }
    private String[] download_house(){
        String hash1=bc.getHash(1);
        String hash2=bc.getHash(2);
        try{
            File f=new File(path);
            if(!f.exists())
                f.mkdirs();
            IPFS_SERVICE_IMPL.download(path+"housetable1.txt",hash1,"null");
            IPFS_SERVICE_IMPL.download(path+"housetable2.txt",hash2,"null");
        }catch (Exception e){
            e.printStackTrace();
        }
        String[] hash={hash1,hash2};
        return hash;
    }

    private String get_addr(String username,String hash){
        String[] key_for_search={"user_name"};
        String[] value_for_search={username};
        String[] key_to_get={"eth_id"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String user_addr=v.get(0)[0];
        return user_addr;
    }

    public void userSet(String house_id_hash,String owner,String username,String ethPassword){
        String hash=download();
        String owner_addr=get_addr(owner,hash);
        String eth_id = get_addr(username, hash);
        String[] key_for_search={"user_name"};
        String[] value_for_search={username};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        System.out.println("sk is " + sk);
        bc.submitOrder(eth_id,sk,ethPassword,owner_addr, house_id_hash, coins);
    }

    private JSONArray infoGet(String owner, String ethPassWord){
        String hash=download();
        String owner_addr=get_addr(owner,hash);
        String[] key_for_search={"user_name"};
        String[] value_for_search={owner};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        JSONArray result=bc.findOrders(owner_addr,sk,ethPassWord);
        return result;
    }

    public String ownerGet(String owner,String ethPassWord){
        String hash=download();
        String addr=get_addr(owner,hash);
        JSONArray _result=infoGet(owner,ethPassWord);
        for(int i=0;i<_result.size();i++){
            JSONObject tmp=_result.getJSONObject(i);
            String role=tmp.getString("role");
            if(role.equals(role_owner))
                _result.remove(i);
        }
        String[] key_for_search={"user_name"};
        String[] value_for_search={owner};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        String msg=bc.getMessage(addr,sk,ethPassWord);
        JSONObject jsonObject= JSON.parseObject(msg);
        JSONArray result=null;
        String[]hash_house=download_house();
        for(int i=0;i<_result.size();i++){
            Map<String,Object>map=new HashMap<>();
            map.put("username",owner);
            map.put("name",jsonObject.getString("username"));
            JSONObject temp=_result.getJSONObject(i);
            map.put("house_id_hash",temp.getString("house_hash"));
            key_for_search=new String[]{"house_id_hash"};
            value_for_search=new String[]{temp.getString("house_hash")};
            key_to_get=new String[]{"commu_name"};
            v=table.query(key_for_search,value_for_search,key_to_get,path+"housetable1.txt");
            String commu_name=null;
            try{
                commu_name=v.get(0)[0];
            }catch (Exception e){
                v=table.query(key_for_search,value_for_search,key_to_get,path+"housetable2.txt");
                commu_name=v.get(0)[0];
            }
            map.put("commu_name",commu_name);
            map.put("tract_status",temp.getString("state"));
            JSONObject result_node=new JSONObject(map);
            result.add(result_node);
        }
        String r=result.toJSONString();
        return r;
    }

    public String userGet(String owner,String ethPassWord){
        String hash=download();
        String addr=get_addr(owner,hash);
        JSONArray _result=infoGet(owner,ethPassWord);
        for(int i=0;i<_result.size();i++){
            JSONObject tmp=_result.getJSONObject(i);
            String role=tmp.getString("role");
            if(role.equals(role_owner))
                _result.remove(i);
        }
        String[] key_for_search={"user_name"};
        String[] value_for_search={owner};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        String msg=bc.getMessage(addr,sk,ethPassWord);
        JSONObject jsonObject= JSON.parseObject(msg);
        JSONArray result=null;
        String[]hash_house=download_house();
        for(int i=0;i<_result.size();i++){
            Map<String,Object>map=new HashMap<>();
            map.put("username",owner);
            map.put("name",jsonObject.getString("username"));
            JSONObject temp=_result.getJSONObject(i);
            map.put("house_id_hash",temp.getString("house_hash"));
            key_for_search=new String[]{"house_id_hash"};
            value_for_search=new String[]{temp.getString("house_hash")};
            key_to_get=new String[]{"commu_name"};
            v=table.query(key_for_search,value_for_search,key_to_get,path+"housetable1.txt");
            String commu_name=null;
            try{
                commu_name=v.get(0)[0];
            }catch (Exception e){
                v=table.query(key_for_search,value_for_search,key_to_get,path+"housetable2.txt");
                commu_name=v.get(0)[0];
            }
            map.put("commu_name",commu_name);
            map.put("tract_status",temp.getString("state"));
            JSONObject result_node=new JSONObject(map);
            result.add(result_node);
        }
        return result.toJSONString();
    }

    public void ownerRes(String username,boolean request_response,String ownername,String ethPassword){
        String hash=download();
        String owner_addr=get_addr(ownername,hash);
        String user_addr=get_addr(username,hash);
//        if(request_response)
            bc.responseOrder(owner_addr,contractAddress,ethPassword,user_addr, request_response, coins);
//        else
//            bc.rejectOrder(owner_addr,contractAddress,ethPassword,user_addr);
    }

    public void userIden(String ownername,boolean requestIdentify,String username,String ethPassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        String owner_addr=get_addr(ownername,hash);
        bc.confirmSecond(1,owner_addr,contractAddress,user_addr,ethPassword, requestIdentify);
    }

    public void ownerIden(String username,boolean requestIdentify,String ownername,String ethPassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        String owner_addr=get_addr(ownername,hash);
        bc.confirmSecond(0,owner_addr,contractAddress,user_addr,ethPassword, requestIdentify);
    }

    public void payPass(String paypass1,String username,String ethpassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        //bc.deplay(user_addr,ethpassword);
    }
}
