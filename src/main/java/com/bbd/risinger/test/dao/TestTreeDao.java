package com.bbd.risinger.test.dao;

import com.bbd.risinger.common.persistence.TreeDao;
import com.bbd.risinger.common.persistence.annotation.MyBatisDao;
import com.bbd.risinger.test.entity.TestTree;

/**
 * 树结构生成DAO接口
 * @author ThinkGem
 * @version 2015-04-06
 */
@MyBatisDao
public interface TestTreeDao extends TreeDao<TestTree> {
	
}