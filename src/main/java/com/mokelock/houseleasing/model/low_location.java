package com.mokelock.houseleasing.model;

/*
* 李晓婷
* 20190624
* 房屋概要地址
*/
public class low_location {

    public low_location() {
    }

    public low_location(String provi, String city, String sector, String commu_name) {
        this.provi = provi;
        this.city = city;
        this.sector = sector;
        this.commu_name = commu_name;
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
