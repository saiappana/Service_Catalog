package com.wissen.servicecatalog.pojo;

import java.time.LocalDateTime;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern.Flag;
import lombok.Data;

@Data
public class EmployeeRegister {

	private Integer id;
	private String employeeId;
	@Email(regexp = "^[A-Za-z0-9._%+-]+@wisseninfotech.com", flags = Flag.CASE_INSENSITIVE,message = "Please enter the wissen mail id")
	private String email;

	private String password;
	private LocalDateTime localDateTime;

	private ApplicationRoleMaster applicationRole;

	private String status;

}
