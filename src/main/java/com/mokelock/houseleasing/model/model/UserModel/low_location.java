package com.mokelock.houseleasing.model.UserModel;

public class low_location {
    private String provi;
    private String city;
    private String sector;
    private String commu_name;

    //不用任何参数创建一个low_location对象
    public low_location(){}

    //依次使用省份，城市，城区，小区地址创建一个low_location对象
    public low_location(String _provi,String _city,String _sector,String _commu_name)
    {
        provi = _provi;
        city = _city;
        sector = _sector;
        commu_name = _commu_name;
    }

    public void setProvi(String _provi){provi = _provi;}
    public String getProvi(){return provi;}
    public void setCity(String _city){city = _city;}
    public String getCity(){return city;}
    public void setSector(String _sector){sector = _sector;}
    public String getSector(){return sector;}
    public void setCommu_name(String _commu_name){commu_name = _commu_name;}
    public String getCommu_name(){return commu_name;}

}
