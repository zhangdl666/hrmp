package com.platform.business.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.platform.app.push.AppPushService;
import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.BadRecord;
import com.platform.business.pojo.BusinessOpinion;
import com.platform.business.pojo.Message;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.business.service.BusinessMessageService;
import com.platform.business.service.BusinessNumberService;
import com.platform.business.service.BusinessOpinionService;
import com.platform.business.service.WorkHireService;
import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.service.OrgDeptService;
import com.platform.organization.service.OrgUserService;

public class WorkHireAction extends BaseAction {
	private final Logger logger = Logger.getLogger(WorkHireAction.class);
	private String message;
	private String workHireId;
	private String workSignId;
	private String workHireVisitId;
	private WorkHire workHire;
	private WorkHireVisit workHireVisit;
	private WorkHireQueryBo workHireQueryBo;
	private List<WorkHire> workHireList;
	private List<WorkSignBo> workSignList;
	private List<BusinessOpinion> opinionList;
	private List<BadRecord> badRecordList;
	private List<WorkHireVisitBo> visitList;
	private String treeNodeData;
	private String checkedIds;
	
	//ҳ���дȨ�޿���
	private String permission;
	
	private Page page;
	
	private WorkHireService workHireService;
	private OrgDeptService orgDeptService;
	private OrgUserService orgUserService;
	private BusinessMessageService businessMessageService;

	private BusinessOpinionService businessOpinionService;
	private BusinessNumberService businessNumberService;
	private AppPushService appPushService;
	
	public List<BadRecord> getBadRecordList() {
		return badRecordList;
	}

	public void setBadRecordList(List<BadRecord> badRecordList) {
		this.badRecordList = badRecordList;
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}
	
	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public AppPushService getAppPushService() {
		return appPushService;
	}

	public void setAppPushService(AppPushService appPushService) {
		this.appPushService = appPushService;
	}

	public BusinessMessageService getBusinessMessageService() {
		return businessMessageService;
	}

	public void setBusinessMessageService(
			BusinessMessageService businessMessageService) {
		this.businessMessageService = businessMessageService;
	}

	public String getWorkSignId() {
		return workSignId;
	}

	public void setWorkSignId(String workSignId) {
		this.workSignId = workSignId;
	}

	public List<WorkHire> getWorkHireList() {
		return workHireList;
	}

	public void setWorkHireList(List<WorkHire> workHireList) {
		this.workHireList = workHireList;
	}

	public WorkHire getWorkHire() {
		return workHire;
	}

	public void setWorkHire(WorkHire workHire) {
		this.workHire = workHire;
	}

	public String getWorkHireVisitId() {
		return workHireVisitId;
	}

	public void setWorkHireVisitId(String workHireVisitId) {
		this.workHireVisitId = workHireVisitId;
	}

	public WorkHireVisit getWorkHireVisit() {
		return workHireVisit;
	}

	public void setWorkHireVisit(WorkHireVisit workHireVisit) {
		this.workHireVisit = workHireVisit;
	}

	public List<WorkHireVisitBo> getVisitList() {
		return visitList;
	}

