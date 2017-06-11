package com.platform.organization.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.bo.Page;
import com.platform.organization.dao.OrgDeptDao;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgDeptView;

public class OrgDeptServiceImpl implements OrgDeptService {
	private final Logger logger = Logger.getLogger(OrgDeptServiceImpl.class);
	
	@Autowired
	private OrgDeptDao orgDeptDao;
	

	public OrgDeptDao getOrgDeptDao() {
		return orgDeptDao;
	}

	public void setOrgDeptDao(OrgDeptDao orgDeptDao) {
		this.orgDeptDao = orgDeptDao;
	}

	@Override
	public OrgDept getOrgDept(String id) {
		return orgDeptDao.getOrgDept(id);
	}

	@Override
	public OrgDept saveDept(OrgDept dept) {
		return orgDeptDao.saveDept(dept);
	}

	@Override
	public boolean delDept(String id) {
		return orgDeptDao.delDept(id);
	}

	@Override
	public OrgDept getDirectCompany(String deptId) {
		return orgDeptDao.getDirectCompany(deptId);
	}

	@Override
	public List<OrgDeptView> queryDepts(String deptName, String parentDeptId,
			boolean isContainChildDept) {
		return orgDeptDao.queryDepts(deptName, parentDeptId, isContainChildDept);
	}

	@Override
	public Page queryDepts(String deptName, String parentDeptId,
			boolean isContainChildDept, Page page) {
		return orgDeptDao.queryDepts(deptName, parentDeptId, isContainChildDept, page);
	}

}
