package com.platform.business.service;

import java.util.List;

import com.platform.business.pojo.WorkKindDef;

public interface BusinessWorkKindDefService {

public WorkKindDef saveWorkKindDef(WorkKindDef wk);
	
	public List<WorkKindDef> getWorkKindList();
}
