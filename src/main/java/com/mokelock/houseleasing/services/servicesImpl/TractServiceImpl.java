package com.mokelock.houseleasing.services.servicesImpl;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.Table;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.services.TractService;
import org.springframework.stereotype.Service;

import java.io.File;
import java.math.BigInteger;
import java.util.ArrayList;

@Service
public class TractServiceImpl implements TractService {
    BlockChain bc=new BlockChain();
    Table table=new TableImpl();
    String path="";
    BigInteger coins=null;
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

    private String get_addr(String username,String hash){
        String[] key_for_search={"user_name"};
        String[] value_for_search={username};
        String[] key_to_get={"eth_id"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+"\\"+hash+"\\"+"user.txt");
        String user_addr=v.get(0)[0];
        return user_addr;
    }

    public void userSet(String house_id_hash,String owner,String userAddress,String ethPassword){
        String hash=download();
        String owner_addr=get_addr(owner,hash);
        bc.submitOrder(userAddress,contractAddress,ethPassword,owner_addr,coins);
    }

    public String ownerGet(String owner,String ethPassWord){
        String hash=download();
        String owner_addr=get_addr(owner,hash);
        String[] key_for_search={"user_name"};
        String[] value_for_search={owner};
        String[] key_to_get={"SK"};
        ArrayList<String[]>v=table.query(key_for_search,value_for_search,key_to_get,path+"\\"+hash+"\\"+"user.txt");
        String sk=v.get(0)[0];
        String result=bc.findOrders(owner_addr,sk,ethPassWord);
        return result;
    }

    public void ownerRes(String username,boolean request_response,String owner,String ethPassword){
        String hash=download();
        String owner_addr=get_addr(owner,hash);
        String user_addr=get_addr(username,hash);
        if(request_response)
            bc.responseOrder(owner_addr,contractAddress,ethPassword,user_addr,coins);
        else
            bc.rejectOrder(owner_addr,contractAddress,ethPassword,user_addr);
    }

    public void userIden(String ownername,boolean requestIdentify,String username,String ethPassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        String owner_addr=get_addr(ownername,hash);
        bc.confirmSecond(1,owner_addr,contractAddress,user_addr,ethPassword);
    }

    public void ownerIden(String username,boolean requestIdentify,String ownername,String ethPassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        String owner_addr=get_addr(ownername,hash);
        bc.confirmSecond(0,owner_addr,contractAddress,user_addr,ethPassword);
    }

    public void payPass(String paypass1,String username,String ethpassword){
        String hash=download();
        String user_addr=get_addr(username,hash);
        bc.deplay(user_addr,ethpassword);
    }
}
