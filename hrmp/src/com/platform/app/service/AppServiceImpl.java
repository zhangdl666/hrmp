package com.platform.app.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.platform.app.service.xmlpo.AndroidVersion;
import com.platform.app.service.xmlpo.BadRecord;
import com.platform.app.service.xmlpo.City;
import com.platform.app.service.xmlpo.Msg;
import com.platform.app.service.xmlpo.Province;
import com.platform.app.service.xmlpo.ReqDetail;
import com.platform.app.service.xmlpo.ReqMsg;
import com.platform.app.service.xmlpo.RspDetail;
import com.platform.app.service.xmlpo.RspMsg;
import com.platform.app.service.xmlpo.SignEmp;
import com.platform.app.service.xmlpo.User;
import com.platform.app.service.xmlpo.WXPay;
import com.platform.app.service.xmlpo.Work;
import com.platform.app.service.xmlpo.WorkKind;
import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.Advertisement;
import com.platform.business.pojo.MIdentifyCode;
import com.platform.business.pojo.Message;
import com.platform.business.pojo.UnitPrice;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkKindDef;
import com.platform.business.pojo.WorkSign;
import com.platform.business.service.AdvertisementService;
import com.platform.business.service.BusinessMIdentifyCodeService;
import com.platform.business.service.BusinessMessageService;
import com.platform.business.service.BusinessNumberService;
import com.platform.business.service.BusinessOpinionService;
import com.platform.business.service.BusinessUnitPriceService;
import com.platform.business.service.BusinessWorkKindDefService;
import com.platform.business.service.MobileMessageService;
import com.platform.business.service.WorkHireService;
import com.platform.core.bo.Page;
import com.platform.core.pojo.SysConfig;
import com.platform.core.service.SysConfigService;
import com.platform.organization.bo.OrgRoleBo;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgUser;
import com.platform.organization.service.OrgDeptService;
import com.platform.organization.service.OrgRoleService;
import com.platform.organization.service.OrgUserService;
import com.platform.security.util.Encrypts;
import com.platform.weixin.PayCommonUtil;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

public class AppServiceImpl implements AppService{
	private final Logger logger = Logger.getLogger(AppServiceImpl.class);
	
	private OrgUserService orgUserService;
	private OrgDeptService orgDeptService;
	private OrgRoleService orgRoleService;
	private WorkHireService workHireService;
	private BusinessMessageService businessMessageService;
	private BusinessOpinionService businessOpinionService;
	private SysConfigService sysConfigService;
	private MobileMessageService mobileMessageService;
	private BusinessMIdentifyCodeService businessMIdentifyCodeService;
	private BusinessNumberService businessNumberService;
	private BusinessUnitPriceService businessUnitPriceService;
	private BusinessWorkKindDefService businessWorkKindDefService;
	private AdvertisementService advertisementService;
	
	/**
	 * ��ȡ������֤��
	 * @param requestXml
	 * @return
	 */
	public String getIdentifyCode(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ������֤��>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ������֤��>>---------operaterֵ������Ϊ��");
			return error(loginName, null, l,"4000", "�û���������Ϊ��");
		}
		if(reqDetail == null) {
			logger.info(l + " ������֤��>>---------reqDetail�ڵ㲻����");
			return error(loginName, null, l,"4000", "reqDetail�ڵ㲻����");
		}
		String identifyCodeType = reqDetail.getIdentifyCodeType();
		if(identifyCodeType == null || "".equals(identifyCodeType)) {
			logger.info(l + " ������֤��>>---------identifyCodeTypeֵ������Ϊ��");
			return error(loginName, null, l,"4000", "identifyCodeType������Ϊ��");
		}
		
		if(!identifyCodeType.equals("regedit") && !identifyCodeType.equals("pwd")) {
			logger.info(l + " ������֤��>>---------identifyCodeTypeֵ��Ч����ȡֵΪpwd��regedit����ǰֵ��" + identifyCodeType);
			return error(loginName, null, l,"4000", "identifyCodeTypeֵ��Ч����ȡֵΪpwd��regedit����ǰֵ��" + identifyCodeType);
		}
		
		OrgUser user = orgUserService.getUserByLoginName(loginName);
		if("regedit".equals(identifyCodeType)) {//ע���û�
			if(user!=null) {
				logger.info(l + " ������֤��>>---------���ֻ�����ע��");
				return error(loginName, null, l,"4000", "���ֻ�����ע��");
			}
			
		}else if("pwd".equals(identifyCodeType)) {
			if(user==null) {
				logger.info(l + " ������֤��>>---------���ֻ���Ϊ��ע���û�");
				return error(loginName, null, l,"4000", "���ֻ���Ϊ��ע���û�");
			}
		}
		
		//���ɶ�����֤��
		MIdentifyCode code = businessMIdentifyCodeService.generateMIdentifyCode(loginName, identifyCodeType);
		
