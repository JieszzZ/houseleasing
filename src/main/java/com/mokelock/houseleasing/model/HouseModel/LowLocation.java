package com.mokelock.houseleasing.model.HouseModel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

/*
* 李晓婷
* 20190624
* 房屋概要地址
*/
//继承
public class LowLocation{

    public LowLocation() {
        super();
    }

    public LowLocation(String provi, String city, String sector, String commu_name) {
        this.provi = provi;
        this.city = city;
        this.sector = sector;
        this.commu_name = commu_name;
    }

    /*通过测试*/
    //将lowlocation对象转换为json对象的形式
    public JSONObject toJson(){
        String jsonString = JSONObject.toJSONString(this,SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty);
        return JSONObject.parseObject(jsonString);
    }
    //将json对象格式的lowlocation对象转化回来
    public LowLocation toLLO(JSONObject jobject){
        return JSONObject.parseObject(jobject.toJSONString(), LowLocation.class);
    }

    //省、市、区、小区名
    private String provi;

    public String getProvi() {
        return provi;
    }
    public void setProvi(String provi) {
        this.provi = provi;
    }

    private	String city;

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    private String sector;

    public String getSector() {
        return sector;
    }
    public void setSector(String sector) {
        this.sector = sector;
    }

    private String commu_name;

    public String getCommu_name() {
        return commu_name;
    }
    public void setCommu_name(String commu_name) {
        this.commu_name = commu_name;
    }

    /*public static void main(String args[]){
        LowLocation a = new LowLocation("h","t","h","t");
        JSONObject o = a.toJson();
        String s = o.toJSONString();
        System.out.println(o.get(a.provi));
        System.out.println(a);
    }*/

}
