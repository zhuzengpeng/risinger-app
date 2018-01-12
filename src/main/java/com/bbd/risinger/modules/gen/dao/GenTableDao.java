package com.bbd.risinger.modules.gen.dao;

import com.bbd.risinger.common.persistence.CrudDao;
import com.bbd.risinger.common.persistence.annotation.MyBatisDao;
import com.bbd.risinger.modules.gen.entity.GenTable;

/**
 * 业务表DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenTableDao extends CrudDao<GenTable> {
	
}
