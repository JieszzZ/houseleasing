package com.mokelock.houseleasing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.services.UserService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final static Logger logger = Logger.getLogger(UserController.class);

    @Resource
    private UserService userService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回状态 0 用户 | 1 管理员 | 2 账户不存在 | 3 密码错误
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public int login(HttpSession session, HttpServletResponse response, String username, String password) {
        boolean checkResult = userService.login(username, password);
        if (checkResult) {
            session.setAttribute("username", username);
            logger.debug(username + " login");
            response.setStatus(200, "success");
            return 0;
        }
        return 3;
    }

    /**
     * 检测用户登录状态
     *
     * @return true 已登录 | false 未登录
     */
    @RequestMapping(value = "/hasLoggedIn", method = RequestMethod.POST)
    public boolean hasLoggedIn(HttpServletResponse response, HttpSession session) {
        response.setStatus(200, "success");
        return session.getAttribute("username") != null;
    }

    /**
     * 注销
     *
     * @return true 成功注销 | false 失败
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public boolean logout(HttpServletResponse response, HttpSession session) {
        logger.debug(session.getAttribute("username") + " logout");
        session.removeAttribute("username");
        session.invalidate();
        response.setStatus(200, "success");
        return true;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(User user, HttpServletResponse response) {
        boolean result = true;
//                userService.register(user);
        if (!result) {
                response.setStatus(200, "fail");
        }
    }

    /**
     * 获取用户信息
     *
     * @return 包含用户信息的Model
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User user(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
        logger.debug("/user/user " + username);
        User user = new User();
        userService.getUser(user, username);
        response.setStatus(200, "success");
        return user;
    }

    /**
     * 获取用户余额
     *
     * @return 用户信息 {"username":"000","name":"000","balance":"000"}
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public String getBalance(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
//        User user = new User();
//        int balance = userService.getBalance(username);
//        userService.getUser(user, username);
//        JSONObject json = new JSONObject();
//        json.put("username", username);
//        json.put("name", user.getName(username));
//        json.put("balance", balance);
        response.setStatus(200, "success");
        return "{\"username\":\"liupenghao\",\"name\":\"刘鹏昊\",\"balance\":85500}";
    }

    /**
     * 充值
     *
     * @param money 充值金额
     * @return 用户信息 {"username":"000","name":"000","balance":"000"}
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public JSON account(HttpServletRequest request, HttpServletResponse response, int money) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
        boolean result = userService.postAccount(username, money);
        JSONObject json = new JSONObject();
        if (result) {
            User user = new User();
            int balance = userService.getBalance(username);
            userService.getUser(user, username);
            json.put("username", username);
            json.put("name", user.getName(username));
            json.put("balance", balance);
        } else {
            json.put("result", false);
        }
        return json;
    }

    /**
     * 交易历史
     *
     * @return "username":"liupenghao",//用户名
     * "name":"刘鹏昊",//姓名
     * “record”:[
     * {
     * "time": 2019-1-13    //交易时间
     * "gas": 500  //花费的手续费
     * “LowLocation”：{
     * “provi”:"山东省"，
     * “city”："济南市"，
     * “sector”:"历下区",
     * "commu_name":"奥龙官邸"
     * }，
     * "specific_location":"2号楼3单元1801",
     * }]
     */
    @RequestMapping(value = "/trans_record", method = RequestMethod.GET)
    public String trans_record(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
//        return userService.getRecords(username);
        return "{\"username\":\"liupenghao\",\"name\":\"刘鹏昊\",\"record\":[{\"time\":\"2019-1-13\",\"gas\":500," +
                "\"low_location\":{\"provi\":\"山东省\",\"city\":\"济南市\",\"sector\":\"历下区\",\"commu_name\":\"奥龙官邸\"},\"specific_location\":\"2号楼3单元1801\"},{\"time\":\"2019-1-13 \",\"gas\":500,\"low_location\":{\"provi\":\"山东省\",\"city\":\"济南市\",\"sector\":\"历下区\",\"commu_name\":\"奥龙官邸\"},\"specific_location\":\"2号楼3单元1801\"}]}";
    }

    /**
     * 获取我的房子的详细信息
     *
     * @param house_hash 房子的hash
     * @return 见接口文档。。。好长
     */
    @RequestMapping(value = "/myHouse", method = RequestMethod.GET)
    public String getMyHouse(HttpServletResponse response, String house_hash) {
//        return userService.getHouses(house_hash);
        return "{\"house_pic\":[\"sdfadfasfasfasdfa\",\"sdfadsfadsfasf\",\"sadfadsfasfsa\"],\"house_hash\":\"sdfwenk31345\",\"owner_id\":\"37012506546564\",\"verify\":\"true\",\"owner\":\"quyanso111\",\"owner_name\":\"曲延松\",\"role\":1,\"state\":0,\"low_location\":{\"provi\":\"山东省\",\"city\":\"济南市\",\"sector\":\"历下区\",\"commu_name\":\"奥龙官邸\"},\"specific_location\":\"2号楼3单元1801\",\"floor\":18,\"elevator\":true,\"lease\":3800,\"house_type\":1,\"house_owner_credit\":16,\"house_comment\":[{\"user_id\":\"quyans111\",\"comment\":\"这个房子挺不错适合居住\",\"comment_pic\":[\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\"]},{\"user_id\":\"quyans111\",\"comment\":\"这个房子挺不错适合居住\",\"comment_pic\":[\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\"]}]}";
    }

    /**
     * 修改我的房子的信息
     *
     * @param house_hash 房子的唯一hash
     */
    @RequestMapping(value = "/myHouse", method = RequestMethod.POST)
    public void setMyHouse(HttpServletResponse response, String house_hash) {

    }

    /**
     * 修改用户信息
     *
     * @param password 密码
     * @param phone    电话
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public void info(HttpServletResponse response, String password, String phone) {

    }

    /**
     * 联系房主
     *
     * @param house_hash 房子hash
     * @return 房主电话
     */
    @RequestMapping(value = "/contact_owner", method = RequestMethod.POST)
    public String contactOwner(String house_hash) {
        return "";
    }

    /**
     * 获取所有用户信息
     *
     * @return 用户信息列表
     */
    @RequestMapping(value = "/all_info", method = RequestMethod.GET)
    public String allInfo(HttpServletResponse response) {
        JSON json = new JSONArray();
        return "{\"users\":[{\"username\":\"liupenghao\",\"name\":\"曲延松\",\"gender\":0,\"phone\":18560125097,\"id\":370102199711111100,\"credit\":15},{\"username\":\"jack\",\"name\":\"JackSmith\",\"gender\":0,\"phone\":13246456789,\"id\":37010219975641,\"credit\":18},{\"username\":\"liupenghao\",\"name\":\"曲延松\",\"gender\":0,\"phone\":18560125097,\"id\":370102199711111100,\"credit\":15}]}";
    }

    /**
     * 修改用户信息
     *
     * @param username 用户账户
     * @param credit   信誉值
     */
    @RequestMapping(value = "/changeinfo", method = RequestMethod.POST)
    public void changeInfo(HttpServletResponse response, String username, String credit) {

    }

}
