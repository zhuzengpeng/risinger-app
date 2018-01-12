package com.bbd.risinger.test.service;

import com.bbd.risinger.common.persistence.Page;
import com.bbd.risinger.common.service.CrudService;
import com.bbd.risinger.test.dao.TestDataDao;
import com.bbd.risinger.test.entity.TestData;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 单表生成Service
 * @author ThinkGem
 * @version 2015-04-06
 */
@Service
@Transactional(readOnly = true)
public class TestDataService extends CrudService<TestDataDao, TestData> {

	@Override
	public TestData get(String id) {
		return super.get(id);
	}

	@Override
	public List<TestData> findList(TestData testData) {
		return super.findList(testData);
	}

	@Override
	public Page<TestData> findPage(Page<TestData> page, TestData testData) {
		return super.findPage(page, testData);
	}

	@Override
	@Transactional(readOnly = false)
	public void save(TestData testData) {
		super.save(testData);
	}

	@Override
	@Transactional(readOnly = false)
	public void delete(TestData testData) {
		super.delete(testData);
	}
	
}