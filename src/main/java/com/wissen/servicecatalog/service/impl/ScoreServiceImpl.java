package com.wissen.servicecatalog.service.impl;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

//import javax.mail.MessagingException;
//import javax.servlet.http.HttpServletRequest;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.EmployeeMaster;
import com.wissen.servicecatalog.entity.Feedback;
import com.wissen.servicecatalog.entity.Project;
import com.wissen.servicecatalog.entity.Score;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Status;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.exception.ScoreException;
import com.wissen.servicecatalog.exception.SettingException;
import com.wissen.servicecatalog.pojo.ScoreRequest;
import com.wissen.servicecatalog.pojo.ScoreResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.ApplicationRoleMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeMasterRepository;
import com.wissen.servicecatalog.repository.EmployeeRoleMasterRepository;
import com.wissen.servicecatalog.repository.FeedbackRepository;
import com.wissen.servicecatalog.repository.ProjectRepository;
import com.wissen.servicecatalog.repository.ScoreRepository;
import com.wissen.servicecatalog.repository.SettingRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.StatusRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.ScoreService;

@Service
public class ScoreServiceImpl implements ScoreService {

	private static final String INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE = "Invalid Project Id to assess the score";

	private static final String INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE = "Invalid Employee Id to assess the score";

	private static final String Q4 = "Q4";

	private static final String Q3 = "Q3";

	private static final String Q2 = "Q2";

	private static final String Q1 = "Q1";

	private static final String APPROVED = "Approved";

	private static final String PUBLISHED = "Published";

	private static final String NO_RECORDS = "No records";

	private static final String SUBMITTED = "Submitted";

	private static final String SAVED = "Saved";

	private static final String THERE_IS_NO_DATA_IN_THE_SKILL = "There is no data in the skill";

	Logger logger = LoggerFactory.getLogger(ScoreServiceImpl.class);

	@Autowired
	EmployeeMasterRepository employeeRepository;

	@Autowired
	TowerSkillScoreRepository skillScoreRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	FeedbackRepository feedbackRepository;

	@Autowired
	SettingRepository settingRepository;

	@Autowired
	EmployeeRoleMasterRepository employeeRoleMasterRepository;

	@Autowired
	ApplicationRoleMasterRepository applicationRoleMasterRepository;

	@Autowired
	StatusRepository statusRepository;

	@Autowired
	ScoreRepository scoreRepository;

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	ProjectRepository projectRepository;

	@Autowired
	TowerRepository towerRepository;

	@Autowired
	TowerSkillScoreRepository towerScoreRepository;

