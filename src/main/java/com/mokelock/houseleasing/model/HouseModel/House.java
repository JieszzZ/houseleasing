package com.mokelock.houseleasing.model.HouseModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/*
 * 李晓婷
 * 20190624
 * 房屋详细信息，用户或者管理员查看
 */
public class House{

    public House() {
    }
    public House(String user_id, String[] house_pic, String house_id, String house_id_hash, String owner_id, boolean verify, String owner, String owner_name, int role, int state, String area, JSONObject low_location, String low_str_location, String specific_location, int floor, String lon, String lat, boolean elevator, int lease, int lease_inter, int lease_type, int house_type, int house_owner_credit, String accessory, JSONArray house_comment) {
        this.user_id = user_id;
        this.house_pic = house_pic;
        this.house_id = house_id;
        this.house_id_hash = house_id_hash;
        this.owner_id = owner_id;
        this.verify = verify;
        this.owner = owner;
        this.owner_name = owner_name;
        this.role = role;
        this.state = state;
        this.area = area;
        this.low_location = low_location;
        this.low_str_location = low_str_location;
        this.specific_location = specific_location;
        this.floor = floor;
        this.lon = lon;
        this.lat = lat;
        this.elevator = elevator;
        this.lease = lease;
        this.lease_inter = lease_inter;
        this.lease_type = lease_type;
        this.house_type = house_type;
        this.house_owner_credit = house_owner_credit;
        this.accessory = accessory;
        this.house_comment = house_comment;
    }

    /**********************************************************************/
    //包含了所有需要返回给前端的信息
    public House(String[] house_pic, String house_id_hash, String owner_id, boolean verify, String owner, String owner_name, int role, int state, String area, JSONObject low_location, String low_str_location, String specific_location, int floor, String lon, String lat, boolean elevator, int lease, int house_type, int house_owner_credit, String accessory, JSONArray house_comment) {
        this.house_pic = house_pic;
        this.house_id_hash = house_id_hash;
        this.owner_id = owner_id;
        this.verify = verify;
        this.owner = owner;
        this.owner_name = owner_name;
        this.role = role;
        this.state = state;
        this.area = area;
        this.low_location = low_location;
        this.low_str_location = low_str_location;
        this.specific_location = specific_location;
        this.floor = floor;
        this.lon = lon;
        this.lat = lat;
        this.elevator = elevator;
        this.lease = lease;
        this.house_type = house_type;
        this.house_owner_credit = house_owner_credit;
        this.accessory = accessory;
        this.house_comment = house_comment;
    }
    //将House对象转换为json对象的形式
    public JSONObject HtoJson(){
        JSONObject hjo = new JSONObject(true);

        hjo.put("house_pic",this.house_pic);
        hjo.put("house_id_hash",this.house_id_hash);
        hjo.put("owner_id",this.owner_id);
        hjo.put("verify",this.verify);
        hjo.put("owner",this.owner);
        hjo.put("owner_name",this.owner_name);
        hjo.put("role",this.role);
        hjo.put("state",this.state);
        hjo.put("area",this.area);
        hjo.put("low_location",this.low_location);
        hjo.put("low_str_location",this.low_str_location);
        hjo.put("specific_location",this.specific_location);
        hjo.put("floor",this.floor);
        hjo.put("lon",this.lon);
        hjo.put("lat",this.lat);
        hjo.put("elevator",this.elevator);
        hjo.put("lease",this.lease);
        hjo.put("house_type",this.house_type);
        hjo.put("house_owner_credit",this.house_owner_credit);
        hjo.put("accessory",this.accessory);
        hjo.put("house_comment",this.house_comment);

        return hjo;
    }
    /**********************************************************************/

    //登录用户账号
    private String user_id = "";

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    //房子图片
    private String[] house_pic;

    public String[] getHouse_pic() {
        return house_pic;
    }
    public void setHouse_pic(String[] house_pic) {
        this.house_pic = house_pic;
    }

    //房产证号
    private String house_id;

    public String getHouse_id() {
        return house_id;
    }
    public void setHouse_id(String house_id) {
        this.house_id = house_id;
    }

    //房产证号HASH
    private String house_id_hash;

