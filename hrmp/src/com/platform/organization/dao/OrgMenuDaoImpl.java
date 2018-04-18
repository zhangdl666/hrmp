package com.platform.organization.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.platform.core.bo.Page;
import com.platform.organization.bo.OrgMenuBo;
import com.platform.organization.pojo.OrgMenu;

public class OrgMenuDaoImpl implements OrgMenuDao {

	@Autowired
	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<OrgMenuBo> getUserMenu(String userId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select distinct m.id,m.menuname,m.parent_id,m.url_,m.order_,m.remark");
		sb.append("   from org_menu m where m.validstatus = '1'");
		sb.append("  start with m.id in");
		sb.append("             (select distinct rm.menu_id");
		sb.append("                from org_role_menu rm");
		sb.append("               where rm.role_id in ((select ur.role_id");
		sb.append("                                     from org_user_role ur");
		sb.append("                                    where ur.user_id = ?)))");
		sb.append(" connect by prior parent_id = id");
		sb.append("  order by parent_id, order_");
		String sql = sb.toString();
		Query query = null;
		userId = "000";
		if("000".equals(userId)) {
			String adminSql = "select m.id,m.menuname,m.parent_id,m.url_,m.order_,m.remark from org_menu m where m.validstatus = '1' ";
			query = sessionFactory.getCurrentSession().createSQLQuery(adminSql);
		}else {
			query = sessionFactory.getCurrentSession().createSQLQuery(sql);
			query.setString(0, userId);
		}
		
		List<Object[]> tempList = query.list();
		if(tempList == null || tempList.size()==0) {
			return null;
		}
		
		List<OrgMenuBo> list = new ArrayList<OrgMenuBo>();
		for(int i=0; i<tempList.size(); i++) {
			Object[] o = tempList.get(i);
			OrgMenuBo bo = new OrgMenuBo();
			bo.setId(o[0]==null?null:(String)o[0]);
			bo.setMenuName(o[1]==null?null:(String)o[1]);
			bo.setParentId(o[2]==null?null:(String)o[2]);
			//bo.setMenuPath(o[3]==null?null:(String)o[3]);
			bo.setMenuUrl(o[3]==null?null:(String)o[3]);
			bo.setMenuIndex(o[4]==null?null:((Number)o[4]).intValue());
			bo.setRemark(o[5]==null?null:(String)o[5]);
			list.add(bo);
		}
		
		List<OrgMenuBo> result = new ArrayList<OrgMenuBo>();
		//取一级菜单，放到result中
		for(int i=0; i<list.size(); i++) {
			OrgMenuBo menu = list.get(i);
			if(menu.getParentId() != null && "000".equals(menu.getParentId())) {
				result.add(menu);
			}
		}
		
		if(result.size() == 0) {
			return null;
		}

		//取二级菜单
		List<OrgMenuBo> secondLevelMenu = new ArrayList<OrgMenuBo>();
		for(int i=0; i<result.size(); i++) {
			OrgMenuBo bo = result.get(i);
			for(int j=0; j<list.size(); j++) {
				OrgMenuBo menu = list.get(j);
				if(menu.getParentId() != null && menu.getParentId().equals(bo.getId())){
					if(bo.getChildMenus()!=null){
						bo.getChildMenus().add(menu);
					}else {
						List<OrgMenuBo> l = new ArrayList<OrgMenuBo>();
						l.add(menu);
						bo.setChildMenus(l);
					}
					secondLevelMenu.add(menu);
				}
			}
		}
		
		//取三级菜单
		List<OrgMenuBo> threeLevelMenu = new ArrayList<OrgMenuBo>();
		for(int i=0; i<secondLevelMenu.size(); i++) {
			OrgMenuBo bo = secondLevelMenu.get(i);
			for(int j=0; j<list.size(); j++) {
				OrgMenuBo menu = list.get(j);
				if(menu.getParentId() != null && menu.getParentId().equals(bo.getId())){
					if(bo.getChildMenus()!=null){
						bo.getChildMenus().add(menu);
					}else {
						List<OrgMenuBo> l = new ArrayList<OrgMenuBo>();
						l.add(menu);
						bo.setChildMenus(l);
					}
					threeLevelMenu.add(menu);
				}
			}
		}
		
		return result;
	}
	
