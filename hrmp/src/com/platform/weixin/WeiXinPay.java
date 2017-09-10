package com.platform.weixin;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.jdom.JDOMException;

import com.platform.business.action.BaseAction;
import com.platform.business.pojo.WorkSign;
import com.platform.business.service.WorkHireService;

//http://blog.csdn.net/gbguanbo/article/details/50915333
//http://www.cnblogs.com/yimiyan/p/5603657.html
//不错 http://www.cnblogs.com/xu-xiang/p/5797575.html
public class WeiXinPay extends BaseAction {
    protected static Logger logger = Logger.getLogger(WeiXinPay.class);
    
//    String timeMillis = String.valueOf(System.currentTimeMillis() / 1000);
//    String randomString = PayCommonUtil.getRandomString(32);
    
    private WorkHireService workHireService;
//    
//    @SuppressWarnings("unchecked")
//    public Map<String, String> weixinPrePay(String trade_no,BigDecimal totalAmount,  
//            String description, String openid,String sym, HttpServletRequest request) { 
//        SortedMap<String, Object> parameterMap = new TreeMap<String, Object>();
//        //应用ID
//        parameterMap.put("appid", PayCommonUtil.APPID);  
//        //商户号
//        parameterMap.put("mch_id", PayCommonUtil.MCH_ID);
//        //设备号
//        parameterMap.put("device_info", "WEB");
//        //随机字符串
//        parameterMap.put("nonce_str", randomString);  
//        //商品描述
//        parameterMap.put("body", description);
//        //商户订单号 
//        parameterMap.put("out_trade_no", trade_no);
//        //货币类型
//        parameterMap.put("fee_type", "CNY");  
//        //总金额  单位：分
//        BigDecimal total = totalAmount.multiply(new BigDecimal(100));  
//        java.text.DecimalFormat df=new java.text.DecimalFormat("0");  
//        parameterMap.put("total_fee", df.format(total));  
//        //终端IP
//        parameterMap.put("spbill_create_ip", request.getRemoteAddr());
//        //交易起始时间
//        Calendar cal = Calendar.getInstance();
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//        parameterMap.put("time_start", sdf.format(cal.getTime()));
//        //交易结束时间  10分钟后订单失效
//        cal.add(Calendar.MINUTE, 10);
//        parameterMap.put("time_expire", sdf.format(cal.getTime()));
//        //通知地址
//        parameterMap.put("notify_url", sym + wxnotify);
//        //支付类型
//        parameterMap.put("trade_type", "APP");
//        String sign = PayCommonUtil.createSign("UTF-8", parameterMap); 
//        logger.info("jiner2");  
//        //签名
//        parameterMap.put("sign", sign);  
//        String requestXML = PayCommonUtil.getRequestXml(parameterMap);  
//        logger.info(requestXML);  
//        String result = PayCommonUtil.httpsRequest(  
//                "https://api.mch.weixin.qq.com/pay/unifiedorder", "POST",  
//                requestXML);  
//        logger.info(result);
//        Map<String, String> map = null;  
//        try {  
//            map = PayCommonUtil.doXMLParse(result);  
//        } catch (JDOMException e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//        } catch (IOException e) {  
//            // TODO Auto-generated catch block  
//            e.printStackTrace();  
//        }  
//        return map;        
//    }

    public WorkHireService getWorkHireService() {
		return workHireService;
	}

	public void setWorkHireService(WorkHireService workHireService) {
		this.workHireService = workHireService;
	}

	public String wxpayNotify() throws IOException {
        logger.info("微信支付回调");
        InputStream inStream = getRequest().getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        String resultxml = new String(outSteam.toByteArray(), "utf-8");
        Map<String, String> params;
		try {
			params = PayCommonUtil.doXMLParse(resultxml);
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.info("xml 解析异常：" + resultxml);
			return "fail";
		}
        outSteam.close();
        inStream.close();
        if (!PayCommonUtil.isTenpaySign(params)) {
            // 支付失败
        	result("FAIL", "验证回调签名失败");
            return "fail";
        } else {
            logger.info("===============付款成功==============");
            // ------------------------------
            // 处理业务开始
            String id = params.get("out_trade_no");
            WorkSign ws = workHireService.getWorkSign(id);
            ws.setPayStatus("1");
            workHireService.saveWorkSign(ws);
            // 处理业务完毕
            // ------------------------------
            result("SUCCESS", "SUCCESS");
            return "success";
        }
    }
	
	private void result(String code,String msg){
		ServletResponse response = this.getResponse();
		try {
			PrintWriter writer = response.getWriter();
			String s = "<xml>" + "<return_code><![CDATA[" + code
					+ "]]></return_code>" + "<return_msg><![CDATA[" + msg
					+ "]]></return_msg>" + "</xml>";
			writer.write(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}
    
}