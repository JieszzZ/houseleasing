package com.mokelock.houseleasing.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.mokelock.houseleasing.model.HouseModel.House;
import com.mokelock.houseleasing.model.HouseModel.HouseTemp;
import com.mokelock.houseleasing.services.HouseService;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/api/house")
public class HouseController {

    private final static Logger logger = Logger.getLogger(HouseController.class);


    @Resource
    HouseService houseService;

    /**
     * 获取房源详细信息
     *
     * @param house_id_hash 房子hash
     * @return 信息列表
     */
    @RequestMapping(value = "/speinfo", method = RequestMethod.POST)
    public String speInfo(HttpServletResponse response, String house_id_hash) {
        logger.debug("speinfo's param is " + house_id_hash);
        return houseService.speInfo(house_id_hash).toJSONString();
//        return "{\"house_pic\":[\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1561902918490" +
//                "&di=93de199997bd27876fb3e72842da2551&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2Fa403a1d2880ef70d" +
//                "311b0626f356f0682b8d77da524b-TAk0MZ_fw658\",\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999" +
//                "_10000&sec=1561902918489&di=5f7f910721b2c154270f1cdeca71dc67&imgtype=0&src=http%3A%2F%2Fpic8.nipic.com%2" +
//                "F20100713%2F1954049_091647155567_2.jpg\",\"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_" +
//                "10000&sec=1561902918489&di=08e89404ddf20c958c789cbd02a37282&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun." +
//                "com%2F67c0b0d20ca69f5a0a0167ab6526dcd2b6a1a3f046e3-l9OP6q_fw658\"],\"house_hash\":\"sdfwenk31345\"," +
//                "\"owner_id\":\"37012506546564\",\"verify\":\"true\",\"owner\":\"quyanso111\",\"owner_name\":\"曲延松\"," +
//                "\"role\":1,\"state\":0,\"low_location\":{\"provi\":\"山东省\",\"city\":\"济南市\",\"sector\":\"历下区\"," +
//                "\"commu_name\":\"奥龙官邸\"},\"specific_location\":\"2号楼3单元1801\",\"floor\":18,\"elevator\":\"true\"," +
//                "\"lease\":3800,\"house_type\":1,\"house_owner_credit\":16,\"house_comment\":[{\"user_id\":\"quyans111\"," +
//                "\"comment\":\"这个房子挺不错适合居住\",\"comment_pic\":[\"sdfadfasfasfasdfa\",\"sdfadfasfasfasdfa\"," +
//                "\"sdfadfasfasfasdfa\"]}]}";
    }

    /**
     *
     * @param lease_inter  价格范围：0 全部 1 500元以下 2 500-1000元 3 1000-1500元 4 1500-2000元 5 2000元以上
     * @param house_type   房子类型：0 全部 1 一室 2 二室 3 其他
     * @param lease_type   租住类型：0 全部 1 整租 2 合租
     * @return 房子列表
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public String search(String provi, String city, String site_select, String lease_inter, String house_type,
                         String lease_type, boolean elevator) {
        JSONObject low_location = new JSONObject();
        low_location.put("provi", "0");
        low_location.put("city", "0");
        low_location.put("site_select", "0");
        logger.debug("request param has \n\t\t" + low_location.toJSONString() + " " + lease_inter + " " + house_type + " " +
                lease_type + " " + elevator);
        String result = houseService.search(low_location, lease_inter, house_type, lease_type, elevator).toJSONString();
        logger.debug("search result is \n\t" + result);
        return result;
//        return "[{\"photo\":\"sadfadsfadf\",\"low_location\":\"山东省济南市历下区**小区\",\"lease\":\"5000\",\"house_type\":\"2\",\"lease_type\":\"1\"},{\"photo\":\"sadfadsfadf\",\"low_location\":\"山东省济南市历下区B小区\",\"lease\":\"3000\",\"house_type\":\"2\",\"lease_type\":\"1\"},{\"photo\":\"sadfadsfadf\",\"low_location\":\"山东省济南市历下区**小区\",\"lease\":\"2000\",\"house_type\":\"2\",\"lease_type\":\"1\"}]";
    }

    /**
     * @param house_hash   房子hash
     * @param house_level  1-5共五个级别
     * @param comment_word 文字评价
     * @param comment_pic  图片评价
     */
    @RequestMapping(value = "/valuation", method = RequestMethod.POST)
    public String valuation(String house_hash, String house_level, String comment_word, String comment_pic[]) {
        return houseService.valuation(house_hash, house_level, comment_word, comment_pic);
    }

    /**
     * 获取房屋列表
     *
     * @return 房屋列表
     */
    @RequestMapping(value = "/allinfo", method = RequestMethod.GET)
    public JSON allInfo() {
        return houseService.allInfo();
    }

    @RequestMapping(value = "/setUpHouse", method = RequestMethod.GET)
    public String setUpHouse(HttpServletRequest request, HouseTemp house) {

//        return houseService.setUpHouse(house.getUser_id(), house.(), );
        return "";
    }

}
