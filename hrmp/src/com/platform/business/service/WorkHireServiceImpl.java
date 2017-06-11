package com.platform.business.service;

import java.util.List;

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.dao.WorkHireDao;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.bo.Page;

public class WorkHireServiceImpl implements WorkHireService {

	private WorkHireDao workHireDao;
	
	
	public WorkHireDao getWorkHireDao() {
		return workHireDao;
	}

	public void setWorkHireDao(WorkHireDao workHireDao) {
		this.workHireDao = workHireDao;
	}

	@Override
	public WorkHire getWorkHire(String id) {
		return workHireDao.getWorkHire(id);
	}

	@Override
	public WorkHire saveWorkHire(WorkHire wh) {
		return workHireDao.saveWorkHire(wh);
	}

	@Override
	public WorkSign getWorkSign(String id) {
		return workHireDao.getWorkSign(id);
	}

	@Override
	public WorkSign saveWorkSign(WorkSign ws) {
		return workHireDao.saveWorkSign(ws);
	}

	@Override
	public List<WorkSignBo> getWorkSignList(String workHireId) {
		return workHireDao.getWorkSignList(workHireId);
	}

	@Override
	public Page getWorkHireList(WorkHireQueryBo bo, Page page) {
		return workHireDao.getWorkHireList(bo, page);
	}

	@Override
	public WorkSign getWorkSign(String workHireId, String empId) {
		return workHireDao.getWorkSign(workHireId, empId);
	}

	@Override
	public Page getWorkSignList(WorkHireQueryBo bo, Page page) {
		return workHireDao.getWorkSignList(bo, page);
	}

	@Override
	public boolean cancelOtherWorkSign(String workSignId, String userId,
			String remark) {
		return workHireDao.cancelOtherWorkSign(workSignId, userId,remark);
	}

	@Override
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId) {
		return workHireDao.getWorkHireVisitBoList(workHireId);
	}

	@Override
	public WorkHireVisit saveWorkHireVisit(WorkHireVisit v) {
		return workHireDao.saveWorkHireVisit(v);
	}

	@Override
	public WorkHireVisit getWorkHireVisit(String id) {
		return workHireDao.getWorkHireVisit(id);
	}

	@Override
	public int getWorkSignNum(String workHireId) {
		return workHireDao.getWorkSignNum(workHireId);
	}

	@Override
	public List<Object[]> getWorkKindList(WorkHireQueryBo bo) {
		return workHireDao.getWorkKindList(bo);
	}


}
