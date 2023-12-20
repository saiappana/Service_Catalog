package com.wissen.servicecatalog.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.wissen.servicecatalog.entity.ApplicationRoleMaster;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.EmployeeRoleMaster;

public interface EmployeeMasterRepository extends JpaRepository<EmployeeMaster, Integer> {

	EmployeeMaster findByEmployeeId(Integer employeeId);
	
	EmployeeMaster findByEmailId(String emailId);

	EmployeeMaster findByEmployeeIdAndApplicationRoleMaster(EmployeeMaster employeeMaster,
			ApplicationRoleMaster applicationrolemaster);

	List<EmployeeMaster> findByCurrentEmployeeRoleIdRoleId(Integer EmployeeRoleId);

	EmployeeMaster findByEmployeeIdOrEmployeeName(Integer employeeId, String employeeName);

	List<EmployeeMaster> findByApplicationRoleMasterApplicationName(String ApplicationRoleName);

	List<EmployeeMaster> findByManagerId(EmployeeMaster employeeMaster);

	@Query("From EmployeeMaster where employeeId=:employeeId AND employeeName=:employeeName AND managerId=:managerId AND emailId=:emailId AND currentEmployeeRoleId=:currentEmployeeRoleId AND applicationRoleMaster=:applicationRoleMaster")
	EmployeeMaster findByDuplicateEmployee(Integer employeeId, String employeeName, Integer managerId, String emailId,
			EmployeeRoleMaster currentEmployeeRoleId, ApplicationRoleMaster applicationRoleMaster);

	@Query("Select e FROM EmployeeMaster e LEFT JOIN Score s ON e.employeeId = s.employeeMaster.employeeId AND s.year=:year AND s.quarter=:quarter WHERE s.employeeMaster.employeeId IS NULL")
	List<EmployeeMaster> findAllEmployeeNotAccessed(String year, String quarter);

}
