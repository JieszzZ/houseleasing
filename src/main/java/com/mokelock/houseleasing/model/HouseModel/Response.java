package com.mokelock.houseleasing.model.HouseModel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Response {

    public Response() {
    }

    public Response(int status, String message, Object data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public Response(int status, String message) {
        this.status = status;
        this.message = message;
    }

    //返回状态
    private int status;

    //返回消息
    private String message;

    //具体数据
    private Object data;

    //将Response对象转换为json对象的形式
    public JSONObject RestoJson2() {
        JSONObject fjo = new JSONObject(true);

        fjo.put("status", this.status);
        fjo.put("message", this.message);

        return fjo;
    }

    public JSONObject RestoJson3() {
        JSONObject fjo = new JSONObject(true);

        fjo.put("status", this.status);
        fjo.put("message", this.message);
        fjo.put("data", this.data);

        return fjo;
        //String jsonString = JSONObject.toJSONString(this, SerializerFeature.PrettyFormat,
        //        SerializerFeature.WriteNullStringAsEmpty);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
