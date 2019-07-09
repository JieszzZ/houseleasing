package com.mokelock.houseleasing.model.UserModel;

import org.web3j.crypto.Sign;

import javax.management.relation.Role;

public class record {
    private String tenant;
    private String homeowner;
    private String submiter;
    private String responder;
    private int submiterEthCoin;
    private int aimerEthCoin;
    private int subFirstSign;
    private int resFirstSign;
    private int subSecondSign;
    private int resSecondSign;
    private int sub_time;
    private int effect_time;
    private int finish_time;
    private int role;
    private int state;
    private int money;
    private int gas;
    private low_location lowLocation;
    private String specific_location;

    private String house_hash;



    public record(){};

    public void setTenant(String _tenant)
    {
        tenant = _tenant;
    }
    public String getTenant()
    {
        return tenant;
    }
    public void setHomeowner(String homeowner){this.homeowner = homeowner;}
    public String getHomeowner(){return homeowner;}
    public void setSubmiter(String submiter){this.submiter = submiter;}
    public String getSubmiter(){return submiter;}
    public void setResponder(String responder){this.responder = responder;}
    public String getResponder(){return responder;}
    public void setSubmiterEthCoin(int submiterEthCoin){this.submiterEthCoin = submiterEthCoin;}
    public int getSubmiterEthCoin(){return submiterEthCoin;}
    public void setAimerEthCoin(int aimerEthCoin){this.aimerEthCoin = aimerEthCoin;}
    public int getAimerEthCoin(){return aimerEthCoin;}
    public void setSubFirstSign(int sign){this.subFirstSign = sign;}
    public int getSubFirstSign(){return subFirstSign;}
    public void setSubSecondSign(int sign){this.subSecondSign = sign;}
    public int getSubSecondSign(){return subSecondSign;}
    public void setResFirstSign(int sign){this.resFirstSign = sign;}
    public int getResFirstSign(){return resFirstSign;}
    public void setResSecondSign(int sign){this.resSecondSign = sign;}
    public int getResSecondSign(){return resSecondSign;}
    public void setSub_time(int sub_time){this.sub_time = sub_time;}
    public int getSub_time(){return sub_time;}
    public void setEffect_time(int effect_time){this.effect_time = effect_time;}
    public int getEffect_time(){return effect_time;}
    public void setFinish_time(int finish_time){this.finish_time = finish_time;}
    public int getFinish_time(){return finish_time;}
    public void setRole(int role){this.role = role;}
    public int getRole(){return role;}
    public void setState(int state){this.state = state;}
    public int getState(){return state;}
    public void setMoney(int money){this.money = money;}
    public int getMoney(){return money;}


    public void setGas(int _gas){gas = _gas;}
    public int getGas(){return gas;}
    public void setLowLocation(low_location _lowLocation){lowLocation = _lowLocation;}
    public low_location getLowLocation(){return lowLocation;}
    public void setSpecific_location(String _specific_location){specific_location = _specific_location;}
    public String getSpecific_location(){return specific_location;}

    public void setHouse_hash(String house_hash){this.house_hash = house_hash;}
    public String getHouse_hash(){return  house_hash;}
}
