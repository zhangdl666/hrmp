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

import com.platform.business.bo.WorkHireQueryBo;
import com.platform.business.bo.WorkHireVisitBo;
import com.platform.business.bo.WorkSignBo;
import com.platform.business.pojo.BadRecord;
import com.platform.business.pojo.WorkHire;
import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkHireVisit;
import com.platform.business.pojo.WorkSign;
import com.platform.core.ApplicationUtil;
import com.platform.core.bo.Page;
import com.platform.organization.dao.OrgDeptDao;
import com.platform.organization.dao.OrgUserDao;
import com.platform.organization.pojo.OrgDept;
import com.platform.organization.pojo.OrgUser;

public class WorkHireDaoImpl implements WorkHireDao  {
	@Autowired
	private SessionFactory sessionFactory;
	
	@Autowired
	private OrgUserDao orgUserDao;
	
	@Autowired
	private OrgDeptDao orgDeptDao;

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
				"and ws.validStatus = '1' and wh.id = ? order by ws.signTime";
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
		String hql = "select sum(ws.num) from WorkSign ws where ws.validStatus = '1' and ws.workHireId = ?";
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
		if("CB".equals(bo.getEmpTypeId())) {
			sb.append(" from WorkHireView w where 1=1");
		}else {
			sb.append(" from WorkHireView w where actualSignNum < hireNum");
		}
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getEmpTypeId()!=null && !bo.getEmpTypeId().equals("")){
			sb.append(" and w.empTypeId = ?");
			params.put(paramIndex, bo.getEmpTypeId());
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
		
		if(bo.getPublisherCompanyId()!=null && !bo.getPublisherCompanyId().equals("")){
			sb.append(" and w.publisherCompanyId = ?");
			params.put(paramIndex, bo.getPublisherCompanyId());
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
		sb.append(" order by w.publishTime desc ");
		
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
	public void deleteWorkSign(WorkSign ws) {
		if(ws==null ||ws.getId()==null) {
			return;
		}
		sessionFactory.getCurrentSession().delete(ws);
	}

	@Override
	public List<WorkSign> getNoPayList() {
		if(sessionFactory==null) {
			sessionFactory = (SessionFactory) ApplicationUtil.getBean("sessionFactory");
		}
		String hql = "From WorkSign s where validStatus = '1' and payStatus = '0'";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		return query.list();
	}

	@Override
	public WorkHire getLastWorkHire(String loginName, String empTypeId) {
		String hql = "from WorkHire d where exists(select u.id from OrgUser u where u.id = d.publisherId and u.loginName = ?) and d.empTypeId = ? order by createTime desc";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, loginName);
		query.setString(1, empTypeId);
		
		List<WorkHire> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		
		return list.get(0);
	}

	@Override
	public Page getMyWorkHireList(String userId,WorkHireQueryBo bo, Page page) {
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		
		StringBuffer sb = new StringBuffer();
		sb.append(" from WorkHireView w where publisherId = ?");
		params.put(paramIndex, userId);
		paramIndex = paramIndex + 1;
		
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getEmpTypeId()!=null && !bo.getEmpTypeId().equals("")){
			sb.append(" and w.empTypeId = ?");
			params.put(paramIndex, bo.getBusinessNumber());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getBusinessNumber()!=null && !bo.getBusinessNumber().equals("")){
			sb.append(" and w.businessNumber like ?");
			params.put(paramIndex, "%" + bo.getBusinessNumber() + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getEmpTypeId()!=null && !bo.getEmpTypeId().equals("")){
			sb.append(" and w.empTypeId = ?");
			params.put(paramIndex, bo.getEmpTypeId());
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
		
		if(bo.getStatus()!=null && !bo.getStatus().equals("")){
			sb.append(" and w.status = ?");
			params.put(paramIndex, bo.getStatus());
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
	public BadRecord saveBadRecord(BadRecord br) {
		sessionFactory.getCurrentSession().saveOrUpdate(br);
		return br;
	}

	@Override
	public Page getMyBadRecordList(String userId,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from BadRecord b where b.badUserId = ? ");
		
		sb.append(" order by b.recordTime desc ");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
		query.setString(0, userId);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		List<BadRecord> list = query.list();
		
		if(list == null || list.size() ==0) {
			return page;
		}
		page.setResult(list);
		
		//取记录总数
		String countSql = "select count(b) " + sql.substring(sql.indexOf("from"),sql.indexOf("order"));
		Query countQuery = sessionFactory.getCurrentSession().createQuery(countSql);
		countQuery.setString(0, userId);
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

	@Override
	public void closeOverTimePublish() {
		String hql = "update WorkHire d set d.status = 'closed',d.closeTime = sysdate " +
				"where d.empTypeId != 'CQ' and d.status != 'closed' and d.empDate < sysdate - 12/24";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.executeUpdate();
		
		hql = "update WorkHire d set d.status = 'closed',d.closeTime = sysdate " +
				"where d.empTypeId = 'CQ' and d.status != 'closed' and d.empDate < sysdate - 30";
		Query query2 = sessionFactory.getCurrentSession().createQuery(hql);
		query2.executeUpdate();
	}

	@Override
	public Page queryClosedWorkHireList(WorkHireQueryBo bo, Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" from WorkHireView w where status = 'closed' ");
		
		HashMap<Integer, Object> params = new HashMap<Integer, Object>();
		int paramIndex = 0;
		if(bo.getId()!=null && !bo.getId().equals("")){
			sb.append(" and w.id = ?");
			params.put(paramIndex, bo.getId());
			paramIndex = paramIndex + 1;
		}
		
		if(bo.getEmpTypeId()!=null && !bo.getEmpTypeId().equals("")){
			sb.append(" and w.empTypeId = ?");
			params.put(paramIndex, bo.getEmpTypeId());
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
		
		if(bo.getPublisherCompanyId()!=null && !bo.getPublisherCompanyId().equals("")){
			sb.append(" and w.publisherCompanyId = ?");
			params.put(paramIndex, bo.getPublisherCompanyId());
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
		sb.append(" order by w.publishTime desc ");
		
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
	public WorkHire toTopWorkHire(String id) {
		WorkHire wh = getWorkHire(id);
		if(wh==null) {
			return null;
		}
		wh.setPublishTime(Calendar.getInstance().getTime());
		this.saveWorkHire(wh);
		return wh;
	}

	@Override
	public Page queryLSWorkHireForSign(String loginName, Page page) {
		OrgUser loginUser = orgUserDao.getUserByLoginName(loginName);
		if(loginUser == null) {
			return null;
		}
		
		OrgDept company = orgDeptDao.getDirectCompany(loginUser.getDeptId());
		
		StringBuffer sb = new StringBuffer();
		sb.append(" from WorkHireView w where actualSignNum < hireNum");
		sb.append(" and not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.validStatus = '1' and ws.empId = '" + loginUser.getId() + "')");
		sb.append(" and w.empTypeId = 'LS'");
		sb.append(" and w.publisherCompanyId = '" + company.getId() + "'");
		sb.append(" and w.status = '" + WorkHire.WORK_HIRE_STATUS_PUBLISHING + "'");
		sb.append(" order by w.publishTime desc ");
		
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
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
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

	@Override
	public Page queryCQWorkHireForSign(String loginName, Page page) {
		OrgUser loginUser = orgUserDao.getUserByLoginName(loginName);
		if(loginUser == null) {
			return null;
		}
		
		OrgDept company = orgDeptDao.getDirectCompany(loginUser.getDeptId());
		
		StringBuffer sb = new StringBuffer();
		sb.append(" from WorkHireView w where actualSignNum < hireNum");
		sb.append(" and not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.validStatus = '1' and ws.empId = '" + loginUser.getId() + "')");
		sb.append(" and w.empTypeId = 'CQ'");
		sb.append(" and w.publisherCompanyId = '" + company.getId() + "'");
		sb.append(" and w.status = '" + WorkHire.WORK_HIRE_STATUS_PUBLISHING + "'");
		sb.append(" order by w.publishTime desc ");
		
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
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
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

	@Override
	public Page queryCBWorkHireForSign(String loginName, Page page) {
		OrgUser loginUser = orgUserDao.getUserByLoginName(loginName);
		if(loginUser == null) {
			return null;
		}
		
		OrgDept company = orgDeptDao.getDirectCompany(loginUser.getDeptId());
		
		StringBuffer sb = new StringBuffer();
		sb.append(" from WorkHireView w");
		sb.append(" where not exists(select 1 from WorkSign ws where ws.workHireId = w.id and ws.validStatus = '1' and ws.empId = '" + loginUser.getId() + "')");
		sb.append(" and w.empTypeId = 'CB'");
		sb.append(" and w.publisherCompanyId = '" + company.getId() + "'");
		sb.append(" and w.status = '" + WorkHire.WORK_HIRE_STATUS_PUBLISHING + "'");
		sb.append(" order by w.publishTime desc ");
		
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createQuery(sql);
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
		Long count = (Long) countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

}
