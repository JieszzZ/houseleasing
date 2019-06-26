//package com.mokelock.houseleasing.services.servicesImpl;
//
//import com.alibaba.fastjson.JSON;
//
//import com.alibaba.fastjson.JSONObject;
//import com.mokelock.houseleasing.model.HouseModel.House;
//import com.mokelock.houseleasing.model.HouseModel.SampleHouse;
//
//import com.mokelock.houseleasing.services.HouseService;
//import org.springframework.stereotype.Service;
//
///*
// * 李晓婷
// * 20190625
// * 房屋相关处理接口
// */
//@Service
//public class HouseServiceImpl implements HouseService {
//    @Override
//
//    //获取房源详细信息
//    public JSON speInfo(String house_hash) {
//
//        House thishouse = new House();
//        thishouse.setHouse_hash(house_hash);
//        //房源的hash就是房源在IPFS中的索引，根据索引就可以得到该房源的其他相关信息
//        //然后分层将房源的信息创建为json格式
//        //最后使用第二个构造函数创建House对象outputhouse将房源信息录入，并转化为json格式
//        //最后使用response来创建最终的返回信息
//        return null;
//    }
//
//    @Override
//    //搜索
//    public JSON search(String low_location, String leaser_inter, String house_type, String lease_type) {
//
//        SampleHouse thissh = new SampleHouse();
//        /*
//         * 维护四个Array
//         * 判断low_location是否为空，如果否，则从全部可以显示的房源中找到简略地址字段与low_location相同的，并将其hash值存入array1
//         * 判断leaser_inter是否为空，如果否，则从array1的房源中找到简略地址字段与leaser_inter相同的，并将其hash值存入array2
//         * 判断house_type是否为空，如果否，则从array2的房源中找到简略地址字段与house_type相同的，并将其hash值存入array3
//         * 判断lease_type是否为空，如果否，则从array3的房源中找到简略地址字段与lease_type相同的，并将其hash值存入array4
//         * 最后的array4就是用户搜索到的房源的hash，根据hash获取概要信息返回
//         * */
//    }
//    @Override
//    public JSON speinfo(String house_hash) {
//        return null;
//    }
//
//    @Override
//    public JSON search(JSON low_location, int lease_inter, int house_type, int lease_type) {
//
//        return null;
//    }
//
//    @Override
//
//    //评价房子
//    public JSONObject valuation(String house_hash, int house_level, String comment, String[] comment_pic) {
//        /*
//        * 获取用户账号、文字评论和图片评论、房屋等级
//        * 根据房源hash找到该房源的详细信息并将房源评论添加上
//        * 返回状态消息
//        * */
//    public JSON valuation(String house_hash, int house_level, String comment, String[] comment_pic) {
//
//        return null;
//    }
//
//    @Override
//
//    //获取房屋列表
//    public JSON allInfo() {
//        /*
//        * 创建JSONArray对象verified和non_verified
//        * 遍历房源hash，每个都写为JSON的形式，并把verify=1的放入verified，verify=0的放入non_verified
//        * 最后将整个json返回
//        * */
//    public JSON allinfo() {
//
//        return null;
//    }
//}
