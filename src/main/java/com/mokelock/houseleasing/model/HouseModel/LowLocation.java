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
        fjo.put("commu_name",this.provi);

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
        int p = llstr.indexOf("省");
        int c = llstr.indexOf("市");
        int s = llstr.indexOf("区");

        int length = llstr.length();

        if(p > -1)
            this.setProvi(llstr.substring(0,p)+"省");
        else if(p <= -1)
            this.setProvi("");

        if((p > -1) && (c > -1))
            this.setCity(llstr.substring(p+1,c)+"市");
        else if((p <= -1) && (c > -1))
            this.setCity(llstr.substring(0,c)+"市");
        else if(c <= -1)
            this.setCity("");

        if((c > -1) && (s > -1))
            this.setSector(llstr.substring(c+1,s)+"区");
        else if((c <= -1) && (s > -1) && (p > -1))
            this.setSector(llstr.substring(p+1,s)+"区");
        else if((c <= -1) && (s > -1) && (p <= -1))
            this.setSector(llstr.substring(0,s)+"区");
        else if(s <= -1)
            this.setSector("");

        if(s > -1){
            if(length > (s+1))
                this.setCommu_name(llstr.substring(s+1));
            else
                this.setCommu_name("");
        }else if((s <= -1) && (c > -1)){
            if(length > (c+1))
                this.setCommu_name(llstr.substring(c+1));
            else
                this.setCommu_name("");
        }else if((s <= -1) && (c <= -1) && (p > -1)){
            if(length > (p+1))
                this.setCommu_name(llstr.substring(p+1));
            else
                this.setCommu_name("");
        }else if((s <= -1) && (c <= -1) && (p <= -1)) {
            if(length > 0)
                this.setCommu_name(llstr);
            else
                this.setCommu_name("");
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

    /*public static void main(String args[]){
        LowLocation a = new LowLocation("h","t","h","t");
        JSONObject o = a.toJson();
        String s = o.toJSONString();
        System.out.println(o.get(a.provi));
        System.out.println(a);
    }*/

}
