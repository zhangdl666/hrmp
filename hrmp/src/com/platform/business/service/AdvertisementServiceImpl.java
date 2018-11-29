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
			logger.info("不存在待支付推荐！");
			return ;
		}
		
		for(int i=0;i<advers.size();i++) {
			Advertisement s = advers.get(i);
			
			//查询微信支付结果
			boolean isPay = isPaySuccess(s.getId());
	        if(isPay) {
	        	logger.info("微信支付成功，advertisementId：" + s.getId());
	        	s.setPayStatus("1");
	        	//relationId非空，表示续费。续费成功时，关掉原推荐
	        	if(s.getRelationId()!=null && !s.getRelationId().equals("")) {
	        		Advertisement old = this.getAdvertisement(s.getRelationId());
	        		if(old != null) {
	        			old.setIsClosed("1");
	        			old.setCloseTime(Calendar.getInstance().getTime());
	        			old.setRemark("续费成功，此旧推荐关闭");
	        			saveAdvertisement(old);
	        			s.setClickCount(old.getClickCount());//浏览次数同步到新推荐，实现累计
	        		}
	        	}
	        	this.saveAdvertisement(s);
	        	
	        }else {
	        	Date signTime = s.getCreateTime();
	        	Date nowTime = Calendar.getInstance().getTime();
	        	long minutes = (nowTime.getTime() - signTime.getTime())/1000/60;
	        	if(minutes > 5){//超过5分钟未支付，取消其订单
	        		s.setValidStatus("0");
	        		s.setRemark("超时未支付，取消订单");
	        		this.saveAdvertisement(s);
	        	}
	        }
	        
	        logger.info("advertisementId：" + s.getId() + "支付结果：" + isPay);
		}
	}
	
	public boolean isPaySuccess(String advertisementId) {
		//查询微信支付结果
		SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
        //应用ID
        parameterMap.put("appid", PayCommonUtil.APPID);  
        //商户号
        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
        //商户订单号 
        parameterMap.put("out_trade_no", advertisementId);
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
        	logger.info("微信支付结果返回：通信失败，return_code：" + return_code + " --" + map.get("return_msg") + ",advertisementId:" + advertisementId);
        	return false;
        }
        
        String result_code = map.get("result_code");
        if("FAIL".equals(result_code)){
        	logger.info("微信支付结果返回：业务处理失败，err_code：" + map.get("err_code") + " --" + map.get("err_code_des") + ",advertisementId:" + advertisementId);
        	return false;
        }
        
        String tradeState = map.get("trade_state");
        if("SUCCESS".equals(tradeState)) {
        	logger.info("微信支付成功，advertisementId：" + advertisementId);
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
