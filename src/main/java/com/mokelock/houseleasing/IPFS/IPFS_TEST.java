package com.mokelock.houseleasing.IPFS;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.IPFS.IPFS_SERVICE ;
import com.mokelock.houseleasing.IPFS.IpfsImpl.IPFS_SERVICE_IMPL;
import com.mokelock.houseleasing.IPFS.TableImpl.TableImpl;
import com.mokelock.houseleasing.model.HouseModel.House;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IPFS_TEST {


    public static void main(String[] args) throws IOException {
//        TableImpl table = new TableImpl();
//        String path1="D:\\untitled\\houseleasing\\src\\main\\file\\用户\\01.txt";
//        String path2="D:\\untitled\\houseleasing\\src\\main\\file\\用户\\02.txt";
//        House house=new House();
//        house.setHouse_id_hash("asfasdas");
//        house.setHouse_id("123456");
//        house.setLease_type(1);
//        house.setLease(3500);
//        house.setHouse_type(1);
//        house.setLease_inter(1);
//        house.setElevator(false);
//        house.setFloor(11);
//        house.setHouse_owner_credit(5);
//        house.setLat("111");
//        house.setLon("333");
//        house.setOwner("abc");
//        house.setOwner_id("123");
//        house.setOwner_name("ppp");
//        Map<String,Object > map=new HashMap<>();
//        map.put("provi","江西");
//        map.put("city","九江");
//        map.put("sector","庐山");
//        map.put("commu_name","威家");
//        JSONObject jsonObject=new JSONObject(map);
//        house.setLow_location(jsonObject);
//        table.insert(house,"123132",path2);

//
        IPFS_SERVICE_IMPL demo = new IPFS_SERVICE_IMPL();
        String hash = demo.upload("C:\\Users\\10922\\Desktop\\code");
        demo.download("D:\\ipfs_downloads\\2",hash,"HouseState0.txt");
        System.out.println(hash);




    }



}