    public String getHouse_id_hash() {
        return house_id_hash;
    }
    public void setHouse_id_hash(String house_id_hash) {
        this.house_id_hash = house_id_hash;
    }


    public void setHouse_level(double house_level) {
        this.house_level = house_level;
    }

    //拥有者的身份证号
    private String owner_id;

    public String getOwner_id() {
        return owner_id;
    }
    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    //是否经过验证
    private boolean verify;

    public boolean isVerify() {
        return verify;
    }
    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    //拥有者的账号
    private String owner;

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }

    //拥有者的姓名
    private String owner_name;

    public String getOwner_name() {
        return owner_name;
    }
    public void setOwner_name(String owner_name) {
        this.owner_name = owner_name;
    }

    //调用接口人的身份：1用户 2管理员 0其他
    private int role;

    public int getRole() {
        return role;
    }
    public void setRole(int role) {
        this.role = role;
    }

    //可租用状态：0下线状态 1发布状态 2 删除状态
    private int state;

    public int getState() {
        return state;
    }
    public void setState(int state) {
        this.state = state;
    }

    public String area;

    public String getArea() {
        return area;
    }
    public void setArea(String area) {
        this.area = area;
    }

    //简略地址JSON
    private JSONObject low_location;

    public JSONObject getLow_location() {
        return low_location;
    }
    public void setLow_location(JSONObject low_location) {
        this.low_location = low_location;
    }

    //简略地址String
    private String low_str_location;

    public String getLow_str_location() {
        return low_str_location;
    }
    public void setLow_str_location(String low_str_location) {
        this.low_str_location = low_str_location;
    }

    //详细地址几号楼几单元门牌号
    private String specific_location;

    public String getSpecific_location() {
        return specific_location;
    }
    public void setSpecific_location(String specific_location) {
        this.specific_location = specific_location;
    }

    //楼层
    private int floor;

    public int getFloor() {
        return floor;
    }
    public void setFloor(int floor) {
        this.floor = floor;
    }

    //房子的面积
    private String lon;//经度

    public String getLon() {
        return lon;
    }
    public void setLon(String lon) {
        this.lon = lon;
    }

    private String lat;//纬度

    public String getLat() {
        return lat;
    }
    public void setLat(String lat) {
        this.lat = lat;
    }

    //有无电梯
    private boolean elevator;

    public boolean isElevator() {
        return elevator;
    }
    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    //租金
    private int lease;

    public int getLease() {
        return lease;
    }
    public void setLease(int lease) {
        this.lease = lease;
    }

    /*查询相关*/
    //租金范围：0 全部 1 500元以下 2 500-1000元 3 1000-1500元 4 1500-2000元 5 2000元以上
    private int lease_inter;

    public int getLease_inter() {
        return lease_inter;
    }
    public void setLease_inter(int lease_inter) {
        this.lease_inter = lease_inter;
    }

    //租房类型：0 全部 1 整租 2 合租
    private int lease_type;

    public int getLease_type() {
        return lease_type;
    }
    public void setLease_type(int lease_type) {
        this.lease_type = lease_type;
    }

    //房子的类型：0 全部 1 一室 2 二室 3 其他
    private int house_type;

    public int getHouse_type() {
        return house_type;
    }
    public void setHouse_type(int house_type) {
        this.house_type = house_type;
    }
    /*查询相关*/

    //房主的信誉值
    private int house_owner_credit;

    public int getHouse_owner_credit() {
        return house_owner_credit;
    }
    public void setHouse_owner_credit(int house_owner_credit) {
        this.house_owner_credit = house_owner_credit;
    }

    //房子的level
    private double house_level;

    public double getHouse_level() {
        return house_level;
    }
    public void setHouse_level(int house_level) {
        this.house_level = house_level;
    }

    //房屋配套
    private String accessory;

    public String getAccessory() {
        return accessory;
    }
    public void setAccessory(String accessory) {
        this.accessory = accessory;
    }

    //房子评论JSONARRAY
    private JSONArray house_comment;

    public JSONArray getHouse_comment() {
        return house_comment;
    }
    public void setHouse_comment(JSONArray house_comment) {
        this.house_comment = house_comment;
    }

}
