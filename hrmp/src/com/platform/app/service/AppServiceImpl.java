package com.platform.app.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.platform.app.service.xmlpo.AndroidVersion;
import com.platform.app.service.xmlpo.Msg;
import com.platform.app.service.xmlpo.ReqDetail;
import com.platform.app.service.xmlpo.ReqMsg;
import com.platform.app.service.xmlpo.RspDetail;
import com.platform.app.service.xmlpo.RspMsg;
import com.platform.app.service.xmlpo.SignEmp;
import com.platform.app.service.xmlpo.Work;
import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.Message;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkSign;
import com.platform.business.service.BusinessMessageService;
import com.platform.business.service.BusinessOpinionService;
import com.platform.business.service.WorkHireService;
import com.platform.core.bo.Page;
import com.platform.core.pojo.SysConfig;
import com.platform.core.service.SysConfigService;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.service.OrgDeptService;
import com.platform.organization.service.OrgUserService;
import com.platform.security.util.Encrypts;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AppServiceImpl implements AppService{
	private final Logger logger = Logger.getLogger(AppServiceImpl.class);
	
	private OrgUserService orgUserService;
	private OrgDeptService orgDeptService;
	private WorkHireService workHireService;
	private BusinessMessageService businessMessageService;
	private BusinessOpinionService businessOpinionService;
	private SysConfigService sysConfigService;
	
	private int unitPrice = 5;
	@Override
	public String appLogin(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��¼>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//�����µ�ƾ֤��
		String newIdentifyCode = loginName + ":" + l;
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��¼>>---------operaterֵ������Ϊ��");
			return error(loginName, newIdentifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��¼>>---------reqDetail�ڵ㲻����");
			return error(loginName, newIdentifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String pwd = reqDetail.getPassword();
		if(pwd == null || "".equals(pwd)) {
			logger.info(l + " ��¼>>---------passwordֵ������Ϊ��");
			return error(loginName, newIdentifyCode, l,"4000", "���벻����Ϊ��");
		}
		
		OrgUser user = orgUserService.getUserByLoginName(loginName);
		if(user == null) {
			logger.info(l + " ��¼>>---------�û������ڣ�����operaterֵ��Ч�ԣ�operater��" + loginName);
			return error(loginName, newIdentifyCode, l,"4002", "�û�������");
		}
		
		//��¼��֤
		String enPassword = user.getPwd();
		String password = Encrypts.decryptPassword(enPassword);
		if(!password.equals(pwd)) {
			logger.info(l + " ��¼>>---------�������");
			return error(loginName, newIdentifyCode, l,"4001", "�������");
		}
		
		//ƴ�ӷ��ر���
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(newIdentifyCode);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("��¼�ɹ�");
		
		RspDetail rspDetail = new RspDetail();
		rspDetail.setUserId(user.getId());
		rspDetail.setUserLoginName(user.getLoginName());
		rspDetail.setUserName(user.getUserName());
		
		rspMsg.setRspDetail(rspDetail);

		XStream xStream = new XStream();
		xStream.alias("rspMsg", RspMsg.class);
		xStream.alias("rspDetail", RspDetail.class);
		String result = xStream.toXML(rspMsg);
		logger.info(l + " ��¼>>���ر���---------" + result);
		return result;
	}
	
	private String error(String loginName,String newIdentifyCode,long l,String resResult,String descri){
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(newIdentifyCode);
		rspMsg.setRspResult(resResult);
		rspMsg.setRspDesc(descri);

		XStream xStream = new XStream();
		xStream.alias("rspMsg", RspMsg.class);
		xStream.alias("rspDetail", RspDetail.class);
		String result = xStream.toXML(rspMsg);
		logger.info(l + " ���ر���---------" + result);
		return result;
	}

	@Override
	public String getWorkList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �й��б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �й��б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �й��б�>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		WorkHireQueryBo workHireQueryBo = new WorkHireQueryBo();
		
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		workHireQueryBo.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
		workHireQueryBo.setNotSignUserId(loginUser.getId());
		workHireQueryBo.setPublisherCompanyId(company.getId());
		
		List<Object[]> workKindList = workHireService.getWorkKindList(workHireQueryBo);
		
		List<Work> workList = new ArrayList<Work>();
		if(workKindList!=null && workKindList.size()>0) {
			for(int i=0;i<workKindList.size();i++) {
				Object[] obj = workKindList.get(i);
				Work work = new Work();
				work.setWorkKind((String)obj[0]);
				work.setHireNum(((Long)obj[1]).intValue());
				workList.add(work);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWorkList(workList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getWorkDetailList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �й������б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �й������б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �й������б�>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workKind = reqDetail.getWorkKind();
		if(workKind == null || "".equals(workKind)) {
			logger.info(l + " �й������б�>>---------workKindֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		//���ò�ѯ�������������ݸ���
		WorkHireQueryBo workHireQueryBo = new WorkHireQueryBo();
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		workHireQueryBo.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
		workHireQueryBo.setNotSignUserId(loginUser.getId());
		workHireQueryBo.setPublisherCompanyId(company.getId());
		workHireQueryBo.setWorkKind(workKind);
		
		Page p = workHireService.getWorkHireList(workHireQueryBo, page);
		List<WorkHireView> list = p.getResult();
		
		List<Work> workList = new ArrayList<Work>();
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				WorkHireView v = list.get(i);
				Work work = new Work();
				work.setId(v.getId());
				work.setUnitPrice(5);
				String descri = v.getWorkKind() + v.getHireNum() + "λ��" + v.getSalary() + "��" + v.getWorkDescri();
				work.setWorkDescri(descri);
				//�ж��Ƿ����ȡ������
				if(!v.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
					work.setCanCancelSign("0");
				}else {
					OrgUser u = orgUserService.getUserByLoginName(loginName);
					WorkSign ws = workHireService.getWorkSign(v.getId(), u.getId());
					if(ws == null){
						work.setCanCancelSign("0");
					}else {
						work.setCanCancelSign("1");
					}
				}
				workList.add(work);
			}
		}
		
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWorkList(workList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}

	@Override
	public String getWorkInfoById(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �й�������Ϣ>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �й�������Ϣ>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �й�������Ϣ>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " �й�������Ϣ>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��й���Ϣ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		Work work = new Work();
		work.setId(wh.getId());
		work.setBusinessNumber(wh.getBusinessNumber());
		work.setPublisherName(wh.getPublisherName());
		work.setPublisherCompanyName(wh.getPublisherCompanyName());
		work.setCreateTime(wh.getCreateTime()==null?"":sdf.format(wh.getCreateTime()));
		work.setPublishTime(wh.getPublishTime()==null?"":sdf.format(wh.getPublishTime()));
		work.setCloseTime(wh.getCloseTime()==null?"":sdf.format(wh.getCloseTime()));
		work.setWorkDescri(wh.getWorkDescri());
		work.setWorkKind(wh.getWorkKind());
		work.setHireNum(wh.getHireNum());
		work.setStatus(wh.getStatus());
		work.setUnitPrice(5);
		
		//�ж��Ƿ����ȡ������
		if(!wh.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			work.setCanCancelSign("0");
		}else {
			OrgUser u = orgUserService.getUserByLoginName(loginName);
			WorkSign ws = workHireService.getWorkSign(workId, u.getId());
			if(ws != null){
				work.setCanCancelSign("0");
			}else {
				work.setCanCancelSign("1");
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWork(work);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}

	public String outputMarshal(RspMsg response) {
		XStream stream = initXStream();
		String responseMsg = stream.toXML(response);
		logger.info(responseMsg);
		return responseMsg;
	}
	
	public static XStream initXStream(){
		XStream stream = new XStream(new DomDriver());
		stream.alias("reqMsg", ReqMsg.class);
		stream.alias("reqDetail", ReqDetail.class);
		stream.alias("rspMsg", RspMsg.class);
		stream.alias("rspDetail", RspDetail.class);
		stream.alias("work", Work.class);
		stream.alias("signEmp", SignEmp.class);
		stream.alias("msg", Msg.class);
		stream.alias("androidVersion", AndroidVersion.class);
		return stream;
	}

	@Override
	public String getSignEmpList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ���������б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ���������б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ���������б�>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " ���������б�>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		
		List<WorkSignBo> signs = workHireService.getWorkSignList(workId);
		List<SignEmp> empList = new ArrayList<SignEmp>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(signs!=null && signs.size()>0) {
			for(int i=0;i<signs.size();i++) {
				WorkSignBo bo = signs.get(i);
				SignEmp emp = new SignEmp();
				emp.setSignTime(bo.getWorkSign().getSignTime()==null?"":sdf.format(bo.getWorkSign().getSignTime()));
				emp.setEmpName(bo.getEmp().getUserName());
				emp.setNum(bo.getWorkSign().getNum() + "");
				empList.add(emp);
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setSignEmpList(empList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String sign(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ����>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ����>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ����>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		SignEmp sign = reqDetail.getSignEmp();
		if(sign == null) {
			logger.info(l + " ����>>---- -----signEmp�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "signEmp�ڵ㲻����");
		}
		
		WorkHire workHire = workHireService.getWorkHire(sign.getWorkId());
		if(workHire == null) {
			logger.info(l + " ����>>---- -----�Ҳ����й���Ϣ��workHireId:" + sign.getWorkId() + "��");
			return error(loginName, identifyCode, l,"4000", "�Ҳ����й���Ϣ��workHireId:" + sign.getWorkId() + "��");
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " ����>>---- -----�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
			return error(loginName, identifyCode, l,"4000", "�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
		}
		
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(emp.getDeptId());
		if(!workHire.getPublisherCompanyId().equals(company.getId())){
			logger.info(l + " ����>>---- -----ֻ�ܱ��������е��й���Ϣ���й����У�" + workHire.getPublisherCompanyName() + "�������ڵ��У�" + company.getDeptName());
			return error(loginName, identifyCode, l,"4000", "ֻ�ܱ��������е��й���Ϣ���й����У�" + workHire.getPublisherCompanyName() + "�������ڵ��У�" + company.getDeptName());
		}

		//��֤�Ƿ��ѱ���
		WorkSign ws = workHireService.getWorkSign(sign.getWorkId(), emp.getId());
		if(ws != null) {
			logger.info(l + " ����>>---- -----�Ѿ������������ظ�������");
			return error(loginName, identifyCode, l,"4000", "�Ѿ������������ظ�������");
		}
		
		//���������û������Ʊ���
		String userKind = emp.getUserKind();
		if(OrgUser.USER_KIND_BADCREDIT.equals(userKind)) {
			logger.info(l + " ����>>---- -----�������Ʊ������뵽��˾���¿�ͨ");
			return error(loginName, identifyCode, l,"4000", "�������Ʊ������뵽��˾���¿�ͨ");
		}
		
		//��֤�Ƿ񳬳���������
		int planSignNum = workHire.getHireNum();
		int acturalNum = workHireService.getWorkSignNum(sign.getWorkId());
		
		if(acturalNum >= planSignNum) {
			logger.info(l + " ����>>---- -----��������");
			return error(loginName, identifyCode, l,"4000", "��������");
		}
		
		int remainNum = planSignNum - acturalNum;
		if(Integer.valueOf(sign.getNum()) > remainNum) {
			logger.info(l + " ����>>---- -----���������ѳ����й���������ǰʣ�����" + remainNum + "��");
			return error(loginName, identifyCode, l,"4000", "���������ѳ����й���������ǰʣ�����" + remainNum + "��");
		}
		
		ws = new WorkSign();
		ws.setWorkHireId(sign.getWorkId());
		ws.setEmpId(emp.getId());
		ws.setNum(Integer.valueOf(sign.getNum()));
		ws.setUnitPrice(Double.valueOf(sign.getUnitPrice()));
		ws.setTotalMoney(Double.valueOf(sign.getTotalMoney()));
		ws.setSignTime(calendar.getTime());
		ws.setConfirmResult(WorkSign.COMFIRM_RESULT_PASS);
		ws.setConfirmTime(calendar.getTime());
		workHireService.saveWorkSign(ws);
		
		//������Ϣ
		Message m = new Message();
		m.setCreateTime(calendar.getTime());
		m.setIsRead("0");//δ��
		m.setMessageContent(emp.getUserName() + "�������й����ţ�" + workHire.getBusinessNumber() + "��" + workHire.getWorkKind() + workHire.getSalary() + workHire.getWorkDescri());
		m.setMessageTitle(emp.getUserName() + "������" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//��¼�����¼
		businessOpinionService.saveBusinessOpinion(workHire.getId(),emp,"����","�����ɹ�");
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String cancelSign(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ȡ������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ȡ������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ȡ������>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workId, emp.getId());
		if(ws == null) {
			logger.info(l + " ȡ������>>---- -----δ�ҵ�������Ϣ");
			return error(loginName, identifyCode, l,"4000", "δ�ҵ�������Ϣ");
		}
		
		if(WorkSign.COMFIRM_RESULT_CANCEL.equals(ws.getConfirmResult())) {
			logger.info(l + " ȡ������>>---- -----�����Ѿ�ȡ���������ظ�ȡ��");
			return error(loginName, identifyCode, l,"4000", "�����Ѿ�ȡ���������ظ�ȡ��");
		}
		
		WorkHire workHire = workHireService.getWorkHire(workId);
		if(workHire == null) {
			logger.info(l + " ȡ������>>---- -----δ�ҵ��й���Ϣ���������workId�Ƿ�����workId��" + workId);
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��й���Ϣ���������workId�Ƿ�����workId��" + workId);
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " ȡ������>>---- -----�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
			return error(loginName, identifyCode, l,"4000", "�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ws.setConfirmResult(WorkSign.COMFIRM_RESULT_CANCEL);
		ws.setConfirmTime(Calendar.getInstance().getTime());
		ws.setConfirmDescri(loginName + "����ȡ������ " + sdf.format(calendar.getTime()));
		workHireService.saveWorkSign(ws);
		
		//������Ϣ
		Message m = new Message();
		m.setCreateTime(Calendar.getInstance().getTime());
		m.setIsRead("0");//δ��
		m.setMessageContent(loginName + "ȡ���������й����ţ�" + workHire.getBusinessNumber() + "��" + workHire.getWorkKind() + workHire.getSalary() + workHire.getWorkDescri());
		m.setMessageTitle(loginName + "ȡ��������" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//��¼�����¼
		businessOpinionService.saveBusinessOpinion(workHire.getId(),emp,"ȡ������","ȡ������");
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMessageList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��Ϣ>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��Ϣ>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��Ϣ>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		MessageQueryBo messageQueryBo = new MessageQueryBo();
		
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		messageQueryBo.setReceiverUserId(loginUser.getId());//������Ϊ��ǰ��¼�û�
		
		page = businessMessageService.queryMessages(messageQueryBo, page);
		List<MessageBo> messageList = page.getResult();
		
		List<Msg> msgList = new ArrayList<Msg>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(messageList!=null && messageList.size()>0) {
			for(int i=0;i<messageList.size();i++) {
				MessageBo mb = messageList.get(i);
				Msg msg = new Msg();
				msg.setId(mb.getId());
				msg.setCreateTime(mb.getCreateTime()==null?"":sdf.format(mb.getCreateTime()));
				msg.setIsRead(mb.getIsRead());
				msg.setSendUserName(mb.getSendUser().getUserName());
				msg.setMessageTitle(mb.getMessageTitle());
				msg.setMessageContent(mb.getMessageContent());
				msgList.add(msg);
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setMsgList(msgList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMessageInfo(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��Ϣ����>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��Ϣ����>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��Ϣ����>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String msgId = reqDetail.getMsgId();
		businessMessageService.readMessage(msgId);
		MessageBo mb = businessMessageService.getMessage(msgId);
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Msg msg = new Msg();
		msg.setId(mb.getId());
		msg.setCreateTime(mb.getCreateTime()==null?"":sdf.format(mb.getCreateTime()));
		msg.setIsRead(mb.getIsRead());
		msg.setSendUserName(mb.getSendUser().getUserName());
		msg.setMessageTitle(mb.getMessageTitle());
		msg.setMessageContent(mb.getMessageContent());
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setMsg(msg);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMySignList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �ҵı���>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �ҵı���>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �ҵı���>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		WorkHireQueryBo workHireQueryBo = new WorkHireQueryBo();
		workHireQueryBo.setSignUserId(loginUser.getId());
		page = workHireService.getWorkSignList(workHireQueryBo, page);
		List<WorkSignBo> workSignList = page.getResult();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Work> workList = new ArrayList<Work>();
		if(workSignList!=null && workSignList.size()>0) {
			for(int i=0;i<workSignList.size();i++) {
				WorkSignBo bo = workSignList.get(i);
				Work work = new Work();
				work.setId(bo.getWorkHire().getId());
				work.setBusinessNumber(bo.getWorkHire().getBusinessNumber());
				String descri = bo.getWorkHire().getWorkKind() + bo.getWorkHire().getHireNum() + "λ��" + bo.getWorkHire().getSalary() + "��" + bo.getWorkHire().getWorkDescri();
				work.setWorkDescri(descri);
				work.setSignTime(sdf.format(bo.getWorkSign().getSignTime()));
				//�ж��Ƿ����ȡ������
				if(!bo.getWorkHire().getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
					work.setCanCancelSign("0");
				}else {
					OrgUser u = orgUserService.getUserByLoginName(loginName);
					WorkSign ws = workHireService.getWorkSign(bo.getWorkHire().getId(), u.getId());
					if(ws == null){
						work.setCanCancelSign("0");
					}else {
						work.setCanCancelSign("1");
						work.setWorkDescri(work.getWorkDescri() + " �绰��" + bo.getWorkHire().getWorkCompanyMobile());
					}
				}
				workList.add(work);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWorkList(workList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String modifyPassword(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �޸�����>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �޸�����>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �޸�����>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String originalPwd = reqDetail.getOriginalPwd();
		String newPwd = reqDetail.getNewPwd();
		String confirmPwd = reqDetail.getConfirmPwd();
		
		if("".equals(newPwd) || "".equals(confirmPwd)) {
			logger.info(l + " �޸�����>>---- -----�����벻��Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�����벻��Ϊ��");
		}
		
		if(!newPwd.equals(confirmPwd)) {
			logger.info(l + " �޸�����>>---- -----������������벻һ��");
			return error(loginName, identifyCode, l,"4000", "������������벻һ��");
		}
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		String pwd = loginUser.getPwd();
		String pwdMingWen = Encrypts.decryptPassword(pwd);
		if(!pwdMingWen.equals(originalPwd)) {
			logger.info(l + " �޸�����>>---- -----�����벻��");
			return error(loginName, identifyCode, l,"4000", "�����벻��");
		}
		
		String newPwdMiWen = Encrypts.encryptPassword(newPwd);
		loginUser.setPwd(newPwdMiWen);
		loginUser.setPwdUpdateTime(calendar.getTime());
		orgUserService.saveUser(loginUser);
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String queryAndroidVersion(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " android�汾��ѯ>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " android�汾��ѯ>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " android�汾��ѯ>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		SysConfig s = sysConfigService.getSysConfigByCFGId("1");
		AndroidVersion ver = new AndroidVersion();
		ver.setVersion(s.getVal1());
		ver.setVersionPath(s.getVal2());
		ver.setIsForceUpdate(s.getVal3());
		ver.setVersionDesc(s.getVal9());
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setAndroidVersion(ver);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
		
	}

	public OrgUserService getOrgUserService() {
		return orgUserService;
	}

	public void setOrgUserService(OrgUserService orgUserService) {
		this.orgUserService = orgUserService;
	}

	public OrgDeptService getOrgDeptService() {
		return orgDeptService;
	}

	public void setOrgDeptService(OrgDeptService orgDeptService) {
		this.orgDeptService = orgDeptService;
	}

	public WorkHireService getWorkHireService() {
		return workHireService;
	}

	public void setWorkHireService(WorkHireService workHireService) {
		this.workHireService = workHireService;
	}

	public BusinessMessageService getBusinessMessageService() {
		return businessMessageService;
	}

	public void setBusinessMessageService(
			BusinessMessageService businessMessageService) {
		this.businessMessageService = businessMessageService;
	}

	public BusinessOpinionService getBusinessOpinionService() {
		return businessOpinionService;
	}

	public void setBusinessOpinionService(
			BusinessOpinionService businessOpinionService) {
		this.businessOpinionService = businessOpinionService;
	}

	public SysConfigService getSysConfigService() {
		return sysConfigService;
	}

	public void setSysConfigService(SysConfigService sysConfigService) {
		this.sysConfigService = sysConfigService;
	}
	
}
