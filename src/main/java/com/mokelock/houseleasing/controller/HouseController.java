package com.mokelock.houseleasing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/house")
public class HouseController {

    /**
     *获取房源详细信息
     * @param house_hash 房子hash
     * @return 信息列表
     */
    @RequestMapping(value = "/speinfo", method = RequestMethod.GET)
    public JSON speInfo(String house_hash) {
        JSON json = new JSONObject();
        return json;
    }

    /**
     *
     * @param low_location 简略地址
     * @param leaser_inter 价格范围：0 全部 1 500元以下 2 500-1000元 3 1000-1500元 4 1500-2000元 5 2000元以上
     * @param house_type 房子类型：0 全部 1 一室 2 二室 3 其他
     * @param lease_type 租住类型：0 全部 1 整租 2 合租
     * @return 房子列表
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public JSON search(String low_location, String leaser_inter, String house_type, String lease_type) {
        JSON json = new JSONObject();
        return json;
    }

    /**
     *
     * @param house_hash 房子hash
     * @param house_level 1-5共五个级别
     * @param comment_word 文字评价
     * @param comment_pic 图片评价
     */
    @RequestMapping(value = "/valuation", method = RequestMethod.POST)
    public void valuation(String house_hash, String house_level, String comment_word, String comment_pic) {

    }

    /**
     * 获取房屋列表
     * @return 房屋列表
     */
    @RequestMapping(value = "/allinfo", method = RequestMethod.GET)
    public JSON allInfo() {
        JSON json = new JSONArray();
        return json;
    }

}
