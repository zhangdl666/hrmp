package com.platform.business.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.bo.Page;
import com.platform.organization.pojo.OrgUser;

public class WorkHireDaoImpl implements WorkHireDao  {
	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public WorkHire getWorkHire(String id) {
		String hql = "from WorkHire d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (WorkHire)query.uniqueResult();
	}

	@Override
	public WorkHire saveWorkHire(WorkHire wh) {
		sessionFactory.getCurrentSession().saveOrUpdate(wh);
		return wh;
	}

	@Override
	public WorkSign getWorkSign(String id) {
		String hql = "from WorkSign d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (WorkSign)query.uniqueResult();
	}

	@Override
	public WorkSign saveWorkSign(WorkSign ws) {
		sessionFactory.getCurrentSession().saveOrUpdate(ws);
		return ws;
	}

	@Override
	public List<WorkSignBo> getWorkSignList(String workHireId) {
		String hql = "select wh,ws,u from WorkHireView wh,WorkSign ws,OrgUser u where wh.id = ws.workHireId and ws.empId = u.id " +
				"and ws.confirmResult != 'noPass' and ws.confirmResult != 'cancel' and wh.id = ? order by ws.signTime";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, workHireId);
		List<Object[]> list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<WorkSignBo> result = new ArrayList<WorkSignBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			WorkHireView wh = (WorkHireView)o[0];
			WorkSign ws = (WorkSign)o[1];
			OrgUser u = (OrgUser)o[2];
			WorkSignBo bo = new WorkSignBo();
			bo.setWorkSign(ws);
			bo.setEmp(u);
			bo.setWorkHire(wh);
			result.add(bo);
		}
		return result;
	}
	
	@Override
	public int getWorkSignNum(String workHireId) {
		String hql = "select sum(ws.num) from WorkHireView wh,WorkSign ws,OrgUser u where wh.id = ws.workHireId and ws.empId = u.id " +
				"and ws.confirmResult != 'noPass' and ws.confirmResult != 'cancel' and wh.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, workHireId);
		Object obj = query.uniqueResult();
		if(obj == null) {
			return 0;
		}
		
		Long num = (Long)obj; 
		return num.intValue();
	}

	@Override
	public Page getWorkHireList(WorkHireQueryBo bo,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from WorkHireView w where actualSignNum < hireNum");
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getBusinessNumber()!=null && !bo.getBusinessNumber().equals("")){
			sb.append(" and w.businessNumber like ?");
			params.put(paramIndex, "%" + bo.getBusinessNumber() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherId()!=null && !bo.getPublisherId().equals("")){
			sb.append(" and w.publisherId = ?");
			params.put(paramIndex, bo.getPublisherId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherName()!=null && !bo.getPublisherName().equals("")){
			sb.append(" and w.publisherName like ?");
			params.put(paramIndex, "%" + bo.getPublisherName() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeFrom()!=null){
			sb.append(" and w.createTime > ?");
			params.put(paramIndex, bo.getCreateTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeEnd()!=null){
			sb.append(" and w.createTime < ?");
			params.put(paramIndex, bo.getCreateTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getTitle()!=null && !bo.getTitle().equals("")){
			sb.append(" and w.title like ?");
			params.put(paramIndex, "%" + bo.getTitle() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkCompany()!=null && !bo.getWorkCompany().equals("")){
			sb.append(" and w.workCompany like ?");
			params.put(paramIndex, "%" + bo.getWorkCompany() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeFrom()!=null){
			sb.append(" and w.publishTime > ?");
			params.put(paramIndex, bo.getPublishTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeEnd()!=null){
			sb.append(" and w.publishTime < ?");
			params.put(paramIndex, bo.getPublishTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkArea()!=null && !bo.getWorkArea().equals("")){
			sb.append(" and w.workArea like ?");
			params.put(paramIndex, "%" + bo.getWorkArea() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkDescri()!=null && !bo.getWorkDescri().equals("")){
			sb.append(" and w.workDescri like ?");
			params.put(paramIndex, "%" + bo.getWorkDescri() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkKind()!=null && !bo.getWorkKind().equals("")){
			sb.append(" and w.workKind like ?");
			params.put(paramIndex, "%" + bo.getWorkKind() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeFrom()!=null){
			sb.append(" and w.planStartTime > ?");
			params.put(paramIndex, bo.getPlanStartTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeEnd()!=null){
			sb.append(" and w.planStartTime < ?");
			params.put(paramIndex, bo.getPlanStartTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeFrom()!=null){
			sb.append(" and w.planEndTime > ?");
			params.put(paramIndex, bo.getPlanEndTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeEnd()!=null){
			sb.append(" and w.planEndTime < ?");
			params.put(paramIndex, bo.getPlanEndTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getStatus()!=null && !bo.getStatus().equals("")){
			sb.append(" and w.status = ?");
			params.put(paramIndex, bo.getStatus());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getSignUserId()!=null && !bo.getSignUserId().equals("")){
			sb.append(" and exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getSignUserId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getNotSignUserId()!=null && !bo.getNotSignUserId().equals("")){
			sb.append(" and not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getNotSignUserId());
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by w.createTime desc ");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<WorkHire> list = query.list();
		
		if(list == null || list.size() ==0) {
			return page;
		}
		page.setResult(list);
		
		//取记录总数
		String countSql = "select count(w) " + sql;
		Query countQuery = sessionFactory.getCurrentSession().createQuery(countSql);
		setQueryParameter(countQuery, params);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}
	
	@Override
	public List<Object[]> getWorkKindList(WorkHireQueryBo bo) {
		StringBuffer sb = new StringBuffer();
		sb.append("select w.workKind,sum(w.hireNum) from WorkHireView w where actualSignNum < hireNum");
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getBusinessNumber()!=null && !bo.getBusinessNumber().equals("")){
			sb.append(" and w.businessNumber like ?");
			params.put(paramIndex, "%" + bo.getBusinessNumber() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherId()!=null && !bo.getPublisherId().equals("")){
			sb.append(" and w.publisherId = ?");
			params.put(paramIndex, bo.getPublisherId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherName()!=null && !bo.getPublisherName().equals("")){
			sb.append(" and w.publisherName like ?");
			params.put(paramIndex, "%" + bo.getPublisherName() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeFrom()!=null){
			sb.append(" and w.createTime > ?");
			params.put(paramIndex, bo.getCreateTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeEnd()!=null){
			sb.append(" and w.createTime < ?");
			params.put(paramIndex, bo.getCreateTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getTitle()!=null && !bo.getTitle().equals("")){
			sb.append(" and w.title like ?");
			params.put(paramIndex, "%" + bo.getTitle() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkCompany()!=null && !bo.getWorkCompany().equals("")){
			sb.append(" and w.workCompany like ?");
			params.put(paramIndex, "%" + bo.getWorkCompany() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeFrom()!=null){
			sb.append(" and w.publishTime > ?");
			params.put(paramIndex, bo.getPublishTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeEnd()!=null){
			sb.append(" and w.publishTime < ?");
			params.put(paramIndex, bo.getPublishTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkArea()!=null && !bo.getWorkArea().equals("")){
			sb.append(" and w.workArea like ?");
			params.put(paramIndex, "%" + bo.getWorkArea() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkDescri()!=null && !bo.getWorkDescri().equals("")){
			sb.append(" and w.workDescri like ?");
			params.put(paramIndex, "%" + bo.getWorkDescri() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkKind()!=null && !bo.getWorkKind().equals("")){
			sb.append(" and w.workKind = ?");
			params.put(paramIndex, bo.getWorkKind());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeFrom()!=null){
			sb.append(" and w.planStartTime > ?");
			params.put(paramIndex, bo.getPlanStartTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeEnd()!=null){
			sb.append(" and w.planStartTime < ?");
			params.put(paramIndex, bo.getPlanStartTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeFrom()!=null){
			sb.append(" and w.planEndTime > ?");
			params.put(paramIndex, bo.getPlanEndTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeEnd()!=null){
			sb.append(" and w.planEndTime < ?");
			params.put(paramIndex, bo.getPlanEndTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getStatus()!=null && !bo.getStatus().equals("")){
			sb.append(" and w.status = ?");
			params.put(paramIndex, bo.getStatus());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getSignUserId()!=null && !bo.getSignUserId().equals("")){
			sb.append(" and exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getSignUserId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getNotSignUserId()!=null && !bo.getNotSignUserId().equals("")){
			sb.append(" and not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getNotSignUserId());
			paramIndex = paramIndex + 1;
		}
		sb.append(" group by w.workKind order by w.workKind ");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		List<Object[]> list = query.list();
		
		if(list == null || list.size() ==0) {
			return null;
		}
		return list;
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
	public WorkSign getWorkSign(String workHireId, String empId) {
		String hql = "from WorkSign d where d.workHireId = ? and empId = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, workHireId);
		query.setString(1, empId);
		
		List<WorkSign> list = query.list();
		if(list == null || list.size() ==0) {
			return null;
		}
		return list.get(0);
	}

	@Override
	public Page getWorkSignList(WorkHireQueryBo bo, Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append("select w,ws,u from WorkHireView w,WorkSign ws,OrgUser u where w.id = ws.workHireId and ws.empId = u.id ");
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherId()!=null && !bo.getPublisherId().equals("")){
			sb.append(" and w.publisherId = ?");
			params.put(paramIndex, bo.getPublisherId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherName()!=null && !bo.getPublisherName().equals("")){
			sb.append(" and w.publisherName like ?");
			params.put(paramIndex, "%" + bo.getPublisherName() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeFrom()!=null){
			sb.append(" and w.createTime > ?");
			params.put(paramIndex, bo.getCreateTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeEnd()!=null){
			sb.append(" and w.createTime < ?");
			params.put(paramIndex, bo.getCreateTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getTitle()!=null && !bo.getTitle().equals("")){
			sb.append(" and w.title like ?");
			params.put(paramIndex, "%" + bo.getTitle() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkCompany()!=null && !bo.getWorkCompany().equals("")){
			sb.append(" and w.workCompany like ?");
			params.put(paramIndex, "%" + bo.getWorkCompany() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeFrom()!=null){
			sb.append(" and w.publishTime > ?");
			params.put(paramIndex, bo.getPublishTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeEnd()!=null){
			sb.append(" and w.publishTime < ?");
			params.put(paramIndex, bo.getPublishTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkArea()!=null && !bo.getWorkArea().equals("")){
			sb.append(" and w.workArea like ?");
			params.put(paramIndex, "%" + bo.getWorkArea() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkDescri()!=null && !bo.getWorkDescri().equals("")){
			sb.append(" and w.workDescri like ?");
			params.put(paramIndex, "%" + bo.getWorkDescri() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkKind()!=null && !bo.getWorkKind().equals("")){
			sb.append(" and w.workKind like ?");
			params.put(paramIndex, "%" + bo.getWorkKind() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeFrom()!=null){
			sb.append(" and w.planStartTime > ?");
			params.put(paramIndex, bo.getPlanStartTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeEnd()!=null){
			sb.append(" and w.planStartTime < ?");
			params.put(paramIndex, bo.getPlanStartTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeFrom()!=null){
			sb.append(" and w.planEndTime > ?");
			params.put(paramIndex, bo.getPlanEndTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeEnd()!=null){
			sb.append(" and w.planEndTime < ?");
			params.put(paramIndex, bo.getPlanEndTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getStatus()!=null && !bo.getStatus().equals("")){
			sb.append(" and w.status = ?");
			params.put(paramIndex, bo.getStatus());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getSignUserId()!=null && !bo.getSignUserId().equals("")){
			sb.append(" and ws.empId = ?");
			params.put(paramIndex, bo.getSignUserId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getNotSignUserId()!=null && !bo.getNotSignUserId().equals("")){
			sb.append(" and not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getNotSignUserId());
			paramIndex = paramIndex + 1;
		}
		sb.append(" order by w.createTime desc ");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<Object[]> list = query.list();
		
		if(list == null || list.size() ==0) {
			return page;
		}
		List<WorkSignBo> result = new ArrayList<WorkSignBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			WorkHireView wh = (WorkHireView)o[0];
			WorkSign ws = (WorkSign)o[1];
			OrgUser u = (OrgUser)o[2];
			WorkSignBo wsBo = new WorkSignBo();
			wsBo.setWorkSign(ws);
			wsBo.setEmp(u);
			wsBo.setWorkHire(wh);
			result.add(wsBo);
		}
		page.setResult(result);
		
		//取记录总数
		String countSql = "select count(w) " + sql.substring(sql.indexOf("from"),sql.indexOf("order"));
		Query countQuery = sessionFactory.getCurrentSession().createQuery(countSql);
		setQueryParameter(countQuery, params);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

	@Override
	public boolean cancelOtherWorkSign(String workSignId, String userId,
			String remark) {
		String hql = "update WorkSign d set d.confirmResult='cancel',d.confirmTime=sysdate,d.confirmDescri = ? " +
				"where d.id != ? and d.empId = ? and d.confirmResult = 'approving'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, remark);
		query.setString(1, workSignId);
		query.setString(2, userId);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public List<WorkHireVisitBo> getWorkHireVisitBoList(String workHireId) {
		String hql = "select v,u from WorkHireVisit v,OrgUser u where v.visitUser = u.id and v.workHireId = ? order by v.visitTime ";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, workHireId);
		List<Object[]> list = query.list();
		if(list == null || list.size() == 0) {
			return null;
		}
		
		List<WorkHireVisitBo> result = new ArrayList<WorkHireVisitBo>();
		for(int i=0;i<list.size();i++) {
			Object[] o = list.get(i);
			WorkHireVisit v = (WorkHireVisit)o[0];
			OrgUser u = (OrgUser)o[1];
			WorkHireVisitBo bo = new WorkHireVisitBo();
			bo.setId(v.getId());
			bo.setVisitTime(v.getVisitTime());
			bo.setVisitUser(u);
			bo.setWorkHireId(v.getWorkHireId());
			bo.setVisitRecord(v.getVisitRecord());
			result.add(bo);
		}
		return result;
	}

	@Override
	public WorkHireVisit saveWorkHireVisit(WorkHireVisit v) {
		sessionFactory.getCurrentSession().saveOrUpdate(v);
		return v;
	}

	@Override
	public WorkHireVisit getWorkHireVisit(String id) {
		String hql = "from WorkHireVisit d where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (WorkHireVisit)query.uniqueResult();
	}

	@Override
	public Page getWorkKindList(WorkHireQueryBo bo, Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append("select w.workKind,sum(w.hireNum) from WorkHireView w where actualSignNum < hireNum");
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getBusinessNumber()!=null && !bo.getBusinessNumber().equals("")){
			sb.append(" and w.businessNumber like ?");
			params.put(paramIndex, "%" + bo.getBusinessNumber() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherId()!=null && !bo.getPublisherId().equals("")){
			sb.append(" and w.publisherId = ?");
			params.put(paramIndex, bo.getPublisherId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublisherName()!=null && !bo.getPublisherName().equals("")){
			sb.append(" and w.publisherName like ?");
			params.put(paramIndex, "%" + bo.getPublisherName() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeFrom()!=null){
			sb.append(" and w.createTime > ?");
			params.put(paramIndex, bo.getCreateTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getCreateTimeEnd()!=null){
			sb.append(" and w.createTime < ?");
			params.put(paramIndex, bo.getCreateTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getTitle()!=null && !bo.getTitle().equals("")){
			sb.append(" and w.title like ?");
			params.put(paramIndex, "%" + bo.getTitle() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkCompany()!=null && !bo.getWorkCompany().equals("")){
			sb.append(" and w.workCompany like ?");
			params.put(paramIndex, "%" + bo.getWorkCompany() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeFrom()!=null){
			sb.append(" and w.publishTime > ?");
			params.put(paramIndex, bo.getPublishTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPublishTimeEnd()!=null){
			sb.append(" and w.publishTime < ?");
			params.put(paramIndex, bo.getPublishTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkArea()!=null && !bo.getWorkArea().equals("")){
			sb.append(" and w.workArea like ?");
			params.put(paramIndex, "%" + bo.getWorkArea() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkDescri()!=null && !bo.getWorkDescri().equals("")){
			sb.append(" and w.workDescri like ?");
			params.put(paramIndex, "%" + bo.getWorkDescri() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getWorkKind()!=null && !bo.getWorkKind().equals("")){
			sb.append(" and w.workKind = ?");
			params.put(paramIndex, bo.getWorkKind());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeFrom()!=null){
			sb.append(" and w.planStartTime > ?");
			params.put(paramIndex, bo.getPlanStartTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanStartTimeEnd()!=null){
			sb.append(" and w.planStartTime < ?");
			params.put(paramIndex, bo.getPlanStartTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeFrom()!=null){
			sb.append(" and w.planEndTime > ?");
			params.put(paramIndex, bo.getPlanEndTimeFrom());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getPlanEndTimeEnd()!=null){
			sb.append(" and w.planEndTime < ?");
			params.put(paramIndex, bo.getPlanEndTimeEnd());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getStatus()!=null && !bo.getStatus().equals("")){
			sb.append(" and w.status = ?");
			params.put(paramIndex, bo.getStatus());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getSignUserId()!=null && !bo.getSignUserId().equals("")){
			sb.append(" and exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getSignUserId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getNotSignUserId()!=null && !bo.getNotSignUserId().equals("")){
			sb.append(" and not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.empId = ?)");
			params.put(paramIndex, bo.getNotSignUserId());
			paramIndex = paramIndex + 1;
		}
		sb.append(" group by w.workKind order by w.workKind ");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		setQueryParameter(query,params);
		
		
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<Object[]> list = query.list();
		
		if(list == null || list.size() ==0) {
			return page;
		}
		page.setResult(list);
		
		//取记录总数
		String countSql = "select count(w.workKind) " + sql.substring(sql.indexOf("from"));
		Query countQuery = sessionFactory.getCurrentSession().createQuery(countSql);
		setQueryParameter(countQuery, params);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

	@Override
	public void deleteWorkSign(WorkSign ws) {
		if(ws==null ||ws.getId()==null) {
			return;
		}
		sessionFactory.getCurrentSession().delete(ws);
	}

}
