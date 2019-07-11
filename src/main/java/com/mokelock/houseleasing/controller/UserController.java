package com.mokelock.houseleasing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.UserModel.User;
import com.mokelock.houseleasing.model.UserModel.UserTemp;
import com.mokelock.houseleasing.services.FaceService;
import com.mokelock.houseleasing.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private FaceService faceService;

    /**
     * 用户登录
     *
     * @param username 用户名
     * @param password 用户密码
     * @return 返回状态 0 用户 | 1 管理员 | 2 账户不存在 | 3 密码错误
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public int login(HttpSession session, String username, String password) {
        boolean checkResult = userService.login(username, password);
        if (checkResult) {
            session.setAttribute("username", username);
            logger.info(username + " login");
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
    public boolean hasLoggedIn(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    /**
     * 注销
     *
     * @return true 成功注销 | false 失败
     */
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public boolean logout(HttpSession session) {
        logger.debug(session.getAttribute("username") + " logout");
        session.removeAttribute("username");
        session.invalidate();
        return true;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void register(UserTemp user, HttpServletResponse response) throws IOException {
        logger.info("userTemp = " + user.toString());
        logger.info(user.getProfile_a().getOriginalFilename());
        // 获取文件名
        String fileName = user.getProfile_a().getOriginalFilename();
        String fileName1 = user.getProfile_a().getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        String prefix1 = fileName1.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        File excelFile = File.createTempFile(user.getId() + "a", prefix);
        File excelFile1 = File.createTempFile(user.getId() + "b", prefix1);
        // MultipartFile to File
        user.getProfile_a().transferTo(excelFile);
        user.getProfile_b().transferTo(excelFile1);
        logger.info(excelFile.getPath());
        boolean result = userService.register(user.getUsername(), user.getPassword(), user.getPay_password(),
                user.getName(), user.getPhone(), excelFile, excelFile1, user.getId(), new Byte(user.getGender()));
        logger.info("result is " + result);
        if (!result) {
            logger.info("register failed");
            response.setStatus(202);
        } else {
            response.setStatus(200);
            logger.info(user.toString());
        }
        if (excelFile.exists()) {
            excelFile.delete();
        }
        if (excelFile1.exists()) {
            excelFile1.delete();
        }
    }
//    @RequestMapping(value = "/register", method = RequestMethod.POST, consumes = "multipart/form-data")
//    public void register(MultipartHttpServletRequest request, HttpServletResponse response) throws IOException {
//        String username = request.getParameter("username");
//        String name = request.getParameter("name");
//        String id = request.getParameter("id");
//        String password = request.getParameter("password");
//        String pay_password = request.getParameter("pay_password");
//        String gender = request.getParameter("gender");
//        String phone = request.getParameter("phone");
//        MultipartFile profile_a = request.getFile("profile_a");
//        MultipartFile profile_b = request.getFile("profile_b");
//        // 获取文件名
//        String fileName = profile_a.getOriginalFilename();
//        String fileName1 = profile_b.getOriginalFilename();
//        logger.info(new UserTemp(username, name, id, pay_password, profile_a, profile_b, password, phone, gender).toString());
//        // 获取文件后缀
//        String prefix = fileName.substring(fileName.lastIndexOf("."));
//        String prefix1 = fileName1.substring(fileName.lastIndexOf("."));
//        // 用uuid作为文件名，防止生成的临时文件重复
//        final File excelFile = File.createTempFile(id + "a", prefix);
//        final File excelFile1 = File.createTempFile(id + "b", prefix1);
//        // MultipartFile to File
//        profile_a.transferTo(excelFile);
//        profile_b.transferTo(excelFile1);
//        boolean result = userService.register(username, password, pay_password, name, phone, excelFile, excelFile1,
//                id, new Byte(gender));
//        if (!result) {
//            logger.debug("register failed");
//            response.setStatus(202);
//        }
//        if (excelFile.exists()) {
//            excelFile.delete();
//        }
//        if (excelFile1.exists()) {
//            excelFile1.delete();
//        }
//    }


    /**
     * 获取用户信息
     *
     * @return 包含用户信息的Model
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public User user(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        if (username == null) {
            username = (String) session.getAttribute("username");
        }
        if (session.getAttribute("payPass") == null) {
            response.setStatus(201);
            return null;
        }
        logger.info("/user/user " + username);
        return userService.getUser(username, (String) session.getAttribute("payPass"));
    }

    /**
     * 获取用户余额
     *
     * @return 用户信息 {"username":"000","name":"000","balance":"000"}
     */
    @RequestMapping(value = "/balance", method = RequestMethod.GET)
    public String getBalance(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        if (username == null) {
            username = (String) session.getAttribute("username");
        }
        if (session.getAttribute("payPass") == null) {
            response.setStatus(201);
            return null;
        }
        User user = new User();
        int balance = userService.getBalance(username);
        user = userService.getUser(username, (String) session.getAttribute("payPass"));
        JSONObject json = new JSONObject();
        json.put("username", username);
        json.put("name", user.getName());
        json.put("balance", balance);
        return json.toJSONString();
//        return "{\"username\":\"liupenghao\",\"name\":\"刘鹏昊\",\"balance\":85500}";
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
        HttpSession session = request.getSession();
        if (username == null) {
            username = (String) session.getAttribute("username");
        }
        if (session.getAttribute("payPass") == null) {
            response.setStatus(201);
            return null;
        }
        boolean result = userService.postAccount(username, money);
        JSONObject json = new JSONObject();
        if (result) {
            User user = new User();
            int balance = userService.getBalance(username);
            user = userService.getUser(username, (String) session.getAttribute("payPass"));
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
    public ArrayList trans_record(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        HttpSession session = request.getSession();
        if (username == null) {
            username = (String) session.getAttribute("username");
        }
        if (session.getAttribute("payPass") == null) {
            response.setStatus(201);
            return null;
        }
        return userService.getRecords(username, (String) session.getAttribute("payPass"));
    }

    /**
     * 获取我的房子的详细信息
     *
     * @param house_hash 房子的hash
     * @return 见接口文档。。。好长
     */
    @RequestMapping(value = "/myhouseInfo", method = RequestMethod.POST)
    public String getMyHouse(String house_hash) {
        House house = userService.getHouses(house_hash);
        return JSON.toJSONString(house);
//        return "{\"house_pic\":[\"sdfadfasfasfasdfa\",\"sdfadsfadsfasf\",\"sadfadsfasfsa\"]," +
//                "\"house_id_hash\":\"sdfaafadsfasd\",\"owner_id\":\"37012506546564\",\"verify\":\"true\"," +
//                "\"owner\":\"quyanso111\",\"owner_name\":\"曲延松\",\"role\":1,\"state\":0,\"low_location\":{\"provi\":\"山东省\",\"city\":\"济南市\",\"sector\":\"历下区\",\"commu_name\":\"奥龙官邸\"},\"specific_location\":\"2号楼3单元1801\",\"floor\":18,\"elevator\":true,\"lease\":3800,\"house_type\":1,\"house_owner_credit\":16,\"house_comment\":[{\"user_id\":\"quyans111\",\"comment\":\"这个房子挺不错适合居住\",\"comment_pic\":[\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\"]},{\"user_id\":\"quyans111\",\"comment\":\"这个房子挺不错适合居住\",\"comment_pic\":[\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\"]}]}";
    }

    /**
     * 修改我的房子的信息
     *
     * @param house_hash 房子的唯一hash
     */
    @RequestMapping(value = "/myhouse", method = RequestMethod.POST)
    public boolean setMyHouse(String house_hash, int state, boolean elevator, int lease, String phone) {
//        return userService.postHouse(house_hash, state, elevator, lease, phone);
        return false;
    }

    /**
     * 修改用户信息
     *
     * @param password 密码
     * @param phone    电话
     */
    @RequestMapping(value = "/info", method = RequestMethod.POST)
    public boolean info(HttpServletRequest request, HttpServletResponse response, String password, String phone) {
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        if (username == null) {
            username = (String) session.getAttribute("username");
        }
        if (session.getAttribute("payPass") == null) {
            response.setStatus(201);
            return false;
        }
        return userService.postPhone(username, password, (String) session.getAttribute("payPass"), phone);
    }

    /**
     * 联系房主
     *
     * @param house_hash 房子hash
     * @return 房主电话
     */
    @RequestMapping(value = "/contact_owner", method = RequestMethod.POST)
    public String contactOwner(HttpServletRequest request, HttpServletResponse response, String house_hash) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (session.getAttribute("payPass") == null) {
            response.setStatus(201);
            return null;
        }
        return userService.getUser(username, (String) session.getAttribute("payPass")).getPhone();
    }

    /**
     * 获取所有用户信息
     *
     * @return 用户信息列表
     */
    @RequestMapping(value = "/all_info", method = RequestMethod.GET)
    public JSON allInfo() {
        JSON json = new JSONArray();
        return json;
    }

    /**
     * 修改用户信息
     *
     * @param username 用户账户
     * @param credit   信誉值
     */
    @RequestMapping(value = "/changeinfo", method = RequestMethod.POST)
    public void changeInfo(HttpServletRequest request, HttpServletResponse response, String username, String credit) {
//        String s = userService.postPhone();
    }

    @RequestMapping(value = "/check")
    public void changeInfo(HttpServletRequest request, HttpServletResponse response, MultipartFile face) {
        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String ethPassword = (String) session.getAttribute("payPass");
        logger.info("payPass in setUpHouse is " + ethPassword);
        if (ethPassword == null) {
            response.setStatus(201);
        }
        String path = System.getProperty("user.dir") + "\\src\\main\\file\\temp\\";
        File file = new File(path + face.getOriginalFilename());
        try {
            face.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int score = Integer.parseInt(faceService.match(file, username, ethPassword));
        if (score > 10) {
            response.setStatus(200);
        } else {
            response.setStatus(203);
        }
    }

}
