package com.mokelock.houseleasing.services;

import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.blockchain.BlockChain;

public interface tract_part1 {
    public static final String contractAddress="";
    void userSet(String house_id_hash,String owner,String userAddress,String ethPassword);
    String ownerGet(String owner,String ethPassWord);
    void ownerRes(String username,boolean request_response,String owner_address,String ethPassword);
}
