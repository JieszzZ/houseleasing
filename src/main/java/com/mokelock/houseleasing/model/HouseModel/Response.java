package com.mokelock.houseleasing.model.HouseModel;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class Response {

    public Response() {
    }

    public Response(int status, String message, JSONObject data) {
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
    private JSONObject data;

    //将Response对象转换为json对象的形式
    public JSONObject toJson(){
        String jsonString = JSONObject.toJSONString(this, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteNullStringAsEmpty);
        return JSONObject.parseObject(jsonString);
    }
    //将json对象格式的Response对象转化回来
    public Response toRPO(JSONObject jobject){
        return JSONObject.parseObject(jobject.toJSONString(), Response.class);
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

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
}
