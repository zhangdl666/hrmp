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
	 * ��֤�û��Ƿ�ɱ���ָ���й�
	 * @param workHire
	 * @param empId
	 * @return
	 */
	public boolean isCanSign(WorkHire workHire, String empId);
	
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
	
	/**
	 * ��ѯ�����й��б�
	 * @param bo
	 * @param page
	 * @deprecated
	 * @return
	 */
	public Page getWorkHireList(WorkHireQueryBo bo,Page page);
	
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
	
	/**
	 * ��ѯ�ѹر��й��б�
	 * @param bo
	 * @param page
	 * @return
	 */
	public Page queryClosedWorkHireList(WorkHireQueryBo bo,Page page);
	
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
	 * ��ѯ΢��֧�����������֧��״̬
	 */
	public void queryWXPayResultFromWX();
	
	/**
	 * �رճ�ʱ�й�
	 */
	public void closeOverTimePublish();
	
	/**
	 * ��ȡ�û����һ�η�������Ϣ
	 * @param loginName
	 * @param empTypeId
	 * @return
	 */
	public WorkHire getLastWorkHire(String loginName,String empTypeId);
	
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
	 * �ö�
	 * @param id
	 * @return
	 */
	public WorkHire toTopWorkHire(String id);
	
	/**
	 * ��ѯ΢��֧�����
	 * @param workSignId
	 * @return
	 */
	public boolean isPaySuccess(String workSignId);
	
	public List<BadRecord> getBadRecordList(String workHireId);
}
