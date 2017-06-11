package com.platform.business.bo;

import com.platform.business.pojo.WorkHireView;
import com.platform.business.pojo.WorkSign;
import com.platform.organization.pojo.OrgUser;

public class WorkSignBo {

	private WorkSign workSign;

	private WorkHireView workHire;

	private OrgUser emp;

	public WorkSign getWorkSign() {
		return workSign;
	}

	public void setWorkSign(WorkSign workSign) {
		this.workSign = workSign;
	}

	public WorkHireView getWorkHire() {
		return workHire;
	}

	public void setWorkHire(WorkHireView workHire) {
		this.workHire = workHire;
	}

	public OrgUser getEmp() {
		return emp;
	}

	public void setEmp(OrgUser emp) {
		this.emp = emp;
	}


}
