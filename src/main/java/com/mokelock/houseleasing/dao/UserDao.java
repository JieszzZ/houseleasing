package com.mokelock.houseleasing.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserDao {

    /**
     * 根据用户名获取密码
     * @param username 用户名
     * @return 密码
     */
    @Select("select password from user_table where username = #{username}")
    String getPasswordByUsername(String username);

    /**
     * 创建用户
     * @param username 用户名
     * @param password 密码
     * @return 返回值>0表示成功
     */
    @Insert("insert into user_table (username, password) values (#{username}, #{password}")
    int insertUser(String username, String password);

    /**
     * 判断用户名是否存在
     * @param username 用户名
     * @return 返回值>0表示存在
     */
    @Select("select count(*) from user_table where username = #{username}")
    int checkUser(String username);

    /**
     * 更改密码
     * @param username 用户名
     * @param password 新密码
     * @return 返回值>0表示成功
     */
    @Update("update user_table set password = #{password} where username = #{username}")
    int updatePassword(String username, String password);

}
