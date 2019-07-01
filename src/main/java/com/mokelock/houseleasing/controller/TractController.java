package com.mokelock.houseleasing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/tract")
public class TractController {

    /**
     * 用户发起租住请求
     * @param house_hash 房子hash
     * @param owner 房主账号
     * @return requestID？
     */
    @RequestMapping(value = "/userSet", method = RequestMethod.POST)
    public String set(String house_hash, String owner) {
        return "{\"requestID\":\"xxoso\",}";
    }

    /**
     * 房主获取所有相关请求
     * @return 所有请求列表
     */
    @RequestMapping(value = "/ownerGet", method = RequestMethod.GET)
    public String get() {
        JSON json = new JSONArray();
        return "{\"tract\":[{\"requestID\":\"xxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\",\"commu_name\":\"茗筑美嘉\",\"state\":0},{\"requestID\":\"xsxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\",\"commu_name\":\"奥龙官邸\",\"state\":0}]}";
    }

    @RequestMapping(value = "/userGet", method = RequestMethod.GET)
    public String userGet() {
        return "{\"tract\":[{\"requestID\":\"xxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\"," +
                "\"commu_name\":\"茗筑美嘉\",\"state\":0},{\"requestID\":\"xsxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\",\"commu_name\":\"奥龙官邸\",\"state\":2}]}";
    }

    /**
     * 请假请求反馈(u)
     * @param requestID 请求编号
     * @param request_response 请求反馈 true 同意签约 false 不同意签约
     * @param pay_password 支付密码
     */
    @RequestMapping(value = "/ownerRes", method = RequestMethod.POST)
    public void acquire(String requestID, String request_response, String pay_password) {

    }

    /**
     * 请假反馈获取(u)
     * @return "checkID":"aadfif",
     *       "groupID":"xxoo",
     *       "request_status":0,   # 0：未处理  1：同意   2：拒绝
     */
    @RequestMapping(value = "/leave/{requestID}/feedback", method = RequestMethod.GET)
    public JSON feedback(@PathVariable("requestID") String requestID){
        JSON json = new JSONObject();
        return json;
    }

}
