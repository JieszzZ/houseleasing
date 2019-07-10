package com.mokelock.houseleasing.services;

import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.blockchain.BlockChain;

public interface TractService {
    public static final String contractAddress = "";

    void userSet(String house_id_hash, String owner, String userAddress, String ethPassword);

    String ownerGet(String owner, String ethPassWord);

    void ownerRes(String username, boolean request_response, String owner_address, String ethPassword);

    void userIden(String ownername, boolean requestIdentify, String username, String ethpassword);

    void ownerIden(String username, boolean requestIdentify, String ownername, String ethpassword);

    void payPass(String paypass, String username, String ethpassword);
}
