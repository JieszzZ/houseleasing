package com.mokelock.houseleasing.model.UserModel;

public class front_record {

    private String tenant;
    private String homeowner;
    private String submiter;
    private String responder;
    private int submiterEthCoin;
    private int aimerEthCoin;
    private String subFirstSign;
    private String resFirstSign;
    private String subSecondSign;
    private String resSecondSign;
    private int sub_time;
    private int effect_time;
    private int finish_time;
    private String role;
    private String state;
    private String money;
    private int gas;
    private low_location lowLocation;
    private String specific_location;

    private String house_hash;

    public front_record() {
    }

    public front_record(record record) {
        tenant = record.getTenant();
        homeowner = record.getHomeowner();
        submiter = record.getSubmiter();
        responder = record.getResponder();
        submiterEthCoin = record.getSubmiterEthCoin();
        aimerEthCoin = record.getAimerEthCoin();
        subFirstSign = translateSign(record.getSubFirstSign());
        resFirstSign = translateSign(record.getResFirstSign());
        subSecondSign = translateSign(record.getSubSecondSign());
        resSecondSign = translateSign(record.getResSecondSign());
        sub_time = record.getSub_time();
        effect_time = record.getEffect_time();
        finish_time = record.getFinish_time();
        role = translateRole(record.getRole());
        state = translateState(record.getState());
        money = translateMoney(record.getMoney());
        gas = record.getGas();
        lowLocation = record.getLowLocation();
        specific_location = record.getSpecific_location();
        house_hash = record.getHouse_hash();

    }


    public void setTenant(String tenant) {
        this.tenant = tenant;
    }

    public String getTenant() {
        return tenant;
    }

    public void setHomeowner(String homeowner) {
        this.homeowner = homeowner;
    }

    public String getHomeowner() {
        return homeowner;
    }

    public void setSubmiter(String submiter) {
        this.submiter = submiter;
    }

    public String getSubmiter() {
        return submiter;
    }

    public void setResponder(String responder) {
        this.responder = responder;
    }

    public String getResponder() {
        return responder;
    }

    public void setSubmiterEthCoin(int submiterEthCoin) {
        this.submiterEthCoin = submiterEthCoin;
    }

    public int getSubmiterEthCoin() {
        return submiterEthCoin;
    }

    public void setAimerEthCoin(int aimerEthCoin) {
        this.aimerEthCoin = aimerEthCoin;
    }

    public int getAimerEthCoin() {
        return aimerEthCoin;
    }

    public void setSubFirstSign(String subFirstSign) {
        this.subFirstSign = subFirstSign;
    }

    public String getSubFirstSign() {
        return subFirstSign;
    }

    public void setSubSecondSign(String sign) {
        this.subSecondSign = sign;
    }

    public String getSubSecondSign() {
        return subSecondSign;
    }

    public void setResFirstSign(String sign) {
        this.resFirstSign = sign;
    }

    public String getResFirstSign() {
        return resFirstSign;
    }

    public void setResSecondSign(String sign) {
        this.resSecondSign = sign;
    }

    public String getResSecondSign() {
        return resSecondSign;
    }

    public void setSub_time(int sub_time) {
        this.sub_time = sub_time;
    }

    public int getSub_time() {
        return sub_time;
    }

    public void setEffect_time(int effect_time) {
        this.effect_time = effect_time;
    }

    public int getEffect_time() {
        return effect_time;
    }

    public void setFinish_time(int finish_time) {
        this.finish_time = finish_time;
    }

    public int getFinish_time() {
        return finish_time;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMoney() {
        return money;
    }


    public void setGas(int _gas) {
        gas = _gas;
    }

    public int getGas() {
        return gas;
    }

    public void setLowLocation(low_location _lowLocation) {
        lowLocation = _lowLocation;
    }

    public low_location getLowLocation() {
        return lowLocation;
    }

    public void setSpecific_location(String _specific_location) {
        specific_location = _specific_location;
    }

    public String getSpecific_location() {
        return specific_location;
    }

    public void setHouse_hash(String house_hash) {
        this.house_hash = house_hash;
    }

    public String getHouse_hash() {
        return house_hash;
    }

    private String translateSign(int sign) {
        switch (sign) {
            case 0:
                return "未签名";
            case 1:
                return "已同意";
            case 2:
                return "已拒绝";
        }
        return "";
    }

    private String translateState(int state) {
        switch (state) {
            case 0:
                return "已提交";
            case 1:
                return "生效中";
            case 2:
                return "已完成";
            case 3:
                return "被拒绝";
            case 4:
                return "毁约";
        }
        return "";
    }

    private String translateRole(int role) {
        switch (role) {
            case 0:
                return "房客";
            case 1:
                return "房主";
        }
        return "";
    }

    private String translateMoney(int money) {
        switch (money) {
            case 0:
                return "未提交";
            case 1:
                return "已提交";
            case 2:
                return "已返还";
            case 3:
                return "被扣除";
        }
        return "";
    }

}
