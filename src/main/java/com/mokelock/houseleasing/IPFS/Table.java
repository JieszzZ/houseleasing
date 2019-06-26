package com.mokelock.houseleasing.IPFS;
import com.mokelock.houseleasing.model.HouseModel.House;

import java.util.ArrayList;

//path是表的地址。流程是先根据哈希下载，然后改，然后上传。
public interface Table {
    void create(String path);//创建一个空表到path
    void insert(String user_name,String eth_id,String path);//插入name-eth_id的表，地址在path
    ArrayList<String[]> query(String[] key_for_search, String[] value_for_search, String[] key_to_get, String path);//修改后支持多关键字查询。需要加载JSON类，可以测试一下效率，效率低可改用纯文本查询。
    /*
    *表结构及关键字查询时必须输入相应关键字，不能输错。
    * user-eth表：{"user_name","eth_id"}
    * house-eth-info表,注：本表没有state属性，根据表的命名来判断房子state
    * 比如在搜索state="0"的房子时，path=/table/for/house/state/0.txt
    * 表结构:{"house_id","eth_id","lease_inter","house_type","lease_type","provi","city","sector","commu_name"}
    * insert时，参数顺序不能出错，query时无所谓。
    *insert之前要调用query来查询houseid，然后判断返回值.size()是否为0，若不为0则重复，不能插入。
    * 文本写入编码一律为utf8，读文件时注意编码。
    *  */
    void insert(House house_obj,String eth_id,String path);//插入查重必须在前面完成，插入之前要先运行一次query house_id，并判断返回值的size是否为0
}
