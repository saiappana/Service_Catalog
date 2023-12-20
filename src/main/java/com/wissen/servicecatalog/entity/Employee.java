package com.wissen.servicecatalog.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "employee_register")

public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@JsonIgnore
	@Column(name = "Id")
	private Integer id;

	@Column(name = "Employee_Id")

	private String employeeId;

	@Column(name = "email")
	private String email;

	@Column(name = "password")
	private String password;
	private LocalDateTime localDateTime;

	@ManyToOne
	@JoinColumn(name = "application_role_id")
	private ApplicationRoleMaster applicationRole;

	private String status;
	@JsonIgnore
	@Column(name = "reset_password_token")
	private String resetPasswordToken;

	
}