package com.hrms.business.concretes;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hrms.business.abstracts.AuthService;
import com.hrms.business.abstracts.EmployeeService;
import com.hrms.business.abstracts.EmployerService;
import com.hrms.business.abstracts.SystemAdministratorService;
import com.hrms.business.abstracts.UserService;
import com.hrms.core.adapters.abstracts.MailService;
import com.hrms.core.adapters.abstracts.UserCheckService;
import com.hrms.core.utilities.business.BusinessRules;
import com.hrms.core.utilities.results.ErrorResult;
import com.hrms.core.utilities.results.Result;
import com.hrms.core.utilities.results.SuccessResult;
import com.hrms.entities.concretes.Employee;
import com.hrms.entities.concretes.Employer;
import com.hrms.entities.dtos.EmployeeForRegisterDto;
import com.hrms.entities.dtos.EmployerForRegisterDto;

@Service
public class AuthManager implements AuthService {

	private UserService userService;
	private EmployeeService employeeService;
	private MailService mailService;
	private EmployerService employerService;
	private UserCheckService checkPersonService;
	private ModelMapper modelMapper;
	private SystemAdministratorService administratorService;

	@Autowired
	public AuthManager(UserService userService, EmployeeService employeeService, MailService mailService,
			EmployerService employerService, UserCheckService checkPersonService, ModelMapper modelMapper,
			SystemAdministratorService administratorService) {
		super();
		this.userService = userService;
		this.employeeService = employeeService;
		this.mailService = mailService;
		this.employerService = employerService;
		this.checkPersonService = checkPersonService;
		this.modelMapper = modelMapper;
		this.administratorService = administratorService;

	}

	@Override
	public Result employerRegister(EmployerForRegisterDto employer) {

		var result = BusinessRules.run(checkEmployerRegisterForm(employer), checkDomain(employer),
				checkEmailVerification(employer.getEmail()), checkHrmsConfirm(),
				checkIfEmailExists(employer.getEmail()),
				checkPasswordSame(employer.getPassword(), employer.getRePassword()));
		if (result != null) {
			return result;
		}

		Employer createEmployer = modelMapper.map(employer, Employer.class);
		this.employerService.add(createEmployer);
		return new SuccessResult("Your registration is completed.");
	}

	@Override
	public Result employeeRegister(EmployeeForRegisterDto employee) {
		Result result = BusinessRules.run(checkEmployeeRegisterForm(employee), checkMernis(employee),
				checkIfEmailExists(employee.getEmail()), checkNationalityId(employee.getNationalityId()),
				checkPasswordSame(employee.getPassword(), employee.getRePassword()),
				checkEmailVerification(employee.getEmail()));

		if (result != null) {
			return result;
		}

		Employee createEmployee = modelMapper.map(employee, Employee.class);
		this.employeeService.add(createEmployee);
		return new SuccessResult("Kaydınız tamamlandı.");

	}

	private Result checkIfEmailExists(String email) {

		Result result = this.userService.getByEmail(email);

		if (result.getMessage() != null) {
			return new ErrorResult("E-posta zaten kayıtlı.");
		}
		return new SuccessResult();
	}

	private Result checkEmailVerification(String email) {

		Result result = this.mailService.verification(email);

		if (result == null) {
			return new ErrorResult("E-posta doğrulanamadı.");
		}

		return new SuccessResult();
	}

	private Result checkPasswordSame(String password, String rePassword) {

		if (!password.equals(rePassword)) {
			return new ErrorResult("Şifreler aynı değil");
		}

		return new SuccessResult();
	}

	private Result checkEmployeeRegisterForm(EmployeeForRegisterDto employee) {
		if (employee.getFirstName().isBlank() == true || employee.getLastName().isBlank() == true
				|| employee.getNationalityId().isBlank() == true || employee.getEmail().isBlank() == true
				|| employee.getPassword().isBlank() == true || employee.getRePassword().isBlank() == true) {
			return new ErrorResult("Lütfen formu doldurun, eksik bilgi var.");
		}
		return new SuccessResult();
	}

	private Result checkMernis(EmployeeForRegisterDto employee) {

		if (checkPersonService.CheckUser(employee.getNationalityId(), employee.getYearOfBirth()) == false) {
			return new ErrorResult("Kimlik doğrulanmadı.");
		}
		return new SuccessResult();
	}

	private Result checkNationalityId(String nationalityId) {

		Result result = this.employeeService.getByNationalityId(nationalityId);

		if (result.getMessage() != null) {
			return new ErrorResult("Kullanıcı Mevcut.");
		}
		return new SuccessResult();
	}

	private Result checkDomain(EmployerForRegisterDto employerForRegisterDto) {

		String domain = employerForRegisterDto.getEmail().split("@")[0];

		if (domain.equals(employerForRegisterDto.getWebAddress())) {
			return new SuccessResult();
		}

		return new ErrorResult("Şirket E-posta uyuşmazlığı.");
	}

	private Result checkHrmsConfirm() {
		if (this.administratorService.confirm() == null) {
			return new ErrorResult("Kaydınız kurumumuz tarafından onaylanmamıştır.");
		}
		return new SuccessResult();
	}

	private Result checkEmployerRegisterForm(EmployerForRegisterDto employer) {
		if (employer.getCompanyName().isBlank() == true || employer.getWebAddress().isBlank() == true
				|| employer.getPhoneNumber().isBlank() == true || employer.getEmail().isBlank() == true
				|| employer.getPassword().isBlank() == true || employer.getRePassword().isBlank() == true) {
			return new ErrorResult("Lütfen formu doldurun, eksik bilgi var.");
		}
		return new SuccessResult();
	}

}
