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
    private String get_name(String hash,String eth_id){
        String[] key_for_search={"eth_id"};
        String[] value_for_search={eth_id};
        String[] key_to_get={"user_name"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
 //       System.out.println("username in tractS is "+username);
        String user_name=v.get(0)[0];
        return user_name;
    }

    private String get_addr(String username,String hash){
        String[] key_for_search={"user_name"};
        String[] value_for_search={username};
        String[] key_to_get={"eth_id"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        System.out.println("username in tractS is "+username);
        String user_addr=v.get(0)[0];
        return user_addr;
    }

    public void userSet(String house_id_hash,String owner,String username,String ethPassword){
        String hash=download();
        String owner_addr=get_addr(owner,hash);
        String eth_id = get_addr(username, hash);
        System.out.println("user_addr is "+eth_id);
        System.out.println("owner_addr is "+owner_addr);
        System.out.println("");
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
        System.out.println("param in tractS is  "+owner+"  "+owner_addr);
        JSONArray result=bc.findOrders(owner_addr,sk,ethPassWord);
        System.out.println("_result in tractS is  "+result.toJSONString());
        return result;
    }

    public String ownerGet(String owner,String ethPassWord){
        String hash=download();
        String addr=get_addr(owner,hash);
        JSONArray _result=infoGet(owner,ethPassWord);
        for(int i=0;i<_result.size();i++){
            JSONObject tmp=_result.getJSONObject(i);
            String role=tmp.getString("role");
            if(role.equals(role_user))
                _result.remove(i);
        }
        String[] key_for_search={"user_name"};
        String[] value_for_search={owner};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        JSONObject jsonObject= bc.getMessage3(addr,sk,ethPassWord);
        JSONArray result=new JSONArray();
        String[]hash_house=download_house();
        for(int i=0;i<_result.size();i++){
            JSONObject temp=_result.getJSONObject(i);
            Map<String,Object>map=new HashMap<>();
            String responder=temp.getString("responder");
            String submiter=temp.getString("submiter");
            System.out.println("sub and res are "+submiter+" "+responder);
            JSONObject submiter_info=bc.getMessage3(addr,sk,ethPassWord);
            String username=get_name(hash,submiter);
            map.put("username",username);
            System.out.println("username return is "+username);
            map.put("name",submiter_info.getString("username"));
            System.out.println("name return is "+submiter_info.getString("username"));
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
            System.out.println("commu_name in tract is "+commu_name);
            map.put("commu_name",commu_name);
            String _status=null;
            String status=temp.getString("state");
            String user_signed=temp.getString("subSecondSign");
            String owner_signed=temp.getString("resSecondSign");
            if(status.equals("0"))
                _status="submit";
            else if(status.equals("1"))
                _status="effect";
            else if(status.equals("2"))
                _status="finish";
            else if(status.equals("3"))
                _status="refused";
            else if(status.equals("4"))
                _status="fail";
            if(user_signed.equals("1")&&!owner_signed.equals("1"))
                _status="userIden";
            else if(!user_signed.equals("1")&&owner_signed.equals("1"))
                _status="ownerIden";
            map.put("tract_status",_status);
            JSONObject result_node=new JSONObject(map);
            result.add(result_node);
            System.out.println("result_node in tractS is "+result_node.toJSONString());
        }
        String r=result.toJSONString();
        System.out.println("result in tractS is "+result.toJSONString());
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
//        String msg=bc.getMessage(addr,sk,ethPassWord);
        JSONObject jsonObject= bc.getMessage3(addr,sk,ethPassWord);
        JSONArray result=new JSONArray();
        String[]hash_house=download_house();
        for(int i=0;i<_result.size();i++){
            Map<String,Object>map=new HashMap<>();
            JSONObject temp=_result.getJSONObject(i);
            String responder=temp.getString("responder");
            String submiter=temp.getString("submiter");
            System.out.println("sub and res in user get are "+submiter+" "+responder);
            JSONObject submiter_info=bc.getMessage3(addr,sk,ethPassWord);
//            String username=get_name(hash,responder);
//            map.put("username",username);
//            map.put("name",submiter_info.getString("username"));
            map.put("house_id_hash",temp.getString("house_hash"));
            key_for_search=new String[]{"house_id_hash"};
            value_for_search=new String[]{temp.getString("house_hash")};
            key_to_get=new String[]{"commu_name","house_hash"};
            v=table.query(key_for_search,value_for_search,key_to_get,path+"housetable1.txt");
            String commu_name=null;
            String de_hash=v.get(0)[1];
            try{
            IPFS_SERVICE_IMPL.download(path+"detailedHouse\\"+hash,de_hash,"");}
            catch (Exception e){
                e.printStackTrace();
            }

//            map.put("username",username);
//            map.put("name",user);
            try{
                commu_name=v.get(0)[0];
            }catch (Exception e){
                v=table.query(key_for_search,value_for_search,key_to_get,path+"housetable2.txt");
                commu_name=v.get(0)[0];
            }
            map.put("commu_name",commu_name);
            key_to_get=new String[]{"owner_name","owner"};
            v=table.get_all(key_to_get,path+"detailedHouse\\"+hash+"\\info.txt");
            String username=v.get(0)[0];
            String user=v.get(0)[1];
            map.put("username",username);
            map.put("name",user);
            String _status=null;
            String status=temp.getString("state");
            String user_signed=temp.getString("subSecondSign");
            String owner_signed=temp.getString("resSecondSign");
            if(status.equals("0"))
                _status="submit";
            else if(status.equals("1"))
                _status="effect";
            else if(status.equals("2"))
                _status="finish";
            else if(status.equals("3"))
                _status="refused";
            else if(status.equals("4"))
                _status="fail";
            if(user_signed.equals("1")&&!owner_signed.equals("1"))
                _status="userIden";
            else if(!user_signed.equals("1")&&owner_signed.equals("1"))
                _status="ownerIden";
            map.put("tract_status",_status);
            JSONObject result_node=new JSONObject(map);
            result.add(result_node);
        }
        return result.toJSONString();
    }

    public void ownerRes(String username,boolean request_response,String ownername,String ethPassword){
        String hash=download();
        String owner_addr=get_addr(ownername,hash);
        String user_addr=get_addr(username,hash);
        String[] key_for_search={"user_name"};
        String[] value_for_search={ownername};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
//        if(request_response)
            bc.responseOrder(owner_addr,sk,ethPassword,user_addr, request_response, coins);
//        else
//            bc.rejectOrder(owner_addr,contractAddress,ethPassword,user_addr);
    }

    public void userIden(String ownername,boolean requestIdentify,String username,String ethPassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        String owner_addr=get_addr(ownername,hash);
        String[] key_for_search={"user_name"};
        String[] value_for_search={username};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        bc.confirmSecond(1,owner_addr,sk,user_addr,ethPassword, requestIdentify);
    }

    public void ownerIden(String username,boolean requestIdentify,String ownername,String ethPassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        String owner_addr=get_addr(ownername,hash);
        String[] key_for_search={"user_name"};
        String[] value_for_search={ownername};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        bc.confirmSecond(0,owner_addr,sk,user_addr,ethPassword, requestIdentify);
    }

    public void payPass(String paypass1,String username,String ethpassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        //bc.deplay(user_addr,ethpassword);
    }
}