	public void setVisitList(List<WorkHireVisitBo> visitList) {
		this.visitList = visitList;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getWorkHireId() {
		return workHireId;
	}

	public void setWorkHireId(String workHireId) {
		this.workHireId = workHireId;
	}

	public List<WorkSignBo> getWorkSignList() {
		return workSignList;
	}

	public void setWorkSignList(List<WorkSignBo> workSignList) {
		this.workSignList = workSignList;
	}

	public WorkHireService getWorkHireService() {
		return workHireService;
	}

	public void setWorkHireService(WorkHireService workHireService) {
		this.workHireService = workHireService;
	}

	public OrgDeptService getOrgDeptService() {
		return orgDeptService;
	}

	public void setOrgDeptService(OrgDeptService orgDeptService) {
		this.orgDeptService = orgDeptService;
	}

	public List<BusinessOpinion> getOpinionList() {
		return opinionList;
	}

	public void setOpinionList(List<BusinessOpinion> opinionList) {
		this.opinionList = opinionList;
	}

	public BusinessOpinionService getBusinessOpinionService() {
		return businessOpinionService;
	}

	public void setBusinessOpinionService(
			BusinessOpinionService businessOpinionService) {
		this.businessOpinionService = businessOpinionService;
	}

	public WorkHireQueryBo getWorkHireQueryBo() {
		return workHireQueryBo;
	}

	public void setWorkHireQueryBo(WorkHireQueryBo workHireQueryBo) {
		this.workHireQueryBo = workHireQueryBo;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public BusinessNumberService getBusinessNumberService() {
		return businessNumberService;
	}

	public void setBusinessNumberService(BusinessNumberService businessNumberService) {
		this.businessNumberService = businessNumberService;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getTreeNodeData() {
		return treeNodeData;
	}

	public void setTreeNodeData(String treeNodeData) {
		this.treeNodeData = treeNodeData;
	}

	public String getCheckedIds() {
		return checkedIds;
	}

	public void setCheckedIds(String checkedIds) {
		this.checkedIds = checkedIds;
	}

	//�鿴
	public String viewWorkHire() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
		}else {
			//�½�
			HttpServletRequest req = ServletActionContext.getRequest();
			OrgUser loginUser = (OrgUser)req.getSession().getAttribute("loginUser");
			OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
			String empTypeId = req.getParameter("empTypeId");
			workHire = new WorkHire();
//			String businessNumber = businessNumberService.getNumber("W");
//			workHire.setBusinessNumber(businessNumber);
			workHire.setCreateTime(Calendar.getInstance().getTime());
			workHire.setPublisherId(loginUser.getId());
			workHire.setPublisherName(loginUser.getUserName() + "��" + loginUser.getLoginName() + "��");
			workHire.setPublisherCompanyId(company.getId());
			workHire.setPublisherCompanyName(company.getDeptName());
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_NOPUBLISH);//�ݸ�״̬
			workHire.setEmpTypeId(empTypeId);
			workHire.setSex("male");
			workHire.setSalaryRemark("BGF");
			workHire.setPayMode("true");
		}
		
		//ȷ��ҳ���дȨ��
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		return workHire.getEmpTypeId();
	}
	
