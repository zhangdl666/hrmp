package com.platform.business.service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

public class MobileMessageServiceImpl implements MobileMessageService {

	@Override
	public String sendMessage(String mobile, String content) {
		String url = "http://www.stongnet.com/sdkhttp/sendsms.aspx";
		String regCode = "101100-WEB-HUAX-115847"; // 华兴软通注册码，请在这里填写您从客服那得到的注册码
		String regPasswod = "FTPAKUZJ"; // 华兴软通注册码对应的密码，请在这里填写您从客服那得到的注册码
		String sourceAdd = null;		//子通道号（最长10位，可为空
		//String phone = "15812345678";		//手机号码（最多1000个），多个用英文逗号(,)隔开，不可为空
		/*
         *  签名:工信部规定,签名表示用户的真实身份,请不要在签名中冒用别人的身份,如客户使用虚假身份我们将封号处理并以诈骗为由提交工信部备案，一切责任后果由客户承担
         *  华兴软通短信系统要求签名必须附加在短信内容的尾部,以全角中文中括号包括,且括号之后不能再有空格,否则将导致发送失败
         *  虽然在程序中,签名是附加在短信内容的尾部,但是真实短信送达到用户手机时,签名则可能出现在短信的头部,这是各地运营商的政策不同,会在它们自己的路由对签名的位置做调整
         *  短信内容的长度计算会包括签名;签名内容的长度限制受政策变化,具体请咨询客服
         *  写在程序里是让用户自定义签名的方式,还有一种方式是让客服绑定签名,这种方式签名不需要写在程序中,具体请咨询客服
         */
		try {
			content = URLEncoder.encode(content,"UTF-8");		//content中含有空格，换行，中文等非ascii字符时，需要进行url编码，否则无法正确传输到服务器
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
		String param = "reg=" + regCode + "&pwd=" + regPasswod + "&sourceadd=" + sourceAdd + "&phone=" + mobile + "&content=" + content;
		
		String returnStr = this.requestPost(url, param);
		System.out.println(returnStr);
		return returnStr;
	}
	
	/**
	 * HTTP的Post请求方式(推荐)
	 * @param strUrl 请求地址
	 * @param param 参数字符串
	 * @return 返回字符串
	 */
	public String requestPost(String strUrl, String param) {
		System.out.println("HTTPS的POST请求:" + strUrl + "?" + param);
		String returnStr = null; // 返回结果定义
		URL url = null;
		HttpsURLConnection httpsURLConnection = null;
		
		try {
			url = new URL(strUrl);
			httpsURLConnection = (HttpsURLConnection) url.openConnection();

			//使用PEM证书格式(推荐)
			String dir = this.getClass().getClassLoader().getResource("").toString();
			dir = dir.replace("file:/", "");
			dir = dir.substring(0, dir.length()-8);
			String trustCertPath = dir + "cacert.pem";		//证书文件路径，发布项目时打包到resource路径
			httpsURLConnection.setSSLSocketFactory(this.initSSLSocketFactoryByPEM(trustCertPath)); // 设置套接工厂
			
			//使用jks格式信任库设置
			//String fileTruseStore = "F:/cert/WoSign.jks";		//信任库文件，发布项目时打包到resource路径,可以用相对路径
			//String pwTruseStore = "hxrt_sms_demo";		//默认密码，和库文件绑定的，不需要改，如果一定要改请和客服联系
			//httpsURLConnection.setSSLSocketFactory(HttpRuquest.initSSLSocketFactoryByJKS()); // 设置套接工厂
			
			httpsURLConnection.setRequestProperty("Accept-Charset", "utf-8");
			httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			httpsURLConnection.setDoOutput(true);
			httpsURLConnection.setDoInput(true);
			httpsURLConnection.setRequestMethod("POST"); // post方式
			httpsURLConnection.connect();

			//POST方法时使用
			byte[] byteParam = param.getBytes("UTF-8");
			DataOutputStream out = new DataOutputStream(httpsURLConnection.getOutputStream());
			out.write(byteParam);
			out.flush();
			out.close();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(httpsURLConnection.getInputStream(), "utf-8"));
			StringBuffer buffer = new StringBuffer();
			String line = "";
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}

			reader.close();
			returnStr = buffer.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			if (httpsURLConnection != null) {
				httpsURLConnection.disconnect();
			}
		}
		return returnStr;
	}
	
	//使用PEM根证书文件然后用别名的方式设置到KeyStore中去，推荐该方法(PEM根证书能从公开地址下载到)
	public SSLSocketFactory initSSLSocketFactoryByPEM(String trustCertPath) throws Exception {
		KeyStore keyStore = KeyStore.getInstance("jks"); 
		keyStore.load(null, null);		//设置一个空密匙库
		
		FileInputStream trustCertStream = new FileInputStream(trustCertPath);
		CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");			
		Collection<? extends Certificate> certs = cerFactory.generateCertificates(trustCertStream);		//读取多份证书(如果文件流中存在的话)
		for (Certificate certificate : certs) {
			keyStore.setCertificateEntry("" + ((X509Certificate)certificate).getSubjectDN(), certificate);		//遍历集合把证书添加到keystore里，每个证书都必须用不同的唯一别名，否则会被覆盖
		}
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509", "SunJSSE");
		tmf.init(keyStore);
		TrustManager tms[] = tmf.getTrustManagers();
		
		//SSLContext sslcontext = SSLContext.getInstance("TLS");		//SSLContext不能限制协议套件和加密算法
		//sslcontext.init(null, tms, new java.security.SecureRandom());
		//SSLSocketFactory returnSSLSocketFactory = sslcontext.getSocketFactory();
		
		SSLSocketFactoryEx sslSocketFactoryEx = new SSLSocketFactoryEx(null, tms, new java.security.SecureRandom());
		return sslSocketFactoryEx;
	}

}
