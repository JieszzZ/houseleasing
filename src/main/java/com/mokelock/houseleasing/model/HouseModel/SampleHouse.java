package com.mokelock.houseleasing.model.HouseModel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/*
 * 李晓婷
 * 20190624
 * 房屋概要信息（根据用户输入查询房屋概要信息列表，或者管理员直接查看房源概要信息列表）
 */
public class SampleHouse {
    /*查询相关*/

    public SampleHouse() {
    }

    public SampleHouse(String use_id, String house_pic, String low_str_location, String lease, String lease_inter, String house_type, String lease_type, boolean elevator, boolean verify, String house_id_hash) {
        this.use_id = use_id;
        this.house_pic = house_pic;
        this.low_str_location = low_str_location;
        this.lease = lease;
        this.lease_inter = lease_inter;
        this.house_type = house_type;
        this.lease_type = lease_type;
        this.elevator = elevator;
        this.verify = verify;
        this.house_id_hash = house_id_hash;
    }

    /*************************************************************************************************/
    //返回前端用到的构造函数
    public SampleHouse(String house_pic, String low_str_location, String lease, String house_type, String lease_type, boolean elevator, String house_id_hash) {
        this.house_pic = house_pic;
        this.low_str_location = low_str_location;
        this.lease = lease;
        this.house_type = house_type;
        this.lease_type = lease_type;
        this.elevator = elevator;
        this.house_id_hash = house_id_hash;
    }

    //将SampleHouse对象转换为json对象的形式
    public JSONObject SHtoJson() {
        JSONObject josh = new JSONObject(true);

        josh.put("house_pic", this.house_pic);
        josh.put("low_str_location", this.low_str_location);
        josh.put("lease", this.lease);
        josh.put("house_type", this.house_type);
        josh.put("lease_type", this.lease_type);
        josh.put("elevator", this.elevator);
        josh.put("house_id_hash", this.house_id_hash);

        return josh;
    }

    /************************************************************************************************/

    //查询的人
    private String use_id = "";

    public String getUse_id() {
        return use_id;
    }

    public void setUse_id(String use_id) {
        this.use_id = use_id;
    }

    //hash
    private String house_id_hash;

    public String getHouse_id_hash() {
        return house_id_hash;
    }

    public void setHouse_id_hash(String house_id_hash) {
        this.house_id_hash = house_id_hash;
    }

    //照片
    private String house_pic;

    public String getHouse_pic() {
        return house_pic;
    }

    public void setHouse_pic(String house_pic) {
        this.house_pic = house_pic;
    }

    //简略地址的字符串形式
    private String low_str_location;

    public String getLow_str_location() {
        return low_str_location;
    }

    public void setLow_str_location(String low_str_location) {
        this.low_str_location = low_str_location;
    }

    //租金
    private String lease;

    public String getLease() {
        return lease;
    }

    public void setLease(String lease) {
        this.lease = lease;
    }

    //租金范围：0 全部 1 500元以下 2 500-1000元 3 1000-1500元 4 1500-2000元 5 2000元以上
    private String lease_inter;

    public String getLease_inter() {
        return lease_inter;
    }

    public void setLease_inter(String lease_inter) {
        this.lease_inter = lease_inter;
    }


    //房子的类型：0 全部 1 一室 2 二室 3 其他
    private String house_type;

    public String getHouse_type() {
        return house_type;
    }

    public void setHouse_type(String house_type) {
        this.house_type = house_type;
    }

    //租房类型：0 全部 1 整租 2 合租
    private String lease_type;

    public String getLease_type() {
        return lease_type;
    }

    public void setLease_type(String lease_type) {
        this.lease_type = lease_type;
    }

    //有无电梯
    private boolean elevator;

    public boolean isElevator() {
        return elevator;
    }

    //public boolean getElevator() {
    //    return elevator;
    //}
    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }

    //是否经过验证
    private boolean verify;

    public boolean isVerify() {
        return verify;
    }

    public void setVerify(boolean verify) {
        this.verify = verify;
    }
}