	//����
	public String saveWorkHire() {
		Calendar calendar = Calendar.getInstance();
		if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(workHire.getStatus())) {
			if(workHire.getPublishTime() == null) {
				workHire.setPublishTime(calendar.getTime());
			}
		}
		boolean isNew = false;
		if(workHire.getId()==null) {
			isNew = true;
			HttpServletRequest req = ServletActionContext.getRequest();
			String empDateFlag = req.getParameter("empDateFlag");
			if("1".equals(empDateFlag)) {
				workHire.setEmpDate(calendar.getTime());
			}else if("2".equals(empDateFlag)) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				workHire.setEmpDate(calendar.getTime());
			}
		}
		
		//ȷ��ҳ���дȨ��
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "����ʧ�ܣ�ֻ�ܱ༭�Լ��������й���Ϣ";
			return SUCCESS;
		}
		
		//���й�����Ϊ���ڹ�����ʱ��ʱ����֤���ʡ��й������Ƿ�����޸�Ҫ��
		if(workHire.getId()!=null && ("LS".equals(workHire.getEmpTypeId()) || "CQ".equals(workHire.getEmpTypeId()))){
			WorkHire wh = workHireService.getWorkHire(workHire.getId());
			int oldHireNum = wh.getHireNum();
			int oldSalary = Integer.valueOf(wh.getSalary());
			int nowHireNum = workHire.getHireNum();
			int nowSalary = Integer.valueOf(workHire.getSalary());
			if(nowHireNum<oldHireNum || nowSalary < oldSalary) {
				int signCount = workHireService.getWorkSignNum(workHire.getId());
				if(nowSalary < oldSalary){
					message = "����ʧ�ܣ����й��˱��������ʲ������µ�";
					return workHire.getEmpTypeId();
				}
				if(nowHireNum < signCount) {
					message = "����ʧ�ܣ����й��˱������й������޸Ĳ���������ѱ�������";
					return workHire.getEmpTypeId();
				}
			}
		}
		
		workHireService.saveWorkHire(workHire);
		//��¼�����¼
		if(isNew) {
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"�½�","�½��й���Ϣ");
		}else{
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"�༭","�༭�й���Ϣ");
		}
		
		message = "����ɹ�";
		
		return workHire.getEmpTypeId();
	}
	
	public String queryWorkHire() {
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(workHireQueryBo == null) {
			workHireQueryBo = new WorkHireQueryBo();
		}
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = getLoginUser();
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		workHireQueryBo.setPublisherCompanyId(company.getId());
		
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return SUCCESS;
	}
		
	//ɾ��
	public String deleteWorkHireById() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
			if(workHire != null) {
				if(WorkHire.WORK_HIRE_STATUS_DELETE.equals(workHire.getStatus())) {
					message = "�Ѿ�ɾ���������ظ�ɾ����";
				}else {
					OrgUser currentUser = getLoginUser();
					if(currentUser.getId().equals(workHire.getPublisherId())) {
						permission = "write";
					}else {
						permission = "readonly";
					}
					if(permission == "readonly"){
						message = "����ʧ�ܣ�ֻ��ɾ���Լ��������й���Ϣ";
					}else {
						workHire.setStatus(WorkHire.WORK_HIRE_STATUS_DELETE);
						workHire.setCloseTime(Calendar.getInstance().getTime());
						workHireService.saveWorkHire(workHire);
						message = "ɾ���ɹ�";
						
						//��¼�����¼
						String remark = getRequest().getParameter("remark");
						if(remark==null || "".equals(remark.trim())) {
							remark = "ɾ���й���Ϣ";
						}
						businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"ɾ��",remark);
					}
				}
			}
		}
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return workHire.getEmpTypeId();
	}
	
	//ɾ��
	public String deleteWorkHire() {
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "����ʧ�ܣ�ֻ��ɾ���Լ��������й���Ϣ";
			return SUCCESS;
		}
		
		if(WorkHire.WORK_HIRE_STATUS_DELETE.equals(workHire.getStatus())) {
			message = "�Ѿ�ɾ���������ظ�ɾ����";
		}else {
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_DELETE);
			workHire.setCloseTime(Calendar.getInstance().getTime());
			workHireService.saveWorkHire(workHire);
			message = "ɾ���ɹ�";
			
			//��¼�����¼
			String remark = getRequest().getParameter("remark");
			if(remark==null || "".equals(remark.trim())) {
				remark = "ɾ���й���Ϣ";
			}
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"ɾ��",remark);		
		}
		
		return workHire.getEmpTypeId();
	}
		
	public String publishWorkHireById() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
			if(workHire != null) {
				if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(workHire.getStatus())) {
					message = "�Ѿ������������ظ�������";
				}else {
					OrgUser currentUser = getLoginUser();
					if(currentUser.getId().equals(workHire.getPublisherId())) {
						permission = "write";
					}else {
						permission = "readonly";
					}
					if(permission == "readonly"){
						message = "����ʧ�ܣ�ֻ�ܷ����Լ��������й���Ϣ";
					}else {
						workHire.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
						workHire.setPublishTime(Calendar.getInstance().getTime());
						workHireService.saveWorkHire(workHire);
						message = "�����ɹ�";
						
						//��¼�����¼
						String remark = getRequest().getParameter("remark");
						if(remark==null || "".equals(remark.trim())) {
							remark = "�����й���Ϣ";
						}
						businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"����",remark);
					}
				}
			}
		}
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		
		//������Ϣ��app
		try {
			String descri = workHire.getWorkKind() + workHire.getHireNum() + "λ��" + workHire.getSalary() + "��" + workHire.getWorkDescri();
			appPushService.sendAndroidBroadcast(descri,descri,descri,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workHire.getEmpTypeId();
	}
	
	//����
	public String publishWorkHire() {
		if(workHire.getId()==null) {
			Calendar calendar = Calendar.getInstance();
			HttpServletRequest req = ServletActionContext.getRequest();
			String empDateFlag = req.getParameter("empDateFlag");
			if("1".equals(empDateFlag)) {
				workHire.setEmpDate(calendar.getTime());
			}else if("2".equals(empDateFlag)) {
				calendar.add(Calendar.DAY_OF_YEAR, 1);
				workHire.setEmpDate(calendar.getTime());
			}
		}
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "����ʧ�ܣ�ֻ�ܷ����Լ��������й���Ϣ";
			return SUCCESS;
		}
		
		if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(workHire.getStatus())) {
			message = "�Ѿ������������ظ�������";
		}else {
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
			workHire.setPublishTime(Calendar.getInstance().getTime());
			workHireService.saveWorkHire(workHire);
			message = "�����ɹ�";
			
			//��¼�����¼
			String remark = getRequest().getParameter("remark");
			if(remark==null || "".equals(remark.trim())) {
				remark = "�����й���Ϣ";
			}
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"����",remark);
		}
		
		//������Ϣ��app
		try {
			String descri = workHire.getWorkKind() + workHire.getHireNum() + "λ��" + workHire.getSalary() + "��" + workHire.getWorkDescri();
			appPushService.sendAndroidBroadcast(descri,descri,descri,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return workHire.getEmpTypeId();
	}
	
	/**
	 * ���Υ���¼
	 * @return
	 */
	public String addBadRecordForWorkHire() {
		OrgUser currentUser = getLoginUser();
		String remark = getRequest().getParameter("remark");
		
		BadRecord br = new BadRecord();
		br.setBadUserId(workHire.getPublisherId());
		br.setDescri(remark);
		br.setRecordTime(Calendar.getInstance().getTime());
		br.setRecordUserId(currentUser.getId());
		br.setRecordUserName(currentUser.getUserName());
		br.setWorkSignId(workHire.getId());
		workHireService.saveBadRecord(br);
		
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		
		
		return workHire.getEmpTypeId();
	}
	
	/**
	 * ���Υ���¼
	 * @return
	 */
	public String addBadRecordForWorkHireByAjax() {
		OrgUser currentUser = getLoginUser();
		String remark = getRequest().getParameter("remark");
		String id = getRequest().getParameter("workHireId");
		workHire = workHireService.getWorkHire(id);
		
		BadRecord br = new BadRecord();
		br.setBadUserId(workHire.getPublisherId());
		br.setDescri(remark);
		br.setRecordTime(Calendar.getInstance().getTime());
		br.setRecordUserId(currentUser.getId());
		br.setRecordUserName(currentUser.getUserName());
		br.setWorkSignId(workHire.getId());
		workHireService.saveBadRecord(br);
		
		return viewWorkSigns();
	}
	
	public void validateWorkHireClose() {
		try {
			PrintWriter pw = this.getResponse().getWriter();
			if(workHireId==null || workHireId.equals("")) {
				pw.print("success");
			}
			int signCount = workHireService.getWorkSignNum(workHireId);
			if(signCount == 0) {
				pw.print("success");
			}
			pw.print("���й��˱������˹رղ���������ΪΥ����Ϊ��Υ���������Ͻ���ر�����ʹ��Ȩ�ޣ�ȷ�Ϲر���");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String closeWorkHireById() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
			if(workHire != null) {
				if(WorkHire.WORK_HIRE_STATUS_CLOSED.equals(workHire.getStatus())) {
					message = "�Ѿ��رգ������ظ��رգ�";
				}else {
					OrgUser currentUser = getLoginUser();
					if(currentUser.getId().equals(workHire.getPublisherId())) {
						permission = "write";
					}else {
						permission = "readonly";
					}
					if(permission == "readonly"){
						message = "����ʧ�ܣ�ֻ�ܹر��Լ��������й���Ϣ";
					}else {
						workHire.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
						workHire.setCloseTime(Calendar.getInstance().getTime());
						workHireService.saveWorkHire(workHire);
						message = "�رճɹ�";
						
						//��¼�����¼
						String remark = getRequest().getParameter("remark");
						if(remark==null || "".equals(remark.trim())) {
							remark = "�ر��й���Ϣ";
						}
						businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"�ر�",remark);
					}
				}
			}
		}
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return workHire.getEmpTypeId();
	}
	
	//�ر�
	public String closeWorkHire() {
		OrgUser currentUser = getLoginUser();
		//ȷ��ҳ���дȨ��
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "����ʧ�ܣ�ֻ�ܹر��Լ��������й���Ϣ";
			return SUCCESS;
		}
		
		if(WorkHire.WORK_HIRE_STATUS_CLOSED.equals(workHire.getStatus())) {
			message = "�Ѿ��رգ������ظ��رգ�";
		}else {
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
			workHire.setCloseTime(Calendar.getInstance().getTime());
			workHireService.saveWorkHire(workHire);
			message = "�رճɹ�";
			
			//��¼�����¼
			String remark = getRequest().getParameter("remark");
			if(remark==null || "".equals(remark.trim())) {
				remark = "�ر��й���Ϣ";
			}
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"�ر�",remark);
		}
		
		return workHire.getEmpTypeId();
	}
	
	//�鿴�����б�
	public String viewWorkSigns() {
		WorkHire w = workHireService.getWorkHire(workHireId);
		if(w==null) {
			permission = "readonly";
			return SUCCESS;
		}
		if(w.getPublisherId().equals(getLoginUser().getId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		workSignList = workHireService.getWorkSignList(workHireId);
		return SUCCESS;
	}
	
	//�鿴�������
	public String viewOpinions() {
		opinionList = businessOpinionService.getBusinessOpinionList(workHireId);
		return SUCCESS;
	}
	
	//�鿴Υ���¼
	public String viewBadRecords() {
		badRecordList = workHireService.getBadRecordList(workHireId);
		return SUCCESS;
	}
	
	//�鿴�����й������б�
	public String viewPublishingWorkHire(){
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(workHireQueryBo == null) {
			workHireQueryBo = new WorkHireQueryBo();
		}
		
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = getLoginUser();
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		workHireQueryBo.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
		workHireQueryBo.setNotSignUserId(loginUser.getId());
		workHireQueryBo.setPublisherCompanyId(company.getId());
		
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return SUCCESS;
	}
	
	public String viewWorkHireForSign() {
		if(workHireId==null || "".equals(workHireId)) {
			message = "����ʧ�ܣ���������(workHireIdΪ��)";
			return SUCCESS;
		}
		
		workHire = workHireService.getWorkHire(workHireId);
		if(workHire == null) {
			message = "����ʧ�ܣ��Ҳ����й���Ϣ��workHireId:" + workHireId + "��";
			return SUCCESS;
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			message = "����ʧ�ܣ��й���Ϣ���ڷ���״̬��status:" + workHire.getStatus() + "��";
			return SUCCESS;
		}
		
		//�ж��û��������Ĳ���
		OrgUser loginUser = getLoginUser();
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		if(!workHire.getPublisherCompanyId().equals(company.getId())){
			permission = "readonly";
		}else {
			WorkSign ws = workHireService.getWorkSign(workHireId, loginUser.getId());
			if(ws == null) {
				permission = "sign";
			}else if(ws != null){
				permission = "cancelSign";
			}
		}
		return SUCCESS;
	}
	
	//����
	public String workSign() {
		if(workHireId==null || "".equals(workHireId)) {
			message = "����ʧ�ܣ���������(workHireIdΪ��)";
			return viewPublishingWorkHire();
		}
		
		workHire = workHireService.getWorkHire(workHireId);
		if(workHire == null) {
			message = "����ʧ�ܣ��Ҳ����й���Ϣ��workHireId:" + workHireId + "��";
			return viewPublishingWorkHire();
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			message = "����ʧ�ܣ��й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��";
			return viewPublishingWorkHire();
		}
		
		OrgUser currentUser = getLoginUser();
		OrgDept company = orgDeptService.getDirectCompany(currentUser.getDeptId());
		if(!workHire.getPublisherCompanyId().equals(company.getId())){
			permission = "readonly";
			message = "����ʧ�ܣ�ֻ�ܱ��������е��й���Ϣ���й����У�" + workHire.getPublisherCompanyName() + "�������ڵ��У�" + company.getDeptName();
			return viewWorkHireForSign();
		}else {
			permission = "write";
		}

		//��֤�Ƿ��ѱ���
		WorkSign ws = workHireService.getWorkSign(workHireId, currentUser.getId());
		if(ws != null) {
			message = "�Ѿ������������ظ�������";
			return viewWorkHireForSign();
		}
		
		//��֤�Ƿ񳬳���������
		int planSignNum = workHire.getHireNum();
		int acturalNum = workHireService.getWorkSignNum(workHire.getId());
		
		if(acturalNum >= planSignNum) {
			message = "��������";
			return viewWorkHireForSign();
		}
		
		ws = new WorkSign();
		ws.setWorkHireId(workHireId);
		ws.setEmpId(currentUser.getId());
		ws.setSignTime(Calendar.getInstance().getTime());
		ws.setValidStatus("1");
		workHireService.saveWorkSign(ws);
		message = "�����ɹ���";
		
		//������Ϣ
		Message m = new Message();
		m.setCreateTime(Calendar.getInstance().getTime());
		m.setIsRead("0");//δ��
		m.setMessageContent(currentUser.getUserName() + "�������й����ţ�" + workHire.getBusinessNumber() + "���й����飺" + workHire.getWorkDescri() + "");
		m.setMessageTitle(currentUser.getUserName() + "������" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//��¼�����¼
		businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"����","�����ɹ�");
		return viewWorkHireForSign();
	}
	
	//����
	public String workSignById() {
		workSign();
		return viewPublishingWorkHire();
	}
	
	//ȡ������
	public String cancelWorkSign() {
		if(workSignId==null || "".equals(workSignId)) {
			message = "����ʧ�ܣ���������(workSignIdΪ��)";
			return viewPublishingWorkHire();
		}
		
		WorkSign ws = workHireService.getWorkSign(workSignId);
		if(ws == null) {
			message = "����ʧ�ܣ��Ҳ���������Ϣ��workSignId:" + workSignId + "��";
			return viewPublishingWorkHire();
		}
		
		if("0".equals(ws.getValidStatus())) {
			message = "����ʧ�ܣ������Ѿ�ȡ���������ظ�ȡ��";
			return viewPublishingWorkHire();
		}
		
		workHire = workHireService.getWorkHire(ws.getWorkHireId());
		if(workHire == null) {
			message = "����ʧ�ܣ��Ҳ����й���Ϣ��workHireId:" + ws.getWorkHireId() + "��";
			return viewPublishingWorkHire();
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			message = "����ʧ�ܣ��й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��";
			return viewPublishingWorkHire();
		}
		
		OrgUser loginUser = getLoginUser();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ws.setValidStatus("0");
		ws.setRemark(loginUser.getLoginName() + "����ȡ������ " + sdf.format(Calendar.getInstance().getTime()));
		workHireService.saveWorkSign(ws);
		message = "�����Ѿ�ȡ����";
		
		//������Ϣ
		Message m = new Message();
		m.setCreateTime(Calendar.getInstance().getTime());
		m.setIsRead("0");//δ��
		m.setMessageContent(loginUser.getUserName() + "ȡ���������й����ţ�" + workHire.getBusinessNumber() + "���й����飺" + workHire.getWorkDescri() + "����֪����");
		m.setMessageTitle(loginUser.getUserName() + "ȡ��������" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//��¼�����¼
		businessOpinionService.saveBusinessOpinion(workHire.getId(),loginUser,"ȡ������","ȡ������");
		return SUCCESS;
	}
	
	public String cancelWorkSignById() {
		cancelWorkSign();
		return viewPublishingWorkHire();
	}
	
	//�ҵĹ���
	public String myWorkSign() {
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(workHireQueryBo == null) {
			workHireQueryBo = new WorkHireQueryBo();
		}
		
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = getLoginUser();
		workHireQueryBo.setSignUserId(loginUser.getId());
		
		page = workHireService.getWorkSignList(workHireQueryBo, page);
		workSignList = page.getResult();
		return SUCCESS;
	}
	
	//�ط�
	public String viewWorkHireVisit() {
		
		OrgUser currentUser = getLoginUser();
		if(workHireVisitId!=null && !workHireVisitId.equals("")) {
			workHireVisit = workHireService.getWorkHireVisit(workHireVisitId);
		}else {
			workHireVisit = new WorkHireVisit();
			workHireVisit.setVisitTime(Calendar.getInstance().getTime());
			workHireVisit.setVisitUser(currentUser.getId());
			workHireVisit.setWorkHireId(workHireId);
		}
		
		//ȷ��ҳ���дȨ��
		workHire = workHireService.getWorkHire(workHireId);
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		return SUCCESS;
	}
	
	//����ط�
	public String saveWorkHireVisit() {
		
		workHireService.saveWorkHireVisit(workHireVisit);
		businessOpinionService.saveBusinessOpinion(workHireVisit.getWorkHireId(),getLoginUser(),"�ط�",workHireVisit.getVisitRecord());
		message = "����ɹ�";
		return SUCCESS;
	}
	
	//�й�ҳ����ʾ�ط��б�
	public String viewVisits() {
		visitList = workHireService.getWorkHireVisitBoList(workHireId);
		return SUCCESS;
	}
	
	//����Ӷ����ù�ҳ��
	public String forwardToSecondEmp() {
		workHireId = getRequest().getParameter("workHireId");
		return SUCCESS;
	}
	
	// �����ù�ѡ����
//	public String selectUsersForSecondEmp() {
//		WorkHire wh = workHireService.getWorkHire(workHireId);
//
//		//�Ȳ�ѯ��������
//		List<OrgDept> depts = orgDeptService.queryDepts(null, wh.getPublisherCompanyId());
//		List<OrgUserBo> users = orgUserService.queryUsers(null, null,wh.getPublisherCompanyId(), true);
//
//		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
//		treeNodeData = "";
//
//		// �ȷ��벿�Žڵ�
//		List<String> checkIdList = new ArrayList<String>();
//		if (depts != null && depts.size() > 0) {
//			for (int i = 0; i < depts.size(); i++) {
//				OrgDept d = depts.get(i);
//				TreeNode tnDept = new TreeNode();
//				tnDept.setId(d.getId());
//				tnDept.setName(d.getDeptName());
//				tnDept.setpId(d.getParentId());
//				tnDept.setParent(true);
//				treeNodes.add(tnDept);
//			}
//		}
//
//		// �ٷ����û��ڵ�
//		if (checkedIds != null && !"".equals(checkedIds)) {
//			String[] s = checkedIds.split(",");
//			Collections.addAll(checkIdList, s);
//		}
//		if (users != null && users.size() > 0) {
//			for (int i = 0; i < users.size(); i++) {
//				OrgUserBo user = users.get(i);
//				TreeNode tn = new TreeNode();
//				tn.setId(user.getId());
//				tn.setName(user.getUserName());
//				tn.setpId(user.getDept().getId());
//				if (checkIdList.contains(user.getId())) {
//					tn.setChecked(true);
//				}
//				treeNodes.add(tn);
//			}
//			JSONArray jsonArray = JSONArray.fromObject(treeNodes);
//			treeNodeData = jsonArray.toString();
//		}
//		return SUCCESS;
//	}
	
	
	
	//�򿪹��˵���ҳ��
	public String forwardToReplaceEmp() {
		workSignId = getRequest().getParameter("workSignId");
		return SUCCESS;
	}

}
