package com.mokelock.houseleasing.model.UserModel;

import javax.management.relation.Role;

public class record {
    private String tenant;
    private String homeowner;
    private int tenantDeposit;
    private int homeDeposit;
    private boolean tenant_fist_sign;
    private boolean home_first_sign;
    private boolean tenant_second_sign;
    private boolean home_second_sign;
    private int sub_time;
    private int effect_time;
    private int finish_time;
    private Role role;
    private State state;
    private Money money;
    private int gas;
    private low_location lowLocation;
    private String specific_location;

    enum Role{tenant,homeowner}
    enum State{submiting,effecting,finished,refused,failed}
    enum Money{nosub,subed,retruned}

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
    public void setTenantDeposit(int tenantDeposit){this.tenantDeposit = tenantDeposit;}
    public int getTenantDeposit(){return tenantDeposit;}
    public void setHomeDeposit(int homeDeposit){this.homeDeposit = homeDeposit;}
    public int getHomeDeposit(){return homeDeposit;}
    public void setTenant_fist_sign(boolean tenant_fist_sign){this.tenant_fist_sign = tenant_fist_sign;}
    public boolean isTenant_fist_sign(){return tenant_fist_sign;}
    public void setHome_first_sign(boolean home_first_sign){this.home_first_sign = home_first_sign;}
    public boolean isHome_first_sign(){return home_first_sign;}
    public void setTenant_second_sign(boolean tenant_second_sign){this.tenant_second_sign = tenant_second_sign;}
    public boolean isTenant_second_sign(){return tenant_second_sign;}
    public void setHome_second_sign(boolean home_second_sign){this.home_second_sign = home_second_sign;}
    public boolean isHome_second_sign(){return home_second_sign;}
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


}
