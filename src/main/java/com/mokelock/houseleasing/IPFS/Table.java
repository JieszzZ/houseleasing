package com.mokelock.houseleasing.IPFS;

public interface Table {
    void create(String path);//创建一个空表到path
    void insert(String user_name,String eth_id,String path);//插入name-eth_id的表，地址在path
    String query(String key,String path);//根据关键字查询位于path的name-eth_id表
    //void
}
