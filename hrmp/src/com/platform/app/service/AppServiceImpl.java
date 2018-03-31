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
import com.platform.business.pojo.MIdentifyCode;
import com.platform.business.pojo.Message;
import com.platform.business.pojo.UnitPrice;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkKindDef;
import com.platform.business.pojo.WorkSign;
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
import com.platform.organization.pojo.OrgDeptView;
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
		
		String msg = "";
		if("regedit".equals(identifyCodeType)){
			msg = "【仁禾劳务】您的注册验证码" + code.getIdentifyCode() + "，有效期5分钟，切勿告知他人！";
		}else if("pwd".equals(identifyCodeType)) {
			msg = "【仁禾劳务】您正在找回密码，验证码" + code.getIdentifyCode() + "，有效期5分钟，切勿告知他人！";
			
		}
		try {
			//mobileMessageService.sendMessage(loginName, msg);
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
		List<OrgDeptView> comList = orgDeptService.queryDepts(null, "000", false);
		
		for(int i=0;i<comList.size();i++) {
			OrgDeptView d = comList.get(i);
			List<OrgDeptView> deptList = orgDeptService.queryDepts(null, d.getId(), false);
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
	
	private Province transDeptToCity(OrgDeptView com,List<OrgDeptView> deptList){
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
			OrgDeptView v = deptList.get(i);
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
		List<OrgDeptView> list = orgDeptService.queryDepts("注册用户", user.getOrgId(), false);
		if(list==null || list.size()==0) {
			logger.info(l + " 注册>>---------工作地点值不对，" + user.getOrgId());
			return error(loginName, identifyCode, l,"4000", "工作地点值不对，" + user.getOrgId());
		}
		
		//获取用户角色id
		List<OrgRoleBo> rl = orgRoleService.queryRoles("注册用户", list.get(0).getId(), false, false);
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
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 找回密码>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
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
		RspMsg rspMsg = new RspMsg();
		rspMsg.setOperater(loginName);
		rspMsg.setIdentification(newIdentifyCode);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("登录成功");
		
		RspDetail rspDetail = new RspDetail();
		rspDetail.setUserId(user.getId());
		rspDetail.setUserLoginName(user.getLoginName());
		rspDetail.setUserName(user.getUserName());
		
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
			work.setPayMode(h.getPayMode());
			work.setPayModeRemark(h.getPayModeRemark());
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
			String businessNumber = businessNumberService.getNumber("W");
			h.setBusinessNumber(businessNumber);
			h.setCreateTime(Calendar.getInstance().getTime());
			h.setPublisherId(loginUser.getId());
			h.setPublisherName(loginUser.getUserName());
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
		h.setPayMode(work.getPayMode());
		h.setPayModeRemark(work.getPayModeRemark());
		h.setWorkArea(work.getWorkArea());
		h.setWorkDescri(work.getWorkDescri());
		
		h = workHireService.saveWorkHire(h);
		
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspDetail.setBusinessNumber(h.getBusinessNumber());
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
		
		WorkHireQueryBo workHireQueryBo = new WorkHireQueryBo();
		
		//设置查询条件，用于数据隔离
		OrgUser loginUser = orgUserService.getUserByLoginName(loginName);
		OrgDept company = orgDeptService.getDirectCompany(loginUser.getDeptId());
		workHireQueryBo.setEmpTypeId(empTypeId);
		workHireQueryBo.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
		workHireQueryBo.setNotSignUserId(loginUser.getId());
		workHireQueryBo.setPublisherCompanyId(company.getId());
		
		Page p = null;
		if(empTypeId.equals("FINISH")) {
			workHireQueryBo.setEmpTypeId(null);
			p = workHireService.getClosedWorkHireList(workHireQueryBo,page);
		}else {
			p = workHireService.getWorkHireList(workHireQueryBo,page);
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
				//判断是否可以取消报名
				if(!bo.getWorkHire().getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
					work.setCanCancelSign("0");
				}else {
					WorkSign ws = bo.getWorkSign();
					if(ws == null){
						work.setCanCancelSign("0");
					}else {
						if(!ws.getValidStatus().equals("0")) {
							if("1".equals(ws.getPayStatus())){//已支付成功
								work.setCanCancelSign("1");
							}else {
								work.setCanCancelSign("2");//待支付
							}
						}else {
							work.setCanCancelSign("0");
						}
						
						//支付成功，显示联系人及电话
						if("1".equals(ws.getPayStatus())){//已支付成功
							work.setWorkDescri(work.getWorkDescri() + " ，联系人：" + bo.getWorkHire().getPublisherName());
						}
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
			}else {
				boolean isPay = workHireService.isPaySuccess(ws.getId());
				if(isPay) {
					ws.setPayStatus("1");
					workHireService.saveWorkSign(ws);
					rspDetail.setPayStatus("1");
					rspDetail.setPayDescri("支付成功");
					//发送短信通知招工方
					OrgUser worker = orgUserService.getUser(ws.getEmpId());
					try {
						mobileMessageService.sendMessage(emp.getLoginName(), "【仁禾劳务】" + worker.getUserName() + "(" + worker.getLoginName() + ")已报名，请知晓！");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkSign ws = workHireService.getWorkSign(workId, emp.getId());
		if(ws == null) {
			logger.info(l + " 发起微信支付>>---- -----未找到报名信息");
			return error(loginName, identifyCode, l,"4000", "未找到报名信息");
		}
		
		if("1".equals(ws.getPayStatus())) {
			logger.info(l + " 发起微信支付>>---- -----报名已经支付，请勿重复支付");
			return error(loginName, identifyCode, l,"4000", "报名已经支付，请勿重复支付");
		}
		
		WorkHire workHire = workHireService.getWorkHire(workId);
		if(workHire == null) {
			logger.info(l + " 发起微信支付>>---- -----未找到招工信息，请检查参数workId是否有误，workId：" + workId);
			return error(loginName, identifyCode, l,"4000", "未找到招工信息，请检查参数workId是否有误，workId：" + workId);
		}
		
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
		work.setPayStatus(ws.getPayStatus());
		//判断是否可以取消报名
		if(!wh.getStatus().equals(WorkHire.WORK_HIRE_STATUS_PUBLISHING)) {
			work.setCanCancelSign("0");
		}else {
			if(ws.getValidStatus().equals("1")) {
				if("1".equals(ws.getPayStatus())){//已支付成功
					work.setCanCancelSign("1");
				}else {
					work.setCanCancelSign("2");//待支付
				}
			}else {
				work.setCanCancelSign("0");
			}
			
			//支付成功，显示联系人及电话
			if("1".equals(ws.getPayStatus())){//已支付成功
				//显示发布人信息
				work.setPublisherName(wh.getPublisherName());
				work.setPublisherCompanyName(wh.getPublisherCompanyName());
				work.setPublishTime(wh.getPublishTime()==null?"":sdf.format(wh.getPublishTime()));
			}
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
		work.setBusinessNumber(wh.getBusinessNumber());
		work.setEmpTypeId(wh.getEmpTypeId());
		work.setSex(wh.getSex());
		work.setSalary(wh.getSalary());
		work.setSalaryRemark(wh.getSalaryRemark());
		work.setAge(wh.getAge());
		if("true".equals(wh.getAm())) {
			work.setAm("1");
			work.setAmStart(wh.getAmStart());
			work.setAmEnd(wh.getAmEnd());
		}
		if("true".equals(wh.getPm())) {
			work.setPm("1");
			work.setPmStart(wh.getPmStart());
			work.setPmEnd(wh.getPmEnd());
		}
		if("true".equals(wh.getNight())) {
			work.setNight("1");
			work.setNightStart(wh.getNightStart());
			work.setNightEnd(wh.getNightEnd());
		}
		if(wh.getEmpDate()!=null) {
			
			work.setEmpDate(sdf2.format(wh.getEmpDate()));
		}
		work.setCondition(wh.getCondition());
		work.setPayMode(wh.getPayMode());
		work.setPayModeRemark(wh.getPayModeRemark());
		work.setWorkArea(wh.getWorkArea());
		work.setCloseTime(wh.getCloseTime()==null?"":sdf.format(wh.getCloseTime()));
		work.setWorkDescri(wh.getWorkDescri());
		work.setWorkKind(wh.getWorkKind());
		work.setHireNum("" + wh.getHireNum());
		work.setStatus(wh.getStatus());
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
		
		if("closed".equals(wh.getStatus())) {
			logger.info(l + " 关闭发布>>---- -----已经关闭，请勿重复操作");
			return error(loginName, identifyCode, l,"4000", "已经关闭，请勿重复操作");
		}
		
		boolean isHaveSign = false;//是否已有工人报名
		List<WorkSignBo> signList = workHireService.getWorkSignList(workId);
		if(signList!=null && signList.size()>0) {//已有用户报名
			isHaveSign = true;
		}
		
		//若当前状态不是closing，则验证是否已有工人报名
		if(!WorkHire.WORK_HIRE_STATUS_CLOSING.equals(wh.getStatus())) {
			if(isHaveSign) {//已有用户报名
				logger.info(l + " 关闭发布>>---- -----已有工人报名，此时关闭可能会影响您的信誉，确认关闭吗？");
				wh.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSING);
				workHireService.saveWorkHire(wh);
				return error(loginName, identifyCode, l,"2000", "已有工人报名，此时关闭可能会影响您的信誉，确认关闭吗？");
			}
		}
		
		wh.setStatus(WorkHire.WORK_HIRE_STATUS_CLOSED);
		wh.setCloseTime(calendar.getTime());
		workHireService.saveWorkHire(wh);
		
		//记录办理记录
		businessOpinionService.saveBusinessOpinion(wh.getId(),emp,"关闭招工","关闭招工");
		
		//发送消息通知客服
		if(isHaveSign) {
			try {
				businessMessageService.notifyAdminAsClosePublish(wh, emp);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
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
	public String cancelClosePublish(String requestXml) {
		Calendar calendar = Calendar.getInstance();
		long l = calendar.getTimeInMillis();
		logger.info(l + " 取消关闭发布>>请求报文---------" + requestXml);
		
		XStream xstream = new XStream();
		xstream.alias("reqMsg", ReqMsg.class);
		xstream.alias("reqDetail", ReqDetail.class);
		
		ReqMsg reqMsg = (ReqMsg) xstream.fromXML(requestXml);
		
		String loginName = reqMsg.getOperater();
		String identifyCode = reqMsg.getIdentification();
		ReqDetail reqDetail = reqMsg.getReqDetail();
		
		//检查报文完整性
		if(loginName == null || "".equals(loginName)) {
			logger.info(l + " 取消关闭发布>>---------operater值不允许为空");
			return error(loginName, identifyCode, l,"4000", "用户名不允许为空");
		}
		
		if(reqDetail == null) {
			logger.info(l + " 取消关闭发布>>---- -----reqDetail节点不存在");
			return error(loginName, identifyCode, l,"4000", "reqDetail节点不存在");
		}
		
		String workId = reqDetail.getWorkId();
		OrgUser emp = orgUserService.getUserByLoginName(loginName);
		WorkHire wh = workHireService.getWorkHire(workId);
		if(wh == null) {
			logger.info(l + " 取消关闭发布>>---- -----未找到发布信息");
			return error(loginName, identifyCode, l,"4000", "未找到发布信息");
		}
		
		if(!WorkHire.WORK_HIRE_STATUS_CLOSING.equals(wh.getStatus())) {
			logger.info(l + " 取消关闭发布>>---- -----当前状态不是closing，无法取消");
			return error(loginName, identifyCode, l,"4000", "当前状态不是closing，无法取消");
		}
		
		wh.setStatus(WorkHire.WORK_HIRE_STATUS_PUBLISHING);
		wh.setCloseTime(null);
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
		
		RspMsg rspMsg = new RspMsg();
		RspDetail rspDetail = new RspDetail();
		rspMsg.setRspDetail(rspDetail);
		rspMsg.setRspResult("1000");
		rspMsg.setRspDesc("成功");
		rspMsg.setOperater(reqMsg.getOperater());
		rspMsg.setIdentification(reqMsg.getIdentification());
		
		return outputMarshal(rspMsg);
	}
}
