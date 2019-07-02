package com.mokelock.houseleasing.IPFS;
import com.mokelock.houseleasing.model.HouseModel.House;

import java.util.ArrayList;

//path是表的地址。流程是先根据哈希下载，然后改，然后上传。
public interface Table {
    void create(String path);//创建一个空表到path
    void insert(String user_name,String eth_id,String SK,String path);//插入name-eth_id的表，地址在path
    ArrayList<String[]> query(String[] key_for_search, String[] value_for_search, String[] key_to_get, String path);//修改后支持多关键字查询。需要加载JSON类，可以测试一下效率，效率低可改用纯文本查询。
    /*
    * 表结构及关键字查询时必须输入相应关键字，不能输错。
    * user-eth表：{"user_name","SK","eth_id"}
    * house-eth-info表,注：本表没有state属性，根据表的命名来判断房子state
    * 比如在搜索state="0"的房子时，path=/table/for/house/state/0.txt
    * 表结构:{"house_id","house_hash","lease_inter","house_type","lease_type","provi","city","sector","commu_name"}
    * insert时，参数顺序不能出错，query时无所谓。
    * insert之前要调用query来查询houseid，然后判断返回值.size()是否为0，若不为0则重复，不能插入。
    * 文本写入编码一律为utf8，读文件时注意编码。
    * 使用方法和结果类似数据库，如查询provi="shandong",city="jinan"的房子的lease_inter和house_hash
    * 则为：a=new TableImpl().query({"provi","city"},{"shandong","jinan"},{"lease_inter","house_hash"},path);
    * a.get(i)为结果的第i行,a.get(i)[0]为第i行的lease_inter值，为字符串形式。
    *  */
    void insert(House house_obj,String house_hash,String path);//插入查重必须在前面完成，插入之前要先运行一次query house_id，并判断返回值的size是否为0
    void update(House house_obj,String new_hash,String path);//更新hash。
    ArrayList<String[]> get_all(String[] key_to_get,String path);//得到整个表。
    void delete(House[] house_obj,String path);//删除指定房源。配合insert可以完成两张表之间的修改。
    void insert_into_more_info(House house_obj,String hash);
    void insert_into_comment(String user_id,String comment ,String []comment_pic,String house_level,String path);
    /*
    * comment的查询返回值如下：
    * 123 不错 ["asdagafas","wqeqwgqe","vcbsada"]
    * 123是用户名，不错是评论，剩下是图片hash数组。
    * 全是String格式，使用时要将hash数组转换成你想要的样子，如去掉方括号，去掉逗号，去掉引号。
    * ********************************************************************************************************
    * 整个IPFS系统存储结构如下：
    * IPFS SERVER
    * ├house_N_file_hash  ├table1_file_hash ├table2_file_hash ├table3_file_hash ├comment_pic_1_hash ... ├comment_pic_N_hash
    *   ├info.txt
    *   ├comment.txt
    *   ├pic1
    *   ├pic2
    *   ├pic3
    *   ├more_pic
    * 每个hash值去对应的区块链或是表中查询。
    * 在调用上述函数之前，都必须先下载最新版本的文件，再将路径作为path参数。
    * 执行任何查询之外的操作后都要在最后上传文件。
    * 下载-增删查改-上传应该被看成是一个原子操作。
    * 不考虑意外事件，不考虑并发情形。
    * */
}
