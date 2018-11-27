package com.platform.business.service;

import java.util.List;

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.BadRecord;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.bo.Page;

public interface WorkHireService {

	public WorkHire getWorkHire(String id);
	
	public WorkHire saveWorkHire(WorkHire wh);
	
	public WorkSign getWorkSign(String id);
	
	public void deleteWorkSign(WorkSign ws);
	
	public WorkHireVisit getWorkHireVisit(String id);
	
	public WorkSign getWorkSign(String workHireId,String empId);
	
	/**
	 * 验证用户是否可报名指定招工
	 * @param workHire
	 * @param empId
	 * @return
	 */
	public boolean isCanSign(WorkHire workHire, String empId);
	
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
	
	/**
	 * 查询正在招工列表
	 * @param bo
	 * @param page
	 * @deprecated
	 * @return
	 */
	public Page getWorkHireList(WorkHireQueryBo bo,Page page);
	
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
	
	/**
	 * 查询已关闭招工列表
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryClosedWorkHireList(WorkHireQueryBo bo,Page page);
	
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
	 * 查询微信支付结果，更新支付状态
	 */
	public void queryWXPayResultFromWX();
	
	/**
	 * 关闭超时招工
	 */
	public void closeOverTimePublish();
	
	/**
	 * 获取用户最近一次发布的消息
	 * @param loginName
	 * @param empTypeId
	 * @return
	 */
	public WorkHire getLastWorkHire(String loginName,String empTypeId);
	
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
	 * 置顶
	 * @param id
	 * @return
	 */
	public WorkHire toTopWorkHire(String id);
	
	/**
	 * 查询微信支付结果
	 * @param workSignId
	 * @return
	 */
	public boolean isPaySuccess(String workSignId);
	
	public List<BadRecord> getBadRecordList(String workHireId);
}
