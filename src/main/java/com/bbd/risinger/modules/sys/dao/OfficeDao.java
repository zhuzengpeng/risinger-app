package com.bbd.risinger.modules.sys.dao;

import com.bbd.risinger.common.persistence.TreeDao;
import com.bbd.risinger.common.persistence.annotation.MyBatisDao;
import com.bbd.risinger.modules.sys.entity.Office;

/**
 * 机构DAO接口
 *
 * @author ThinkGem
 * @version 2014-05-16
 */
@MyBatisDao
public interface OfficeDao extends TreeDao<Office> {

}
