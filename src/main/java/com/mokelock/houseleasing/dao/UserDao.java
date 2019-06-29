package com.mokelock.houseleasing.dao;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {

    /**
     * 根据用户名获取密码
     * @param username 用户名
     * @return 密码
     */
    String getPasswordByUsername(String username);

    /**
     * 创建用户
     * @param username 用户名
     * @param password 密码
     * @return 返回值>0表示成功
     */
    int insertUser(String username, String password);

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 返回值>0表示存在
     */
    int checkUser(String username);

    /**
     * 更改密码
     * @param username 用户名
     * @param password 新密码
     * @return 返回值>0表示成功
     */
    int updatePassword(String username, String password);

}
