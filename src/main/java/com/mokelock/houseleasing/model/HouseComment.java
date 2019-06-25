package com.mokelock.houseleasing.model;

import java.text.DecimalFormat;

/*
 * 李晓婷
 * 20190624
 * 房屋评论
 */
public class HouseComment {

    public HouseComment() {
    }
    //详情页需要
    public HouseComment(String user_id, String comment, String[] comment_pic) {
        this.user_id = user_id;
        this.comment = comment;
        this.comment_pic = comment_pic;
    }
    //存储评论需要
    public HouseComment(String user_id, String house_hash, double level, String comment, String[] comment_pic) {
        this.user_id = user_id;
        this.house_hash = house_hash;
        this.level = level;
        this.comment = comment;
        this.comment_pic = comment_pic;
    }

    //DecimalFormat format = new DecimalFormat("0.0");

    //登录用户账号
    private String user_id;

    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    //房产证号HASH
    private String house_hash;

    public String getHouse_hash() {
        return house_hash;
    }
    public void setHouse_hash(String house_hash) {
        this.house_hash = house_hash;
    }

    //五个级别
    private double level;

    public double getLevel() {
        return level;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    //文字评价
    private String comment;

    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }

    //图片评价
    private String[] comment_pic = new String[3];

    public String[] getComment_pic() {
        return comment_pic;
    }
    public void setComment_pic(String[] comment_pic) {
        this.comment_pic = comment_pic;

    }

  /*  public String toString(){

        return "HouseComment{" +
                "user_id='" + user_id + '\'' +
                ", comment='" + comment + '\'' +
                ", comment_pic='" + comment_pic + '\'' +
                ", house_hash='" + house_hash + '\'' +
                ", level='" + level + '\'' +
                '}';
    }*/

}
