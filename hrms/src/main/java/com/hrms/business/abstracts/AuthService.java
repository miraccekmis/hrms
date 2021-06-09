package com.hrms.business.abstracts;


import com.hrms.core.utilities.results.Result;
import com.hrms.entities.dtos.EmployeeForRegisterDto;
import com.hrms.entities.dtos.EmployerForRegisterDto;

public interface AuthService {
	
	Result employerRegister(EmployerForRegisterDto employer);
	
    Result employeeRegister(EmployeeForRegisterDto jobSeeker);
}
