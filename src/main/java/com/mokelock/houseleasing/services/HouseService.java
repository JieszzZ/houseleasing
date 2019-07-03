package com.mokelock.houseleasing.services;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/*
 * 李晓婷
 * 20190624
 * 房屋相关处理接口
 */
public interface HouseService {

    //获取房源详细信息（用户获取和管理员获取的返回值中role字段不同，而游客不能获取详细信息）
    JSON speInfo(String house_hash);

    //搜索
    JSON search(String low_location, String leaser_inter, String house_type, String lease_type, boolean elevator);

    //评价房子
    JSONObject valuation(String user_id,String house_id_hash,String comment, String[] comment_pic);

    //获取房屋列表
    JSON allInfo();
    JSON myHouse(String house_id_hash, int state, boolean elevator, int lease);

}

