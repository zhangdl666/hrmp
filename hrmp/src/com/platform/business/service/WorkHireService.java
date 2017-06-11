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
	 * ȡ����������������Ϣ
	 * @param workSignId
	 * @param userId
	 * @param remark
	 */
	public boolean cancelOtherWorkSign(String workSignId,String userId,String remark);
	
	/**
	 * ��ѯ�ѱ����б���������˲�ͨ���ĺ��û�ȡ���ı�����
	 * @param workHireId
	 * @return
	 */
	public List<WorkSignBo> getWorkSignList(String workHireId);
	
	/**
	 * ��ѯ��������
	 * @param workHireId
	 * @return
	 */
	public int getWorkSignNum(String workHireId);
	
	/**
	 * ��ѯ�طü�¼
	 * @param workHireId
	 * @return
	 */
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId);
	
	public Page getWorkHireList(WorkHireQueryBo bo,Page page);
	
	public List<Object[]> getWorkKindList(WorkHireQueryBo bo);
	
	/**
	 * ��ѯ�ҵĹ���
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page getWorkSignList(WorkHireQueryBo bo,Page page);
}
