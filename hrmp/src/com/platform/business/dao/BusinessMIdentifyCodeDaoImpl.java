package com.platform.business.dao;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.pojo.MIdentifyCode;
import com.platform.business.pojo.MMobileMessage;

public class BusinessMIdentifyCodeDaoImpl implements BusinessMIdentifyCodeDao {

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public MIdentifyCode saveMIdentifyCode(MIdentifyCode mIdentifyCode) {
		sessionFactory.getCurrentSession().save(mIdentifyCode);
		return mIdentifyCode;
	}

	@Override
	public MIdentifyCode getMIdentifyCode(String id) {
		String hql = "from MIdentifyCode d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		List<MIdentifyCode> list = query.list();
		if(list == null || list.size() ==0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public MIdentifyCode getMIdentifyCodeByMobile(String mobile,String identifyCodeType) {
		String hql = "from MIdentifyCode d where d.mobile = ? and d.identifyCodeType = ? order by d.createTime desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, mobile);
		query.setString(1, identifyCodeType);
		
		List<MIdentifyCode> list = query.list();
		if(list == null || list.size() ==0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public int deleteMIdentifyCodeByMobile(String mobile,String identifyCodeType) {
		String hql = "delete from MIdentifyCode d where d.mobile = ? and d.identifyCodeType = ? ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, mobile);
		query.setString(1, identifyCodeType);
		
		return query.executeUpdate();
	}

	@Override
	public MIdentifyCode generateMIdentifyCode(String mobile,
			String identifyCodeType) {
		String random = String.valueOf((int)((Math.random()*9+1)*100000));
		MIdentifyCode code = new MIdentifyCode();
		code.setIdentifyCode(random);
		code.setIdentifyCodeType(identifyCodeType);
		code.setMobile(mobile);
		code.setCreateTime(Calendar.getInstance().getTime());
		
		this.deleteMIdentifyCodeByMobile(mobile, identifyCodeType);
		this.saveMIdentifyCode(code);
		return code;
	}
	
}
