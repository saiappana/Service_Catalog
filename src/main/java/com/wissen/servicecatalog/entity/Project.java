package com.wissen.servicecatalog.entity;

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
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer projectId;
	private String projectName;
	@ManyToOne
	@JoinColumn(name = "project_manager_id", referencedColumnName = "employeeId")
	private EmployeeMaster projectManagerId;
	@ManyToOne
	@JoinColumn(name = "tower_id")
	private Tower tower;

}
