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
     * @param username 用户名
     * @param password 用户密码
     * @return 返回状态 0 用户 | 1 管理员 | 2 账户不存在 | 3 密码错误
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public int login(HttpSession session, String username, String password) {
        boolean checkResult = userService.login(username, password);
        if (checkResult) {
            session.setAttribute("username", username);
            logger.debug(username + " login");
            return 0;
        }
        return 3;
    }

    /**
     * 检测用户登录状态
     * @return true 已登录 | false 未登录
     */
    @RequestMapping(value = "/hasLoggedIn", method = RequestMethod.POST)
    public boolean hasLoggedIn(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    /**
     * 注销
     * @return true 成功注销 | false 失败
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public boolean logout (HttpSession session) {
        logger.debug(session.getAttribute("username") + " logout");
        session.removeAttribute("username");
        session.invalidate();
        return true;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(User user, HttpServletResponse response) {
        boolean result = userService.register(user.getUsername(), user.getPassword(), user.getPay_password(),
                user.getName(), user.getPhone(), user.getProfile_a(), user.getProfile_b(), user.getId(), user.getGender());
        if(!result) {
            try {
                response.getWriter().append("fail");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取用户信息
     * @return 包含用户信息的Model
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User user(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
        logger.debug("/user/user " + username);
        User user = new User();
        userService.getUser(user, username);
        return user;
    }

    /**
     * 获取用户余额
     * @return 用户信息 {"username":"000","name":"000","balance":"000"}
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public JSON getBalance(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
        User user = new User();
        int balance = userService.getBalance(username);
        userService.getUser(user, username);
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("name", user.getName());
        json.put("balance", balance);
        return json;
    }

    /**
     * 充值
     * @param money 充值金额
     * @return 用户信息 {"username":"000","name":"000","balance":"000"}
     */
    @RequestMapping(value = "/account", method = RequestMethod.POST)
    public JSON account(HttpServletRequest request, int money) {
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
            json.put("name", user.getName());
            json.put("balance", balance);
        } else {
            json.put("result", false);
        }
        return json;
    }

    /**
     * 交易历史
     * @return "username":"liupenghao",//用户名
     *   "name":"刘鹏昊",//姓名
     *   “record”:[
     *       {
     *       "time": 2019-1-13    //交易时间
     *       "gas": 500  //花费的手续费
     *       “LowLocation”：{
     *               “provi”:"山东省"，
     *               “city”："济南市"，
     *               “sector”:"历下区",
     *               "commu_name":"奥龙官邸"
     *       }，
     *      "specific_location":"2号楼3单元1801",
     *      }]
     */
    @RequestMapping(value = "/trans_record", method = RequestMethod.GET)
    public ArrayList trans_record(HttpServletRequest request) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
        return userService.getRecords(username);
    }

    /**
     * 获取我的房子的详细信息
     * @param house_hash 房子的hash
     * @return 见接口文档。。。好长
     */
    @RequestMapping(value = "/myHouse", method = RequestMethod.GET)
    public House getMyHouse(String house_hash){
        return userService.getHouses(house_hash);
    }

    /**
     * 修改我的房子的信息
     * @param house_hash 房子的唯一hash
     */
    @RequestMapping(value = "/myHouse", method = RequestMethod.POST)
    public boolean setMyHouse(String house_hash, int state , boolean elevator, int lease, String phone){
        return userService.postHouse(house_hash, state, elevator, lease, phone);
    }

    /**
     * 修改用户信息
     * @param password 密码
     * @param phone 电话
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public boolean info (HttpServletRequest request, String password, String phone) {
        String username = request.getParameter("username");
        if (username == null) {
            HttpSession session = request.getSession();
            username = (String) session.getAttribute("username");
        }
        return userService.postPhone(username, password, phone);
    }

    /**
     * 联系房主
     * @param house_hash 房子hash
     * @return 房主电话
     */
    @RequestMapping(value = "/contact_owner", method = RequestMethod.POST)
    public String contactOwner(String house_hash) {
        return userService.getUser();
    }

    /**
     * 获取所有用户信息
     * @return 用户信息列表
     */
    @RequestMapping(value = "/all_info", method = RequestMethod.GET)
    public JSON allInfo() {
        JSON json = new JSONArray();
        return userService.;
    }

    /**
     * 修改用户信息
     * @param username 用户账户
     * @param credit 信誉值
     */
    @RequestMapping(value = "/changeinfo", method = RequestMethod.POST)
    public void changeInfo(String username, String credit){

    }

}
