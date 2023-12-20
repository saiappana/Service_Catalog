package com.wissen.servicecatalog.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ProjectRequest;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.service.impl.ProjectServiceImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600) 
@RestController
@RequestMapping("/service-catalog/project")
public class ProjectController {

	Logger logger = LoggerFactory.getLogger(ProjectController.class);

	@Autowired
	ProjectServiceImpl projectService;

	@GetMapping("/get/{projectName}")
	public ProjectResponse getProject(@PathVariable String projectName) throws ProjectException {
		logger.info("Get project through Project Id from Project Controller");
		return projectService.getProject(projectName);
	}
	@GetMapping("/getAll")
	public List<ProjectResponse> getAllProject() throws ProjectException {
		logger.info("Get all project from Project Controller");
		return projectService.getAllProject();
	}
	
	@PostMapping("/add")
	public String addProject(@RequestBody @Valid ProjectRequest project) throws ProjectException, EmployeeException, TowerException {
		logger.info("Adding the project from Project Controller");
		return projectService.addProject(project);
	}
	
}