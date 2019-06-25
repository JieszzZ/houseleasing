package com.mokelock.houseleasing.services;

import com.alibaba.fastjson.JSON;
import com.mokelock.houseleasing.model.HouseComment;
import com.mokelock.houseleasing.model.low_location;

/*
 * 李晓婷
 * 20190624
 * 房屋相关处理接口
 */
public interface HouseService {

    //判断是否登录
    boolean hasLoggedIn();

    //判断当前操作者的身份
    int currentRole();

    //获取房源详细信息（用户获取和管理员获取的返回值中role字段不同，而游客不能获取详细信息）
    JSON speinfo(String house_hash);

    //搜索
    JSON search(JSON low_location, int lease_inter, int house_type, int lease_type);

    //评价房子
    JSON valuation(String house_hash, int house_level, String comment, String[] comment_pic);

    //获取房屋列表
    JSON allinfo();

}

