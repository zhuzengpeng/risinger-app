/**
 * Copyright &copy; 2012-2016 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.bbd.risinger.modules.gen.dao;

import com.bbd.risinger.common.persistence.CrudDao;
import com.bbd.risinger.common.persistence.annotation.MyBatisDao;
import com.bbd.risinger.modules.gen.entity.GenScheme;

/**
 * 生成方案DAO接口
 * @author ThinkGem
 * @version 2013-10-15
 */
@MyBatisDao
public interface GenSchemeDao extends CrudDao<GenScheme> {
	
}
