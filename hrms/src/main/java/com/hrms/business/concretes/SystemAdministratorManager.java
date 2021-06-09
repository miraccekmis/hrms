package com.hrms.business.concretes;

import org.springframework.stereotype.Service;

import com.hrms.business.abstracts.SystemAdministratorService;
import com.hrms.core.utilities.results.Result;
import com.hrms.core.utilities.results.SuccessResult;

@Service
public class SystemAdministratorManager implements SystemAdministratorService{

	@Override
	public Result confirm() {
		return new SuccessResult("Onaylandı.");
	}

}
