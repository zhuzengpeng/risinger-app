package com.bbd.risinger.test.service;

import com.bbd.risinger.common.service.TreeService;
import com.bbd.risinger.test.entity.TestTree;
import com.bbd.risinger.common.utils.StringUtils;
import com.bbd.risinger.test.dao.TestTreeDao;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 树结构生成Service
 * @author ThinkGem
 * @version 2015-04-06
 */
@Service
@Transactional(readOnly = true)
public class TestTreeService extends TreeService<TestTreeDao, TestTree> {

	@Override
	public TestTree get(String id) {
		return super.get(id);
	}

	@Override
	public List<TestTree> findList(TestTree testTree) {
		if (StringUtils.isNotBlank(testTree.getParentIds())){
			testTree.setParentIds(","+testTree.getParentIds()+",");
		}
		return super.findList(testTree);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(TestTree testTree) {
		super.save(testTree);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(TestTree testTree) {
		super.delete(testTree);
	}
	
}