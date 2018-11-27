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
	 * ��֤�û��Ƿ�ɱ���ָ���й�
	 * @param workHire
	 * @param empId
	 * @return
	 */
	public boolean isCanSign(WorkHire workHire, String empId);
	
	public WorkHireVisit saveWorkHireVisit(WorkHireVisit v);
	
	/**
	 * ��ѯ�ѹر��й��б�
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryClosedWorkHireList(WorkHireQueryBo bo,Page page);
	
	/**
	 * ȡ����������������Ϣ
	 * @param workSignId
	 * @param userId
	 * @param remark
	 */
	public boolean cancelOtherWorkSign(String workSignId,String userId,String remark);
	
	public List<WorkSignBo> getWorkSignList(String workHireId);
	
	/**
	 * ��ѯ��������
	 * @param workHireId
	 * @return
	 */
	public int getWorkSignNum(String workHireId);
	
	public Page getWorkHireList(WorkHireQueryBo bo,Page page);
	
	/**
	 * ��ѯ�ҷ������й���Ϣ
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page getMyWorkHireList(String userId,WorkHireQueryBo bo,Page page);
	
	/**
	 * ��ѯ�ҵĹ���
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page getWorkSignList(WorkHireQueryBo bo,Page page);
	
	/**
	 * ��������δ֧����������
	 * @return
	 */
	public List<WorkSign> getNoPayList();
	
	/**
	 * ��ѯ�طü�¼
	 * @param workHireId
	 * @return
	 */
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId);
	
	/**
	 * ɾ��������Ϣ������΢��֧���ӿڵ���ʧ��ʱɾ��������Ϣ
	 * @param ws
	 */
	public void deleteWorkSign(WorkSign ws);
	
	/**
	 * ���ֲ�����¼
	 * @param br
	 * @return
	 */
	public BadRecord saveBadRecord(BadRecord br);
	
	/**
	 * ��ѯ�ҵĲ�����¼
	 * @param userId
	 * @return
	 */
	public Page getMyBadRecordList(String userId,Page page);
	
	/**
	 * �رճ�ʱ�й�
	 */
	public void closeOverTimePublish();
	
	/**
	 * �ö�
	 * @param id
	 * @return
	 */
	public WorkHire toTopWorkHire(String id);
	
	/**
	 * ��ѯ�ɱ�����ʱ�й�
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryLSWorkHireForSign(String loginName,Page page);
	
	/**
	 * ��ѯ�ɱ��������й�
	 * @param bo
	 * @param page
	 * @param keyword
	 * @return
	 */
	public Page queryCQWorkHireForSign(String loginName,Page page,String keyword);
	
	/**
	 * ��ѯ�ɱ����а�ʩ��
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryCBWorkHireForSign(String loginName,Page page);
	
	public List<BadRecord> getBadRecordList(String workHireId);
	
}
