package com.mokelock.houseleasing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.services.TractService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/tract")
public class TractController {

    private final static Logger logger = LoggerFactory.getLogger(TractController.class);

    @Resource
    TractService tractService;

    @RequestMapping(value = "/payPass")
    public void setPayPassword(HttpServletRequest request, HttpServletResponse response, String payPass) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
//        if (username == null) {
//            return "notLogin";
//        }
        session.setAttribute("payPass", payPass);
        logger.info((String) session.getAttribute("payPass"));
//        return "true";
    }
    /**
     * 用户发起租住请求
     *
     * @param house_id_hash 房子hash
     * @param owner      房主账号
     * @return requestID？
     */
    @RequestMapping(value = "/userSet", method = RequestMethod.POST)
    public String set(HttpServletRequest request, HttpServletResponse response, String house_id_hash, String owner) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "notLogin";
        }
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
            return null;
        }
        logger.info("param in userSet controller is " + house_id_hash + owner + username + ethPassword);
        tractService.userSet(house_id_hash, owner, username, ethPassword);
        logger.info("userSet is end");
        return "true";
//        return "{\"requestID\":\"xxoso\",}";
    }

    /**
     * 房主获取所有相关请求
     *
     * @return 所有请求列表
     */
    @RequestMapping(value = "/ownerGet", method = RequestMethod.GET)
    public String get(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "notLogin";
        }
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
            return null;
        }
        return tractService.ownerGet(username, ethPassword);
//        JSON json = new JSONArray();
//        return "{\"tract\":[{\"requestID\":\"xxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\",\"commu_name\":\"茗筑美嘉\",\"state\":0},{\"requestID\":\"xsxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\",\"commu_name\":\"奥龙官邸\",\"state\":0}]}";
    }

    @RequestMapping(value = "/userGet", method = RequestMethod.GET)
    public String userGet(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "notLogin";
        }
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
            return null;
        }
        return tractService.userGet(username, ethPassword);
//        return "{\"tract\":[{\"requestID\":\"xxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\"," +
//                "\"commu_name\":\"茗筑美嘉\",\"state\":0},{\"requestID\":\"xsxoo\",\"username\":\"xxdd\",\"name\":\"曲延松\",\"house_hash\":\"adfafd\",\"commu_name\":\"奥龙官邸\",\"state\":2}]}";
    }

    /**
     * 请假请求反馈(u)
     *
     * @param request_response 请求反馈 true 同意签约 false 不同意签约
     */
    @RequestMapping(value = "/ownerRes", method = RequestMethod.POST)
    public void acquire(HttpServletRequest request, HttpServletResponse response, boolean request_response, String username) {
        HttpSession session = request.getSession();
        String ownername = (String) session.getAttribute("username");
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
        }
        tractService.ownerRes(username, request_response, ownername, ethPassword);
    }

//    /**
////     * 请假反馈获取(u)
////     *
////     * @return "checkID":"aadfif",
////     * "groupID":"xxoo",
////     * "request_status":0,   # 0：未处理  1：同意   2：拒绝
////     */
////    @RequestMapping(value = "/leave/{requestID}/feedback", method = RequestMethod.GET)
////    public String feedback(@PathVariable("requestID") String requestID) {
////        HttpSession session = request.getSession();
////        String username = (String) session.getAttribute("username");
////        if (username == null) {
////            return "notLogin";
////        }
////        String ethPassword = (String) session.getAttribute("payPass");
////        logger.info("payPass in setUpHouse is " + ethPassword);
////        if (ethPassword == null) {
////            response.setStatus(201);
////            return null;
////        }
//////        JSON json = new JSONObject();
//////        return json;
////    }

    @RequestMapping(value = "/userIden")
    public void userIden(HttpServletRequest request, HttpServletResponse response, String ownername,
                         boolean requestIdentify){
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
        }
        tractService.userIden(ownername, requestIdentify, username, ethPassword);
    }

    @RequestMapping(value = "/ownerIden")
    public void ownerIden(HttpServletRequest request, HttpServletResponse response, String username,
                      boolean requestIdentify){
        HttpSession session = request.getSession();
        String ownername = (String) session.getAttribute("username");
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
        }
        tractService.ownerIden(username, requestIdentify, ownername, ethPassword);
    }

    @RequestMapping(value = "/managerGet")
    public void managerGet(){

    }

    @RequestMapping(value = "/managerRes")
    public void managerRes(String username, String ownername, String request_response){

    }

}
