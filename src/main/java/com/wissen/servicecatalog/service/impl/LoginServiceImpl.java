package com.wissen.servicecatalog.service.impl;

import java.time.LocalDateTime;
import java.util.Random;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Setting;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.EmployeeRegisterRequest;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.repository.OtpRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.service.LoginService;

@Service
public class LoginServiceImpl implements LoginService{

	private static final String FAILED_TO_SEND_EMAIL_DUE_TO_ERROR = "Failed to send email due to error : ";
	private static final String FAILED_TO_SEND_THE_EMAIL_DUE_TO_PORT_ISSUE = "Failed To send the email !,due to port issue";
	private static final String ADMIN = "Admin";
	private static final String PLEASE_ENTER_THE_WISSEN_EMAIL_ID = "Please enter the wissen mail id";

	@Value("${regex}")
	String regex;

	private static final String USER_REGISTRATION = "User Registration";
	private static final String OTP_NOT_VERIFIED = "OTP not verified";
	private static final String PROJECT_MANAGER = "Project Manager";
	private static final String SERVICE_CATALOG_MAIL_ID = "service-catalog mail id";
	private static final String SERVICE_CATALOG_MAIL_ID_PASSWORD = "service-catalog mail id password";
	private static final String PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID = "Please add the settings for Service catalog mail id";
	private static final String PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID = "Please add the settings for Service catalog password id";
	
	Logger logger = LoggerFactory.getLogger(LoginServiceImpl.class);

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	EmployeeMasterRepository employeeMasterRepository;

	@Autowired
	ApplicationRoleMasterRepository applicationRoleRepository;

	@Autowired
	OtpRepository otpRepository;

	@Autowired
	SettingRepository settingRepository; 
	
	Random random = new Random();
	
	
	public String userRegister(@RequestBody EmployeeRegisterRequest employee) throws Exception {
	
		Setting findBySettingMailId = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID);
		if (findBySettingMailId == null) {
			throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_PASSWORD_ID);
		}
		Setting findBySettingMailPassword = settingRepository.findBySettingName(SERVICE_CATALOG_MAIL_ID_PASSWORD);

		if (findBySettingMailPassword == null) {
			throw new SettingException(PLEASE_ADD_THE_SETTINGS_FOR_SERVICE_CATALOG_MAIL_ID);
		}
		logger.info("User Registration fom Login Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true); 
		
		EmployeeMaster employeeMaster = employeeMasterRepository
				.findByEmployeeId(Integer.parseInt(employee.getEmployeeId()));
		
		if (employeeMaster == null) {
			logger.error("Unauthorized Employee registration, Please enter the wissen Employee ID to registration");
			throw new EmployeeException(
					"Unauthorized Employee registration, Please enter the wissen Employee ID to registration");
		}
		
		Employee employeeId = employeeRepository.findByEmployeeId(employeeMaster.getEmployeeId() + "");
		if (employeeId != null && employeeId.getStatus().equalsIgnoreCase("Mail not sent")) {
			throw new EmployeeException("your default password is expired,please reset the password!");
		}
		
		if (employee.getApplicationRole().equalsIgnoreCase("Manager")) {
			employee.setApplicationRole(PROJECT_MANAGER);
		}
		
		if (employee.getEmployeeId().length() < 3) {
			logger.error("Employee Id should be 4 digits");
			throw new EmployeeException("Employee Id should be 4 digits");
		}
		if (employeeRepository.findByEmail(employee.getEmail()) != null) {
			logger.error("Email id is already registered");
			throw new EmployeeException("Email id is already registered");
		}
		
		if (employeeRepository.findByEmployeeId(employee.getEmployeeId()) != null) {
			logger.error("Employee id is already Exist");
			throw new EmployeeException("Employee id is already Exist");
		}
		if (!employeeMaster.getEmailId().equalsIgnoreCase(employee.getEmail())) {
			logger.error("Invalid email id,Please enter your wissen employee id");
			throw new EmployeeException("Invalid email id,Please enter your wissen employee id");
		}
		
		if (!employeeMaster.getApplicationRoleMaster().getApplicationName()
				.equalsIgnoreCase(employee.getApplicationRole())) {
			logger.error("Invalid Role,Please select your wissen Application role");
			throw new EmployeeException("Invalid Role,Please select your wissen Application role");
		}
		Employee employee1 = new Employee();
		modelMapper.map(employee, employee1);
		
//		employee1.setEmployeeId(employee.getEmployeeId());
//		employee1.setEmail(employee.getEmail());
		
		employee1.setLocalDateTime(LocalDateTime.now());
		employee1.setStatus(OTP_NOT_VERIFIED);
		try {
			employee1.setPassword(employee.getPassword());
		} catch (Exception e) {
			throw new EmployeeException(e.getLocalizedMessage());
		}
		ApplicationRoleMaster applicationRoleMasterCheck = applicationRoleRepository
				.findByApplicationName(employee.getApplicationRole());

		if (applicationRoleMasterCheck == null) {
			ApplicationRoleMaster applicationRole = new ApplicationRoleMaster();
			applicationRole.setApplicationName(employee.getApplicationRole());
			ApplicationRoleMaster applicationRoleMaster = applicationRoleRepository.save(applicationRole);
			employee1.setApplicationRole(applicationRoleMaster);
//			return sendMailForUserRegieration(employee1, findBySettingMailId, findBySettingMailPassword);
			employeeRepository.save(employee1);
			return "Registration Successful";
		}

		employee1.setApplicationRole(applicationRoleMasterCheck);
		employeeRepository.save(employee1);
//		return sendMailForUserRegieration(employee1, findBySettingMailId, findBySettingMailPassword);
		return "Registration Successful";
	}
	

}
