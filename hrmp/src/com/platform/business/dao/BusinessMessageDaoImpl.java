package com.platform.business.dao;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.bo.MessageBo;
import com.platform.business.bo.MessageQueryBo;
import com.platform.business.pojo.Message;
import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgUser;

public class BusinessMessageDaoImpl implements BusinessMessageDao{

	@Autowired
	private SessionFactory sessionFactory;
	
	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public Message saveMessage(Message message) {
		sessionFactory.getCurrentSession().save(message);
		return message;
	}


	@Override
	public MessageBo getMessage(String id) {
		String hql = "select m,ru,su from Message m,OrgUser ru,OrgUser su where m.sendUserId = su.id and m.receiverUserId = ru.id and m.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		Object[] o = (Object[])query.uniqueResult();
		if(o == null || o.length==0){
			return null;
		}
		
		Message m = (Message)o[0];
		OrgUser receiveUser = (OrgUser)o[1];
		OrgUser sendUser = (OrgUser)o[2];
		
		MessageBo bo = new MessageBo();
		bo.setCreateTime(m.getCreateTime());
		bo.setId(m.getId());
		bo.setIsRead(m.getIsRead());
		bo.setMessageContent(m.getMessageContent());
		bo.setMessageTitle(m.getMessageTitle());
		bo.setReadTime(m.getReadTime());
		bo.setReceiverUser(receiveUser);
		bo.setSendUser(sendUser);
		return bo;
	}


	@Override
	public Page queryMessages(MessageQueryBo bo, Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select m,ru,su from Message m,OrgUser ru,OrgUser su ");
		sb.append(" where m.sendUserId = su.id and m.receiverUserId = ru.id ");
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		
		if(bo.getCreateTimeFrom()!=null && !bo.getCreateTimeFrom().equals("")){
			sb.append(" and m.createTime > ?");
			params.put(paramIndex, bo.getCreateTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeEnd()!=null && !bo.getCreateTimeEnd().equals("")){
			sb.append(" and m.createTime < ?");
			params.put(paramIndex, bo.getCreateTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getIsRead()!=null && !bo.getIsRead().equals("")){
			sb.append(" and m.isRead = ?");
			params.put(paramIndex, bo.getIsRead());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getMessageContent()!=null && !bo.getMessageContent().equals("")){
			sb.append(" and m.messageContent like ?");
			params.put(paramIndex, "%" + bo.getMessageContent() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getMessageTitle()!=null && !bo.getMessageTitle().equals("")){
			sb.append(" and m.messageTitle like ?");
			params.put(paramIndex, "%" + bo.getMessageTitle() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getReadTimeFrom()!=null && !bo.getReadTimeFrom().equals("")){
			sb.append(" and m.readTime > ?");
			params.put(paramIndex, bo.getReadTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getReadTimeEnd()!=null && !bo.getReadTimeEnd().equals("")){
			sb.append(" and m.readTime < ?");
			params.put(paramIndex, bo.getReadTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getReceiverUserName()!=null && !bo.getReceiverUserName().equals("")){
			sb.append(" and ru.userName like ?");
			params.put(paramIndex, "%" + bo.getReceiverUserName() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getSendUserName()!=null && !bo.getSendUserName().equals("")){
			sb.append(" and su.userName like ?");
			params.put(paramIndex, "%" + bo.getSendUserName() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getSendUserId()!=null && !bo.getSendUserId().equals("")){
			sb.append(" and su.id = ?");
			params.put(paramIndex, bo.getSendUserId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getReceiverUserId()!=null && !bo.getReceiverUserId().equals("")){
			sb.append(" and ru.id = ?");
			params.put(paramIndex, bo.getReceiverUserId());
			paramIndex = paramIndex + 1;
		}
		
		sb.append(" order by m.createTime desc ");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<Object[]> list = query.list();
		
		if(list == null || list.size() ==0) {
			return page;
		}
		List<MessageBo> result = new ArrayList<MessageBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			Message m = (Message)o[0];
			OrgUser receiveUser = (OrgUser)o[1];
			OrgUser sendUser = (OrgUser)o[2];
			
			MessageBo messageBo = new MessageBo();
			messageBo.setCreateTime(m.getCreateTime());
			messageBo.setId(m.getId());
			messageBo.setIsRead(m.getIsRead());
			messageBo.setMessageContent(m.getMessageContent());
			messageBo.setMessageTitle(m.getMessageTitle());
			messageBo.setReadTime(m.getReadTime());
			messageBo.setReceiverUser(receiveUser);
			messageBo.setSendUser(sendUser);
			result.add(messageBo);
		}
		page.setResult(result);
		
		//取记录总数
		String countSql = "select count(m) " + sql.substring(sql.indexOf("from"),sql.indexOf("order"));
		Query countQuery = sessionFactory.getCurrentSession().createQuery(countSql);
		setQueryParameter(countQuery, params);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}
	
	// 设置query参数
	private void setQueryParameter(Query query, Map<Integer, Object> paraMap) {
		if (paraMap == null || paraMap.isEmpty()) {
			return;
		}

		Set<Integer> keys = paraMap.keySet();
		Iterator<Integer> it = keys.iterator();
		while (it.hasNext()) {
			Integer key = it.next();
			query.setParameter(key, paraMap.get(key));
		}
	}

	@Override
	public boolean readMessage(String id) {
		String hql = "update Message m set m.isRead = 1 , m.readTime = ? where m.id = ? and m.isRead = 0";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setDate(0, Calendar.getInstance().getTime());
		query.setString(1, id);
		
		return (query.executeUpdate() > 0);
	}

}
