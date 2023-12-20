package com.wissen.servicecatalog.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.entity.Employee;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.pojo.EmployeeRegisterRequest;
import com.wissen.servicecatalog.pojo.EmployeeResponse;
import com.wissen.servicecatalog.pojo.LoginRequest;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRepository;
import com.wissen.servicecatalog.service.impl.LoginServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/service-catalog/employee")
@CrossOrigin(origins = "*", maxAge = 3600)
public class LoginController {

	Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	LoginServiceImpl loginService;

	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	EmployeeMasterRepository employeeMasterRepository;
	
	@PostMapping("/signup")
	public String userRegister(@RequestBody @Valid EmployeeRegisterRequest user) throws Exception {
		logger.info("Sign up from Login Controller ");
		return loginService.userRegister(user);
	}
	
	@PostMapping("/signin")
	public EmployeeResponse authenticateUser(@RequestBody @Valid LoginRequest loginRequest) throws Exception {
		logger.info("Authenticating user before signing in from Login Controller");

		if (loginRequest.getEmployeeId().length() < 3) {
			logger.error("Employee Id should be 4 digits");
			throw new EmployeeException("Employee Id should be 4 digits");
		}
  
		if (loginRequest.getPassword().length() < 8) {
			logger.error("Password should be minimum 8 characters");
			throw new EmployeeException("Password should be minimum 8 characters");
		}
//		UserDetails userDetails = null;
		try {
			loginRequest.setPassword(loginRequest.getPassword());
		} catch (Exception e) {
			throw new EmployeeException(e.getLocalizedMessage());
		}
		try {
//			authenticate(loginRequest.getEmployeeId(), loginRequest.getPassword());
//			userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmployeeId());
			EmployeeMaster employee=employeeMasterRepository.findByEmployeeId(Integer.parseInt(loginRequest.getEmployeeId()));
			if((employee== null)||employee.getEmployeeStatus().equals("Verified/Inactive")) {
				throw new EmployeeException("Failed to login please contact the Admin");
			}
			
		} catch (Exception e) {
			logger.error("Sign in from Login Controller");
			throw new EmployeeException("Invalid Employee Id & Password");
		}

		Employee employee = employeeRepository.findByEmployeeId(loginRequest.getEmployeeId() + "");

		if (employee == null || !employee.getPassword().equals(loginRequest.getPassword())) {
			logger.error("Invalid Credentials");
			throw new EmployeeException("Invalid Credentials");
		}

		if (employee.getApplicationRole().getApplicationName().equalsIgnoreCase("Admin")) {
			employee.setStatus("Approved");
			employee.setLocalDateTime(LocalDateTime.now());
			employeeRepository.save(employee);
		}

		if (employee.getStatus().equalsIgnoreCase("OTP not verified")) {
			logger.error("Your email id is not verified");
			throw new EmployeeException("Your email id is not verifed!");
		} else if (employee.getStatus().equalsIgnoreCase("Pending")) {
			logger.error("Registration is still pending, Please contact Admin");
			throw new EmployeeException("Registration is still pending, Please contact Admin");
		} else if (employee.getStatus().equalsIgnoreCase("Rejected")
				|| employee.getStatus().equalsIgnoreCase("Reject")) {
			logger.error("Registration is rejected, Please contact Admin");
			throw new EmployeeException("Registration is rejected, Please contact Admin");
		} else if (employee.getStatus().equalsIgnoreCase("Mail not sent")) {
			logger.error("Please reset the password");
			throw new EmployeeException("Please reset the password");
		}

		LocalDateTime loginPresentDateTime = LocalDateTime.now();
		LocalDateTime registerationDateTime = employee.getLocalDateTime();
		if (registerationDateTime.isBefore(loginPresentDateTime)) {
			LocalDate loginPresentDate = LocalDate.of(loginPresentDateTime.getYear(), loginPresentDateTime.getMonth(),
		 			loginPresentDateTime.getDayOfMonth());
			LocalDate registerationDate = LocalDate.of(registerationDateTime.getYear(),
					registerationDateTime.getMonth(), registerationDateTime.getDayOfMonth());
			Period difference = Period.between(registerationDate, loginPresentDate);

			if (difference.getYears() >= 1) {
				logger.error("Reset the password");
				throw new EmployeeException("Reset the password" 
//				+ jwtUtil.generateToken(userDetails)
				);
			}
		}

		EmployeeResponse employeeResponse=new EmployeeResponse();
		EmployeeMaster employeeMaster=employeeMasterRepository.findByEmployeeId(Integer.parseInt(loginRequest.getEmployeeId()));
		
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		
		modelMapper.map(employeeMaster, employeeResponse);
		
//		employeeResponse.setEmployeeId(employeeMaster.getEmployeeId());
//		employeeResponse.setEmployeeName(employeeMaster.getEmployeeName());
//		employeeResponse.setCurrentRoleExperienceMonths(employeeMaster.getCurrentRoleExperienceMonths());
//		employeeResponse.setCertification(employeeMaster.getCertification());
//		employeeResponse.setCurrentEmployeeRoleName(employeeMaster.getCurrentEmployeeRoleId().getEmployeeRoleName());
//		employeeResponse.setEmailId(employeeMaster.getEmailId());
//		employeeResponse.setTotalExperienceMonths(employeeMaster.getTotalExperienceMonths());
//		employeeResponse.setPrimarySkill(employeeMaster.getPrimarySkill());
//		employeeResponse.setSecondarySkill(employeeMaster.getSecondarySkill());
//		employeeResponse.setApplicationRoleName(employeeMaster.getApplicationRoleMaster().getApplicationName());
//		employeeResponse.setManagerId(employeeMaster.getManagerId());
//		employeeResponse.setEmployeeStatus(employeeMaster.getEmployeeStatus());
//		return jwtUtil.generateToken(userDetails);
		return employeeResponse;

	}
}
