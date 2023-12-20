package com.wissen.servicecatalog.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeMaster implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;
	@Column(unique = true)
	private Integer employeeId;
	private String employeeName;
	private Double totalExperienceMonths;
	private Double currentRoleExperienceMonths;
	private Integer managerId;
	private String primarySkill;
	private String secondarySkill;
	private String certification;
	@Column(name = "email_id", unique = true)
	private String emailId;
	private String employeeStatus;
	@ManyToOne
	@JoinColumn(name = "employee_role_id")
	private EmployeeRoleMaster currentEmployeeRoleId;
	@ManyToOne
	@JoinColumn(name = "application_role_id")
	private ApplicationRoleMaster applicationRoleMaster;

}
