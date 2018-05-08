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
		
		//����Ϊ����״̬ʱ��ÿ�α��涼�����䷢��ʱ�䣬ʵ���ö�Ч����
		if(WorkHire.WORK_HIRE_STATUS_PUBLISHING.equals(wh.getStatus())) {
			wh.setPublishTime(Calendar.getInstance().getTime());
		}
		
		//�а�ʩ����û���������ƣ�Ĭ����Ϊ10000�������й���ѯʱ������actualSignNum < hireNum���˵�
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
			logger.info("�����ڴ�֧��������");
			return ;
		}
		
		for(int i=0;i<signs.size();i++) {
			WorkSign s = signs.get(i);
			
			//��ѯ΢��֧�����
			boolean isPay = isPaySuccess(s.getId());
	        if(isPay) {
	        	logger.info("΢��֧���ɹ���workSignId��" + s.getId());
	        	s.setPayStatus("1");
	        	this.saveWorkSign(s);
	        }else {
	        	Date signTime = s.getSignTime();
	        	Date nowTime = Calendar.getInstance().getTime();
	        	long hours = (nowTime.getTime() - signTime.getTime())/1000/60/60;
	        	if(hours > 0.5){//����0.5Сʱδ֧����ȡ���䶩��
	        		s.setValidStatus("0");
	        		s.setRemark("��ʱδ֧����ȡ������");
	        		this.saveWorkSign(s);
	        	}
	        }
	        
	        logger.info("workSignId��" + s.getId() + "֧�������" + isPay);
		}
	}
	
	@Override
	public boolean isPaySuccess(String workSignId) {
		//��ѯ΢��֧�����
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //�̻������� 
        parameterMap.put("out_trade_no", workSignId);
        //����ַ���
        parameterMap.put("nonce_str", PayCommonUtil.getRandomString(32));  
        String signStr = PayCommonUtil.createSign("UTF-8", parameterMap); 
        //ǩ��
        parameterMap.put("sign", signStr);  
        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
        logger.info(">>>>>>>>>>>>>>>>��΢�Ŷ˲�ѯ΢��֧�����:" + requestXML);  
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
        if("FAIL".equals(return_code)){//΢��ͨ��ʧ��
        	logger.info("΢��֧��������أ�ͨ��ʧ�ܣ�return_code��" + return_code + " --" + map.get("return_msg") + ",workSignId:" + workSignId);
        	return false;
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	logger.info("΢��֧��������أ�ҵ����ʧ�ܣ�err_code��" + map.get("err_code") + " --" + map.get("err_code_des") + ",workSignId:" + workSignId);
        	return false;
        }
        
        String tradeState = map.get("trade_state");
        if("SUCCESS".equals(tradeState)) {
        	logger.info("΢��֧���ɹ���workSignId��" + workSignId);
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
