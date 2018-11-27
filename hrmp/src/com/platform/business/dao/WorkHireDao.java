package com.platform.business.dao;

import java.util.List;

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.BadRecord;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.bo.Page;

public interface WorkHireDao {

	public WorkHire getWorkHire(String id);
	
	public WorkHire saveWorkHire(WorkHire wh);
	
	public WorkHire getLastWorkHire(String loginName,String empTypeId);
	
	public WorkSign getWorkSign(String id);
	
	public WorkHireVisit getWorkHireVisit(String id);
	
	public WorkSign getWorkSign(String workHireId,String empId);
	
	public WorkSign saveWorkSign(WorkSign ws);
	/**
	 * 验证用户是否可报名指定招工
	 * @param workHire
	 * @param empId
	 * @return
	 */
	public boolean isCanSign(WorkHire workHire, String empId);
	
	public WorkHireVisit saveWorkHireVisit(WorkHireVisit v);
	
	/**
	 * 查询已关闭招工列表
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryClosedWorkHireList(WorkHireQueryBo bo,Page page);
	
	/**
	 * 取消工人其他报名信息
	 * @param workSignId
	 * @param userId
	 * @param remark
	 */
	public boolean cancelOtherWorkSign(String workSignId,String userId,String remark);
	
	public List<WorkSignBo> getWorkSignList(String workHireId);
	
	/**
	 * 查询报名人数
	 * @param workHireId
	 * @return
	 */
	public int getWorkSignNum(String workHireId);
	
	public Page getWorkHireList(WorkHireQueryBo bo,Page page);
	
	/**
	 * 查询我发布的招工信息
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page getMyWorkHireList(String userId,WorkHireQueryBo bo,Page page);
	
	/**
	 * 查询我的工作
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page getWorkSignList(WorkHireQueryBo bo,Page page);
	
	/**
	 * 查找所有未支付报名订单
	 * @return
	 */
	public List<WorkSign> getNoPayList();
	
	/**
	 * 查询回访记录
	 * @param workHireId
	 * @return
	 */
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId);
	
	/**
	 * 删除报名信息，用于微信支付接口调用失败时删除报名信息
	 * @param ws
	 */
	public void deleteWorkSign(WorkSign ws);
	
	/**
	 * 保持不良记录
	 * @param br
	 * @return
	 */
	public BadRecord saveBadRecord(BadRecord br);
	
	/**
	 * 查询我的不良记录
	 * @param userId
	 * @return
	 */
	public Page getMyBadRecordList(String userId,Page page);
	
	/**
	 * 关闭超时招工
	 */
	public void closeOverTimePublish();
	
	/**
	 * 置顶
	 * @param id
	 * @return
	 */
	public WorkHire toTopWorkHire(String id);
	
	/**
	 * 查询可报名临时招工
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryLSWorkHireForSign(String loginName,Page page);
	
	/**
	 * 查询可报名长期招工
	 * @param bo
	 * @param page
	 * @param keyword
	 * @return
	 */
	public Page queryCQWorkHireForSign(String loginName,Page page,String keyword);
	
	/**
	 * 查询可报名承包施工
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryCBWorkHireForSign(String loginName,Page page);
	
	public List<BadRecord> getBadRecordList(String workHireId);
	
}
