package com.mokelock.houseleasing.model.UserModel;

import javax.management.relation.Role;

public class record {
    private String tenant;
    private String homeowner;
    private String submiter;
    private String responder;
    private int submiterEthCoin;
    private int aimerEthCoin;
    private Sign subFirstSign;
    private Sign resFirstSign;
    private Sign subSecondSign;
    private Sign resSecondSign;
    private int sub_time;
    private int effect_time;
    private int finish_time;
    private Role role;
    private State state;
    private Money money;
    private int gas;
    private low_location lowLocation;
    private String specific_location;

    private String house_hash;

    public enum Sign{signed,unsigned,refused}
    public enum Role{submiter,responder}
    public enum State{submiting,effecting,finished,refused,failed}
    public enum Money{nosub,subed,retruned,deduction}

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
    public void setSubFirstSign(Sign sign){this.subFirstSign = sign;}
    public Sign getSubFirstSign(){return subFirstSign;}
    public void setSubSecondSign(Sign sign){this.subSecondSign = sign;}
    public Sign getSubSecondSign(){return subSecondSign;}
    public void setResFirstSign(Sign sign){this.resFirstSign = sign;}
    public Sign getResFirstSign(){return resFirstSign;}
    public void setResSecondSign(Sign sign){this.resSecondSign = sign;}
    public Sign getResSecondSign(){return resSecondSign;}
    public void setSub_time(int sub_time){this.sub_time = sub_time;}
    public int getSub_time(){return sub_time;}
    public void setEffect_time(int effect_time){this.effect_time = effect_time;}
    public int getEffect_time(){return effect_time;}
    public void setFinish_time(int finish_time){this.finish_time = finish_time;}
    public int getFinish_time(){return finish_time;}
    public void setRole(Role role){this.role = role;}
    public Role getRole(){return role;}
    public void setState(State state){this.state = state;}
    public State getState(){return state;}
    public void setMoney(Money money){this.money = money;}
    public Money getMoney(){return money;}


    public void setGas(int _gas){gas = _gas;}
    public int getGas(){return gas;}
    public void setLowLocation(low_location _lowLocation){lowLocation = _lowLocation;}
    public low_location getLowLocation(){return lowLocation;}
    public void setSpecific_location(String _specific_location){specific_location = _specific_location;}
    public String getSpecific_location(){return specific_location;}

    public void setHouse_hash(String house_hash){this.house_hash = house_hash;}
    public String getHouse_hash(){return  house_hash;}
}