	private OrgMenuBo transOrgMenuToBO(OrgMenu menu) {
		if(menu == null) {
			return null;
		}
		OrgMenuBo bo = new OrgMenuBo();
		bo.setId(menu.getId());
		bo.setCreateTime(menu.getCreateTime());
		bo.setMenuIndex(menu.getMenuIndex());
		bo.setMenuName(menu.getMenuName());
		bo.setMenuUrl(menu.getMenuUrl());
		bo.setParentId(menu.getParentId());
		bo.setRemark(menu.getRemark());
		bo.setValidstatus(menu.getValidstatus());
		return bo;
	}

	@Override
	public OrgMenu getOrgMenu(String id) {
		String hql = "from OrgMenu r where r.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (OrgMenu)query.uniqueResult();
	}

	@Override
	public List<OrgMenu> queryMenus(String menuName, String url,String parentMenuId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {m.*} from org_menu m");
		sb.append("  where m.validstatus = '1'");
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(menuName != null && !"".equals(menuName)){
			sb.append(" and m.menuName like ?");
			params.put(paramIndex, "%" + menuName + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(url != null && !"".equals(url)){
			sb.append(" and m.url_ like ?");
			params.put(paramIndex, "%" + url + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(parentMenuId != null && !"".equals(parentMenuId)){
			sb.append("    and m.parent_Id = ? ");
			params.put(paramIndex, parentMenuId);
			paramIndex = paramIndex + 1;
			
		}
		
		sb.append(" order by m.menuName");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("m",OrgMenu.class);
		setQueryParameter(query,params);
		
		List<OrgMenu> list = query.list();
		if(list==null || list.size()==0) {
			return null;
		}
		return list;
		
	}
	
	// 设置query参数
	private void setQueryParameter(Query query, Map<Integer, String> paraMap) {
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
	public Page queryMenus(String menuName, String url,String parentMenuId,Page page) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select {m.*} from org_menu m");
		sb.append("  where m.validstatus = '1'");
		HashMap<Integer, String> params = new HashMap<Integer, String>();
		int paramIndex = 0;
		if(menuName != null && !"".equals(menuName)){
			sb.append(" and m.menuName like ?");
			params.put(paramIndex, "%" + menuName + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(url != null && !"".equals(url)){
			sb.append(" and m.url_ like ?");
			params.put(paramIndex, "%" + url + "%");
			paramIndex = paramIndex + 1;
		}
		
		if(parentMenuId != null && !"".equals(parentMenuId)){
			sb.append("    and m.parent_Id = ? ");
			params.put(paramIndex, parentMenuId);
			paramIndex = paramIndex + 1;
			
		}
		
		sb.append(" order by m.menuName");
		
		String sql = sb.toString();
		Query query = sessionFactory.getCurrentSession().createSQLQuery(sql).addEntity("m",OrgMenu.class);
		setQueryParameter(query,params);
		query.setFirstResult(page.getCurrentPageOffset());
		query.setMaxResults(page.getPageSize());
		
		List<Object[]> list = query.list();
		if(list==null || list.size()==0) {
			page.setTotalRowSize(0);
			return page;
		}
		
		page.setResult(list);
		
		//取记录总数
		String countSql = "select count(1) " + sql.substring(sql.indexOf("from") - 1);
		Query countQuery = sessionFactory.getCurrentSession().createSQLQuery(countSql);
		setQueryParameter(countQuery, params);
		Number count = (Number)countQuery.uniqueResult();
		page.setTotalRowSize(count.intValue());
		
		return page;
	}

	@Override
	public boolean delMenu(String id) {
		String hql = "update OrgMenu d set d.validstatus = 0 where d.id = ?";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString(0, id);
		
		return (query.executeUpdate() > 0);
	}

	@Override
	public OrgMenu saveOrgMenu(OrgMenu orgMenu) {
		sessionFactory.getCurrentSession().saveOrUpdate(orgMenu);
		return orgMenu;
	}
	
}