	@Override
	public List<ScoreResponse> saveScore(List<ScoreRequest> scoreRequest, String projectRoleName) throws ScoreException {
		logger.info("Saving Score from Score Service");

		if (scoreRequest.isEmpty()) {
			throw new ScoreException("please select the score need to be saved");
		}
		if (projectRoleName == null) {
			throw new ScoreException("please select the role to save the score");
		}

		List<ScoreResponse> scoreResoponses = new ArrayList<>();

		List<Score> saveScore = new ArrayList<>();
		for (ScoreRequest request : scoreRequest) {
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			request.setEmployeeId(Integer.parseInt("5001"));
			EmployeeMaster employeeMasterResult = employeeRepository.findByEmployeeId(request.getEmployeeId());

			if (employeeMasterResult == null) {
				logger.error("Invalid Employee Id to access to score");
				throw new ScoreException(INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE);
			}
			Project projectResult = projectRepository.findByProjectId(request.getProjectId());

			if (projectResult == null) {
				logger.error("Invalid Project Id to access to score");
				throw new ScoreException(INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE);
			}
			Tower towerResult = towerRepository.findByTowerId(request.getTowerId());

			if (towerResult == null) {
				logger.error("Invalid Tower Id to access to score");
				throw new ScoreException("Invalid tower Id to assess the score");
			}
			Activity activityResult = activityRepository.findByActivityId(request.getActivityId());

			if (activityResult == null) {
				logger.error("Invalid Activity Id to access to score");
				throw new ScoreException("Invalid Activity Id to assess the score");
			}
			Skill skill = skillRepository.findBySkillLevel(projectRoleName);
			if (skill == null) {
				logger.error(THERE_IS_NO_DATA_IN_THE_SKILL);
				throw new ScoreException(THERE_IS_NO_DATA_IN_THE_SKILL);

			}
			Project projectData = projectRepository.findByProjectNameAndTowerTowerId(projectResult.getProjectName(),
					request.getTowerId());
			request.setProjectId(projectData.getProjectId());
			List<Score> scoreResult = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndStatus(
							request.getEmployeeId(), request.getQuarter(), request.getProjectId(), request.getYear(),
							towerResult.getTowerId(), SUBMITTED);
			List<Score> scorePublished = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndStatus(
							request.getEmployeeId(), request.getQuarter(), request.getProjectId(), request.getYear(),
							towerResult.getTowerId(), PUBLISHED);
			List<Score> scoreApprove = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndStatus(
							request.getEmployeeId(), request.getQuarter(), request.getProjectId(), request.getYear(),
							towerResult.getTowerId(), APPROVED);

			if (!scoreResult.isEmpty() || !scorePublished.isEmpty() || !scoreApprove.isEmpty()) {
				logger.error("Score is already submitted for this project, tower, quarter, year you cant assess it");
				throw new ScoreException(
						"Score is already submitted for this project, tower, quarter, year you cant assess it");
			} else {

				Score scoreResult2 = scoreRepository.findByEmployeeIdAndQuarterAndActivityIdAndYear(
						request.getEmployeeId(), request.getQuarter(), request.getActivityId(), request.getProjectId(),
						request.getTowerId(), request.getYear());

				if (scoreResult2 != null && scoreResult2.getStatus().equalsIgnoreCase(SAVED)) {

					scoreResult2.setScore(request.getScore());
					saveScore.add(scoreResult2);
					scoreResoponses.add(commonScoreResponse(scoreResult2));
				} else {
					Score score = new Score();
					mapper.map(request, score);
					score.setActivity(activityResult);
					score.setEmployeeMaster(employeeMasterResult);
					score.setTower(towerResult);
					score.setProject(projectData);
					score.setStatus(SAVED);
					score.setProjectSkill(skill);
					Feedback feedback2 = feedbackRepository.findByFeedbackName(SAVED);
					if (feedback2 == null) {
						Feedback feedback = new Feedback();
						feedback.setFeedbackName(SAVED);
						feedback.setFlag(false);
						Feedback feedbackResult = feedbackRepository.save(feedback);
						score.setFeedbackMaster(feedbackResult);
						Score scoreSave = scoreRepository.save(score);
						scoreResoponses.add(commonScoreResponse(scoreSave));
					} else {
						score.setFeedbackMaster(feedback2);
						saveScore.add(score);
						scoreResoponses.add(commonScoreResponse(score));
					}
				}
			}
		}
		List<Score> employeeSavedScores = scoreRepository
				.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerId(
						scoreRequest.get(0).getEmployeeId(), scoreRequest.get(0).getQuarter(),
						scoreRequest.get(0).getProjectId(), scoreRequest.get(0).getYear(),
						scoreRequest.get(0).getTowerId());

		scoreRepository.saveAll(saveScore);

		employeeSavedScores.stream().forEach(i -> {
			Skill newSkill = skillRepository.findBySkillLevel(projectRoleName);
			i.setProjectSkill(newSkill);
			scoreRepository.save(i);
		});
		int count=scoreResoponses.size();
		int activityCount=activityRepository.getActivity(scoreRequest.get(0).getTowerId()).size(); 
		if(count!=activityCount) {
			throw new ScoreException("Give score for all activities");
		}
		return scoreResoponses;
	}

	public ScoreResponse commonScoreResponse(Score scoreSave) {
		ScoreResponse scoreResponse = new ScoreResponse();
		scoreResponse.setActivityId(scoreSave.getActivity().getActivityId());
		scoreResponse.setActivityName(scoreSave.getActivity().getActivityName());
		scoreResponse.setCategory(scoreSave.getActivity().getCategory());
		scoreResponse.setFacilitator(scoreSave.getActivity().getFacilitator());
		scoreResponse.setQuarter(scoreSave.getQuarter());
		scoreResponse.setScoreId(scoreSave.getScoreId());
		scoreResponse.setFeedbackName(scoreSave.getFeedbackMaster().getFeedbackName());
		scoreResponse.setScore(scoreSave.getScore());
		scoreResponse.setStatus(scoreSave.getStatus());
		scoreResponse.setTechnologies(scoreSave.getActivity().getTechnologies());
		scoreResponse.setService(scoreSave.getActivity().getService());
		scoreResponse.setYear(scoreSave.getYear());
		scoreResponse.setProjectSkill(scoreSave.getProjectSkill().getSkillLevel());
		return scoreResponse;
	}

