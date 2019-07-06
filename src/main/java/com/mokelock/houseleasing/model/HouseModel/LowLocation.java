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
    public JSONObject LLtoJson(){
        JSONObject fjo = new JSONObject(true);

        fjo.put("provi",this.provi);
        fjo.put("city",this.city);
        fjo.put("sector",this.sector);
        fjo.put("commu_name",this.commu_name);

        return fjo;
    }
    //将json对象格式的lowlocation对象转化回来
    //public LowLocation toLLO(JSONObject jobject){
    //    return JSONObject.parseObject(jobject.toJSONString(), LowLocation.class);
    //}

    /*通过测试*/
    //将lowlocation对象转换为String的形式
    public String lltoStr(){
        return this.provi+this.city+this.sector+this.commu_name;
    }
    //将String格式的lowlocation对象转化回来
    public void StoLL(String llstr){
        //山东省济南市++区++小区
        if(llstr.length()<=0){
            this.setProvi("");
            this.setCity("");
            this.setSector("");
            this.setCommu_name("");
        }else if(llstr.length()>0 && llstr.length()<=3){
            this.setProvi("山东省");
            this.setCity("");
            this.setSector("");
            this.setCommu_name("");
        }else if(llstr.length()>=4 && llstr.length()<=6){
            this.setProvi("山东省");
            this.setCity("济南市");
            this.setSector("");
            this.setCommu_name("");
        }else if(llstr.length()>=7 && llstr.length() <= 9){
            this.setProvi("山东省");
            this.setCity("济南市");
            this.setSector(llstr.substring(6));
            this.setCommu_name("");
        }else if(llstr.length() >= 10){
            this.setProvi("山东省");
            this.setCity("济南市");
            this.setSector(llstr.substring(6,9));
            this.setCommu_name(llstr.substring(9));
        }
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

}
