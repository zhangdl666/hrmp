package com.platform.business.service;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.dao.WorkHireDao;
import com.platform.business.pojo.BadRecord;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.ApplicationUtil;
import com.platform.core.bo.Page;
import com.platform.weixin.PayCommonUtil;

public class WorkHireServiceImpl implements WorkHireService {
	private final Logger logger = Logger.getLogger(WorkHireServiceImpl.class);

	private WorkHireDao workHireDao;
	
	
	public WorkHireDao getWorkHireDao() {
		return workHireDao;
	}

	public void setWorkHireDao(WorkHireDao workHireDao) {
		this.workHireDao = workHireDao;
	}

	@Override
	public WorkHire getWorkHire(String id) {
		return workHireDao.getWorkHire(id);
	}

	@Override
	public WorkHire saveWorkHire(WorkHire wh) {
		if(wh.getAm()==null || "false".equals(wh.getAm())) {
			wh.setAmStart(null);
			wh.setAmEnd(null);
		}
		if(wh.getPm()==null || "false".equals(wh.getPm())) {
			wh.setPmStart(null);
			wh.setPmEnd(null);
		}
		if(wh.getNight()==null || "false".equals(wh.getNight())) {
			wh.setNightStart(null);
			wh.setNightEnd(null);
		}
		
		//工单为发布状态时，每次保存都更新其发布时间，实现置顶效果。
		if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(wh.getStatus())) {
			wh.setPublishTime(Calendar.getInstance().getTime());
		}
		
		//承包施工：没有人数限制，默认其为10000，避免招工查询时被条件actualSignNum < hireNum过滤掉
		if("CB".equals(wh.getEmpTypeId())) {
			wh.setHireNum(10000);
		}
		return workHireDao.saveWorkHire(wh);
	}

	@Override
	public WorkSign getWorkSign(String id) {
		return workHireDao.getWorkSign(id);
	}

	@Override
	public WorkSign saveWorkSign(WorkSign ws) {
		return workHireDao.saveWorkSign(ws);
	}

	@Override
	public List<WorkSignBo> getWorkSignList(String workHireId) {
		return workHireDao.getWorkSignList(workHireId);
	}

	@Override
	public Page getWorkHireList(WorkHireQueryBo bo, Page page) {
		return workHireDao.getWorkHireList(bo, page);
	}

	@Override
	public WorkSign getWorkSign(String workHireId, String empId) {
		return workHireDao.getWorkSign(workHireId, empId);
	}

	@Override
	public Page getWorkSignList(WorkHireQueryBo bo, Page page) {
		return workHireDao.getWorkSignList(bo, page);
	}

	@Override
	public boolean cancelOtherWorkSign(String workSignId, String userId,
			String remark) {
		return workHireDao.cancelOtherWorkSign(workSignId, userId,remark);
	}

	@Override
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId) {
		return workHireDao.getWorkHireVisitBoList(workHireId);
	}

	@Override
	public WorkHireVisit saveWorkHireVisit(WorkHireVisit v) {
		return workHireDao.saveWorkHireVisit(v);
	}

	@Override
	public WorkHireVisit getWorkHireVisit(String id) {
		return workHireDao.getWorkHireVisit(id);
	}

	@Override
	public int getWorkSignNum(String workHireId) {
		return workHireDao.getWorkSignNum(workHireId);
	}

	@Override
	public void deleteWorkSign(WorkSign ws) {
		workHireDao.deleteWorkSign(ws);
	}

	@Override
	public void queryWXPayResultFromWX() {
		
		if(workHireDao==null) {
			workHireDao = (WorkHireDao) ApplicationUtil.getBean("workHireDao");
		}
		List<WorkSign> signs = workHireDao.getNoPayList();
		if(signs==null || signs.size()==0) {
			logger.info("不存在待支付订单！");
			return ;
		}
		
		for(int i=0;i<signs.size();i++) {
			WorkSign s = signs.get(i);
			
			//查询微信支付结果
			boolean isPay = isPaySuccess(s.getId());
	        if(isPay) {
	        	logger.info("微信支付成功，workSignId：" + s.getId());
	        	s.setPayStatus("1");
	        	this.saveWorkSign(s);
	        }else {
	        	Date signTime = s.getSignTime();
	        	Date nowTime = Calendar.getInstance().getTime();
	        	long hours = (nowTime.getTime() - signTime.getTime())/1000/60/60;
	        	if(hours > 0.5){//超过0.5小时未支付，取消其订单
	        		s.setValidStatus("0");
	        		s.setRemark("超时未支付，取消订单");
	        		this.saveWorkSign(s);
	        	}
	        }
	        
	        logger.info("workSignId：" + s.getId() + "支付结果：" + isPay);
		}
	}
	
	@Override
	public boolean isPaySuccess(String workSignId) {
		//查询微信支付结果
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //应用ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //商户订单号 
        parameterMap.put("out_trade_no", workSignId);
        //随机字符串
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        //签名
        parameterMap.put("sign", signStr);  
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
        logger.info(">>>>>>>>>>>>>>>>从微信端查询微信支付结果:" + requestXML);  
        String result = PayCommonUtil.httpsRequest(  
                "https://api.mch.weixin.qq.com/pay/orderquery", "POST",  
                requestXML);
        logger.info(result);
        Map<String, String> map = null;  
        try {  
            map = PayCommonUtil.doXMLParse(result);
        } catch (JDOMException e) {  
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }
        
        String return_code = map.get("return_code");
        if("FAIL".equals(return_code)){//微信通信失败
        	logger.info("微信支付结果返回：通信失败，return_code：" + return_code + " --" + map.get("return_msg") + ",workSignId:" + workSignId);
        	return false;
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	logger.info("微信支付结果返回：业务处理失败，err_code：" + map.get("err_code") + " --" + map.get("err_code_des") + ",workSignId:" + workSignId);
        	return false;
        }
        
        String tradeState = map.get("trade_state");
        if("SUCCESS".equals(tradeState)) {
        	logger.info("微信支付成功，workSignId：" + workSignId);
        	return true;
        }
        return false;
	}

	@Override
	public WorkHire getLastWorkHire(String loginName, String empTypeId) {
		return workHireDao.getLastWorkHire(loginName, empTypeId);
	}

	@Override
	public Page getMyWorkHireList(String userId,WorkHireQueryBo bo, Page page) {
		return workHireDao.getMyWorkHireList(userId,bo,page);
	}

	@Override
	public BadRecord saveBadRecord(BadRecord br) {
		return workHireDao.saveBadRecord(br);
	}

	@Override
	public Page getMyBadRecordList(String userId, Page page) {
		return workHireDao.getMyBadRecordList(userId, page);
	}

	@Override
	public void closeOverTimePublish() {
		workHireDao.closeOverTimePublish();
	}

	@Override
	public Page queryClosedWorkHireList(WorkHireQueryBo bo, Page page) {
		return workHireDao.queryClosedWorkHireList(bo, page);
	}

	@Override
	public WorkHire toTopWorkHire(String id) {
		return workHireDao.toTopWorkHire(id);
	}

	@Override
	public Page queryLSWorkHireForSign(String loginName, Page page) {
		return workHireDao.queryLSWorkHireForSign(loginName, page);
	}

	@Override
	public Page queryCQWorkHireForSign(String loginName, Page page) {
		return workHireDao.queryCQWorkHireForSign(loginName, page);
	}

	@Override
	public Page queryCBWorkHireForSign(String loginName, Page page) {
		return workHireDao.queryCBWorkHireForSign(loginName, page);
	}


}
