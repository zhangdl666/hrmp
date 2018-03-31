package com.platform.business.dao;

import java.util.List;

import com.platform.business.pojo.WorkKindDef;

public interface BusinessWorkKindDefDao {

	public WorkKindDef saveWorkKindDef(WorkKindDef wk);
	
	public List<WorkKindDef> getWorkKindList();
}
