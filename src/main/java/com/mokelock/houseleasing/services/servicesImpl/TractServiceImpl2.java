package com.mokelock.houseleasing.services.servicesImpl;

import com.mokelock.houseleasing.blockchain.BlockChain;
import com.mokelock.houseleasing.blockchain.HouseContract;
import com.mokelock.houseleasing.dao.UserDao;
import com.mokelock.houseleasing.services.TractService2;

public class TractServiceImpl2 implements TractService2 {
    private final static String contractAddress = "";

    @Override
    public void postUserIden(int i, String ethFile, String userAddress, String ethPassword,String ownerAddress) {

        BlockChain bc = new BlockChain();
        bc.confirmSecond(1, ownerAddress, ethFile, userAddress, ethPassword);
    }

    @Override
    public void postOwnerIden(int i, String ethFile, String userAddress, String ethPassword,String ownerAddress) {
        BlockChain bc = new BlockChain();
        bc.confirmSecond(0, ownerAddress, ethFile, userAddress, ethPassword);
    }

    @Override
    public void postPayPass(String _payPass) {


    }
}

