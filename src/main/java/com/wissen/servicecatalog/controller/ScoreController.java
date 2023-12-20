package com.wissen.servicecatalog.controller;

import java.util.List;

//import javax.mail.MessagingException;

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

import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.pojo.ScoreResponse;
import com.wissen.servicecatalog.service.impl.ScoreServiceImpl;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/service-catalog/score")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ScoreController {
	Logger logger = LoggerFactory.getLogger(ScoreController.class);
	@Autowired
	ScoreServiceImpl scoreService;

	@PostMapping("/save/{projectRoleName}")
	public List<ScoreResponse> saveScore(@RequestBody @Valid List<ScoreRequest> scoreRequest,@PathVariable String projectRoleName) throws ScoreException {
		logger.info("Saving score by projectRoleName from Score Controller");
		return scoreService.saveScore(scoreRequest, projectRoleName);
	}

	@PostMapping("/add/{projectRoleName}")
	public List<ScoreResponse> addScore(@RequestBody @Valid List<ScoreRequest> scoreRequest,
			@PathVariable String projectRoleName) throws ScoreException, 
//	MessagingException, 
	SettingException {
		logger.info("Adding score by Project Role Name from");
		return scoreService.addScore(scoreRequest, projectRoleName);
	}
//
	@GetMapping("/get/{employeeId}/{quarter}/{year}")
	public List<ScoreResponse> getScore(@PathVariable Integer employeeId,
										@PathVariable String quarter,
										@PathVariable Integer year) throws ScoreException {
		logger.info("Get Score through Employee ID from Score Controller");
		return scoreService.getScore(employeeId, quarter, year);
	}
//
//	@GetMapping("/activity/get/{employeeId}/{towerId}/{projectId}")
//	public List<ScoreResponse> getActivity(@PathVariable("employeeId") Integer employeeId,
//			@PathVariable("towerId") Integer towerId, @PathVariable("projectId") Integer projectId)
//			throws ActivityException {
//		logger.info("Getting Activity from EmployeeId/TowerId/ProjectId from Score Controller");
//		return scoreService.getActivity(employeeId, projectId, towerId);
//	}
//
//	@PatchMapping("/publish/{feedback}")
//	public String publishScore(@RequestBody List<PublishScore> publishScore,@PathVariable String feedback) throws ScoreException {
//		logger.info("Publish Score thorughj Score Id from Score Controller");
//		return scoreService.publishScore(publishScore,feedback);
//	}
//
//	@GetMapping("/get/{employeeId}/{towerId}/{projectId}/{quarter}/{year}")
//	public List<ScoreResponse> getScoreByTowerProjectQuarterYear(@PathVariable(name = "employeeId") Integer employeeId,
//			@PathVariable(name = "towerId") Integer towerId, @PathVariable(name = "projectId") Integer projectId,
//			@PathVariable(name = "quarter") String quarter, @PathVariable(name = "year") int year)
//			throws ScoreException {
//		logger.info("getScoreByTowerProjectQuarterYear thorugh EmployeeId/TowerId/ProjectID from Score Controller");
//		return scoreService.getScoreByTowerProjectQuarterYear(employeeId, towerId, projectId, quarter, year);
//	}
//
//	@GetMapping("/admin-get/{towerId}/{projectName}/{quarter}/{year}")
//	public List<ManagerScoreResponse> getScoreByTowerProjectQuarterYear(@PathVariable(name = "towerId") Integer towerId,
//			@PathVariable(name = "projectName") String projectName, @PathVariable(name = "quarter") String quarter,
//			@PathVariable(name = "year") int year) throws ScoreException {
//		logger.info("getScoreByTowerProjectQuarterYear thorugh EmployeeId/TowerId/ProjectID from Score Controller");
//		return scoreService.getScoresByTowerProjectQuarterYear(towerId, projectName, quarter, year);
//	}
//
//	@GetMapping("/getListYear")
//	public List<String> getListYear() throws ScoreException {
//		logger.info("get List Year");
//		return scoreService.getListYear();
//	}
	
}
