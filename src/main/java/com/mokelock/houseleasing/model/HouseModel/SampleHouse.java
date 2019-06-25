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

    public SampleHouse(String use_id, String photo, LowLocation low_location, int lease, int lease_type, int house_type) {
        this.use_id = use_id;
        this.photo = photo;
        this.low_location = low_location;
        this.lease = lease;
        this.lease_type = lease_type;
        this.house_type = house_type;
    }

    public SampleHouse(String photo, LowLocation low_location, int lease, int lease_type, int house_type) {
        this.photo = photo;
        this.low_location = low_location;
        this.lease = lease;
        this.lease_type = lease_type;
        this.house_type = house_type;
    }

    //将SampleHouse对象转换为json对象的形式
    public JSONObject toJson(){
        String jsonString = JSONObject.toJSONString(this, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty);
        return JSONObject.parseObject(jsonString);
    }
    //将json对象格式的SampleHouse对象转化回来
    public SampleHouse toSHO(JSONObject jobject){
        return JSONObject.parseObject(jobject.toJSONString(), SampleHouse.class);
    }

    //查询的人
    private String use_id = "";

    public String getUse_id() {
        return use_id;
    }
    public void setUse_id(String use_id) {
        this.use_id = use_id;
    }

    //是否经过验证
    private boolean verify;

    public boolean isVerify() {
        return verify;
    }
    public void setVerify(boolean verify) {
        this.verify = verify;
    }

    //照片
    private String photo;

    public String getPhoto() {
        return photo;
    }
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    //lowlocation
    private LowLocation low_location;

    public LowLocation getLow_location() {
        return low_location;
    }
    public void setLow_location(LowLocation low_location) {
        this.low_location = low_location;
    }

    //租金
    private int lease;

    public int getLease() {
        return lease;
    }
    public void setLease(int lease) {
        this.lease = lease;
    }

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

}
