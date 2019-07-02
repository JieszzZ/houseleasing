package com.mokelock.houseleasing.dao.UserDaoImpl;

import com.mokelock.houseleasing.dao.UserDao;

public class UserDaoImpl implements UserDao {
    /**
     * 根据用户名获取密码
     * @param username 用户名
     * @return 密码
     */
    public String getPasswordByUsername(String username){return "";}

    /**
     * 创建用户
     * @param username 用户名
     * @param password 密码
     * @return 返回值>0表示成功
     */
    public int insertUser(String username, String password){return 1;}

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 返回值>0表示存在
     */
    public int checkUser(String username){return 1;}

    /**
     * 更改密码
     * @param username 用户名
     * @param password 新密码
     * @return 返回值>0表示成功
     */
    public int updatePassword(String username, String password){return 1;}

}
