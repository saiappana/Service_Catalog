package com.wissen.servicecatalog.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.exception.EmployeeException;
import com.wissen.servicecatalog.exception.ProjectException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ProjectRequest;
import com.wissen.servicecatalog.pojo.ProjectResponse;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.service.ProjectService;

@Service
public class ProjectServiceImpl implements ProjectService{
	
	Logger logger = LoggerFactory.getLogger(ProjectServiceImpl.class);
	private static final String PLEASE_UPLOAD_THE_FILE = "Please upload the file";
	private static final String PROJECT_NAME_IS_ALREADY_EXIST = "Project name is already exist";
	private static final String PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER = "please check the employee id, not an project manager";
	private static final String INVALID_EMPLOYEE_ID = "Invalid employee id";
	private static final String INVALID_TOWER_ID = "Invalid Tower Id";

	@Autowired
	ProjectRepository projectRepository;
	
	@Autowired
	TowerRepository towerRepository;
	
	@Autowired
	EmployeeMasterRepository employeeRepository;
	
	@Override
	public ProjectResponse getProject(String projectName) throws ProjectException{
		logger.info("Getting Project from Project Service");
		if (projectName == null) {
			logger.error("Please enter the project id");
			throw new ProjectException("Please enter the project id");
		}
		
		List<Project> project = projectRepository.findByProjectName(projectName);
		if (project.isEmpty()) {
			logger.error("Invalid project Name");
			throw new ProjectException("Invalid project Name");

		}

		List<Tower> collect = project.stream().map(Project::getTower).collect(Collectors.toList());
		if (collect.isEmpty()) {
			throw new ProjectException("No towers for this project");
		}
		
		
		ProjectResponse projectResponse = new ProjectResponse();
		projectResponse.setProjectId(project.get(0).getProjectId());
		projectResponse.setProjectManagerId(project.get(0).getProjectManagerId().getEmployeeId());
		projectResponse.setProjectManagerName(project.get(0).getProjectManagerId().getEmployeeName());
		projectResponse.setProjectName(project.get(0).getProjectName());
		projectResponse.setTower(collect);

		return projectResponse;
	}

	@Override
	public List<ProjectResponse> getAllProject() throws ProjectException {
		logger.info("Getting all Project from Project Service");
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<ProjectResponse> projectsResponse = new ArrayList<>();
		List<Project> allProjects = projectRepository.groupByProjectAndManagerId();
		if (allProjects.isEmpty()) {
			logger.error("No projects found");
			throw new ProjectException("no projects found");
		}

		for (Project project : allProjects) {
			List<Project> findByProjectName = projectRepository.findByProjectManagerIdAndProjectName(
					project.getProjectManagerId().getEmployeeId(), project.getProjectName());
			List<Tower> projectTowerList = findByProjectName.stream().map(Project::getTower)
					.collect(Collectors.toList());
			ProjectResponse projectResponse = new ProjectResponse();
			projectResponse.setProjectId(project.getProjectId());
			projectResponse.setProjectManagerId(project.getProjectManagerId().getEmployeeId());
			projectResponse.setProjectManagerName(project.getProjectManagerId().getEmployeeName());
			projectResponse.setProjectName(project.getProjectName());
			projectResponse.setTower(projectTowerList);
			projectsResponse.add(projectResponse);
		}
		return projectsResponse;
	}
	
	public String addProject(ProjectRequest projectRequest)
			throws ProjectException, EmployeeException, TowerException {
		logger.info("Adding Project from Project Service");
		LinkedList<Tower> tower = new LinkedList<>();

//		String bearerToken = request.getHeader("Authorization");
//
//		if (bearerToken == null) {
//			logger.error("Session Expired");
//			throw new EmployeeException("Session Expired");
//		}
//
//		String jwtToken = bearerToken.substring(7, bearerToken.length());
//
//		String employeeIdFromToken = jwtUtil.extractUsername(jwtToken);
//		projectRequest.setProjectManagerId(Integer.parseInt(employeeIdFromToken));

		for (Integer list1 : projectRequest.getTowerId()) {

			Tower towerId = towerRepository.findByTowerId(list1);

			if (towerId == null) {
				throw new TowerException(INVALID_TOWER_ID);
			}
			tower.add(towerId);
		}
		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(projectRequest.getProjectManagerId());
//		logger.info(employeeMaster.toString());
		if (employeeMaster == null) {
			logger.error(INVALID_EMPLOYEE_ID);
			throw new EmployeeException(INVALID_EMPLOYEE_ID);
		}

		if (!employeeMaster.getApplicationRoleMaster().getApplicationName().equalsIgnoreCase("Project Manager")) {
			logger.error(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
			throw new EmployeeException(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
		}
		Integer count = 0;
		for (Tower list : tower) {
			Project projectName = projectRepository.findByProjectManagerIdAndProjectNameAndTowerTowerId(
					projectRequest.getProjectManagerId(), projectRequest.getProjectName(), list.getTowerId());
			if (projectName != null) {
				count++;
				continue;
			}
			Project project = new Project();
			project.setProjectName(projectRequest.getProjectName());
			project.setProjectManagerId(employeeMaster);
			project.setTower(list);
			projectRepository.save(project);
		}
		if (tower.size() == count) {
			logger.error(PROJECT_NAME_IS_ALREADY_EXIST);
			throw new ProjectException("Project name is already exist!");
		}

		return "Project added";

	} 
	
	public String updateProject(String projectName, Integer managerId)
			throws ProjectException, EmployeeException, TowerException {


		logger.info("Updating Project from Project Service");
		if (projectName == null) {
			logger.error("Please enter the project name");
			throw new ProjectException("Please enter the project name");
		}
		if (managerId == null) {
			logger.error("Please enter manager id");
			throw new ProjectException("Please enter manager id");
		}
//		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(Integer.parseInt(employeeIdFromToken));
		EmployeeMaster employeeMaster = employeeRepository.findByEmployeeId(managerId);
		if (employeeMaster == null) {
			logger.error(PLEASE_CHECK_THE_EMPLOYEE_ID_NOT_AN_PROJECT_MANAGER);
			throw new EmployeeException("please check the employee id, not an project manager!");
		}
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setAmbiguityIgnored(true);
		List<Project> project = projectRepository.findByProjectName(projectName);

		if (project.isEmpty()) {
			logger.error("Invalid project name");
			throw new ProjectException("Invalid project name");
		}
		List<Project> list = new LinkedList<>();
		for (Project proj : project) {

			Project projectR = new Project();
			projectR.setProjectId(proj.getProjectId());
			projectR.setProjectName(proj.getProjectName());
			projectR.setTower(proj.getTower());
			projectR.setProjectManagerId(employeeMaster);
			list.add(projectR);
		}
		projectRepository.saveAll(list);

		return "Project updated";

	}
	
	
}