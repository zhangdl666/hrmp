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
	 * 获取短信验证码
	 * @param requestXml
	 * @return
	 */
	public String getIdentifyCode(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 短信验证码>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 短信验证码>>---------operater值不允许为空");
			return error(loginName, null, l,"4000", "用户名不允许为空");
		}
		if(reqDetail == null) {
			logger.info(l + " 短信验证码>>---------reqDetail节点不存在");
			return error(loginName, null, l,"4000", "reqDetail节点不存在");
		}
		String identifyCodeType = reqDetail.getIdentifyCodeType();
		if(identifyCodeType == null || "".equals(identifyCodeType)) {
			logger.info(l + " 短信验证码>>---------identifyCodeType值不允许为空");
			return error(loginName, null, l,"4000", "identifyCodeType不允许为空");
		}
		
		if(!identifyCodeType.equals("regedit") && !identifyCodeType.equals("pwd")) {
			logger.info(l + " 短信验证码>>---------identifyCodeType值无效，可取值为pwd、regedit，当前值：" + identifyCodeType);
			return error(loginName, null, l,"4000", "identifyCodeType值无效，可取值为pwd、regedit，当前值：" + identifyCodeType);
		}
		
		OrgUser user = orgUserService.getUserByLoginName(loginName);
		if("regedit".equals(identifyCodeType)) {//注册用户
			if(user!=null) {
				logger.info(l + " 短信验证码>>---------此手机号已注册");
				return error(loginName, null, l,"4000", "此手机号已注册");
			}
			
		}else if("pwd".equals(identifyCodeType)) {
			if(user==null) {
				logger.info(l + " 短信验证码>>---------此手机号为非注册用户");
				return error(loginName, null, l,"4000", "此手机号为非注册用户");
			}
		}
		
		//生成短信验证码
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
			return error(loginName, null, l,"4000", "验证码发送失败！");
		}
		
		//拼接返回报文
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(null);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("验证码发送成功！");
		
		XStream xStream = new XStream();
		xStream.alias("rspMsg", RspMsg.class);
		xStream.alias("rspDetail", RspDetail.class);
		String result = xStream.toXML(rspMsg);
		logger.info(l + " 短信验证码>>返回报文---------" + result);
		return result;
	}
	
	/**
	 * 获取分公司
	 * @param requestXml
	 * @return
	 */
	public String getCompanyList(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 获取分公司>>请求报文---------" + requestXml);
		
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
		
		//拼接返回报文
		RspMsg rspMsg = new RspMsg();
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功！");
		
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
	 * 注册
	 * @param requestXml
	 * @return
	 */
	public String userRegister(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 注册>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(reqDetail == null) {
			logger.info(l + " 注册>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		User user = reqDetail.getUser();
		if(user == null) {
			logger.info(l + " 注册>>---------user节点不存在");
			return error(loginName, identifyCode, l,"4000", "user节点不存在");
		}
		
		if(user.getMobile()==null || user.getMobile().equals("")) {
			logger.info(l + " 注册>>---------手机号不允许为空");
			return error(loginName, identifyCode, l,"4000", "手机号不允许为空");
		}
		
		OrgUser orgU = orgUserService.getUserByLoginName(user.getMobile());
		if(orgU != null) {
			logger.info(l + " 注册>>---------手机号已注册");
			return error(loginName, identifyCode, l,"4000", "手机号已注册");
		}
		
		if(user.getName()==null || user.getName().equals("")) {
			logger.info(l + " 注册>>---------用户名称不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名称不允许为空");
		}
		
		if(user.getPwd()==null || user.getPwd().equals("")) {
			logger.info(l + " 注册>>---------密码不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		
		if(user.getOrgId()==null || user.getOrgId().equals("")) {
			logger.info(l + " 注册>>---------工作地点不允许为空");
			return error(loginName, identifyCode, l,"4000", "工作地点不允许为空");
		}
		
		MIdentifyCode code = businessMIdentifyCodeService.getMIdentifyCodeByMobile(user.getMobile(), "regedit");
		boolean b = validIdentifyCode(user.getIdentifyCode(), code);
		if(!b) {
			logger.info(l + " 注册>>---------短信验证码不正确");
			return error(loginName, identifyCode, l,"4000", "短信验证码不正确");
		}
		
		//获取用户部门ID
		List<OrgDept> list = orgDeptService.queryDepts("注册用户", user.getOrgId());
		if(list==null || list.size()==0) {
			logger.info(l + " 注册>>---------工作地点值不对，" + user.getOrgId());
			return error(loginName, identifyCode, l,"4000", "工作地点值不对，" + user.getOrgId());
		}
		
		//获取用户角色id
		List<OrgRoleBo> rl = orgRoleService.queryRoles("注册用户", list.get(0).getId());
		if(rl==null || rl.size()==0) {
			logger.info(l + " 注册>>---------未找到匹配的角色");
			return error(loginName, identifyCode, l,"4000", "未找到匹配的角色");
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
		orgUserService.saveUser(newUser,rl.get(0).getId());//默认角色为工人
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}
	
	/**
	 * 校验验证码
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
	 * 找回密码
	 * @param requestXml
	 * @return
	 */
	public String retrievePwd(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 找回密码>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(reqDetail == null) {
			logger.info(l + " 找回密码>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		User user = reqDetail.getUser();
		if(user == null) {
			logger.info(l + " 找回密码>>---------user节点不存在");
			return error(loginName, identifyCode, l,"4000", "user节点不存在");
		}
		
		if(user.getMobile()==null || user.getMobile().equals("")) {
			logger.info(l + " 找回密码>>---------手机号不允许为空");
			return error(loginName, identifyCode, l,"4000", "手机号不允许为空");
		}
		
		OrgUser orgU = orgUserService.getUserByLoginName(user.getMobile());
		if(orgU == null) {
			logger.info(l + " 找回密码>>---------手机号未注册");
			return error(loginName, identifyCode, l,"4000", "手机号未注册");
		}
		
		if(user.getPwd()==null || user.getPwd().equals("")) {
			logger.info(l + " 注册>>---------密码不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		
		MIdentifyCode code = businessMIdentifyCodeService.getMIdentifyCodeByMobile(user.getMobile(), "pwd");
		boolean b = validIdentifyCode(user.getIdentifyCode(), code);
		if(!b) {
			logger.info(l + " 注册>>---------短信验证码不正确");
			return error(loginName, identifyCode, l,"4000", "短信验证码不正确");
		}
		
		String pwd = user.getPwd();
		pwd = Encrypts.encryptPassword(pwd);
		orgU.setPwd(pwd);
		orgU.setPwdUpdateTime(Calendar.getInstance().getTime());
		orgUserService.saveUser(orgU);//默认角色为工人
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String appLogin(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 登录>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//生成新的凭证码
		String newIdentifyCode = loginName + ":" + l;
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 登录>>---------operater值不允许为空");
			return error(loginName, newIdentifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 登录>>---------reqDetail节点不存在");
			return error(loginName, newIdentifyCode, l,"4000", "reqDetail节点不存在");
		}
		String pwd = reqDetail.getPassword();
		if(pwd == null || "".equals(pwd)) {
			logger.info(l + " 登录>>---------password值不允许为空");
			return error(loginName, newIdentifyCode, l,"4000", "密码不允许为空");
		}
		
		OrgUser user = orgUserService.getUserByLoginName(loginName);
		if(user == null) {
			logger.info(l + " 登录>>---------用户不存在，请检查operater值有效性，operater：" + loginName);
			return error(loginName, newIdentifyCode, l,"4002", "用户不存在");
		}
		
		//登录验证
		String enPassword = user.getPwd();
		String password = Encrypts.decryptPassword(enPassword);
		if(!password.equals(pwd)) {
			logger.info(l + " 登录>>---------密码错误！");
			return error(loginName, newIdentifyCode, l,"4001", "密码错误！");
		}
		
		//拼接返回报文
		int userCount = orgUserService.getRegisterUserCount();
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(newIdentifyCode);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("登录成功");
		
		RspDetail rspDetail = new RspDetail();
		rspDetail.setUserId(user.getId());
		rspDetail.setUserLoginName(user.getLoginName());
		rspDetail.setUserName(user.getUserName() + "(已注册用户：" + userCount + ")");
		
		rspMsg.setRspDetail(rspDetail);

		XStream xStream = new XStream();
		xStream.alias("rspMsg", RspMsg.class);
		xStream.alias("rspDetail", RspDetail.class);
		String result = xStream.toXML(rspMsg);
		logger.info(l + " 登录>>返回报文---------" + result);
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
		logger.info(l + " 返回报文---------" + result);
		return result;
	}
	
	/**
	 * 获取此用户最近一次发布信息
	 * @param requestXml
	 * @return
	 */
	@Override
	public String getLastWork(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 最近一次发布信息>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 最近一次发布信息>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 最近一次发布信息>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String empTypeId = reqDetail.getEmpTypeId();
		if(empTypeId == null || "".equals(empTypeId)) {
			logger.info(l + " 最近一次发布信息>>---------empTypeId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "empTypeId不允许为空");
		}
		
		if(!empTypeId.equals("LS") && !empTypeId.equals("CQ") && !empTypeId.equals("CB")) {
			logger.info(l + " 最近一次发布信息>>---------empTypeId值无效，可取值为LS、CQ、CB，当前值：" + empTypeId);
			return error(loginName, null, l,"4000", "empTypeId值无效，可取值为LS、CQ、CB，当前值：" + empTypeId);
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
					work.setPayMode("面议");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	/**
	 * 发布招工信息
	 * @param requestXml
	 * @return
	 */
	public String publishWork(String requestXml){
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 发布招工信息>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("work", Work.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 发布招工信息>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 发布招工信息>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		Work work = reqDetail.getWork();
		if(work == null) {
			logger.info(l + " 发布招工信息>>---------work节点不存在");
			return error(loginName, identifyCode, l,"4000", "work节点不存在");
		}
		
		String id = work.getId();
		WorkHire h = null;
		if(id!=null && !id.equals("")) {
			h = workHireService.getWorkHire(id);
			if(h==null) {
				logger.info(l + " 发布招工信息>>---------未找到要更新的招工信息，id：" + id);
				return error(loginName, identifyCode, l,"4000", "未找到要更新的招工信息，id：" + id);
			}
			
			//当招工类型为长期工、临时工时，验证工资、招工人数是否符合修改要求
			if("LS".equals(h.getEmpTypeId()) || "CQ".equals(h.getEmpTypeId())){
				int oldHireNum = h.getHireNum();
				int oldSalary = Integer.valueOf(h.getSalary());
				int nowHireNum = Integer.valueOf(work.getHireNum());
				int nowSalary = Integer.valueOf(work.getSalary());
				if(nowHireNum < oldHireNum || nowSalary < oldSalary) {
					int signCount = workHireService.getWorkSignNum(h.getId());
					if(nowSalary < oldSalary){
						logger.info(l + " 发布招工信息>>---------操作失败，已有工人报名，工资不允许下调");
						return error(loginName, identifyCode, l,"4000", "操作失败，已有工人报名，工资不允许下调" );
					}
					if(nowHireNum < signCount) {
						logger.info(l + " 发布招工信息>>---------操作失败，已有工人报名，招工人数修改不允许低于已报名人数");
						return error(loginName, identifyCode, l,"4000", "操作失败，已有工人报名，招工人数修改不允许低于已报名人数" );
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
			h.setPublisherName(loginUser.getUserName() + "（" + loginUser.getLoginName() + "）");
			h.setPublisherCompanyId(company.getId());
			h.setPublisherCompanyName(company.getDeptName());
			h.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);//发布状态
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
		if("1".equals(empDataFlag)) {//今天
			h.setEmpDate(calendar.getTime());
		}else if("2".equals(empDataFlag)) {//明天
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			h.setEmpDate(calendar.getTime());
		}
		h.setCondition(work.getCondition());
		
		//app传过来的值为“面议”或填写的其他内容，此处做一处理
		if(work.getPayMode()!=null) {
			if("面议".equals(work.getPayMode())) {
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getWorkList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 招工列表>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 招工列表>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 招工列表>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		String empTypeId = reqDetail.getEmpTypeId();
		
		if(!empTypeId.equals("LS") && !empTypeId.equals("CQ") && !empTypeId.equals("CB") && !empTypeId.equals("FINISH")) {
			logger.info(l + " 招工列表>>---------empTypeId值无效，可取值为LS、CQ、CB、FINISH，当前值：" + empTypeId);
			return error(loginName, null, l,"4000", "empTypeId值无效，可取值为LS、CQ、CB、FINISH，当前值：" + empTypeId);
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
			//设置查询条件，用于数据隔离
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	/**
	 * 将日期转换为今天、明天或具体日期
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
		if(s1.equals(s2)) {//今天
			return "1";
		}
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		s2 = sdf.format(calendar.getTime());
		if(s1.equals(s2)) {//明天
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
			sb.append("临时工：");
			sb.append(wh.getWorkKind());
			sb.append(wh.getHireNum());
			
			if("male".equals(wh.getSex())) {
				sb.append("（男）");
			}else if("female".equals(wh.getSex())) {
				sb.append("（女）");
			}
			
			sb.append("，日结工资：");
			sb.append(wh.getSalary());
			if("GF".equals(wh.getSalaryRemark())) {
				sb.append("，管饭");
			}else if("GCZ".equals(wh.getSalaryRemark())) {
				sb.append("，管吃住");
			}
			
			sb.append("，工作描述：");
			sb.append(wh.getWorkDescri());
			
			sb.append("，工作地点：");
			sb.append(wh.getWorkArea());
		}else if("CQ".equals(wh.getEmpTypeId())) {
			sb.append("长期工：");
			sb.append(wh.getWorkKind());
			sb.append(wh.getHireNum());
			
			if("male".equals(wh.getSex())) {
				sb.append("（男）");
			}else if("female".equals(wh.getSex())) {
				sb.append("（女）");
			}
			
			sb.append("，工资：");
			sb.append(wh.getSalary());
			
			sb.append("，年龄：");
			sb.append(wh.getAge());
			
			sb.append("，工作描述：");
			sb.append(wh.getWorkDescri());
			
			sb.append("，工作地点：");
			sb.append(wh.getWorkArea());
		}else if("CB".equals(wh.getEmpTypeId())) {
			sb.append("承包施工：");
			sb.append("条件要求：");
			sb.append(wh.getCondition());
			
			sb.append("，工作描述：");
			sb.append(wh.getWorkDescri());
			
			sb.append("，工作地点：");
			sb.append(wh.getWorkArea());
			
			sb.append("，付款方式：");
			if("1".equals(wh.getPayMode())) {
				sb.append("面议");
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
		logger.info(l + " 报名工人列表>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 报名工人列表>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 报名工人列表>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " 报名工人列表>>---------workId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String sign(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 报名>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 报名>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 报名>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		SignEmp sign = reqDetail.getSignEmp();
		if(sign == null) {
			logger.info(l + " 报名>>---- -----signEmp节点不存在");
			return error(loginName, identifyCode, l,"4000", "signEmp节点不存在");
		}
		
		WorkHire workHire = workHireService.getWorkHire(sign.getWorkId());
		if(workHire == null) {
			logger.info(l + " 报名>>---- -----找不到招工信息（workHireId:" + sign.getWorkId() + "）");
			return error(loginName, identifyCode, l,"4000", "找不到招工信息（workHireId:" + sign.getWorkId() + "）");
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " 报名>>---- -----招工信息不处于发布状态（status:" + workHire.getStatus() + "）");
			return error(loginName, identifyCode, l,"4000", "招工信息不处于发布状态（status:" + workHire.getStatus() + "）");
		}
		
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(emp.getDeptId());
		if(!workHire.getPublisherCompanyId().equals(company.getId())){
			logger.info(l + " 报名>>---- -----只能报名本地市的招工信息，招工地市：" + workHire.getPublisherCompanyName() + "，您所在地市：" + company.getDeptName());
			return error(loginName, identifyCode, l,"4000", "只能报名本地市的招工信息，招工地市：" + workHire.getPublisherCompanyName() + "，您所在地市：" + company.getDeptName());
		}

		//验证是否已报名
		WorkSign ws = workHireService.getWorkSign(sign.getWorkId(), emp.getId());
		if(ws != null) {
			logger.info(l + " 报名>>---- -----已经报名，请勿重复报名！");
			return error(loginName, identifyCode, l,"4000", "已经报名，请勿重复报名！");
		}
		
		//临时工不允许重复报名
		boolean b = workHireService.isCanSign(workHire, emp.getId());
		if(!b) {
			logger.info(l + " 报名>>---- -----已报名当天的其他招工，请勿重复报名！");
			return error(loginName, identifyCode, l,"4000", "已报名当天的其他招工，请勿重复报名！");
		}
		
		//验证是否超出报名上限
		int planSignNum = workHire.getHireNum();
		int acturalNum = workHireService.getWorkSignNum(sign.getWorkId());
		
		if(acturalNum >= planSignNum) {
			logger.info(l + " 报名>>---- -----报名已满");
			return error(loginName, identifyCode, l,"4000", "报名已满");
		}
		
		int remainNum = planSignNum - acturalNum;
		if(Integer.valueOf(sign.getNum()) > remainNum) {
			logger.info(l + " 报名>>---- -----报名人数已超出招工人数，当前剩余名额：" + remainNum + "个");
			return error(loginName, identifyCode, l,"4000", "报名人数已超出招工人数，当前剩余名额：" + remainNum + "个");
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
		
		//调用微信支付统一下单接口
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //应用ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //设备号
        parameterMap.put("device_info", "WEB");
        //随机字符串
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        //商品描述
        parameterMap.put("body", "renhelaowu-sign");
        //商户订单号 
        parameterMap.put("out_trade_no", ws.getId());
        //货币类型
        parameterMap.put("fee_type", "CNY");  
        //总金额  单位：分
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
        parameterMap.put("total_fee", df.format(ws.getTotalMoney()*100));  
        //终端IP
        parameterMap.put("spbill_create_ip", reqMsg.getClientIp());
        //交易起始时间
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameterMap.put("time_start", sdf.format(cal.getTime()));
        //交易结束时间  10分钟后订单失效
        cal.add(Calendar.MINUTE, 10);
        parameterMap.put("time_expire", sdf.format(cal.getTime()));
        //通知地址
        parameterMap.put("notify_url", PayCommonUtil.notifyUrl);
        //支付类型
        parameterMap.put("trade_type", "APP");
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        logger.info("jiner2");  
        //签名
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
        if("FAIL".equals(return_code)){//微信通信失败
        	workHireService.deleteWorkSign(ws);
        	return error(loginName, identifyCode, l,"4000", map.get("return_msg"));
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	workHireService.deleteWorkSign(ws);
        	return error(loginName, identifyCode, l,"4000", map.get("err_code") + " --" + map.get("err_code_des"));
        }
        
        //保存预支付id
        ws.setPrepayId(map.get("prepay_id"));
        ws.setPayStatus("0");
        workHireService.saveWorkSign(ws);
        
        //生成签名，返回app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //应用ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //设备号
        appParameterMap.put("prepayid", map.get("prepay_id"));
        appParameterMap.put("package", "Sign=WXPay");
        //随机字符串
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //时间戳
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //签名
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String cancelSign(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 取消报名>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 取消报名>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 取消报名>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workId);
		if(ws == null) {
			logger.info(l + " 取消报名>>---- -----未找到报名信息");
			return error(loginName, identifyCode, l,"4000", "未找到报名信息");
		}
		
		if("0".equals(ws.getValidStatus())) {
			logger.info(l + " 取消报名>>---- -----报名已经取消，请勿重复取消");
			return error(loginName, identifyCode, l,"4000", "报名已经取消，请勿重复取消");
		}
		
		WorkHire workHire = workHireService.getWorkHire(ws.getWorkHireId());
		if(workHire == null) {
			logger.info(l + " 取消报名>>---- -----未找到招工信息，workId：" + ws.getWorkHireId());
			return error(loginName, identifyCode, l,"4000", "未找到招工信息，请检查参数workId是否有误，workId：" + workId);
		}
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " 取消报名>>---- -----招工信息不处于发布状态（status:" + workHire.getStatus() + "），无法取消");
			return error(loginName, identifyCode, l,"4000", "招工信息不处于发布状态（status:" + workHire.getStatus() + "）");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ws.setRemark(loginName + "主动取消报名 " + sdf.format(calendar.getTime()));
		ws.setValidStatus("0");
		workHireService.saveWorkSign(ws);
		
		//发送消息
		Message m = new Message();
		m.setCreateTime(Calendar.getInstance().getTime());
		m.setIsRead("0");//未读
		m.setMessageContent(loginName + "取消报名，招工单号：" + workHire.getBusinessNumber() + "，" + workHire.getWorkKind() + workHire.getSalary() + workHire.getWorkDescri());
		m.setMessageTitle(loginName + "取消报名：" + workHire.getBusinessNumber() + " " + workHire.getWorkKind() );
		m.setReceiverUserId(workHire.getPublisherId());
		m.setSendUserId(ws.getEmpId());
		businessMessageService.saveMessage(m);
		
		//记录办理记录
		businessOpinionService.saveBusinessOpinion(workHire.getId(),emp,"取消报名","取消报名");
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMessageList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 消息>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 消息>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 消息>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		MessageQueryBo messageQueryBo = new MessageQueryBo();
		
		//设置查询条件，用于数据隔离
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		messageQueryBo.setReceiverUserId(loginUser.getId());//接收人为当前登录用户
		
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMessageInfo(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 消息详情>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 消息详情>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 消息详情>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMySignList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 我的报名>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 我的报名>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 我的报名>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		//设置查询条件，用于数据隔离
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
				
				//判断是否可以取消报名 1：可以取消报名；		0：不可以取消报名	
				if(bo.getWorkHire().getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
					if(bo.getWorkSign().getValidStatus().equals("1")) {
						work.setCanCancelSign("1");
					}else {
						work.setCanCancelSign("0");
					}
				}else {
					work.setCanCancelSign("0");
				}
				
				//支付状态 0：未支付		1：已支付；		2：订单取消
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String modifyPassword(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 修改密码>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 修改密码>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 修改密码>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String originalPwd = reqDetail.getOriginalPwd();
		String newPwd = reqDetail.getNewPwd();
		String confirmPwd = reqDetail.getConfirmPwd();
		
		if("".equals(newPwd) || "".equals(confirmPwd)) {
			logger.info(l + " 修改密码>>---- -----新密码不能为空");
			return error(loginName, identifyCode, l,"4000", "新密码不能为空");
		}
		
		if(!newPwd.equals(confirmPwd)) {
			logger.info(l + " 修改密码>>---- -----两次输入的密码不一致");
			return error(loginName, identifyCode, l,"4000", "两次输入的密码不一致");
		}
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		String pwd = loginUser.getPwd();
		String pwdMingWen = Encrypts.decryptPassword(pwd);
		if(!pwdMingWen.equals(originalPwd)) {
			logger.info(l + " 修改密码>>---- -----旧密码不对");
			return error(loginName, identifyCode, l,"4000", "旧密码不对");
		}
		
		String newPwdMiWen = Encrypts.encryptPassword(newPwd);
		loginUser.setPwd(newPwdMiWen);
		loginUser.setPwdUpdateTime(calendar.getTime());
		orgUserService.saveUser(loginUser);
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String queryAndroidVersion(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " android版本查询>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(reqDetail == null) {
			logger.info(l + " android版本查询>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
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
		rspMsg.setRspDesc("成功");
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
		logger.info(l + " 查询微信支付结果>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 查询微信支付结果>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 查询微信支付结果>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workId, emp.getId());
		
		RspDetail rspDetail = new RspDetail();
		if(ws == null) {
			logger.info(l + " 查询微信支付结果>>---- -----未找到报名信息");
			rspDetail.setPayStatus("0");
			rspDetail.setPayDescri("未找到报名信息");
		}else {
			String s = ws.getPayStatus();
			if("1".equals(s)){
				rspDetail.setPayStatus("1");
				rspDetail.setPayDescri("支付成功");
				//显示招工人及联系电话
				OrgUser worker = orgUserService.getUser(ws.getEmpId());
				rspDetail.setRemark("招工方：" + worker.getUserName() + "，联系电话：" + worker.getLoginName());
			}else {
				boolean isPay = workHireService.isPaySuccess(ws.getId());
				if(isPay) {
					ws.setPayStatus("1");
					workHireService.saveWorkSign(ws);
					rspDetail.setPayStatus("1");
					rspDetail.setPayDescri("支付成功");
					//显示招工人及联系电话
					OrgUser worker = orgUserService.getUser(ws.getEmpId());
					rspDetail.setRemark("招工方：" + worker.getUserName() + "，联系电话：" + worker.getLoginName());
				}else {
					rspDetail.setPayStatus("0");
					rspDetail.setPayDescri("待支付");
				}
			}
		}
		
		RspMsg rspMsg = new RspMsg();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String wxPay(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 发起微信支付>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 发起微信支付>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 发起微信支付>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String workSignId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workSignId);
		if(ws == null) {
			logger.info(l + " 发起微信支付>>---- -----未找到报名信息");
			return error(loginName, identifyCode, l,"4000", "未找到报名信息");
		}
		
		if("1".equals(ws.getPayStatus())) {
			logger.info(l + " 发起微信支付>>---- -----报名已经支付，请勿重复支付");
			return error(loginName, identifyCode, l,"4000", "报名已经支付，请勿重复支付");
		}
		
		WorkHire workHire = workHireService.getWorkHire(ws.getWorkHireId());
		
		if(!workHire.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			logger.info(l + " 发起微信支付>>---- -----招工信息不处于发布状态（status:" + workHire.getStatus() + "）");
			return error(loginName, identifyCode, l,"4000", "招工信息不处于发布状态（status:" + workHire.getStatus() + "）");
		}
		
		//生成签名，返回app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //应用ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //设备号
        appParameterMap.put("prepayid", ws.getPrepayId());
        appParameterMap.put("package", "Sign=WXPay");
        //随机字符串
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //时间戳
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //签名
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getWorkDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 招工详情>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 招工详情>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 招工详情>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " 招工详情>>---------workId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "未找到招工信息");
		}
		
		Work work = transWorkHire(wh);
		//设置单价
		UnitPrice up = businessUnitPriceService.getUnitPrice(wh.getPublisherCompanyId(), wh.getEmpTypeId());
		if(up!=null) {
			work.setUnitPrice("" + up.getPrice());
		}else {
			work.setUnitPrice("5");
		}
		
		//长期工显示发布人及联系方式
		if("CQ".equals(wh.getEmpTypeId())) {
			work.setContactUser(wh.getContactUser());
			work.setContactUserPhone(wh.getContactUserPhone());
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWork(work);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}
	
	/**
	 * 获取工种列表，用于发布招工信息
	 * @param requestXml
	 * @return
	 */
	@Override
	public String getWorkKindList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 获取工种列表>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 获取工种列表>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getSignDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 报名详情>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 报名详情>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 报名详情>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " 报名详情>>---------workId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		WorkSign ws = workHireService.getWorkSign(workId);
		WorkHire wh = workHireService.getWorkHire(ws.getWorkHireId());
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "未找到招工信息");
		}
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Work work = transWorkHire(wh);
		work.setId(ws.getId());
		work.setSignTime(sdf.format(ws.getSignTime()));
		work.setPrepayId(ws.getPrepayId());
		work.setNum("" + ws.getNum());
		work.setUnitPrice("" + ws.getUnitPrice());
		work.setPayFee("" + ws.getTotalMoney());
		//判断是否可以取消报名 1：可以取消报名；		0：不可以取消报名	
		if(wh.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			if(ws.getValidStatus().equals("1")) {
				work.setCanCancelSign("1");
			}else {
				work.setCanCancelSign("0");
			}
		}else {
			work.setCanCancelSign("0");
		}
		
		//支付状态 0：未支付		1：已支付；		2：订单取消
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

		//支付成功，显示联系人及电话
		if("1".equals(ws.getPayStatus())){//已支付成功
			//显示发布人信息
			work.setContactUser(wh.getContactUser());
			work.setContactUserPhone(wh.getContactUserPhone());
			work.setPublishTime(wh.getPublishTime()==null?"":sdf.format(wh.getPublishTime()));
		}
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWork(work);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMyPublishList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 我的发布>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("signEmp", SignEmp.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 我的发布>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 我的发布>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		int pageNo = Integer.valueOf(reqDetail.getPageNo());
		int pageSize = Integer.valueOf(reqDetail.getPageSize());
		
		Page page = new Page();
		page.setCurrentPage(pageNo);
		page.setPageSize(pageSize);
		
		//设置查询条件，用于数据隔离
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
				//判断是否可以关闭发布
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getPublishDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 发布详情>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 发布详情>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 发布详情>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " 发布详情>>---------workId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			return error(loginName, identifyCode, l,"4000", "未找到招工信息");
		}
		
		Work work = transWorkHire(wh);
		work.setContactUser(wh.getContactUser());
		work.setContactUserPhone(wh.getContactUserPhone());

		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setWork(work);
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
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
				work.setPayMode("面议");
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
		
		//拼接报名用户
		String signUsers = "";
		int hireNumActural = 0;
		for(int i=0;i<signList.size();i++) {
			WorkSignBo bo = signList.get(i);
			signUsers = signUsers + bo.getEmp().getUserName() + "（" + bo.getWorkSign().getNum() + "）；";
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
		logger.info(l + " 关闭发布>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 关闭发布>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 关闭发布>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			logger.info(l + " 关闭发布>>---- -----未找到发布信息");
			return error(loginName, identifyCode, l,"4000", "未找到发布信息");
		}
		
		if(WorkHire.WORK_HIRE_STATUS_CLOSED.equals(wh.getStatus())) {
			logger.info(l + " 关闭发布>>---- -----已经关闭，请勿重复操作");
			return error(loginName, identifyCode, l,"4000", "已经关闭，请勿重复操作");
		}
		
		wh.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
		wh.setCloseTime(calendar.getTime());
		workHireService.saveWorkHire(wh);
		
		//记录办理记录
		businessOpinionService.saveBusinessOpinion(wh.getId(),emp,"关闭招工","关闭招工");
		
		//发送消息通知客服
//		boolean isHaveSign = false;//是否已有工人报名
//		List<WorkSignBo> signList = workHireService.getWorkSignList(workId);
//		if(signList!=null && signList.size()>0) {//已有用户报名
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String querySignCount(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 查询报名人数>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 查询报名人数>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 查询报名人数>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMyBadRecordList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 获取违规记录>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 获取违规记录>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String toTopWorkHire(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 置顶>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 置顶>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 置顶>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " 置顶>>---------workId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		WorkHire wh = workHireService.toTopWorkHire(workId);
		wh.setPublishTime(calendar.getTime());
		workHireService.saveWorkHire(wh);
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
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
		logger.info(l + " 广告列表>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 广告列表>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 广告列表>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getMyAdvertisementList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 我发布的广告列表>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 我发布的广告列表>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 我发布的广告列表>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String getAdvertisementUnitPriceList(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 广告单价列表>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 广告单价列表>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		
		int freePublish = businessUnitPriceService.getFreePublistCount(loginUser.getId());
		List<UnitPrice> list = businessUnitPriceService.getAdvertisementUnitPriceList(company.getId());
		List<com.platform.app.service.xmlpo.UnitPrice> priceList = new ArrayList<com.platform.app.service.xmlpo.UnitPrice>();
		if(freePublish < 3){
			com.platform.app.service.xmlpo.UnitPrice u = new com.platform.app.service.xmlpo.UnitPrice();
			u.setPrice(0.0);
			u.setMonths("免费试用");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}

	@Override
	public String getAdvertisementDetail(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 广告详情>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 广告详情>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 广告详情>>---------reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		String workId = reqDetail.getWorkId();
		if(workId == null || "".equals(workId)) {
			logger.info(l + " 广告详情>>---------workId值不允许为空");
			return error(loginName, identifyCode, l,"4000", "密码不允许为空");
		}
		advertisementService.click(workId);//点击量 + 1
		Advertisement adver = advertisementService.getAdvertisement(workId);
		if(adver == null) {
			return error(loginName, identifyCode, l,"4000", "未找到广告信息");
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}

	@Override
	public String wxPayAdvertisement(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 发起微信支付(广告)>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 发起微信支付(广告)>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 发起微信支付(广告)>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String workId = reqDetail.getWorkId();
		Advertisement adver = advertisementService.getAdvertisement(workId);
		if(adver == null) {
			logger.info(l + " 发起微信支付(广告)>>---- -----未找到广告信息");
			return error(loginName, identifyCode, l,"4000", "未找到广告信息");
		}
		
		if("1".equals(adver.getPayStatus())) {
			logger.info(l + " 发起微信支付(广告)>>---- -----已经支付，请勿重复支付");
			return error(loginName, identifyCode, l,"4000", "已经支付，请勿重复支付");
		}
		
		if(!"1".equals(adver.getValidStatus())) {
			logger.info(l + " 发起微信支付(广告)>>---- -----支付超时，已取消");
			return error(loginName, identifyCode, l,"4000", "支付超时，已取消");
		}
		
		//生成签名，返回app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //应用ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //设备号
        appParameterMap.put("prepayid", adver.getPrepayId());
        appParameterMap.put("package", "Sign=WXPay");
        //随机字符串
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //时间戳
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //签名
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String publishAdvertisement(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 发布广告>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("advertisement", com.platform.app.service.xmlpo.Advertisement.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 发布广告>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 发布广告>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		com.platform.app.service.xmlpo.Advertisement a = reqDetail.getAdvertisement();
		if(a == null) {
			logger.info(l + " 发布广告>>---------advertisement节点不存在");
			return error(loginName, identifyCode, l,"4000", "advertisement节点不存在");
		}
		
		Advertisement adver = new Advertisement();;
		
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		adver.setCreateTime(Calendar.getInstance().getTime());
		adver.setPublisherId(loginUser.getId());
		adver.setPublisherName(loginUser.getUserName() + "（" + loginUser.getLoginName() + "）");
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
		if("免费试用".equals(a.getMonths())) {
			adver.setMonths(1);
			adver.setUnitPrice(0);
			adver.setTotalMoney(0);
			adver.setRemark("免费试用");
			adver.setPayStatus("1");
			adver = advertisementService.saveAdvertisement(adver);
			
		}else {
			adver.setMonths(Integer.valueOf(a.getMonths()));
			if(a.getUnitPrice()==null) {
				//设置单价
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
		
		if(adver.getTotalMoney() == 0) {//总价为0时，表示免费发布，此时无需走支付接口
			adver.setPayStatus("1");
			RspMsg rspMsg = new RspMsg();
			RspDetail rspDetail = new RspDetail();
			rspDetail.setAdvertisement(transAdvertisement(adver));
			rspMsg.setRspDetail(rspDetail);
			rspMsg.setRspResult("1000");
			rspMsg.setRspDesc("成功");
			rspMsg.setOperater(reqMsg.getOperater());
			rspMsg.setIdentification(reqMsg.getIdentification());
			return outputMarshal(rspMsg);
		}
				
		//调用微信支付统一下单接口
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //应用ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //设备号
        parameterMap.put("device_info", "WEB");
        //随机字符串
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        //商品描述
        parameterMap.put("body", "renhelaowu-sign");
        //商户订单号 
        parameterMap.put("out_trade_no", adver.getId());
        //货币类型
        parameterMap.put("fee_type", "CNY");  
        //总金额  单位：分
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
        parameterMap.put("total_fee", df.format(adver.getTotalMoney()*100));  
        //终端IP
        parameterMap.put("spbill_create_ip", reqMsg.getClientIp());
        //交易起始时间
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameterMap.put("time_start", sdf.format(cal.getTime()));
        //交易结束时间  10分钟后订单失效
        cal.add(Calendar.MINUTE, 10);
        parameterMap.put("time_expire", sdf.format(cal.getTime()));
        //通知地址
        parameterMap.put("notify_url", PayCommonUtil.notifyUrl);
        //支付类型
        parameterMap.put("trade_type", "APP");
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        logger.info("jiner2");  
        //签名
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
        if("FAIL".equals(return_code)){//微信通信失败
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("return_msg"));
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("err_code") + " --" + map.get("err_code_des"));
        }
        
        //保存预支付id
        adver.setPrepayId(map.get("prepay_id"));
        adver.setPayStatus("0");
        advertisementService.saveAdvertisement(adver);
        
        //生成签名，返回app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //应用ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //设备号
        appParameterMap.put("prepayid", map.get("prepay_id"));
        appParameterMap.put("package", "Sign=WXPay");
        //随机字符串
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //时间戳
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //签名
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
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		return outputMarshal(rspMsg);
	}
	
	@Override
	public String continueAdvertisement(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 发布广告>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		xstream.alias("advertisement", com.platform.app.service.xmlpo.Advertisement.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 发布广告>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 发布广告>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		com.platform.app.service.xmlpo.Advertisement a = reqDetail.getAdvertisement();
		if(a == null) {
			logger.info(l + " 发布广告>>---------advertisement节点不存在");
			return error(loginName, identifyCode, l,"4000", "advertisement节点不存在");
		}
		
		String id = a.getId();
		if(id == null || id.equals("")) {
			logger.info(l + " 发布广告>>---- -----advertisement.id为空");
			return error(loginName, identifyCode, l,"4000", "advertisement.id为空");
		}
		
		Advertisement oldAdver = advertisementService.getAdvertisement(id);
		if(oldAdver == null) {
			logger.info(l + " 发布广告>>---- -----未找到推荐信息，advertisement.id：" + id);
			return error(loginName, identifyCode, l,"4000", "未找到推荐信息，advertisement.id：" + id);
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
		if("免费试用".equals(a.getMonths())) {
			adver.setMonths(1);
			adver.setUnitPrice(0);
			adver.setTotalMoney(0);
			adver.setRemark("免费试用");
			adver.setPayStatus("1");
			adver = advertisementService.saveAdvertisement(adver);
		}else {
			adver.setMonths(Integer.valueOf(a.getMonths()));
			if(a.getUnitPrice()==null) {
				//设置单价
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
		
		if(adver.getTotalMoney() == 0) {//总价为0时，表示免费发布，此时无需走支付接口
			adver.setPayStatus("1");
			RspMsg rspMsg = new RspMsg();
			RspDetail rspDetail = new RspDetail();
			rspDetail.setAdvertisement(transAdvertisement(adver));
			rspMsg.setRspDetail(rspDetail);
			rspMsg.setRspResult("1000");
			rspMsg.setRspDesc("成功");
			rspMsg.setOperater(reqMsg.getOperater());
			rspMsg.setIdentification(reqMsg.getIdentification());
			return outputMarshal(rspMsg);
		}
				
		//调用微信支付统一下单接口
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //应用ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //设备号
        parameterMap.put("device_info", "WEB");
        //随机字符串
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        //商品描述
        parameterMap.put("body", "renhelaowu-sign");
        //商户订单号 
        parameterMap.put("out_trade_no", adver.getId());
        //货币类型
        parameterMap.put("fee_type", "CNY");  
        //总金额  单位：分
        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
        parameterMap.put("total_fee", df.format(adver.getTotalMoney()*100));  
        //终端IP
        parameterMap.put("spbill_create_ip", reqMsg.getClientIp());
        //交易起始时间
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        parameterMap.put("time_start", sdf.format(cal.getTime()));
        //交易结束时间  10分钟后订单失效
        cal.add(Calendar.MINUTE, 10);
        parameterMap.put("time_expire", sdf.format(cal.getTime()));
        //通知地址
        parameterMap.put("notify_url", PayCommonUtil.notifyUrl);
        //支付类型
        parameterMap.put("trade_type", "APP");
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        logger.info("jiner2");  
        //签名
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
        if("FAIL".equals(return_code)){//微信通信失败
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("return_msg"));
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	advertisementService.deleteAdvertisement(adver);
        	return error(loginName, identifyCode, l,"4000", map.get("err_code") + " --" + map.get("err_code_des"));
        }
        
        //保存预支付id
        adver.setPrepayId(map.get("prepay_id"));
        adver.setPayStatus("0");
        advertisementService.saveAdvertisement(adver);
        
        //生成签名，返回app
        SortedMap<String, Object> appParameterMap = new TreeMap<String, Object>();
        //应用ID
        appParameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        appParameterMap.put("partnerid", PayCommonUtil.MCH_ID);
        //设备号
        appParameterMap.put("prepayid", map.get("prepay_id"));
        appParameterMap.put("package", "Sign=WXPay");
        //随机字符串
        String noncestr = PayCommonUtil.getRandomString(32);
        appParameterMap.put("noncestr", noncestr);  
        //时间戳
        String timeStr = Calendar.getInstance().getTimeInMillis()/1000 + "";
        appParameterMap.put("timestamp", timeStr);
        String appSignStr = PayCommonUtil.createSign("UTF-8", appParameterMap); 
        
        //签名
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
		rspMsg.setRspDesc("成功");
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
		sb.append("  <rspDesc>成功</rspDesc>");
		sb.append("  <rspDetail>");
		sb.append("    <firstList>");
		sb.append("      <first>");
		sb.append("        <firstCode>普工</firstCode>");
		sb.append("        <firstName>普工</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>普工</secondCode>");
		sb.append("        		<secondName>普工</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>");
		sb.append("      <first>");
		sb.append("        <firstCode>保洁</firstCode>");
		sb.append("        <firstName>保洁</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>保洁</secondCode>");
		sb.append("        		<secondName>保洁</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>      ");
		sb.append("      <first>");
		sb.append("        <firstCode>建筑装修</firstCode>");
		sb.append("        <firstName>建筑装修</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>瓦工</secondCode>");
		sb.append("        		<secondName>瓦工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>钢筋工</secondCode>");
		sb.append("        		<secondName>钢筋工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>架子工</secondCode>");
		sb.append("        		<secondName>架子工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>建筑木工</secondCode>");
		sb.append("        		<secondName>建筑木工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>家装木工</secondCode>");
		sb.append("        		<secondName>家装木工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>电工</secondCode>");
		sb.append("        		<secondName>电工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>水暖工</secondCode>");
		sb.append("        		<secondName>水暖工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>防水工</secondCode>");
		sb.append("        		<secondName>防水工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>腻子工</secondCode>");
		sb.append("        		<secondName>腻子工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>油漆工</secondCode>");
		sb.append("        		<secondName>油漆工</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>外墙保温</secondCode>");
		sb.append("        		<secondName>外墙保温</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>外墙涂料</secondCode>");
		sb.append("        		<secondName>外墙涂料</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>      ");
		sb.append("      <first>");
		sb.append("        <firstCode>电气焊类</firstCode>");
		sb.append("        <firstName>电气焊类</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>二保焊</secondCode>");
		sb.append("        		<secondName>二保焊</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>电焊</secondCode>");
		sb.append("        		<secondName>电焊</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>氩弧焊</secondCode>");
		sb.append("        		<secondName>氩弧焊</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>铆焊</secondCode>");
		sb.append("        		<secondName>铆焊</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>气割气焊</secondCode>");
		sb.append("        		<secondName>气割气焊</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>      ");
		sb.append("      <first>");
		sb.append("        <firstCode>安装类</firstCode>");
		sb.append("        <firstName>安装类</firstName>");
		sb.append("        <secondList>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>彩钢打板安装</secondCode>");
		sb.append("        		<secondName>彩钢打板安装</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>钢结构安装</secondCode>");
		sb.append("        		<secondName>钢结构安装</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>门窗安装</secondCode>");
		sb.append("        		<secondName>门窗安装</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>空调制冷安装</secondCode>");
		sb.append("        		<secondName>空调制冷安装</secondName>");
		sb.append("        	</second>");
		sb.append("        	<second>");
		sb.append("        		<secondCode>机械安装</secondCode>");
		sb.append("        		<secondName>机械安装</secondName>");
		sb.append("        	</second>");
		sb.append("        </secondList>");
		sb.append("      </first>");
		sb.append("    </firstList>");
		sb.append("  </rspDetail>");
		sb.append("</rspMsg>");
		return sb.toString();
	}
}