	@Override
	public List<ScoreResponse> addScore(List<ScoreRequest> scoreRequest, String projectRoleName) throws ScoreException,
//	MessagingException,
	SettingException {
		logger.info("Adding Score from Score Service");
		Integer scoreSavedCount = 0;
		Integer scoreSubmittedCount = 0;

		if (scoreRequest.isEmpty()) {
			throw new ScoreException("please select the score need to be saved");
		}
		if (projectRoleName == null) {
			throw new ScoreException("please select the role to save the score");
		}

		List<ScoreResponse> scoreResponses = new LinkedList<>();

		EmployeeMaster manager = null;
		EmployeeMaster employeeMasterResult = null;
		Integer oneCount = 0;
		for (ScoreRequest request : scoreRequest) {
			oneCount++;
			ModelMapper mapper = new ModelMapper();
			mapper.getConfiguration().setAmbiguityIgnored(true);
			request.setEmployeeId(Integer.parseInt("5001"));
			employeeMasterResult = employeeRepository.findByEmployeeId(request.getEmployeeId());

			if (employeeMasterResult == null) {
				logger.error(INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE);
				throw new ScoreException(INVALID_EMPLOYEE_ID_TO_ASSESS_THE_SCORE);
			}
			Project projectResult = projectRepository.findByProjectId(request.getProjectId());

			if (projectResult == null) {
				logger.error(INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE);
				throw new ScoreException(INVALID_PROJECT_ID_TO_ASSESS_THE_SCORE);
			}
			manager = employeeRepository.findByEmployeeId(projectResult.getProjectManagerId().getEmployeeId());
			if (manager == null) {
				logger.error("Invalid Manager Id or There is no manager Present in the data");
				throw new ScoreException("Invalid Manager Id or There is no manager Present in the data");
			}
			Skill skill = skillRepository.findBySkillLevel(projectRoleName);
			if (skill == null) {
				logger.error(THERE_IS_NO_DATA_IN_THE_SKILL);
				throw new ScoreException(THERE_IS_NO_DATA_IN_THE_SKILL);
			}
			Tower towerResult = towerRepository.findByTowerId(request.getTowerId());
			if (towerResult == null) {
				logger.error("Invalid tower Id to access the score");
				throw new ScoreException("Invalid tower Id to assess the score");
			}
			Activity activityResult = activityRepository.findByActivityId(request.getActivityId());
			if (activityResult == null) {
				logger.error("Invalid Activity Id to access the score");
				throw new ScoreException("Invalid Activity Id to assess the score");
			}

			Status status = statusRepository.findByCurrentEmployeeStatus("Available to share");
			Project projectData = projectRepository.findByProjectNameAndTowerTowerId(projectResult.getProjectName(),
					request.getTowerId());
			request.setProjectId(projectData.getProjectId());

			List<Score> scorePublished = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndStatus(
							request.getEmployeeId(), request.getQuarter(), request.getProjectId(), request.getYear(),
							towerResult.getTowerId(), PUBLISHED);

			List<Score> scoreApproved = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndStatus(
							request.getEmployeeId(), request.getQuarter(), request.getProjectId(), request.getYear(),
							towerResult.getTowerId(), APPROVED);

			if (!scorePublished.isEmpty()) {
				throw new ScoreException("Scores are already published for this quarter");
			}

			if (!scoreApproved.isEmpty()) {
				throw new ScoreException("Scores are approved for this quarter");
			}
			Score scoreResult = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerIdAndActivityActivityId(
							request.getEmployeeId(), request.getQuarter(), request.getProjectId(), request.getYear(),
							request.getTowerId(), request.getActivityId());

			if (scoreResult == null) {
				Score score = new Score();
				mapper.map(request, score);
				score.setProjectSkill(skill);
				score.setTimeLine(null);
				score.setActivity(activityResult);
				score.setEmployeeMaster(employeeMasterResult);
				score.setTower(towerResult);
				score.setProject(projectData);
				score.setStatus(SUBMITTED);
				score.setCurrentEmployeeStatus(status);
				Feedback feedback2 = feedbackRepository.findByFeedbackName(SUBMITTED);
				if (feedback2 == null) {
					Feedback feedback = new Feedback();
					feedback.setFlag(false);
					feedback.setFeedbackName(SUBMITTED);
					Feedback feedbackResult = feedbackRepository.save(feedback);
					score.setFeedbackMaster(feedbackResult);
					Score scoreSave = scoreRepository.save(score);
					scoreResponses.add(commonScoreResponse(scoreSave));
				} else {
					score.setFeedbackMaster(feedback2);
					score.setTimeLine(null);
					score.setProjectSkill(skill);
					Score scoreSave = scoreRepository.save(score);
					scoreResponses.add(commonScoreResponse(scoreSave));
				}
			} else {
				if (scoreResult.getStatus().equalsIgnoreCase(SAVED)) {
					scoreSavedCount++;

					scoreResult.setScore(request.getScore());
					scoreResult.setProjectSkill(skill);
					scoreResult.setStatus(SUBMITTED);
					scoreResult.setTimeLine(null);
					scoreResult.setCurrentEmployeeStatus(status);
					Score scoreSave = scoreRepository.save(scoreResult);
					scoreResponses.add(commonScoreResponse(scoreSave));
				} else {

					if (scoreResult.getStatus().equalsIgnoreCase(SUBMITTED)
							|| scoreResult.getStatus().equalsIgnoreCase(PUBLISHED)
							|| scoreResult.getStatus().equalsIgnoreCase(APPROVED)) {

						scoreSubmittedCount++;
					}
				}
			}

			Skill updateSkill = skillRepository.findBySkillLevel(projectRoleName);
			List<Score> employeeSumittedScores = scoreRepository
					.findByEmployeeIdAndQuarterAndProjectProjectIdAndYearAndTowerTowerId(
							scoreRequest.get(0).getEmployeeId(), scoreRequest.get(0).getQuarter(),
							scoreRequest.get(0).getProjectId(), scoreRequest.get(0).getYear(),
							scoreRequest.get(0).getTowerId());
			employeeSumittedScores.stream().forEach(i -> {
				i.setProjectSkill(updateSkill);
				i.setStatus(SUBMITTED);
				i.setTimeLine(null);
				i.setCurrentEmployeeStatus(status);
				scoreRepository.save(i);
			});
		}
		if (scoreSavedCount == 0 && scoreSubmittedCount != 0) {
			logger.error("score is already submitted for this project, tower, quarter , year you cant update it");
			throw new ScoreException(
					"Score is already submitted for this project, tower, quarter, year you cant assess it");
		}
//		logger.info(employeeIdFromToken+" has Assessed for the no of Activitys :"+scoreRequest.size()+" And the total No of actvity stored is :"+scoreResponses.size());
//		sendScoreMail(manager, employeeMasterResult);
		return scoreResponses;
	}
//
//	public void sendScoreMail(EmployeeMaster manager, EmployeeMaster employeeMasterResult)
//			throws SettingException, ScoreException {
//		String sub = "Score Assessment Request";
//		String msg = EmailTemplates.getAssessScoreRequestEmailContent(manager.getEmployeeName(),
//				employeeMasterResult.getEmployeeName(), employeeMasterResult.getEmployeeId());
//
//		Setting findBySettingMailId = settingRepository.findBySettingName("service-catalog mail id");
//		if (findBySettingMailId == null) {
//			throw new SettingException("Please add the settings for Service catalog mail id");
//		}
//		Setting findBySettingMailPassword = settingRepository.findBySettingName("service-catalog mail id password");
//
//		if (findBySettingMailPassword == null) {
//			throw new SettingException("Please add the settings for Service catalog password id");
//		}
//
//		Setting adminMail = settingRepository.findBySettingName("Admin Mail id");
//		Setting adminMail2 = settingRepository.findBySettingName("Sub Admin Mail id");
//		if (adminMail == null && adminMail2 == null) {
//			throw new SettingException("Please add the settings for Admin & sub admin mail id");
//		}
//		try {
//			String mail[] = { adminMail.getData(), adminMail2.getData() };
//			sendText.send(findBySettingMailId.getData(), findBySettingMailPassword.getData(), manager.getEmailId(),
//					mail, sub, msg);
//		} catch (Exception e) {
//
//			logger.error("Failed To send The Mail !,due to port issue", e);
//			throw new ScoreException("Failed to send email due to error : ".concat(e.getMessage()));
//
//		}
//
//	}
//
	@Override
	public List<ScoreResponse> getScore(Integer employeeId, String quarter, Integer year) throws ScoreException {
		logger.info("Getting score from Score Service");
		if (employeeId == null) {
			logger.error(NO_RECORDS);
			throw new ScoreException(NO_RECORDS);
		}
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setAmbiguityIgnored(true);

		List<Score> scoreList = scoreRepository.findByEmployeeIdAndQuarterAndYearAndStatus(employeeId, quarter, year,
				SAVED);
		if (scoreList.isEmpty()) {
			logger.error(NO_RECORDS);
			throw new ScoreException(NO_RECORDS);
		}
		List<ScoreResponse> reponseList = new ArrayList<>();
		for (Score score : scoreList) {
			ScoreResponse scoreResponse = new ScoreResponse();
			mapper.map(score, scoreResponse);
			scoreResponse.setSkill(score.getActivity().getSkill().getSkillLevel());
			scoreResponse.setCategory(score.getActivity().getCategory());
			scoreResponse.setService(score.getActivity().getService());
			scoreResponse.setFacilitator(score.getActivity().getFacilitator());
			scoreResponse.setTechnologies(score.getActivity().getTechnologies());
			scoreResponse.setPrimarySkill(score.getEmployeeMaster().getPrimarySkill());
			scoreResponse.setSecondarySkill(score.getEmployeeMaster().getSecondarySkill());
			scoreResponse.setFeedbackName(score.getFeedbackMaster().getFeedbackName());
			reponseList.add(scoreResponse);
		}

		return reponseList;

	}
//
//	@Override
//	public List<ScoreResponse> getActivity(Integer employeeId, Integer projectId, Integer towerId)
//			throws ActivityException {
//		logger.info("Getting activity from Score Service");
//		Project project = projectRepository.findByProjectId(projectId);
//		if (project == null) {
//			logger.error("The project doesn't exists");
//			throw new ActivityException("The Project Doesn't exists");
//		}
//		List<Activity> activitys = activityRepository.getActivity(towerId);
//		if (activitys.isEmpty()) {
//			logger.error("No activities for this tower");
//			throw new ActivityException("No activities for this tower");
//		}
//		Tower findByTowerId = towerRepository.findByTowerId(towerId);
//		List<TowerScoreSkillDetails> towerScore = towerScoreRepository.findByTower(findByTowerId);
//		if (towerScore.isEmpty())
//			throw new ActivityException("No scores for this tower,Cant assess the activity");
//
//		String quater = null;
//
//		List<ScoreResponse> scoreResponsResult = new ArrayList<>();
//		LocalDate date = LocalDate.now();
//		int month = date.getMonthValue();
//		int year = date.getYear();
//		if (month >= 1 && month <= 3) {
//			quater = Q1;
//		} else if (month >= 4 && month <= 6) {
//			quater = Q2;
//		} else if (month >= 7 && month < 10) {
//			quater = Q3;
//		} else {
//			quater = Q4;
//		}
//		for (Activity activityResult : activitys) {
//
//			Score scoreDraft = scoreRepository.findByActivity(activityResult.getActivityId(), towerId, projectId,
//					employeeId, quater, year, SAVED);
//			Score score = scoreRepository.findByActivity(activityResult.getActivityId(), towerId, projectId, employeeId,
//					quater, year, SUBMITTED);
//			if (score != null) {
//				ScoreResponse scoreResponse = new ScoreResponse();
//
//				scoreResponse.setSkill(score.getActivity().getSkill().getSkillLevel());
//				scoreResponse.setActivityId(score.getActivity().getActivityId());
//				scoreResponse.setProjectSkill(score.getProjectSkill().getSkillLevel());
//				scoreResponse.setProjectSkillId(score.getProjectSkill().getSkillId());
//				scoreResponse.setActivityName(score.getActivity().getActivityName());
//				scoreResponse.setCategory(score.getActivity().getCategory());
//				scoreResponse.setFacilitator(score.getActivity().getFacilitator());
//				scoreResponse.setService(score.getActivity().getService());
//				scoreResponse.setTechnologies(score.getActivity().getTechnologies());
//				scoreResponse.setScore(score.getScore());
//				scoreResponse.setStatus(score.getStatus());
//				scoreResponse.setQuarter(score.getQuarter());
//				scoreResponse.setYear(score.getYear());
//				scoreResponse.setScoreId(score.getScoreId());
//				scoreResponsResult.add(scoreResponse);
//
//			} else if (scoreDraft != null) {
//				ScoreResponse scoreResponse = new ScoreResponse();
//				scoreResponse.setSkill(scoreDraft.getActivity().getSkill().getSkillLevel());
//				scoreResponse.setProjectSkill(scoreDraft.getProjectSkill().getSkillLevel());
//				scoreResponse.setProjectSkillId(scoreDraft.getProjectSkill().getSkillId());
//				scoreResponse.setActivityId(scoreDraft.getActivity().getActivityId());
//				scoreResponse.setActivityName(scoreDraft.getActivity().getActivityName());
//				scoreResponse.setCategory(scoreDraft.getActivity().getCategory());
//				scoreResponse.setFacilitator(scoreDraft.getActivity().getFacilitator());
//				scoreResponse.setService(scoreDraft.getActivity().getService());
//				scoreResponse.setTechnologies(scoreDraft.getActivity().getTechnologies());
//				scoreResponse.setScore(scoreDraft.getScore());
//				scoreResponse.setStatus(scoreDraft.getStatus());
//				scoreResponse.setQuarter(scoreDraft.getQuarter());
//				scoreResponse.setYear(scoreDraft.getYear());
//				scoreResponse.setScoreId(scoreDraft.getScoreId());
//				scoreResponsResult.add(scoreResponse);
//			}
//
//			else {
//				String currentQuarter = "";
//				if (LocalDate.now().getMonthValue() >= 1 && LocalDate.now().getMonthValue() <= 3) {
//					currentQuarter = "Q1";
//				} else if (LocalDate.now().getMonthValue() >= 4 && LocalDate.now().getMonthValue() <= 6) {
//					currentQuarter = "Q2";
//				} else if (LocalDate.now().getMonthValue() >= 7 && LocalDate.now().getMonthValue() <= 9) {
//					currentQuarter = "Q3";
//				} else if (LocalDate.now().getMonthValue() >= 10 && LocalDate.now().getMonthValue() <= 12) {
//					currentQuarter = Q4;
//				}
//				List<Score> findByActivity = scoreRepository.findByActivity(activityResult.getActivityId(), towerId,
//						projectId, employeeId);
//				if (!findByActivity.isEmpty()) {
//					ScoreResponse scoreResponse = new ScoreResponse();
//					scoreResponse.setSkill(findByActivity.get(0).getActivity().getSkill().getSkillLevel());
//					scoreResponse.setProjectSkill(findByActivity.get(0).getProjectSkill().getSkillLevel());
//					scoreResponse.setProjectSkillId(findByActivity.get(0).getProjectSkill().getSkillId());
//					scoreResponse.setActivityId(findByActivity.get(0).getActivity().getActivityId());
//					scoreResponse.setActivityName(findByActivity.get(0).getActivity().getActivityName());
//					scoreResponse.setCategory(findByActivity.get(0).getActivity().getCategory());
//					scoreResponse.setFacilitator(findByActivity.get(0).getActivity().getFacilitator());
//					scoreResponse.setService(findByActivity.get(0).getActivity().getService());
//					scoreResponse.setTechnologies(findByActivity.get(0).getActivity().getTechnologies());
//					scoreResponse.setScore(findByActivity.get(0).getScore());
//					scoreResponse.setStatus("Not Added");
//					scoreResponse.setQuarter(currentQuarter);
//					scoreResponse.setYear(LocalDate.now().getYear());
//					scoreResponse.setScoreId(findByActivity.get(0).getScoreId());
//					scoreResponsResult.add(scoreResponse);
//				} else {
//					ScoreResponse scoreResponse = new ScoreResponse();
//					scoreResponse.setSkill(activityResult.getSkill().getSkillLevel());
//					scoreResponse.setActivityId(activityResult.getActivityId());
//					scoreResponse.setActivityName(activityResult.getActivityName());
//					scoreResponse.setCategory(activityResult.getCategory());
//					scoreResponse.setFacilitator(activityResult.getFacilitator());
//					scoreResponse.setService(activityResult.getService());
//					scoreResponse.setTechnologies(activityResult.getTechnologies());
//					scoreResponse.setScore(null);
//					scoreResponse.setStatus("Not Added");
//					scoreResponse.setQuarter(quater);
//					scoreResponse.setYear(year);
//					scoreResponsResult.add(scoreResponse);
//				}
//			}
//		}
//		return scoreResponsResult;
//	}
//
//	@Override
//	public List<ScoreResponse> getScoreByTowerAndProject(String toweName, String projectName, Integer employeeId)
//			throws ScoreException {
//		logger.info("Getting score by Tower and Project from ");
//		ModelMapper modelMapper = new ModelMapper();
//		modelMapper.getConfiguration().setAmbiguityIgnored(true);
//		List<Score> score = scoreRepository.findByTowerNameAndProjectAndEmployeeId(toweName, projectName, employeeId);
//		if (score.isEmpty()) {
//			logger.error("The employee doesn't have any data to display");
//			throw new ScoreException("The employe doesn't have any data to display");
//		}
//		List<ScoreResponse> employeeResponseList = new ArrayList<>();
//		for (Score scoreList : score) {
//			ScoreResponse scoreDetailsResponse = new ScoreResponse();
//			modelMapper.map(scoreList, scoreDetailsResponse);
//			scoreDetailsResponse.setActivityName(scoreList.getActivity().getActivityName());
//			scoreDetailsResponse.setCategory(scoreList.getActivity().getCategory());
//			scoreDetailsResponse.setFacilitator(scoreList.getActivity().getFacilitator());
//			scoreDetailsResponse.setService(scoreList.getActivity().getService());
//			scoreDetailsResponse.setPrimarySkill(scoreList.getEmployeeMaster().getPrimarySkill());
//			scoreDetailsResponse.setSecondarySkill(scoreList.getEmployeeMaster().getSecondarySkill());
//			scoreDetailsResponse.setProjectSkillId(scoreList.getProjectSkill().getSkillId());
//			scoreDetailsResponse.setTechnologies(scoreList.getActivity().getTechnologies());
//			scoreDetailsResponse.setScore(scoreList.getScore());
//			employeeResponseList.add(scoreDetailsResponse);
//		}
//		return employeeResponseList;
//	}
//
//	@Override
//	public String publishScore(List<PublishScore> scoreId, String feedback) throws ScoreException {
//		logger.info("Publising Score from Score Service");
//
//		List<Score> saveList = new LinkedList<>();
//		Feedback feedbackNew = new Feedback();
//
//		Feedback activeFeedback = feedbackRepository.findByFeedbackNameAndFlag(feedback, true);
//		if (activeFeedback == null) {
//			feedbackNew.setFeedbackName(feedback);
//			feedbackNew.setFlag(true);
//			feedbackRepository.save(feedbackNew);
//		} else {
//			feedbackNew.setFeedbackId(activeFeedback.getFeedbackId());
//			feedbackNew.setFeedbackName(activeFeedback.getFeedbackName());
//			feedbackNew.setFlag(true);
//		}
//
//		scoreId.forEach(i -> {
//			Score findByScoreId = scoreRepository.findByScoreId(i.getScoreId());
//
//			if (findByScoreId.getStatus().equalsIgnoreCase(SUBMITTED)
//					|| findByScoreId.getStatus().equalsIgnoreCase(APPROVED)) {
//				findByScoreId.setScore(i.getScore());
//				findByScoreId.setStatus(PUBLISHED);
//
//				findByScoreId.setFeedbackMaster(feedbackNew);
//				saveList.add(findByScoreId);
//			}
//		});
//		List<Score> saveAll = scoreRepository.saveAll(saveList);
//
//		if (saveAll.isEmpty()) {
//			throw new ScoreException("Scores are already published");
//		}
//		return "Score Published";
//
//	}
//
//	@Override
//	public List<ScoreResponse> getScoreByTowerProjectQuarterYear(Integer employeeId, Integer towerId, Integer projectId,
//			String quarter, int year) throws ScoreException {
//		logger.info("Getting score by tower project Quarter Year from Score Service");
//
//		List<Score> scoreList = scoreRepository.findByPublisedScore(employeeId, towerId, projectId, quarter, year,
//				PUBLISHED);
//		if (scoreList.isEmpty()) {
//			logger.error(NO_RECORDS);
//			throw new ScoreException(NO_RECORDS);
//		}
//
//		return scoreList.stream()
//				.map(score -> new ScoreResponse(score.getScoreId(), score.getActivity().getActivityId(),
//						score.getActivity().getCategory(), score.getActivity().getService(),
//						score.getActivity().getTechnologies(), score.getActivity().getActivityName(),
//						score.getEmployeeMaster().getPrimarySkill(), score.getEmployeeMaster().getSecondarySkill(),
//						score.getActivity().getFacilitator(), score.getScore(), score.getStatus(), score.getQuarter(),
//						score.getYear(), score.getActivity().getSkill().getSkillLevel(),
//						score.getFeedbackMaster().getFeedbackName(), score.getProjectSkill().getSkillLevel(),
//						score.getProjectSkill().getSkillId(), score.getTimeLine()))
//				.collect(Collectors.toList());
//	}
//
//	@Override
//	public List<ManagerScoreResponse> getScoresByTowerProjectQuarterYear(Integer towerId, String projectName,
//			String quarter, int year) throws ScoreException {
//		logger.info("Getting score by tower project Quarter Year from Score Service");
//		if (projectName == null) {
//			logger.error("please enter the valid projectId");
//			throw new ScoreException("please enter the valid projectId");
//			
//		}
//		Project project = projectRepository.findByProjectNameAndTowerTowerId(projectName, towerId);
//		if (project == null) {
//			logger.error("Invalid project name");
//			throw new ScoreException("Invalid project name");
//		}
//		List<Score> scoreFilter = scoreRepository.findByProjectIdAndTowerIdAndYearAndQuarter(project.getProjectId(), towerId, year,
//				quarter);
//		return scoreFilter.stream()
//				.filter(score -> score.getStatus().equalsIgnoreCase(SUBMITTED)
//						|| score.getStatus().equalsIgnoreCase(APPROVED)
//						|| score.getStatus().equalsIgnoreCase(PUBLISHED)
//						||(score.getEmployeeMaster().getEmployeeStatus().equals("Verified/Active")||score.getEmployeeMaster().equals("Verified")))
//				.map(score -> {
//					TowerScoreSkillDetails findByTowerScore = skillScoreRepository.findByTowerSkill(towerId,
//							score.getProjectSkill().getSkillLevel());
//					long count = scoreRepository.findByEmployeeIdAndTowerAndProject(score.getEmployeeMaster().getEmployeeId(),towerId,project.getProjectId()).stream().map(i -> i.getScore()).count();
//					ManagerScoreResponse scoreResponse = new ManagerScoreResponse(
//							score.getEmployeeMaster().getEmployeeId(), score.getEmployeeMaster().getEmployeeName(),
//							score.getScoreId(), score.getTower().getTowerName(), score.getProject().getProjectName(),score.getProjectSkill().getSkillLevel(),
//							score.getActivity().getCategory(), score.getActivity().getService(),
//							score.getActivity().getTechnologies(), score.getActivity().getActivityName(),
//							score.getEmployeeMaster().getPrimarySkill(), score.getEmployeeMaster().getSecondarySkill(),
//							score.getActivity().getFacilitator(), score.getScore(), score.getStatus(),
//							score.getQuarter(), score.getYear(), score.getFeedbackMaster().getFeedbackName(),
//							score.getRoadMap(), score.getTimeLine(),
//							score.getCurrentEmployeeStatus().getCurrentEmployeeStatus(),
//							score.getActivity().getSkill().getSkillLevel(), findByTowerScore.getMinimumScore(),
//							findByTowerScore.getMaximumScore(), findByTowerScore.getNextLevelMin());
//
//					scoreResponse.setFeedbackName(score.getFeedbackMaster().getFeedbackName());
////					if (scores1 < findByTowerScore.getMinimumScore()) {
////						scoreResponse.setFeedbackName("Low competence");
////					}
////					if (scores1 > findByTowerScore.getMinimumScore() && scores1 < findByTowerScore.getMaximumScore()) {
////						scoreResponse.setFeedbackName("Adequate competence");
////					}
////					if (scores1 > findByTowerScore.getMaximumScore() && scores1 < findByTowerScore.getNextLevelMin()) {
////						scoreResponse.setFeedbackName("Over Qualified");
////					}
////					if (scores1 > findByTowerScore.getMaximumScore() && scores1 > findByTowerScore.getNextLevelMin()) {
////						scoreResponse.setFeedbackName("Qualified for next level");
////					}
//					return scoreResponse;
//				}).collect(Collectors.toList());
//
//	}
//
//	@Override
//	public List<String> getListYear() throws ScoreException {
//		return scoreRepository.getListYear();
//	}
}