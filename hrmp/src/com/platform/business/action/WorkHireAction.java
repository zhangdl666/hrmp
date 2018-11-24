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
	
	//页面读写权限控制
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

	//查看
	public String viewWorkHire() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
		}else {
			//新建
			HttpServletRequest req = ServletActionContext.getRequest();
			OrgUser loginUser = (OrgUser)req.getSession().getAttribute("loginUser");
			OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
			String empTypeId = req.getParameter("empTypeId");
			workHire = new WorkHire();
//			String businessNumber = businessNumberService.getNumber("W");
//			workHire.setBusinessNumber(businessNumber);
			workHire.setCreateTime(Calendar.getInstance().getTime());
			workHire.setPublisherId(loginUser.getId());
			workHire.setPublisherName(loginUser.getUserName() + "（" + loginUser.getLoginName() + "）");
			workHire.setPublisherCompanyId(company.getId());
			workHire.setPublisherCompanyName(company.getDeptName());
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_NOPUBLISH);//草稿状态
			workHire.setEmpTypeId(empTypeId);
			workHire.setSex("male");
			workHire.setSalaryRemark("BGF");
			workHire.setPayMode("true");
		}
		
		//确定页面读写权限
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		return workHire.getEmpTypeId();
	}
	
	//保存
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
		
		//确定页面读写权限
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "操作失败，只能编辑自己创建的招工信息";
			return SUCCESS;
		}
		
		//当招工类型为长期工、临时工时，验证工资、招工人数是否符合修改要求
		if(workHire.getId()!=null && ("LS".equals(workHire.getEmpTypeId()) || "CQ".equals(workHire.getEmpTypeId()))){
			WorkHire wh = workHireService.getWorkHire(workHire.getId());
			int oldHireNum = wh.getHireNum();
			int oldSalary = Integer.valueOf(wh.getSalary());
			int nowHireNum = workHire.getHireNum();
			int nowSalary = Integer.valueOf(workHire.getSalary());
			if(nowHireNum<oldHireNum || nowSalary < oldSalary) {
				int signCount = workHireService.getWorkSignNum(workHire.getId());
				if(nowSalary < oldSalary){
					message = "操作失败，已有工人报名，工资不允许下调";
					return workHire.getEmpTypeId();
				}
				if(nowHireNum < signCount) {
					message = "操作失败，已有工人报名，招工人数修改不允许低于已报名人数";
					return workHire.getEmpTypeId();
				}
			}
		}
		
		workHireService.saveWorkHire(workHire);
		//记录办理记录
		if(isNew) {
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"新建","新建招工信息");
		}else{
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"编辑","编辑招工信息");
		}
		
		message = "保存成功";
		
		return workHire.getEmpTypeId();
	}
	
	public String queryWorkHire() {
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(workHireQueryBo == null) {
			workHireQueryBo = new WorkHireQueryBo();
		}
		//设置查询条件，用于数据隔离
		OrgUser loginUser = getLoginUser();
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		workHireQueryBo.setPublisherCompanyId(company.getId());
		
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return SUCCESS;
	}
		
	//删除
	public String deleteWorkHireById() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
			if(workHire != null) {
				if(WorkHire.WORK_HIRE_STATUS_DELETE.equals(workHire.getStatus())) {
					message = "已经删除，请勿重复删除！";
				}else {
					OrgUser currentUser = getLoginUser();
					if(currentUser.getId().equals(workHire.getPublisherId())) {
						permission = "write";
					}else {
						permission = "readonly";
					}
					if(permission == "readonly"){
						message = "操作失败，只能删除自己创建的招工信息";
					}else {
						workHire.setStatus(WorkHire.WORK_HIRE_STATUS_DELETE);
						workHire.setCloseTime(Calendar.getInstance().getTime());
						workHireService.saveWorkHire(workHire);
						message = "删除成功";
						
						//记录办理记录
						String remark = getRequest().getParameter("remark");
						if(remark==null || "".equals(remark.trim())) {
							remark = "删除招工信息";
						}
						businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"删除",remark);
					}
				}
			}
		}
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return workHire.getEmpTypeId();
	}
	
	//删除
	public String deleteWorkHire() {
		OrgUser currentUser = getLoginUser();
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "操作失败，只能删除自己创建的招工信息";
			return SUCCESS;
		}
		
		if(WorkHire.WORK_HIRE_STATUS_DELETE.equals(workHire.getStatus())) {
			message = "已经删除，请勿重复删除！";
		}else {
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_DELETE);
			workHire.setCloseTime(Calendar.getInstance().getTime());
			workHireService.saveWorkHire(workHire);
			message = "删除成功";
			
			//记录办理记录
			String remark = getRequest().getParameter("remark");
			if(remark==null || "".equals(remark.trim())) {
				remark = "删除招工信息";
			}
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"删除",remark);		
		}
		
		return workHire.getEmpTypeId();
	}
		
	public String publishWorkHireById() {
		if(workHireId!=null && !"".equals(workHireId)) {
			workHire = workHireService.getWorkHire(workHireId);
			if(workHire != null) {
				if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(workHire.getStatus())) {
					message = "已经发布，请勿重复发布！";
				}else {
					OrgUser currentUser = getLoginUser();
					if(currentUser.getId().equals(workHire.getPublisherId())) {
						permission = "write";
					}else {
						permission = "readonly";
					}
					if(permission == "readonly"){
						message = "操作失败，只能发布自己创建的招工信息";
					}else {
						workHire.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
						workHire.setPublishTime(Calendar.getInstance().getTime());
						workHireService.saveWorkHire(workHire);
						message = "发布成功";
						
						//记录办理记录
						String remark = getRequest().getParameter("remark");
						if(remark==null || "".equals(remark.trim())) {
							remark = "发布招工信息";
						}
						businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"发布",remark);
					}
				}
			}
		}
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		
		//推送消息至app
		try {
			String descri = workHire.getWorkKind() + workHire.getHireNum() + "位，" + workHire.getSalary() + "，" + workHire.getWorkDescri();
			appPushService.sendAndroidBroadcast(descri,descri,descri,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return workHire.getEmpTypeId();
	}
	
	//发布
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
			message = "操作失败，只能发布自己创建的招工信息";
			return SUCCESS;
		}
		
		if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(workHire.getStatus())) {
			message = "已经发布，请勿重复发布！";
		}else {
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
			workHire.setPublishTime(Calendar.getInstance().getTime());
			workHireService.saveWorkHire(workHire);
			message = "发布成功";
			
			//记录办理记录
			String remark = getRequest().getParameter("remark");
			if(remark==null || "".equals(remark.trim())) {
				remark = "发布招工信息";
			}
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"发布",remark);
		}
		
		//推送消息至app
		try {
			String descri = workHire.getWorkKind() + workHire.getHireNum() + "位，" + workHire.getSalary() + "，" + workHire.getWorkDescri();
			appPushService.sendAndroidBroadcast(descri,descri,descri,null,null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return workHire.getEmpTypeId();
	}
	
	/**
	 * 添加违规记录
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
	 * 添加违规记录
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
			pw.print("已有工人报名，此关闭操作将被视为违规行为，违规两次以上将会关闭您的使用权限，确认关闭吗？");
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
					message = "已经关闭，请勿重复关闭！";
				}else {
					OrgUser currentUser = getLoginUser();
					if(currentUser.getId().equals(workHire.getPublisherId())) {
						permission = "write";
					}else {
						permission = "readonly";
					}
					if(permission == "readonly"){
						message = "操作失败，只能关闭自己创建的招工信息";
					}else {
						workHire.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
						workHire.setCloseTime(Calendar.getInstance().getTime());
						workHireService.saveWorkHire(workHire);
						message = "关闭成功";
						
						//记录办理记录
						String remark = getRequest().getParameter("remark");
						if(remark==null || "".equals(remark.trim())) {
							remark = "关闭招工信息";
						}
						businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"关闭",remark);
					}
				}
			}
		}
		page = workHireService.getWorkHireList(workHireQueryBo, page);
		workHireList = page.getResult();
		return workHire.getEmpTypeId();
	}
	
	//关闭
	public String closeWorkHire() {
		OrgUser currentUser = getLoginUser();
		//确定页面读写权限
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		if(permission == "readonly"){
			message = "操作失败，只能关闭自己创建的招工信息";
			return SUCCESS;
		}
		
		if(WorkHire.WORK_HIRE_STATUS_CLOSED.equals(workHire.getStatus())) {
			message = "已经关闭，请勿重复关闭！";
		}else {
			workHire.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
			workHire.setCloseTime(Calendar.getInstance().getTime());
			workHireService.saveWorkHire(workHire);
			message = "关闭成功";
			
			//记录办理记录
			String remark = getRequest().getParameter("remark");
			if(remark==null || "".equals(remark.trim())) {
				remark = "关闭招工信息";
			}
			businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"关闭",remark);
		}
		
		return workHire.getEmpTypeId();
	}
	
	//查看报名列表
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
	
	//查看办理过程
	public String viewOpinions() {
		opinionList = businessOpinionService.getBusinessOpinionList(workHireId);
		return SUCCESS;
	}
	
	//查看违规记录
	public String viewBadRecords() {
		badRecordList = workHireService.getBadRecordList(workHireId);
		return SUCCESS;
	}
	
	//查看正在招工工作列表
	public String viewPublishingWorkHire(){
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(workHireQueryBo == null) {
			workHireQueryBo = new WorkHireQueryBo();
		}
		
		//设置查询条件，用于数据隔离
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
			message = "操作失败，参数有误(workHireId为空)";
			return SUCCESS;
		}
		
		workHire = workHireService.getWorkHire(workHireId);
		if(workHire == null) {
			message = "操作失败，找不到招工信息（workHireId:" + workHireId + "）";
			return SUCCESS;
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			message = "操作失败，招工信息不在发布状态（status:" + workHire.getStatus() + "）";
			return SUCCESS;
		}
		
		//判断用户可以做的操作
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
	
	//报名
	public String workSign() {
		if(workHireId==null || "".equals(workHireId)) {
			message = "操作失败，参数有误(workHireId为空)";
			return viewPublishingWorkHire();
		}
		
		workHire = workHireService.getWorkHire(workHireId);
		if(workHire == null) {
			message = "操作失败，找不到招工信息（workHireId:" + workHireId + "）";
			return viewPublishingWorkHire();
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			message = "操作失败，招工信息不处于发布状态（status:" + workHire.getStatus() + "）";
			return viewPublishingWorkHire();
		}
		
		OrgUser currentUser = getLoginUser();
		OrgDept company = orgDeptService.getDirectCompany(currentUser.getDeptId());
		if(!workHire.getPublisherCompanyId().equals(company.getId())){
			permission = "readonly";
			message = "操作失败，只能报名本地市的招工信息，招工地市：" + workHire.getPublisherCompanyName() + "，您所在地市：" + company.getDeptName();
			return viewWorkHireForSign();
		}else {
			permission = "write";
		}

		//验证是否已报名
		WorkSign ws = workHireService.getWorkSign(workHireId, currentUser.getId());
		if(ws != null) {
			message = "已经报名，请勿重复报名！";
			return viewWorkHireForSign();
		}
		
		//验证是否超出报名上限
		int planSignNum = workHire.getHireNum();
		int acturalNum = workHireService.getWorkSignNum(workHire.getId());
		
		if(acturalNum >= planSignNum) {
			message = "报名已满";
			return viewWorkHireForSign();
		}
		
		ws = new WorkSign();
		ws.setWorkHireId(workHireId);
		ws.setEmpId(currentUser.getId());
		ws.setSignTime(Calendar.getInstance().getTime());
		ws.setValidStatus("1");
		workHireService.saveWorkSign(ws);
		message = "报名成功！";
		
		//发送消息
		Message m = new Message();
		m.setCreateTime(Calendar.getInstance().getTime());
		m.setIsRead("0");//未读
		m.setMessageContent(currentUser.getUserName() + "报名，招工单号：" + workHire.getBusinessNumber() + "，招工详情：" + workHire.getWorkDescri() + "");
		m.setMessageTitle(currentUser.getUserName() + "报名：" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//记录办理记录
		businessOpinionService.saveBusinessOpinion(workHire.getId(),currentUser,"报名","报名成功");
		return viewWorkHireForSign();
	}
	
	//报名
	public String workSignById() {
		workSign();
		return viewPublishingWorkHire();
	}
	
	//取消报名
	public String cancelWorkSign() {
		if(workSignId==null || "".equals(workSignId)) {
			message = "操作失败，参数有误(workSignId为空)";
			return viewPublishingWorkHire();
		}
		
		WorkSign ws = workHireService.getWorkSign(workSignId);
		if(ws == null) {
			message = "操作失败，找不到报名信息（workSignId:" + workSignId + "）";
			return viewPublishingWorkHire();
		}
		
		if("0".equals(ws.getValidStatus())) {
			message = "操作失败，报名已经取消，请勿重复取消";
			return viewPublishingWorkHire();
		}
		
		workHire = workHireService.getWorkHire(ws.getWorkHireId());
		if(workHire == null) {
			message = "操作失败，找不到招工信息（workHireId:" + ws.getWorkHireId() + "）";
			return viewPublishingWorkHire();
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			message = "操作失败，招工信息不处于发布状态（status:" + workHire.getStatus() + "）";
			return viewPublishingWorkHire();
		}
		
		OrgUser loginUser = getLoginUser();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ws.setValidStatus("0");
		ws.setRemark(loginUser.getLoginName() + "主动取消报名 " + sdf.format(Calendar.getInstance().getTime()));
		workHireService.saveWorkSign(ws);
		message = "报名已经取消！";
		
		//发送消息
		Message m = new Message();
		m.setCreateTime(Calendar.getInstance().getTime());
		m.setIsRead("0");//未读
		m.setMessageContent(loginUser.getUserName() + "取消报名，招工单号：" + workHire.getBusinessNumber() + "，招工详情：" + workHire.getWorkDescri() + "，请知晓！");
		m.setMessageTitle(loginUser.getUserName() + "取消报名：" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//记录办理记录
		businessOpinionService.saveBusinessOpinion(workHire.getId(),loginUser,"取消报名","取消报名");
		return SUCCESS;
	}
	
	public String cancelWorkSignById() {
		cancelWorkSign();
		return viewPublishingWorkHire();
	}
	
	//我的工作
	public String myWorkSign() {
		if(page == null) {
			page = Page.getDefaultPage();
		}
		if(workHireQueryBo == null) {
			workHireQueryBo = new WorkHireQueryBo();
		}
		
		//设置查询条件，用于数据隔离
		OrgUser loginUser = getLoginUser();
		workHireQueryBo.setSignUserId(loginUser.getId());
		
		page = workHireService.getWorkSignList(workHireQueryBo, page);
		workSignList = page.getResult();
		return SUCCESS;
	}
	
	//回访
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
		
		//确定页面读写权限
		workHire = workHireService.getWorkHire(workHireId);
		if(currentUser.getId().equals(workHire.getPublisherId())) {
			permission = "write";
		}else {
			permission = "readonly";
		}
		return SUCCESS;
	}
	
	//保存回访
	public String saveWorkHireVisit() {
		
		workHireService.saveWorkHireVisit(workHireVisit);
		businessOpinionService.saveBusinessOpinion(workHireVisit.getWorkHireId(),getLoginUser(),"回访",workHireVisit.getVisitRecord());
		message = "保存成功";
		return SUCCESS;
	}
	
	//招工页面显示回访列表
	public String viewVisits() {
		visitList = workHireService.getWorkHireVisitBoList(workHireId);
		return SUCCESS;
	}
	
	//打开添加二次用工页面
	public String forwardToSecondEmp() {
		workHireId = getRequest().getParameter("workHireId");
		return SUCCESS;
	}
	
	// 二次用工选择工人
//	public String selectUsersForSecondEmp() {
//		WorkHire wh = workHireService.getWorkHire(workHireId);
//
//		//先查询部门数据
//		List<OrgDept> depts = orgDeptService.queryDepts(null, wh.getPublisherCompanyId());
//		List<OrgUserBo> users = orgUserService.queryUsers(null, null,wh.getPublisherCompanyId(), true);
//
//		List<TreeNode> treeNodes = new ArrayList<TreeNode>();
//		treeNodeData = "";
//
//		// 先放入部门节点
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
//		// 再放入用户节点
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
	
	
	
	//打开工人调换页面
	public String forwardToReplaceEmp() {
		workSignId = getRequest().getParameter("workSignId");
		return SUCCESS;
	}

}
