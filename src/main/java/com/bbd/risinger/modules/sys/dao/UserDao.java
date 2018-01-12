package com.bbd.risinger.modules.sys.dao;

import com.bbd.risinger.common.persistence.CrudDao;
import com.bbd.risinger.modules.sys.entity.User;
import com.bbd.risinger.common.persistence.annotation.MyBatisDao;

import java.util.List;

/**
 * 用户DAO接口
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface UserDao extends CrudDao<User> {

    /**
     * 根据登录名称查询用户
     *
     * @param user
     * @return
     */
    User getByLoginName(User user);

    /**
     * 通过OfficeId获取用户列表，仅返回用户id和name（树查询用户时用）
     *
     * @param user
     * @return
     */
    List<User> findUserByOfficeId(User user);

    /**
     * 查询全部用户数目
     *
     * @return
     */
    long findAllCount(User user);

    /**
     * 更新用户密码
     *
     * @param user
     * @return
     */
    int updatePasswordById(User user);

    /**
     * 更新登录信息，如：登录IP、登录时间
     *
     * @param user
     * @return
     */
    int updateLoginInfo(User user);

    /**
     * 删除用户角色关联数据
     *
     * @param user
     * @return
     */
    int deleteUserRole(User user);

    /**
     * 插入用户角色关联数据
     *
     * @param user
     * @return
     */
    int insertUserRole(User user);

    /**
     * 更新用户信息
     *
     * @param user
     * @return
     */
    int updateUserInfo(User user);

}
