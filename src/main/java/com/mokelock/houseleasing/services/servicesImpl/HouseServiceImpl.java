package com.mokelock.houseleasing.services.servicesImpl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.HouseModel.SampleHouse;
import com.mokelock.houseleasing.services.HouseService;
import org.springframework.stereotype.Service;

@Service
public class HouseServiceImpl implements HouseService {
    @Override
    //获取房源详细信息
    public JSON speInfo(String house_hash) {

        House thishouse = new House();
        thishouse.setHouse_hash(house_hash);
        //房源的hash就是房源在IPFS中的索引，根据索引就可以得到该房源的其他相关信息
        //然后分层将房源的信息创建为json格式
        //最后使用第二个构造函数创建House对象outputhouse将房源信息录入，并转化为json格式
        //最后使用response来创建最终的返回信息
        return null;
    }

    @Override
    //搜索
    public JSON search(String low_location, String leaser_inter, String house_type, String lease_type) {

        SampleHouse thissh = new SampleHouse();


        return null;
    }

    @Override
    //评价房子
    public JSONObject valuation(String house_hash, int house_level, String comment, String[] comment_pic) {
        return null;
    }

    @Override
    //获取房屋列表
    public JSON allInfo() {
        return null;
    }
}
