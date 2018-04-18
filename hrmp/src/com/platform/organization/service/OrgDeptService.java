package com.platform.organization.service;

import java.util.List;

import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgDept;

public interface OrgDeptService {

	public OrgDept getOrgDept(String id);

	public OrgDept saveDept(OrgDept dept);

	public boolean delDept(String id);

	/**
	 * ���Ų�ѯ
	 * @param deptName ��������
	 * @param parentDeptId ������id
	 * @return
	 */
	public List<OrgDept> queryDepts(String deptName,String parentDeptId);
	
	/**
	 * ���ŷ�ҳ��ѯ
	 * @param deptName ��������
	 * @param parentDeptId ������id
	 * @param page ��ҳ����
	 * @return
	 */
	public Page queryDepts(String deptName,String parentDeptId,Page page);
	
	/**
	 * ��ȡָ�����ŵ�ֱ����˾����deptIDΪ��˾ID���򷵻ر���˾
	 * @param deptId
	 * @return
	 */
	public OrgDept getDirectCompany(String deptId);
}
