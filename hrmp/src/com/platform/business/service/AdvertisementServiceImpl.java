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

import com.platform.business.dao.AdvertisementDao;
import com.platform.business.pojo.Advertisement;
import com.platform.core.ApplicationUtil;
import com.platform.core.bo.Page;
import com.platform.weixin.PayCommonUtil;

public class AdvertisementServiceImpl implements AdvertisementService {
	private final Logger logger = Logger.getLogger(AdvertisementServiceImpl.class);
	private AdvertisementDao advertisementDao;
	
	
	public AdvertisementDao getAdvertisementDao() {
		return advertisementDao;
	}

	public void setAdvertisementDao(AdvertisementDao advertisementDao) {
		this.advertisementDao = advertisementDao;
	}

	
	@Override
	public Advertisement getAdvertisement(String id) {
		return advertisementDao.getAdvertisement(id);
	}

	@Override
	public Advertisement saveAdvertisement(Advertisement adver) {
		if(adver.getClickCount() == null) {
			adver.setClickCount(0);
		}
		if(adver.getEndTime() == null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(adver.getCreateTime());
			calendar.add(Calendar.MONTH, adver.getMonths());
			adver.setEndTime(calendar.getTime());
		}
		return advertisementDao.saveAdvertisement(adver);
	}

	@Override
	public Page queryMyAdvertisementList(String userId,
			Advertisement advertisement, String keyword, Page page) {
		return advertisementDao.queryMyAdvertisementList(userId, advertisement, keyword, page);
	}

	@Override
	public Page queryAdvertisementList(String loginName,Advertisement advertisement,
			String keyword, Page page) {
		return advertisementDao.queryAdvertisementList(loginName,advertisement, keyword, page);
	}

	@Override
	public List<Advertisement> getNoPayList() {
		return advertisementDao.getNoPayList();
	}
	
	@Override
	public void queryWXPayResultFromWX() {
		
		if(advertisementDao==null) {
			advertisementDao = (AdvertisementDao) ApplicationUtil.getBean("advertisementDao");
		}
		List<Advertisement> advers = advertisementDao.getNoPayList();
		if(advers==null || advers.size()==0) {
			logger.info("�����ڴ�֧���Ƽ���");
			return ;
		}
		
		for(int i=0;i<advers.size();i++) {
			Advertisement s = advers.get(i);
			
			//��ѯ΢��֧�����
			boolean isPay = isPaySuccess(s.getId());
	        if(isPay) {
	        	logger.info("΢��֧���ɹ���advertisementId��" + s.getId());
	        	s.setPayStatus("1");
	        	//relationId�ǿգ���ʾ���ѡ����ѳɹ�ʱ���ص�ԭ�Ƽ�
	        	if(s.getRelationId()!=null && !s.getRelationId().equals("")) {
	        		Advertisement old = this.getAdvertisement(s.getRelationId());
	        		if(old != null) {
	        			old.setIsClosed("1");
	        			old.setCloseTime(Calendar.getInstance().getTime());
	        			old.setRemark("���ѳɹ����˾��Ƽ��ر�");
	        			saveAdvertisement(old);
	        			s.setClickCount(old.getClickCount());//�������ͬ�������Ƽ���ʵ���ۼ�
	        		}
	        	}
	        	this.saveAdvertisement(s);
	        	
	        }else {
	        	Date signTime = s.getCreateTime();
	        	Date nowTime = Calendar.getInstance().getTime();
	        	long minutes = (nowTime.getTime() - signTime.getTime())/1000/60;
	        	if(minutes > 5){//����5����δ֧����ȡ���䶩��
	        		s.setValidStatus("0");
	        		s.setRemark("��ʱδ֧����ȡ������");
	        		this.saveAdvertisement(s);
	        	}
	        }
	        
	        logger.info("advertisementId��" + s.getId() + "֧�������" + isPay);
		}
	}
	
	public boolean isPaySuccess(String advertisementId) {
		//��ѯ΢��֧�����
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //Ӧ��ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //�̻���
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //�̻������� 
        parameterMap.put("out_trade_no", advertisementId);
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
        	logger.info("΢��֧��������أ�ͨ��ʧ�ܣ�return_code��" + return_code + " --" + map.get("return_msg") + ",advertisementId:" + advertisementId);
        	return false;
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	logger.info("΢��֧��������أ�ҵ����ʧ�ܣ�err_code��" + map.get("err_code") + " --" + map.get("err_code_des") + ",advertisementId:" + advertisementId);
        	return false;
        }
        
        String tradeState = map.get("trade_state");
        if("SUCCESS".equals(tradeState)) {
        	logger.info("΢��֧���ɹ���advertisementId��" + advertisementId);
        	return true;
        }
        return false;
	}

	@Override
	public void closeOverTimeAdvertisement() {
		advertisementDao.closeOverTimeAdvertisement();
	}

	@Override
	public void deleteAdvertisement(Advertisement adver) {
		advertisementDao.deleteAdvertisement(adver);
	}

	@Override
	public void click(String id) {
		advertisementDao.click(id);		
	}

}
