package com.wissen.servicecatalog.service;

import java.util.List;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ProjectRequest;
import com.wissen.servicecatalog.pojo.ProjectResponse;

import jakarta.servlet.http.HttpServletRequest;

public interface ProjectService {

	public String addProject(ProjectRequest projectrequest) throws ProjectException, EmployeeException, TowerException;

	public ProjectResponse getProject(String projectId) throws ProjectException;

	public List<ProjectResponse> getAllProject() throws ProjectException;
	
	public String updateProject(String projectName, Integer managerId)
			throws ProjectException, EmployeeException, TowerException ;
	
//	public ResponseEntity<Object> importProject(MultipartFile file)
//            throws ProjectException, MessagingException, EmployeeException, TowerException, IOException;

}