		String[] params = {code.getIdentifyCode()};
		try {
			if("regedit".equals(identifyCodeType)){
				mobileMessageService.sendRegiteValidateCode(loginName, params);
			}else if("pwd".equals(identifyCodeType)) {
				mobileMessageService.sendPwdValidateCode(loginName, params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return error(loginName, null, l,"4000", "��֤�뷢��ʧ�ܣ�");
		}
		
		//ƴ�ӷ��ر���
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(null);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("��֤�뷢�ͳɹ���");
		
		XStream xStream = new XStream();
		xStream.alias("rspMsg", RspMsg.class);
		xStream.alias("rspDetail", RspDetail.class);
		String result = xStream.toXML(rspMsg);
		logger.info(l + " ������֤��>>���ر���---------" + result);
		return result;
	}
	
	/**
	 * ��ȡ�ֹ�˾
	 * @param requestXml
	 * @return
	 */
	public String getCompanyList(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��ȡ�ֹ�˾>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		List<Province> provinceList = new ArrayList<Province>();
		List<OrgDept> comList = orgDeptService.queryDepts(null, "000");
		
		for(int i=0;i<comList.size();i++) {
			OrgDept d = comList.get(i);
			List<OrgDept> deptList = orgDeptService.queryDepts(null, d.getId());
			Province p = transDeptToCity(d,deptList);
			provinceList.add(p);
		}
		
		//ƴ�ӷ��ر���
		RspMsg rspMsg = new RspMsg();
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ���");
		
		RspDetail rspDetail = new RspDetail();
		rspDetail.setProvinceList(provinceList);
		
		rspMsg.setRspDetail(rspDetail);

		return outputMarshal(rspMsg);
	}
	
	private Province transDeptToCity(OrgDept com,List<OrgDept> deptList){
		if(com == null) {
			return null;
		}
		
		Province prov = new Province();
		prov.setProvinceId(com.getId());
		prov.setProvinceName(com.getDeptName());
		if(deptList==null || deptList.size()==0) {
			return prov;
		}
		
		prov.setCityList(new ArrayList<City>());
		for(int i=0; i<deptList.size();i++) {
			OrgDept v = deptList.get(i);
			City c = new City();
			c.setCityId(v.getId());
			c.setCityName(v.getDeptName());
			prov.getCityList().add(c);
		}
		return prov;
	}
	
	/**
	 * ע��
	 * @param requestXml
	 * @return
	 */
	public String userRegister(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ע��>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(reqDetail == null) {
			logger.info(l + " ע��>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		User user = reqDetail.getUser();
		if(user == null) {
			logger.info(l + " ע��>>---------user�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "user�ڵ㲻����");
		}
		
		if(user.getMobile()==null || user.getMobile().equals("")) {
			logger.info(l + " ע��>>---------�ֻ��Ų�����Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�ֻ��Ų�����Ϊ��");
		}
		
		OrgUser orgU = orgUserService.getUserByLoginName(user.getMobile());
		if(orgU != null) {
			logger.info(l + " ע��>>---------�ֻ�����ע��");
			return error(loginName, identifyCode, l,"4000", "�ֻ�����ע��");
		}
		
		if(user.getName()==null || user.getName().equals("")) {
			logger.info(l + " ע��>>---------�û����Ʋ�����Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û����Ʋ�����Ϊ��");
		}
		
		if(user.getPwd()==null || user.getPwd().equals("")) {
			logger.info(l + " ע��>>---------���벻����Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		
		if(user.getOrgId()==null || user.getOrgId().equals("")) {
			logger.info(l + " ע��>>---------�����ص㲻����Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�����ص㲻����Ϊ��");
		}
		
		MIdentifyCode code = businessMIdentifyCodeService.getMIdentifyCodeByMobile(user.getMobile(), "regedit");
		boolean b = validIdentifyCode(user.getIdentifyCode(), code);
		if(!b) {
			logger.info(l + " ע��>>---------������֤�벻��ȷ");
			return error(loginName, identifyCode, l,"4000", "������֤�벻��ȷ");
		}
		
		//��ȡ�û�����ID
		List<OrgDept> list = orgDeptService.queryDepts("ע���û�", user.getOrgId());
		if(list==null || list.size()==0) {
			logger.info(l + " ע��>>---------�����ص�ֵ���ԣ�" + user.getOrgId());
			return error(loginName, identifyCode, l,"4000", "�����ص�ֵ���ԣ�" + user.getOrgId());
		}
		
		//��ȡ�û���ɫid
		List<OrgRoleBo> rl = orgRoleService.queryRoles("ע���û�", list.get(0).getId());
		if(rl==null || rl.size()==0) {
			logger.info(l + " ע��>>---------δ�ҵ�ƥ��Ľ�ɫ");
			return error(loginName, identifyCode, l,"4000", "δ�ҵ�ƥ��Ľ�ɫ");
		}
		OrgUser newUser = new OrgUser();
		newUser.setLoginName(user.getMobile());
		newUser.setUserName(user.getName());
		newUser.setMobile(user.getMobile());
		newUser.setDeptId(list.get(0).getId());
		String pwd = user.getPwd();
		pwd = Encrypts.encryptPassword(pwd);
		newUser.setPwd(pwd);
		newUser.setCreateTime(Calendar.getInstance().getTime());
		newUser.setPwdUpdateTime(Calendar.getInstance().getTime());
		newUser.setValidstatus("1");
		newUser.setUserKind("register");
		orgUserService.saveUser(newUser,rl.get(0).getId());//Ĭ�Ͻ�ɫΪ����
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}
	
	/**
	 * У����֤��
	 * @param reqIdentifyCode
	 * @param dbIdentifyCode
	 * @return
	 */
	private boolean validIdentifyCode(String reqIdentifyCode,MIdentifyCode dbIdentifyCode) {
		if(reqIdentifyCode==null || reqIdentifyCode.equals("")) {
			return false;
		}
		if(dbIdentifyCode == null) {
			return false;
		}
		
		long minutes = (Calendar.getInstance().getTimeInMillis() - dbIdentifyCode.getCreateTime().getTime())/1000/60; 
		if(minutes > 5) {
			return false;
		}
		
		if(reqIdentifyCode.equals(dbIdentifyCode.getIdentifyCode())){
			return true;
		}
		return false;
	}
	
	/**
	 * �һ�����
	 * @param requestXml
	 * @return
	 */
	public String retrievePwd(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �һ�����>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(reqDetail == null) {
			logger.info(l + " �һ�����>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		User user = reqDetail.getUser();
		if(user == null) {
			logger.info(l + " �һ�����>>---------user�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "user�ڵ㲻����");
		}
		
		if(user.getMobile()==null || user.getMobile().equals("")) {
			logger.info(l + " �һ�����>>---------�ֻ��Ų�����Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�ֻ��Ų�����Ϊ��");
		}
		
		OrgUser orgU = orgUserService.getUserByLoginName(user.getMobile());
		if(orgU == null) {
			logger.info(l + " �һ�����>>---------�ֻ���δע��");
			return error(loginName, identifyCode, l,"4000", "�ֻ���δע��");
		}
		
		if(user.getPwd()==null || user.getPwd().equals("")) {
			logger.info(l + " ע��>>---------���벻����Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		
		MIdentifyCode code = businessMIdentifyCodeService.getMIdentifyCodeByMobile(user.getMobile(), "pwd");
		boolean b = validIdentifyCode(user.getIdentifyCode(), code);
		if(!b) {
			logger.info(l + " ע��>>---------������֤�벻��ȷ");
			return error(loginName, identifyCode, l,"4000", "������֤�벻��ȷ");
		}
		
		String pwd = user.getPwd();
		pwd = Encrypts.encryptPassword(pwd);
		orgU.setPwd(pwd);
		orgU.setPwdUpdateTime(Calendar.getInstance().getTime());
		orgUserService.saveUser(orgU);//Ĭ�Ͻ�ɫΪ����
		
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
		int userCount = orgUserService.getRegisterUserCount();
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(newIdentifyCode);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("��¼�ɹ�");
		
		RspDetail rspDetail = new RspDetail();
		rspDetail.setUserId(user.getId());
		rspDetail.setUserLoginName(user.getLoginName());
		rspDetail.setUserName(user.getUserName() + "(��ע���û���" + userCount + ")");
		
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
	
	/**
	 * ��ȡ���û����һ�η�����Ϣ
	 * @param requestXml
	 * @return
	 */
	@Override
	public String getLastWork(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ���һ�η�����Ϣ>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ���һ�η�����Ϣ>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ���һ�η�����Ϣ>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String empTypeId = reqDetail.getEmpTypeId();
		if(empTypeId == null || "".equals(empTypeId)) {
			logger.info(l + " ���һ�η�����Ϣ>>---------empTypeIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "empTypeId������Ϊ��");
		}
		
		if(!empTypeId.equals("LS") && !empTypeId.equals("CQ") && !empTypeId.equals("CB")) {
			logger.info(l + " ���һ�η�����Ϣ>>---------empTypeIdֵ��Ч����ȡֵΪLS��CQ��CB����ǰֵ��" + empTypeId);
			return error(loginName, null, l,"4000", "empTypeIdֵ��Ч����ȡֵΪLS��CQ��CB����ǰֵ��" + empTypeId);
		}
		
		WorkHire h = workHireService.getLastWorkHire(loginName, empTypeId);
		Work work = new Work();
		if(h == null) {
		}else {
			work.setWorkKind(h.getWorkKind());
			work.setHireNum("" + h.getHireNum());
			work.setSex(h.getSex());
			work.setSalaryRemark(h.getSalaryRemark());
			work.setSalary(h.getSalary());
			work.setAge(h.getAge());
			if("true".equals(h.getAm())) {
				work.setAm("1");
				work.setAmStart(h.getAmStart());
				work.setAmEnd(h.getAmEnd());
			}
			if("true".equals(h.getPm())) {
				work.setPm("1");
				work.setPmStart(h.getPmStart());
				work.setPmEnd(h.getPmEnd());
			}
			if("true".equals(h.getNight())) {
				work.setNight("1");
				work.setNightStart(h.getNightStart());
				work.setNightEnd(h.getNightEnd());
			}
			work.setCondition(h.getCondition());
			if(h.getPayMode()!=null) {
				if("1".equals(h.getPayMode())) {
					work.setPayMode("����");
				}else{
					work.setPayMode(h.getPayModeRemark());
				}
			}
			work.setWorkArea(h.getWorkArea());
			work.setWorkDescri(h.getWorkDescri());
		}
		
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		if(h!=null) {
			rspDetail.setWork(work);
		}
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	/**
	 * �����й���Ϣ
	 * @param requestXml
	 * @return
	 */
	public String publishWork(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �����й���Ϣ>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("work", Work.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �����й���Ϣ>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �����й���Ϣ>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		Work work = reqDetail.getWork();
		if(work == null) {
			logger.info(l + " �����й���Ϣ>>---------work�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "work�ڵ㲻����");
		}
		
		String id = work.getId();
		WorkHire h = null;
		if(id!=null && !id.equals("")) {
			h = workHireService.getWorkHire(id);
			if(h==null) {
				logger.info(l + " �����й���Ϣ>>---------δ�ҵ�Ҫ���µ��й���Ϣ��id��" + id);
				return error(loginName, identifyCode, l,"4000", "δ�ҵ�Ҫ���µ��й���Ϣ��id��" + id);
			}
			
			//���й�����Ϊ���ڹ�����ʱ��ʱ����֤���ʡ��й������Ƿ�����޸�Ҫ��
			if("LS".equals(h.getEmpTypeId()) || "CQ".equals(h.getEmpTypeId())){
				int oldHireNum = h.getHireNum();
				int oldSalary = Integer.valueOf(h.getSalary());
				int nowHireNum = Integer.valueOf(work.getHireNum());
				int nowSalary = Integer.valueOf(work.getSalary());
				if(nowHireNum < oldHireNum || nowSalary < oldSalary) {
					int signCount = workHireService.getWorkSignNum(h.getId());
					if(nowSalary < oldSalary){
						logger.info(l + " �����й���Ϣ>>---------����ʧ�ܣ����й��˱��������ʲ������µ�");
						return error(loginName, identifyCode, l,"4000", "����ʧ�ܣ����й��˱��������ʲ������µ�" );
					}
					if(nowHireNum < signCount) {
						logger.info(l + " �����й���Ϣ>>---------����ʧ�ܣ����й��˱������й������޸Ĳ���������ѱ�������");
						return error(loginName, identifyCode, l,"4000", "����ʧ�ܣ����й��˱������й������޸Ĳ���������ѱ�������" );
					}
				}
			}
		}else {
			OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
			OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
			h = new WorkHire();
//			String businessNumber = businessNumberService.getNumber("W");
//			h.setBusinessNumber(businessNumber);
			h.setCreateTime(Calendar.getInstance().getTime());
			h.setPublisherId(loginUser.getId());
			h.setPublisherName(loginUser.getUserName() + "��" + loginUser.getLoginName() + "��");
			h.setPublisherCompanyId(company.getId());
			h.setPublisherCompanyName(company.getDeptName());
			h.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);//����״̬
		}
		
		h.setEmpTypeId(work.getEmpTypeId());
		h.setWorkKind(work.getWorkKind());
		h.setHireNum(Integer.valueOf(work.getHireNum()));
		h.setSex(work.getSex());
		h.setSalaryRemark(work.getSalaryRemark());
		h.setSalary(work.getSalary());
		h.setAge(work.getAge());
		h.setAm(work.getAm());
		h.setAmStart(work.getAmStart());
		h.setAmEnd(work.getAmEnd());
		h.setPm(work.getPm());
		h.setPmStart(work.getPmStart());
		h.setPmEnd(work.getPmEnd());
		h.setNight(work.getNight());
		h.setNightStart(work.getNightStart());
		h.setNightEnd(work.getNightEnd());
		String empDataFlag = work.getEmpDate();
		if("1".equals(empDataFlag)) {//����
			h.setEmpDate(calendar.getTime());
		}else if("2".equals(empDataFlag)) {//����
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			h.setEmpDate(calendar.getTime());
		}
		h.setCondition(work.getCondition());
		
		//app��������ֵΪ�����顱����д���������ݣ��˴���һ����
		if(work.getPayMode()!=null) {
			if("����".equals(work.getPayMode())) {
				h.setPayMode("1");
				h.setPayModeRemark(null);
			}else {
				h.setPayMode("0");
				h.setPayModeRemark(work.getPayMode());
			}
		}
		h.setWorkArea(work.getWorkArea());
		h.setWorkDescri(work.getWorkDescri());
		h.setContactUser(work.getContactUser());
		h.setContactUserPhone(work.getContactUserPhone());
		
		h = workHireService.saveWorkHire(h);
		
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setBusinessNumber(h.getBusinessNumber() + "");
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
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
		String empTypeId = reqDetail.getEmpTypeId();
		
		if(!empTypeId.equals("LS") && !empTypeId.equals("CQ") && !empTypeId.equals("CB") && !empTypeId.equals("FINISH")) {
			logger.info(l + " �й��б�>>---------empTypeIdֵ��Ч����ȡֵΪLS��CQ��CB��FINISH����ǰֵ��" + empTypeId);
			return error(loginName, null, l,"4000", "empTypeIdֵ��Ч����ȡֵΪLS��CQ��CB��FINISH����ǰֵ��" + empTypeId);
		}
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		
		
		Page p = null;
		if(empTypeId.equals("LS")) {
			logger.debug("LS");
			p = workHireService.queryLSWorkHireForSign(loginName, page);
		}else if(empTypeId.equals("CQ")) {
			logger.debug("CQ");
			p = workHireService.queryCQWorkHireForSign(loginName, page,reqDetail.getKeyword());
		}else if(empTypeId.equals("CB")) {
			logger.debug("CB");
			p = workHireService.queryCBWorkHireForSign(loginName, page);
		}else {
			logger.debug("---------" + empTypeId);
			WorkHireQueryBo workHireQueryBo = new WorkHireQueryBo();
			//���ò�ѯ�������������ݸ���
			OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
			OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
			workHireQueryBo.setPublisherCompanyId(company.getId());
			workHireQueryBo.setEmpTypeId(null);
			p = workHireService.queryClosedWorkHireList(workHireQueryBo,page);
		}
		
		List<WorkHireView> workKindList = p.getResult();
		List<Work> workList = new ArrayList<Work>();
		if(workKindList!=null && workKindList.size()>0) {
			for(int i=0;i<workKindList.size();i++) {
				WorkHireView wh = workKindList.get(i);
				Work work = new Work();
				work.setId(wh.getId());
				work.setEmpDate(transEmpDate(wh.getEmpDate()));
				work.setWorkDescri(transWorkDescri(wh));
				work.setEmpTypeId(wh.getEmpTypeId());
				work.setHireNum("" + wh.getHireNum());
				workList.add(work);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setPageNo(p.getCurrentPage() + "");
		rspDetail.setTotalPage(p.getTotalPage() + "");
		rspDetail.setWorkList(workList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	/**
	 * ������ת��Ϊ���졢������������
	 * @param date
	 * @return
	 */
	private String transEmpDate(Date date){
		if(date == null) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s1 = sdf.format(date);
		Calendar calendar = Calendar.getInstance();
		String s2 = sdf.format(calendar.getTime());
		if(s1.equals(s2)) {//����
			return "1";
		}
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		s2 = sdf.format(calendar.getTime());
		if(s1.equals(s2)) {//����
			return "2";
		}
		return s1;
	}
	
	private String transWorkDescri(WorkHireView wh) {
		if(wh == null) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		if("LS".equals(wh.getEmpTypeId())) {
			sb.append("��ʱ����");
			sb.append(wh.getWorkKind());
			sb.append(wh.getHireNum());
			
			if("male".equals(wh.getSex())) {
				sb.append("���У�");
			}else if("female".equals(wh.getSex())) {
				sb.append("��Ů��");
			}
			
			sb.append("���սṤ�ʣ�");
			sb.append(wh.getSalary());
			if("GF".equals(wh.getSalaryRemark())) {
				sb.append("���ܷ�");
			}else if("GCZ".equals(wh.getSalaryRemark())) {
				sb.append("���ܳ�ס");
			}
			
			sb.append("������������");
			sb.append(wh.getWorkDescri());
			
			sb.append("�������ص㣺");
			sb.append(wh.getWorkArea());
		}else if("CQ".equals(wh.getEmpTypeId())) {
			sb.append("���ڹ���");
			sb.append(wh.getWorkKind());
			sb.append(wh.getHireNum());
			
			if("male".equals(wh.getSex())) {
				sb.append("���У�");
			}else if("female".equals(wh.getSex())) {
				sb.append("��Ů��");
			}
			
			sb.append("�����ʣ�");
			sb.append(wh.getSalary());
			
			sb.append("�����䣺");
			sb.append(wh.getAge());
			
			sb.append("������������");
			sb.append(wh.getWorkDescri());
			
			sb.append("�������ص㣺");
			sb.append(wh.getWorkArea());
		}else if("CB".equals(wh.getEmpTypeId())) {
			sb.append("�а�ʩ����");
			sb.append("����Ҫ��");
			sb.append(wh.getCondition());
			
			sb.append("������������");
			sb.append(wh.getWorkDescri());
			
			sb.append("�������ص㣺");
			sb.append(wh.getWorkArea());
			
			sb.append("�����ʽ��");
			if("1".equals(wh.getPayMode())) {
				sb.append("����");
			}else {
				sb.append(wh.getPayModeRemark());
			}
		}
		
		return sb.toString();
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
		stream.alias("province", Province.class);
		stream.alias("city", City.class);
		stream.alias("badRecord", BadRecord.class);
		stream.alias("workKind", WorkKind.class);
		stream.alias("androidVersion", AndroidVersion.class);
		stream.alias("advertisement", com.platform.app.service.xmlpo.Advertisement.class);
		stream.alias("unitPrice", com.platform.app.service.xmlpo.UnitPrice.class);
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
		
		//��ʱ���������ظ�����
		boolean b = workHireService.isCanSign(workHire, emp.getId());
		if(!b) {
			logger.info(l + " ����>>---- -----�ѱ�������������й��������ظ�������");
			return error(loginName, identifyCode, l,"4000", "�ѱ�������������й��������ظ�������");
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
		ws.setPayStatus("0");
		ws.setValidStatus("1");
		workHireService.saveWorkSign(ws);
		
		//����΢��֧��ͳһ�µ��ӿ�
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //�豸��
        parameterMap.put("device_info", "WEB");
        //����ַ���
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        //��Ʒ����
        parameterMap.put("body", "renhelaowu-sign");
        //�̻������� 
        parameterMap.put("out_trade_no", ws.getId());
        //��������
        parameterMap.put("fee_type", "CNY");  
        //�ܽ��  ��λ����
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
        parameterMap.put("total_fee", df.format(ws.getTotalMoney()*100));  
        //�ն�IP
        parameterMap.put("spbill_create_ip", reqMsg.getClientIp());
        //������ʼʱ��
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameterMap.put("time_start", sdf.format(cal.getTime()));
        //���׽���ʱ��  10���Ӻ󶩵�ʧЧ
        cal.add(Calendar.MINUTE, 10);
        parameterMap.put("time_expire", sdf.format(cal.getTime()));
        //֪ͨ��ַ
        parameterMap.put("notify_url", PayCommonUtil.notifyUrl);
        //֧������
        parameterMap.put("trade_type", "APP");
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        logger.info("jiner2");  
        //ǩ��
        parameterMap.put("sign", signStr);  
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
        logger.info(">>>>>>>>>>>>>>>>requestXML:" + requestXML);  
        String result = PayCommonUtil.httpsRequest(  
                "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  
                requestXML);
        logger.info(result);
        Map<String, String> map = null;  
        try {  
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        
        String return_code = map.get("return_code");
        if("FAIL".equals(return_code)){//΢��ͨ��ʧ��
        	workHireService.deleteWorkSign(ws);
        	return error(loginName, identifyCode, l,"4000", map.get("return_msg"));
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	workHireService.deleteWorkSign(ws);
        	return error(loginName, identifyCode, l,"4000", map.get("err_code") + " --" + map.get("err_code_des"));
        }
        
        //����Ԥ֧��id
        ws.setPrepayId(map.get("prepay_id"));
        ws.setPayStatus("0");
        workHireService.saveWorkSign(ws);
        
        //����ǩ��������app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //�豸��
        appParameterMap.put("prepayid", map.get("prepay_id"));
        appParameterMap.put("package", "Sign=WXPay");
        //����ַ���
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //ʱ���
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //ǩ��
        parameterMap.put("sign", appSignStr);  
        WXPay pay = new WXPay();
        pay.setAppId(map.get("appid"));
        pay.setPartnerId(map.get("mch_id"));
        pay.setPrepayId(map.get("prepay_id"));
        pay.setNonceStr(noncestr);
        pay.setTimestamp(timeStr);
        pay.setSign(appSignStr);
        
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWxPay(pay);
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
		WorkSign ws = workHireService.getWorkSign(workId);
		if(ws == null) {
			logger.info(l + " ȡ������>>---- -----δ�ҵ�������Ϣ");
			return error(loginName, identifyCode, l,"4000", "δ�ҵ�������Ϣ");
		}
		
		if("0".equals(ws.getValidStatus())) {
			logger.info(l + " ȡ������>>---- -----�����Ѿ�ȡ���������ظ�ȡ��");
			return error(loginName, identifyCode, l,"4000", "�����Ѿ�ȡ���������ظ�ȡ��");
		}
		
		WorkHire workHire = workHireService.getWorkHire(ws.getWorkHireId());
		if(workHire == null) {
			logger.info(l + " ȡ������>>---- -----δ�ҵ��й���Ϣ��workId��" + ws.getWorkHireId());
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��й���Ϣ���������workId�Ƿ�����workId��" + workId);
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " ȡ������>>---- -----�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "�����޷�ȡ��");
			return error(loginName, identifyCode, l,"4000", "�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ws.setRemark(loginName + "����ȡ������ " + sdf.format(calendar.getTime()));
		ws.setValidStatus("0");
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
		rspDetail.setPageNo(page.getCurrentPage() + "");
		rspDetail.setTotalPage(page.getTotalPage() + "");
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
				work.setId(bo.getWorkSign().getId());
				work.setBusinessNumber(bo.getWorkHire().getBusinessNumber());
				String descri = transWorkDescri(bo.getWorkHire());
				work.setWorkDescri(descri);
				work.setHireNum("" + bo.getWorkHire().getHireNum());
				work.setSignTime(sdf.format(bo.getWorkSign().getSignTime()));
				work.setPrepayId(bo.getWorkSign().getPrepayId());
				work.setNum("" + bo.getWorkSign().getNum());
				work.setUnitPrice("" + bo.getWorkSign().getUnitPrice());
				work.setPayFee("" + bo.getWorkSign().getTotalMoney());
				work.setPayStatus(bo.getWorkSign().getPayStatus());
				
				//�ж��Ƿ����ȡ������ 1������ȡ��������		0��������ȡ������	
				if(bo.getWorkHire().getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
					if(bo.getWorkSign().getValidStatus().equals("1")) {
						work.setCanCancelSign("1");
					}else {
						work.setCanCancelSign("0");
					}
				}else {
					work.setCanCancelSign("0");
				}
				
				//֧��״̬ 0��δ֧��		1����֧����		2������ȡ��
				work.setPayStatus(bo.getWorkSign().getPayStatus());
				if(bo.getWorkSign().getPayStatus().equals("1")) {
					work.setPayStatus("1");
				}else {
					if(bo.getWorkSign().getValidStatus().equals("1")) {
						work.setPayStatus("0");
					}else {
						work.setPayStatus("2");
					}
				}
				
				workList.add(work);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setPageNo(page.getCurrentPage() + "");
		rspDetail.setTotalPage(page.getTotalPage() + "");
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
		if(reqDetail == null) {
			logger.info(l + " android�汾��ѯ>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		SysConfig s = sysConfigService.getSysConfigByCFGId("1");
		AndroidVersion ver = new AndroidVersion();
		ver.setVersion(s.getVal1());
		ver.setVersionCode(s.getVal5());
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

	public OrgRoleService getOrgRoleService() {
		return orgRoleService;
	}

	public void setOrgRoleService(OrgRoleService orgRoleService) {
		this.orgRoleService = orgRoleService;
	}

	public BusinessNumberService getBusinessNumberService() {
		return businessNumberService;
	}

	public void setBusinessNumberService(BusinessNumberService businessNumberService) {
		this.businessNumberService = businessNumberService;
	}

	public BusinessUnitPriceService getBusinessUnitPriceService() {
		return businessUnitPriceService;
	}

	public void setBusinessUnitPriceService(
			BusinessUnitPriceService businessUnitPriceService) {
		this.businessUnitPriceService = businessUnitPriceService;
	}

	public BusinessWorkKindDefService getBusinessWorkKindDefService() {
		return businessWorkKindDefService;
	}

	public void setBusinessWorkKindDefService(
			BusinessWorkKindDefService businessWorkKindDefService) {
		this.businessWorkKindDefService = businessWorkKindDefService;
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
	

	public BusinessMIdentifyCodeService getBusinessMIdentifyCodeService() {
		return businessMIdentifyCodeService;
	}

	public void setBusinessMIdentifyCodeService(
			BusinessMIdentifyCodeService businessMIdentifyCodeService) {
		this.businessMIdentifyCodeService = businessMIdentifyCodeService;
	}

	public MobileMessageService getMobileMessageService() {
		return mobileMessageService;
	}

	public void setMobileMessageService(MobileMessageService mobileMessageService) {
		this.mobileMessageService = mobileMessageService;
	}

	@Override
	public String queryWXPayResult(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��ѯ΢��֧�����>>������---------" + requestXml);
		
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
			logger.info(l + " ��ѯ΢��֧�����>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��ѯ΢��֧�����>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workId, emp.getId());
		
		RspDetail rspDetail = new RspDetail();
		if(ws == null) {
			logger.info(l + " ��ѯ΢��֧�����>>---- -----δ�ҵ�������Ϣ");
			rspDetail.setPayStatus("0");
			rspDetail.setPayDescri("δ�ҵ�������Ϣ");
		}else {
			String s = ws.getPayStatus();
			if("1".equals(s)){
				rspDetail.setPayStatus("1");
				rspDetail.setPayDescri("֧���ɹ�");
				//��ʾ�й��˼���ϵ�绰
				OrgUser worker = orgUserService.getUser(ws.getEmpId());
				rspDetail.setRemark("�й�����" + worker.getUserName() + "����ϵ�绰��" + worker.getLoginName());
			}else {
				boolean isPay = workHireService.isPaySuccess(ws.getId());
				if(isPay) {
					ws.setPayStatus("1");
					workHireService.saveWorkSign(ws);
					rspDetail.setPayStatus("1");
					rspDetail.setPayDescri("֧���ɹ�");
					//��ʾ�й��˼���ϵ�绰
					OrgUser worker = orgUserService.getUser(ws.getEmpId());
					rspDetail.setRemark("�й�����" + worker.getUserName() + "����ϵ�绰��" + worker.getLoginName());
				}else {
					rspDetail.setPayStatus("0");
					rspDetail.setPayDescri("��֧��");
				}
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String wxPay(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ����΢��֧��>>������---------" + requestXml);
		
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
			logger.info(l + " ����΢��֧��>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ����΢��֧��>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String workSignId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workSignId);
		if(ws == null) {
			logger.info(l + " ����΢��֧��>>---- -----δ�ҵ�������Ϣ");
			return error(loginName, identifyCode, l,"4000", "δ�ҵ�������Ϣ");
		}
		
		if("1".equals(ws.getPayStatus())) {
			logger.info(l + " ����΢��֧��>>---- -----�����Ѿ�֧���������ظ�֧��");
			return error(loginName, identifyCode, l,"4000", "�����Ѿ�֧���������ظ�֧��");
		}
		
		WorkHire workHire = workHireService.getWorkHire(ws.getWorkHireId());
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " ����΢��֧��>>---- -----�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
			return error(loginName, identifyCode, l,"4000", "�й���Ϣ�����ڷ���״̬��status:" + workHire.getStatus() + "��");
		}
		
		//����ǩ��������app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //�豸��
        appParameterMap.put("prepayid", ws.getPrepayId());
        appParameterMap.put("package", "Sign=WXPay");
        //����ַ���
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //ʱ���
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //ǩ��
        WXPay pay = new WXPay();
        pay.setAppId(PayCommonUtil.APPID);
        pay.setPartnerId(PayCommonUtil.MCH_ID);
        pay.setPrepayId(ws.getPrepayId());
        pay.setNonceStr(noncestr);
        pay.setTimestamp(timeStr);
        pay.setSign(appSignStr);
        
        Work work = new Work();
        work.setUnitPrice("" + ws.getUnitPrice());
        work.setNum(ws.getNum() + "");
        work.setPayFee(ws.getTotalMoney() + "");
        
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWxPay(pay);
		rspDetail.setWork(work);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getWorkDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �й�����>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �й�����>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �й�����>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " �й�����>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��й���Ϣ");
		}
		
		Work work = transWorkHire(wh);
		//���õ���
		UnitPrice up = businessUnitPriceService.getUnitPrice(wh.getPublisherCompanyId(), wh.getEmpTypeId());
		if(up!=null) {
			work.setUnitPrice("" + up.getPrice());
		}else {
			work.setUnitPrice("5");
		}
		
		//���ڹ���ʾ�����˼���ϵ��ʽ
		if("CQ".equals(wh.getEmpTypeId())) {
			work.setContactUser(wh.getContactUser());
			work.setContactUserPhone(wh.getContactUserPhone());
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
	
	/**
	 * ��ȡ�����б����ڷ����й���Ϣ
	 * @param requestXml
	 * @return
	 */
	@Override
	public String getWorkKindList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��ȡ�����б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��ȡ�����б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		
		List<WorkKind> kindList = new ArrayList<WorkKind>();
		List<WorkKindDef> kindDefList = businessWorkKindDefService.getWorkKindList();
		if(kindDefList!=null && kindDefList.size()>0) {
			for(int i=0; i<kindDefList.size(); i++) {
				WorkKindDef def = kindDefList.get(i);
				WorkKind wk = new WorkKind();
				wk.setKindId(def.getKindName());
				wk.setKindName(def.getKindName());
				kindList.add(wk);
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWorkKindList(kindList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getSignDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��������>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " ��������>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		WorkSign ws = workHireService.getWorkSign(workId);
		WorkHire wh = workHireService.getWorkHire(ws.getWorkHireId());
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��й���Ϣ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Work work = transWorkHire(wh);
		work.setId(ws.getId());
		work.setSignTime(sdf.format(ws.getSignTime()));
		work.setPrepayId(ws.getPrepayId());
		work.setNum("" + ws.getNum());
		work.setUnitPrice("" + ws.getUnitPrice());
		work.setPayFee("" + ws.getTotalMoney());
		//�ж��Ƿ����ȡ������ 1������ȡ��������		0��������ȡ������	
		if(wh.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			if(ws.getValidStatus().equals("1")) {
				work.setCanCancelSign("1");
			}else {
				work.setCanCancelSign("0");
			}
		}else {
			work.setCanCancelSign("0");
		}
		
		//֧��״̬ 0��δ֧��		1����֧����		2������ȡ��
		work.setPayStatus(ws.getPayStatus());
		if(ws.getPayStatus().equals("1")) {
			work.setPayStatus("1");
		}else {
			if(ws.getValidStatus().equals("1")) {
				work.setPayStatus("0");
			}else {
				work.setPayStatus("2");
			}
		}

		//֧���ɹ�����ʾ��ϵ�˼��绰
		if("1".equals(ws.getPayStatus())){//��֧���ɹ�
			//��ʾ��������Ϣ
			work.setContactUser(wh.getContactUser());
			work.setContactUserPhone(wh.getContactUserPhone());
			work.setPublishTime(wh.getPublishTime()==null?"":sdf.format(wh.getPublishTime()));
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

	@Override
	public String getMyPublishList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �ҵķ���>>������---------" + requestXml);
		
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
			logger.info(l + " �ҵķ���>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �ҵķ���>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		//���ò�ѯ�������������ݸ���
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		page = workHireService.getMyWorkHireList(loginUser.getId(), new WorkHireQueryBo(), page);
		List<WorkHireView> workHireList = page.getResult();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		List<Work> workList = new ArrayList<Work>();
		if(workHireList!=null && workHireList.size()>0) {
			for(int i=0;i<workHireList.size();i++) {
				WorkHireView vo = workHireList.get(i);
				Work work = new Work();
				work.setId(vo.getId());
				work.setBusinessNumber(vo.getBusinessNumber());
				work.setSignTime(transEmpDate(vo.getPublishTime()));
				String descri = transWorkDescri(vo);
				work.setWorkDescri(descri);
				work.setHireNum("" + vo.getHireNum());
				work.setNum("" + vo.getActualSignNum());
				work.setEmpTypeId(vo.getEmpTypeId());
				//�ж��Ƿ���Թرշ���
				if(!vo.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
					work.setCanClosePublish("0");
				}else {
					work.setCanClosePublish("1");
					
				}
				workList.add(work);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setPageNo(page.getCurrentPage() + "");
		rspDetail.setTotalPage(page.getTotalPage() + "");
		rspDetail.setWorkList(workList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getPublishDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��������>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " ��������>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��й���Ϣ");
		}
		
		Work work = transWorkHire(wh);
		work.setContactUser(wh.getContactUser());
		work.setContactUserPhone(wh.getContactUserPhone());

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
	
	private Work transWorkHire(WorkHire wh) {
		Work work = new Work();
		if(wh == null) {
			return work;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		work.setId(wh.getId());
		work.setBusinessNumber(wh.getBusinessNumber() + "");
		work.setEmpTypeId(wh.getEmpTypeId());
		work.setSex(wh.getSex());
		work.setSalary(wh.getSalary());
		work.setSalaryRemark(wh.getSalaryRemark());
		work.setAge(wh.getAge());
		if("true".equals(wh.getAm()) || "1".equals(wh.getAm())) {
			work.setAm("1");
			work.setAmStart(wh.getAmStart());
			work.setAmEnd(wh.getAmEnd());
		}
		if("true".equals(wh.getPm()) || "1".equals(wh.getPm())) {
			work.setPm("1");
			work.setPmStart(wh.getPmStart());
			work.setPmEnd(wh.getPmEnd());
		}
		if("true".equals(wh.getNight()) || "1".equals(wh.getNight())) {
			work.setNight("1");
			work.setNightStart(wh.getNightStart());
			work.setNightEnd(wh.getNightEnd());
		}
		if(wh.getEmpDate()!=null) {
			
			work.setEmpDate(sdf2.format(wh.getEmpDate()));
		}
		work.setCondition(wh.getCondition());
		
		if(wh.getPayMode()!=null) {
			if("1".equals(wh.getPayMode())) {
				work.setPayMode("����");
			}else{
				work.setPayMode(wh.getPayModeRemark());
			}
		}
		work.setWorkArea(wh.getWorkArea());
		work.setCloseTime(wh.getCloseTime()==null?"":sdf.format(wh.getCloseTime()));
		work.setWorkDescri(wh.getWorkDescri());
		work.setWorkKind(wh.getWorkKind());
		work.setHireNum("" + wh.getHireNum());
		work.setStatus(wh.getStatus());
		
		List<WorkSignBo> signList = workHireService.getWorkSignList(wh.getId());
		if(signList==null || signList.size()==0) {
			work.setHireNumActural("0");
			return work;
		}
		
		//ƴ�ӱ����û�
		String signUsers = "";
		int hireNumActural = 0;
		for(int i=0;i<signList.size();i++) {
			WorkSignBo bo = signList.get(i);
			signUsers = signUsers + bo.getEmp().getUserName() + "��" + bo.getWorkSign().getNum() + "����";
			hireNumActural = hireNumActural + bo.getWorkSign().getNum();
		}
		work.setHireNumActural(hireNumActural + "");
		work.setSignUsers(signUsers);
		return work;
	}

	@Override
	public String closePublish(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �رշ���>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �رշ���>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �رշ���>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			logger.info(l + " �رշ���>>---- -----δ�ҵ�������Ϣ");
			return error(loginName, identifyCode, l,"4000", "δ�ҵ�������Ϣ");
		}
		
		if(WorkHire.WORK_HIRE_STATUS_CLOSED.equals(wh.getStatus())) {
			logger.info(l + " �رշ���>>---- -----�Ѿ��رգ������ظ�����");
			return error(loginName, identifyCode, l,"4000", "�Ѿ��رգ������ظ�����");
		}
		
		wh.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
		wh.setCloseTime(calendar.getTime());
		workHireService.saveWorkHire(wh);
		
		//��¼�����¼
		businessOpinionService.saveBusinessOpinion(wh.getId(),emp,"�ر��й�","�ر��й�");
		
		//������Ϣ֪ͨ�ͷ�
//		boolean isHaveSign = false;//�Ƿ����й��˱���
//		List<WorkSignBo> signList = workHireService.getWorkSignList(workId);
//		if(signList!=null && signList.size()>0) {//�����û�����
//			isHaveSign = true;
//		}
//		if(isHaveSign) {
//			try {
//				businessMessageService.notifyAdminAsClosePublish(wh, emp);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
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
	public String querySignCount(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��ѯ��������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��ѯ��������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ��ѯ��������>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String workId = reqDetail.getWorkId();
		
		List<WorkSignBo> signs = workHireService.getWorkSignList(workId);
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		if(signs == null || signs.size()==0) {
			rspDetail.setSignCount("0");
		}else {
			rspDetail.setSignCount(signs.size() + "");
		}
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMyBadRecordList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��ȡΥ���¼>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��ȡΥ���¼>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		page = workHireService.getMyBadRecordList(loginUser.getId(), page);
		List<com.platform.business.pojo.BadRecord> queryList = page.getResult();
		
		List<BadRecord> targetList = new ArrayList<BadRecord>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(queryList!=null && queryList.size()>0) {
			for(int i=0; i<queryList.size(); i++) {
				com.platform.business.pojo.BadRecord def = queryList.get(i);
				BadRecord wk = new BadRecord();
				wk.setId(def.getId());
				wk.setRecordTime(sdf.format(def.getRecordTime()));
				wk.setDescri(def.getDescri());
				targetList.add(wk);
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setPageNo(page.getCurrentPage() + "");
		rspDetail.setTotalPage(page.getTotalPage() + "");
		rspDetail.setBadRecordList(targetList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String toTopWorkHire(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �ö�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �ö�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �ö�>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " �ö�>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		WorkHire wh = workHireService.toTopWorkHire(workId);
		wh.setPublishTime(calendar.getTime());
		workHireService.saveWorkHire(wh);
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}

	public AdvertisementService getAdvertisementService() {
		return advertisementService;
	}

	public void setAdvertisementService(AdvertisementService advertisementService) {
		this.advertisementService = advertisementService;
	}
	
	@Override
	public String getAdvertisementList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ����б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ����б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ����б�>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		String keyword = reqDetail.getKeyword();
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		
		
		Page p = advertisementService.queryAdvertisementList(loginName, null, keyword, page);
		
		List<Advertisement> adverList = p.getResult();
		List<com.platform.app.service.xmlpo.Advertisement> respAdverList = new ArrayList<com.platform.app.service.xmlpo.Advertisement>();
		if(adverList!=null && adverList.size()>0) {
			for(int i=0;i<adverList.size();i++) {
				Advertisement ad = adverList.get(i);
				com.platform.app.service.xmlpo.Advertisement a = new com.platform.app.service.xmlpo.Advertisement();
				a.setId(ad.getId());
				a.setTitle(ad.getTitle());
				a.setContent(ad.getContent());
				a.setContactUser(ad.getContactUser());
				a.setContactUserPhone(ad.getContactUserPhone());
				a.setClickCount(ad.getClickCount() + "");
				a.setImage(ad.getImage());
				respAdverList.add(a);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setPageNo(p.getCurrentPage() + "");
		rspDetail.setTotalPage(p.getTotalPage() + "");
		rspDetail.setAdvertisementList(respAdverList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMyAdvertisementList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �ҷ����Ĺ���б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �ҷ����Ĺ���б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �ҷ����Ĺ���б�>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		String keyword = reqDetail.getKeyword();
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		Page p = advertisementService.queryMyAdvertisementList(loginName, null, keyword, page);
		
		List<Advertisement> adverList = p.getResult();
		List<com.platform.app.service.xmlpo.Advertisement> respAdverList = new ArrayList<com.platform.app.service.xmlpo.Advertisement>();
		if(adverList!=null && adverList.size()>0) {
			for(int i=0;i<adverList.size();i++) {
				Advertisement ad = adverList.get(i);
				com.platform.app.service.xmlpo.Advertisement a = new com.platform.app.service.xmlpo.Advertisement();
				a.setId(ad.getId());
				a.setTitle(ad.getTitle());
				a.setContent(ad.getContent());
				a.setContactUser(ad.getContactUser());
				a.setContactUserPhone(ad.getContactUserPhone());
				a.setClickCount(ad.getClickCount() + "");
				a.setImage(ad.getImage());
				respAdverList.add(a);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setPageNo(p.getCurrentPage() + "");
		rspDetail.setTotalPage(p.getTotalPage() + "");
		rspDetail.setAdvertisementList(respAdverList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getAdvertisementUnitPriceList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ��浥���б�>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ��浥���б�>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		
		int freePublish = businessUnitPriceService.getFreePublistCount(loginUser.getId());
		List<UnitPrice> list = businessUnitPriceService.getAdvertisementUnitPriceList(company.getId());
		List<com.platform.app.service.xmlpo.UnitPrice> priceList = new ArrayList<com.platform.app.service.xmlpo.UnitPrice>();
		if(freePublish < 3){
			com.platform.app.service.xmlpo.UnitPrice u = new com.platform.app.service.xmlpo.UnitPrice();
			u.setPrice(0.0);
			u.setMonths("�������");
			priceList.add(u);
		}
		if(list!=null && list.size()>0) {
			for(int i=0;i<list.size();i++) {
				UnitPrice ad = list.get(i);
				com.platform.app.service.xmlpo.UnitPrice u = new com.platform.app.service.xmlpo.UnitPrice();
				u.setPrice(ad.getPrice());
				u.setMonths(ad.getMonths());
				priceList.add(u);
			}
		}
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setAdvertisementPriceList(priceList);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getAdvertisementDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �������>>---------reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " �������>>---------workIdֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "���벻����Ϊ��");
		}
		advertisementService.click(workId);//����� + 1
		Advertisement adver = advertisementService.getAdvertisement(workId);
		if(adver == null) {
			return error(loginName, identifyCode, l,"4000", "δ�ҵ������Ϣ");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		com.platform.app.service.xmlpo.Advertisement a = new com.platform.app.service.xmlpo.Advertisement();
		a.setId(adver.getId());
		a.setTitle(adver.getTitle());
		a.setContent(adver.getContent());
		a.setContactUser(adver.getContactUser());
		a.setContactUserPhone(adver.getContactUserPhone());
		a.setUnitPrice(adver.getUnitPrice() + "");
		a.setCreateTime(sdf.format(adver.getCreateTime()));
		a.setMonths(adver.getMonths() + "");
		a.setPayFee(adver.getTotalMoney() + "");
		a.setPayStatus(adver.getPayStatus());
		a.setIsClosed(adver.getIsClosed());
		a.setClickCount(adver.getClickCount() + "");
		a.setImage(adver.getImage());
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setAdvertisement(a);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}

	@Override
	public String wxPayAdvertisement(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " ����΢��֧��(���)>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " ����΢��֧��(���)>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " ����΢��֧��(���)>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		String workId = reqDetail.getWorkId();
		Advertisement adver = advertisementService.getAdvertisement(workId);
		if(adver == null) {
			logger.info(l + " ����΢��֧��(���)>>---- -----δ�ҵ������Ϣ");
			return error(loginName, identifyCode, l,"4000", "δ�ҵ������Ϣ");
		}
		
		if("1".equals(adver.getPayStatus())) {
			logger.info(l + " ����΢��֧��(���)>>---- -----�Ѿ�֧���������ظ�֧��");
			return error(loginName, identifyCode, l,"4000", "�Ѿ�֧���������ظ�֧��");
		}
		
		if(!"1".equals(adver.getValidStatus())) {
			logger.info(l + " ����΢��֧��(���)>>---- -----֧����ʱ����ȡ��");
			return error(loginName, identifyCode, l,"4000", "֧����ʱ����ȡ��");
		}
		
		//����ǩ��������app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //�豸��
        appParameterMap.put("prepayid", adver.getPrepayId());
        appParameterMap.put("package", "Sign=WXPay");
        //����ַ���
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //ʱ���
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //ǩ��
        WXPay pay = new WXPay();
        pay.setAppId(PayCommonUtil.APPID);
        pay.setPartnerId(PayCommonUtil.MCH_ID);
        pay.setPrepayId(adver.getPrepayId());
        pay.setNonceStr(noncestr);
        pay.setTimestamp(timeStr);
        pay.setSign(appSignStr);
        
        
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWxPay(pay);
		rspDetail.setAdvertisement(transAdvertisement(adver));
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String publishAdvertisement(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("advertisement", com.platform.app.service.xmlpo.Advertisement.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �������>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		com.platform.app.service.xmlpo.Advertisement a = reqDetail.getAdvertisement();
		if(a == null) {
			logger.info(l + " �������>>---------advertisement�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "advertisement�ڵ㲻����");
		}
		
		Advertisement adver = new Advertisement();;
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		adver.setCreateTime(Calendar.getInstance().getTime());
		adver.setPublisherId(loginUser.getId());
		adver.setPublisherName(loginUser.getUserName() + "��" + loginUser.getLoginName() + "��");
		adver.setPublisherCompanyId(company.getId());
		adver.setPublisherCompanyName(company.getDeptName());
		adver.setValidStatus("1");
		adver.setPayStatus("0");
		adver.setIsClosed("0");
		adver.setImage(a.getImage());
		
		adver.setTitle(a.getTitle());
		adver.setContent(a.getContent());
		adver.setContactUser(a.getContactUser());
		adver.setContactUserPhone(a.getContactUserPhone());
		if("�������".equals(a.getMonths())) {
			adver.setMonths(1);
			adver.setUnitPrice(0);
			adver.setTotalMoney(0);
			adver.setRemark("�������");
			adver.setPayStatus("1");
			adver = advertisementService.saveAdvertisement(adver);
			
		}else {
			adver.setMonths(Integer.valueOf(a.getMonths()));
			if(a.getUnitPrice()==null) {
				//���õ���
				UnitPrice up = businessUnitPriceService.getUnitPrice(adver.getPublisherCompanyId(), a.getMonths());
				if(up!=null) {
					adver.setUnitPrice(up.getPrice());
					adver.setTotalMoney(up.getPrice() * adver.getMonths());
				}else {
					adver.setUnitPrice(100);
					adver.setTotalMoney(100 * adver.getMonths());
				}
			}else {
				adver.setUnitPrice(Double.valueOf(a.getUnitPrice()));
			}
		}
		adver.setTotalMoney(adver.getUnitPrice() * adver.getMonths());
		
		if(adver.getTotalMoney() == 0) {//�ܼ�Ϊ0ʱ����ʾ��ѷ�������ʱ������֧���ӿ�
			adver.setPayStatus("1");
			RspMsg rspMsg = new RspMsg();
			RspDetail rspDetail = new RspDetail();
			rspDetail.setAdvertisement(transAdvertisement(adver));
			rspMsg.setRspDetail(rspDetail);
			rspMsg.setRspResult("1000");
			rspMsg.setRspDesc("�ɹ�");
			rspMsg.setOperater(reqMsg.getOperater());
			rspMsg.setIdentification(reqMsg.getIdentification());
			return outputMarshal(rspMsg);
		}
				
		//����΢��֧��ͳһ�µ��ӿ�
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //�豸��
        parameterMap.put("device_info", "WEB");
        //����ַ���
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        //��Ʒ����
        parameterMap.put("body", "renhelaowu-sign");
        //�̻������� 
        parameterMap.put("out_trade_no", adver.getId());
        //��������
        parameterMap.put("fee_type", "CNY");  
        //�ܽ��  ��λ����
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
        parameterMap.put("total_fee", df.format(adver.getTotalMoney()*100));  
        //�ն�IP
        parameterMap.put("spbill_create_ip", reqMsg.getClientIp());
        //������ʼʱ��
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameterMap.put("time_start", sdf.format(cal.getTime()));
        //���׽���ʱ��  10���Ӻ󶩵�ʧЧ
        cal.add(Calendar.MINUTE, 10);
        parameterMap.put("time_expire", sdf.format(cal.getTime()));
        //֪ͨ��ַ
        parameterMap.put("notify_url", PayCommonUtil.notifyUrl);
        //֧������
        parameterMap.put("trade_type", "APP");
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        logger.info("jiner2");  
        //ǩ��
        parameterMap.put("sign", signStr);  
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
        logger.info(">>>>>>>>>>>>>>>>requestXML:" + requestXML);  
        String result = PayCommonUtil.httpsRequest(  
                "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  
                requestXML);
        logger.info(result);
        Map<String, String> map = null;  
        try {  
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        
        String return_code = map.get("return_code");
        if("FAIL".equals(return_code)){//΢��ͨ��ʧ��
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("return_msg"));
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("err_code") + " --" + map.get("err_code_des"));
        }
        
        //����Ԥ֧��id
        adver.setPrepayId(map.get("prepay_id"));
        adver.setPayStatus("0");
        advertisementService.saveAdvertisement(adver);
        
        //����ǩ��������app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //�豸��
        appParameterMap.put("prepayid", map.get("prepay_id"));
        appParameterMap.put("package", "Sign=WXPay");
        //����ַ���
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //ʱ���
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //ǩ��
        parameterMap.put("sign", appSignStr);  
        WXPay pay = new WXPay();
        pay.setAppId(map.get("appid"));
        pay.setPartnerId(map.get("mch_id"));
        pay.setPrepayId(map.get("prepay_id"));
        pay.setNonceStr(noncestr);
        pay.setTimestamp(timeStr);
        pay.setSign(appSignStr);
        
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWxPay(pay);
		rspDetail.setAdvertisement(transAdvertisement(adver));
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String continueAdvertisement(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " �������>>������---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("advertisement", com.platform.app.service.xmlpo.Advertisement.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//��鱨��������
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " �������>>---------operaterֵ������Ϊ��");
			return error(loginName, identifyCode, l,"4000", "�û���������Ϊ��");
		}
		
		if(reqDetail == null) {
			logger.info(l + " �������>>---- -----reqDetail�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "reqDetail�ڵ㲻����");
		}
		
		com.platform.app.service.xmlpo.Advertisement a = reqDetail.getAdvertisement();
		if(a == null) {
			logger.info(l + " �������>>---------advertisement�ڵ㲻����");
			return error(loginName, identifyCode, l,"4000", "advertisement�ڵ㲻����");
		}
		
		String id = a.getId();
		if(id == null || id.equals("")) {
			logger.info(l + " �������>>---- -----advertisement.idΪ��");
			return error(loginName, identifyCode, l,"4000", "advertisement.idΪ��");
		}
		
		Advertisement oldAdver = advertisementService.getAdvertisement(id);
		if(oldAdver == null) {
			logger.info(l + " �������>>---- -----δ�ҵ��Ƽ���Ϣ��advertisement.id��" + id);
			return error(loginName, identifyCode, l,"4000", "δ�ҵ��Ƽ���Ϣ��advertisement.id��" + id);
		}
		
		Advertisement adver = new Advertisement();;
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		
		Date currentDate = calendar.getTime();
		if(oldAdver.getEndTime().getTime() < currentDate.getTime()) {
			adver.setCreateTime(currentDate);
		}else {
			adver.setCreateTime(oldAdver.getEndTime());
		}
		adver.setPublisherId(oldAdver.getPublisherId());
		adver.setPublisherName(oldAdver.getPublisherName());
		adver.setPublisherCompanyId(oldAdver.getPublisherCompanyId());
		adver.setPublisherCompanyName(oldAdver.getPublisherCompanyName());
		adver.setValidStatus("1");
		adver.setPayStatus("0");
		adver.setIsClosed("0");
		adver.setImage(oldAdver.getImage());
		
		adver.setTitle(oldAdver.getTitle());
		adver.setContent(oldAdver.getContent());
		adver.setContactUser(oldAdver.getContactUser());
		adver.setContactUserPhone(oldAdver.getContactUserPhone());
		if("�������".equals(a.getMonths())) {
			adver.setMonths(1);
			adver.setUnitPrice(0);
			adver.setTotalMoney(0);
			adver.setRemark("�������");
			adver.setPayStatus("1");
			adver = advertisementService.saveAdvertisement(adver);
		}else {
			adver.setMonths(Integer.valueOf(a.getMonths()));
			if(a.getUnitPrice()==null) {
				//���õ���
				UnitPrice up = businessUnitPriceService.getUnitPrice(adver.getPublisherCompanyId(), a.getMonths());
				if(up!=null) {
					adver.setUnitPrice(up.getPrice());
					adver.setTotalMoney(up.getPrice() * adver.getMonths());
				}else {
					adver.setUnitPrice(100);
					adver.setTotalMoney(100 * adver.getMonths());
				}
			}else {
				adver.setUnitPrice(Double.valueOf(a.getUnitPrice()));
			}
		}
		adver.setTotalMoney(adver.getUnitPrice() * adver.getMonths());
		
		if(adver.getTotalMoney() == 0) {//�ܼ�Ϊ0ʱ����ʾ��ѷ�������ʱ������֧���ӿ�
			adver.setPayStatus("1");
			RspMsg rspMsg = new RspMsg();
			RspDetail rspDetail = new RspDetail();
			rspDetail.setAdvertisement(transAdvertisement(adver));
			rspMsg.setRspDetail(rspDetail);
			rspMsg.setRspResult("1000");
			rspMsg.setRspDesc("�ɹ�");
			rspMsg.setOperater(reqMsg.getOperater());
			rspMsg.setIdentification(reqMsg.getIdentification());
			return outputMarshal(rspMsg);
		}
				
		//����΢��֧��ͳһ�µ��ӿ�
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //�豸��
        parameterMap.put("device_info", "WEB");
        //����ַ���
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        //��Ʒ����
        parameterMap.put("body", "renhelaowu-sign");
        //�̻������� 
        parameterMap.put("out_trade_no", adver.getId());
        //��������
        parameterMap.put("fee_type", "CNY");  
        //�ܽ��  ��λ����
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
        parameterMap.put("total_fee", df.format(adver.getTotalMoney()*100));  
        //�ն�IP
        parameterMap.put("spbill_create_ip", reqMsg.getClientIp());
        //������ʼʱ��
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameterMap.put("time_start", sdf.format(cal.getTime()));
        //���׽���ʱ��  10���Ӻ󶩵�ʧЧ
        cal.add(Calendar.MINUTE, 10);
        parameterMap.put("time_expire", sdf.format(cal.getTime()));
        //֪ͨ��ַ
        parameterMap.put("notify_url", PayCommonUtil.notifyUrl);
        //֧������
        parameterMap.put("trade_type", "APP");
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        logger.info("jiner2");  
        //ǩ��
        parameterMap.put("sign", signStr);  
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
        logger.info(">>>>>>>>>>>>>>>>requestXML:" + requestXML);  
        String result = PayCommonUtil.httpsRequest(  
                "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  
                requestXML);
        logger.info(result);
        Map<String, String> map = null;  
        try {  
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }
        
        String return_code = map.get("return_code");
        if("FAIL".equals(return_code)){//΢��ͨ��ʧ��
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("return_msg"));
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("err_code") + " --" + map.get("err_code_des"));
        }
        
        //����Ԥ֧��id
        adver.setPrepayId(map.get("prepay_id"));
        adver.setPayStatus("0");
        advertisementService.saveAdvertisement(adver);
        
        //����ǩ��������app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //�豸��
        appParameterMap.put("prepayid", map.get("prepay_id"));
        appParameterMap.put("package", "Sign=WXPay");
        //����ַ���
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //ʱ���
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //ǩ��
        parameterMap.put("sign", appSignStr);  
        WXPay pay = new WXPay();
        pay.setAppId(map.get("appid"));
        pay.setPartnerId(map.get("mch_id"));
        pay.setPrepayId(map.get("prepay_id"));
        pay.setNonceStr(noncestr);
        pay.setTimestamp(timeStr);
        pay.setSign(appSignStr);
        
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWxPay(pay);
		rspDetail.setAdvertisement(transAdvertisement(adver));
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("�ɹ�");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	public com.platform.app.service.xmlpo.Advertisement transAdvertisement(Advertisement adver) {
		if(adver == null) {
			return null;
		}
		com.platform.app.service.xmlpo.Advertisement adverXml = new com.platform.app.service.xmlpo.Advertisement();
		adverXml.setId(adver.getId());
		adverXml.setPayFee(adver.getTotalMoney() + "");
		adverXml.setUnitPrice(adver.getUnitPrice() + "");
		adverXml.setMonths(adver.getMonths() + "");
		return adverXml;
	}

	@Override
	public String getLSWorkKindList(String requestXml) {
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		
		StringBuffer sb = new StringBuffer();
		sb.append("<rspMsg>");
		sb.append("  <operater>" + loginName + "</operater>");
		sb.append("  <rspResult>1000</rspResult>");
		sb.append("  <rspDesc>�ɹ�</rspDesc>");
		sb.append("  <rspDetail>");
		sb.append("    <firstList>");
		sb.append("      <first>");
		sb.append("        <firstCode>�չ�</firstCode>");
		sb.append("        <firstName>�չ�</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�չ�</secondCode>");
		sb.append("        		<secondName>�չ�</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>");
		sb.append("      <first>");
		sb.append("        <firstCode>����</firstCode>");
		sb.append("        <firstName>����</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>����</secondCode>");
		sb.append("        		<secondName>����</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>      ");
		sb.append("      <first>");
		sb.append("        <firstCode>����װ��</firstCode>");
		sb.append("        <firstName>����װ��</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�߹�</secondCode>");
		sb.append("        		<secondName>�߹�</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�ֽ</secondCode>");
		sb.append("        		<secondName>�ֽ</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>���ӹ�</secondCode>");
		sb.append("        		<secondName>���ӹ�</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>����ľ��</secondCode>");
		sb.append("        		<secondName>����ľ��</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>��װľ��</secondCode>");
		sb.append("        		<secondName>��װľ��</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�繤</secondCode>");
		sb.append("        		<secondName>�繤</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>ˮů��</secondCode>");
		sb.append("        		<secondName>ˮů��</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>��ˮ��</secondCode>");
		sb.append("        		<secondName>��ˮ��</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>���ӹ�</secondCode>");
		sb.append("        		<secondName>���ӹ�</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>���Ṥ</secondCode>");
		sb.append("        		<secondName>���Ṥ</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>��ǽ����</secondCode>");
		sb.append("        		<secondName>��ǽ����</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>��ǽͿ��</secondCode>");
		sb.append("        		<secondName>��ǽͿ��</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>      ");
		sb.append("      <first>");
		sb.append("        <firstCode>��������</firstCode>");
		sb.append("        <firstName>��������</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>������</secondCode>");
		sb.append("        		<secondName>������</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�纸</secondCode>");
		sb.append("        		<secondName>�纸</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>벻���</secondCode>");
		sb.append("        		<secondName>벻���</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>í��</secondCode>");
		sb.append("        		<secondName>í��</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>��������</secondCode>");
		sb.append("        		<secondName>��������</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>      ");
		sb.append("      <first>");
		sb.append("        <firstCode>��װ��</firstCode>");
		sb.append("        <firstName>��װ��</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�ʸִ�尲װ</secondCode>");
		sb.append("        		<secondName>�ʸִ�尲װ</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�ֽṹ��װ</secondCode>");
		sb.append("        		<secondName>�ֽṹ��װ</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�Ŵ���װ</secondCode>");
		sb.append("        		<secondName>�Ŵ���װ</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>�յ����䰲װ</secondCode>");
		sb.append("        		<secondName>�յ����䰲װ</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>��е��װ</secondCode>");
		sb.append("        		<secondName>��е��װ</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>");
		sb.append("    </firstList>");
		sb.append("  </rspDetail>");
		sb.append("</rspMsg>");
		return sb.toString();
	}
}
