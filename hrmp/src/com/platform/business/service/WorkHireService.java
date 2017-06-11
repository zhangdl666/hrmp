package com.platform.business.service;

import java.util.List;

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.bo.Page;

public interface WorkHireService {

public WorkHire getWorkHire(String id);
	
	public WorkHire saveWorkHire(WorkHire wh);
	
	public WorkSign getWorkSign(String id);
	
	public WorkHireVisit getWorkHireVisit(String id);
	
	public WorkSign getWorkSign(String workHireId,String empId);
	
	public WorkSign saveWorkSign(WorkSign ws);
	
	public WorkHireVisit saveWorkHireVisit(WorkHireVisit v);
	
	/**
	 * 取消工人其他报名信息
	 * @param workSignId
	 * @param userId
	 * @param remark
	 */
	public boolean cancelOtherWorkSign(String workSignId,String userId,String remark);
	
	/**
	 * 查询已报名列表（不包含审核不通过的和用户取消的报名）
	 * @param workHireId
	 * @return
	 */
	public List<WorkSignBo> getWorkSignList(String workHireId);
	
	/**
	 * 查询报名人数
	 * @param workHireId
	 * @return
	 */
	public int getWorkSignNum(String workHireId);
	
	/**
	 * 查询回访记录
	 * @param workHireId
	 * @return
	 */
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId);
	
	public Page getWorkHireList(WorkHireQueryBo bo,Page page);
	
	public List<Object[]> getWorkKindList(WorkHireQueryBo bo);
	
	/**
	 * 查询我的工作
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page getWorkSignList(WorkHireQueryBo bo,Page page);
}
