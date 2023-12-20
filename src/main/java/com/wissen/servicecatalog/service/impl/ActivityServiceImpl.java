package com.wissen.servicecatalog.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wissen.servicecatalog.entity.Activity;
import com.wissen.servicecatalog.entity.Skill;
import com.wissen.servicecatalog.entity.Tower;
import com.wissen.servicecatalog.exception.ActivityException;
import com.wissen.servicecatalog.exception.TowerException;
import com.wissen.servicecatalog.pojo.ActivityGlobalChanges;
import com.wissen.servicecatalog.pojo.ActivityRequest;
import com.wissen.servicecatalog.pojo.ActivityResponse;
import com.wissen.servicecatalog.repository.ActivityRepository;
import com.wissen.servicecatalog.repository.SkillRepository;
import com.wissen.servicecatalog.repository.TowerRepository;
import com.wissen.servicecatalog.repository.TowerSkillScoreRepository;
import com.wissen.servicecatalog.service.ActivityService;

@Service
public class ActivityServiceImpl implements ActivityService {

	private static final String INVALID_TOWER = "Invalid Tower";

	private static final String CATEGORY_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK = "Category Cell should not be null/Cell type should be string/blank";
	private static final String SERVICE_CELL_SHOULD_NOT_BE_NULL_CELL_TYPE_SHOULD_BE_STRING_BLANK = "service Cell should not be null/cell type should be string/blank";
	private static final String NO_COLUMN_CALLED_CATEGORY = "No column called category";

	private static final String PLEASE_UPLOAD_THE_FILE = "Please upload the file";

	private static final String INVALID_ACTIVITY_ID = "Invalid Activity ID";

	private static final String INVALID_TOWER_ID = "Invalid Tower id";

	Logger logger = LoggerFactory.getLogger(ActivityServiceImpl.class);

	@Autowired
	ActivityRepository activityRepository;

	@Autowired
	TowerRepository towerRepository;

	@Autowired
	SkillRepository skillRepository;

	@Autowired
	TowerSkillScoreRepository towerScoreRepository;

	private List<Activity> listActivity;
	
	public List<ActivityResponse> addActivity(List<ActivityRequest> activityRequest) throws TowerException {
		logger.info("Adding the activity from Activity Service");
		List<Activity> save = new LinkedList<>();
		List<ActivityResponse> activityResponses = new LinkedList<>();

		if (activityRequest.isEmpty()) {
			logger.error("Please enter the activities");
			throw new TowerException("Please enter the activities");
		}
		for (ActivityRequest activitys : activityRequest) {

			Skill skill = skillRepository.findBySkillLevel(activitys.getSkillLevel());

			Tower tower = towerRepository.findByTowerId(activitys.getTowerId());
			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}

			if (skill == null) {
				ActivityResponse response = new ActivityResponse();
				Activity activity = new Activity();
				activity.setActivityName(activitys.getActivityName());
				activity.setCategory(activitys.getCategory());
				activity.setFacilitator(activitys.getFacilitator());
				activity.setService(activitys.getService());
				activity.setTechnologies(activitys.getTechnologies());
				Skill newLevelSkill = new Skill();
				newLevelSkill.setFlag(true);
				newLevelSkill.setSkillLevel(activitys.getSkillLevel());
				skillRepository.save(newLevelSkill);
				activity.setSkill(newLevelSkill);
				activity.setTower(tower);
				save.add(activity);
				Activity result = activityRepository.save(activity);

				response.setSkill(activitys.getSkillLevel());
				response.setActivityId(result.getActivityId());
				response.setActivityName(activity.getActivityName());
				response.setCategory(activity.getCategory());
				response.setFacilitator(activity.getFacilitator());
				response.setService(activity.getService());
				response.setTechnologies(activity.getTechnologies());
				activityResponses.add(response);

				response.setSkill(activitys.getSkillLevel());

			} else {
				Activity duplicates = activityRepository.findByDuplicates(activitys.getCategory(),
						activitys.getService(), activitys.getTechnologies(), activitys.getActivityName(),
						activitys.getSkillLevel(), activitys.getFacilitator(), tower.getTowerName(),
						skill.getSkillLevel());
				if (duplicates != null) {

					continue;
				}
				ActivityResponse response = new ActivityResponse();
				Activity activity = new Activity();
				activity.setActivityName(activitys.getActivityName());
				activity.setCategory(activitys.getCategory());
				activity.setFacilitator(activitys.getFacilitator());
				activity.setService(activitys.getService());
				activity.setTechnologies(activitys.getTechnologies());
				activity.setSkill(skill);
				activity.setTower(tower);
				Activity result = activityRepository.save(activity);
				response.setSkill(activitys.getSkillLevel());
				response.setActivityId(result.getActivityId());
				response.setActivityName(activity.getActivityName());
				response.setCategory(activity.getCategory());
				response.setFacilitator(activity.getFacilitator());
				response.setService(activity.getService());
				response.setTechnologies(activity.getTechnologies());
				activityResponses.add(response);
			}

		}
		if (activityResponses.isEmpty() && !activityRequest.isEmpty()) {
			logger.error("Cant add duplicate activity's to the respective tower");
			throw new TowerException("Cant add duplicate activity's to the respective tower");
		}
		return activityResponses;
	}

	@Override
	public List<ActivityResponse> getTowerId(Integer towerId) throws TowerException {
		logger.info("Getting Tower Id from Activity Service");
		ModelMapper modelMapper = new ModelMapper();
		if (towerId == null) {
			logger.error("Please enter the tower id");
			throw new TowerException("Please enter the tower id");

		}
		Tower tower = towerRepository.findByTowerId(towerId);
		if (tower == null) {
			logger.error(INVALID_TOWER_ID);
			throw new TowerException(INVALID_TOWER_ID);

		}
		List<Activity> activityList = activityRepository.getActivity(tower.getTowerId());
		if (activityList.isEmpty()) {

			logger.error("No activites in this tower");
			throw new TowerException("No activites in this tower");
		}
		List<ActivityResponse> activityResponseList = new LinkedList<>();
		for (Activity activity : activityList) {
			ActivityResponse activityResponse = modelMapper.map(activity, ActivityResponse.class);
			activityResponse.setSkill(activity.getSkill().getSkillLevel());
			activityResponseList.add(activityResponse);
		}
		return activityResponseList;

	}

	@Override
	public List<ActivityResponse> updateActivity(List<ActivityRequest> activityRequest)
			throws ActivityException, TowerException {
		logger.info("Updating Activity from Activity Service");

		List<ActivityResponse> activityResponse = new LinkedList<>();

		for (ActivityRequest list : activityRequest) {

			Activity activity = activityRepository.findByActivityId(list.getActivityId());

			if (activity == null) {
				logger.error(INVALID_ACTIVITY_ID);
				throw new ActivityException(INVALID_ACTIVITY_ID);
			}
			Skill skill = skillRepository.findBySkillLevel(list.getSkillLevel());

			Tower tower = towerRepository.findByTowerId(list.getTowerId());
			if (tower == null) {
				logger.error(INVALID_TOWER_ID);
				throw new TowerException(INVALID_TOWER_ID);
			}
			if (skill == null) {
				ActivityResponse response = new ActivityResponse();

				Skill newLevelSkill = new Skill();
				newLevelSkill.setFlag(true);
				newLevelSkill.setSkillLevel(list.getSkillLevel());
				skillRepository.save(newLevelSkill);
				activity.setSkill(newLevelSkill);
				activity.setTower(tower);
				activity.setActivityName(list.getActivityName());
				activity.setActivityId(list.getActivityId());
				activity.setCategory(list.getCategory());
				activity.setFacilitator(list.getFacilitator());
				activity.setService(list.getService());
				activity.setTechnologies(list.getTechnologies());
				activity.setTower(tower);
				activityRepository.save(activity);

				response.setActivityId(list.getActivityId());
				response.setActivityName(list.getActivityName());
				response.setCategory(list.getCategory());
				response.setFacilitator(list.getFacilitator());
				response.setService(list.getService());
				response.setSkill(newLevelSkill.getSkillLevel());
				response.setTechnologies(list.getTechnologies());

				activityResponse.add(response);
			} else {
				ActivityResponse response = new ActivityResponse();
				Activity activityR = new Activity();
				activityR.setSkill(skill);
				activityR.setTower(tower);
				activityR.setActivityName(list.getActivityName());
				activityR.setActivityId(list.getActivityId());
				activityR.setCategory(list.getCategory());
				activityR.setFacilitator(list.getFacilitator());
				activityR.setService(list.getService());
				activityR.setTechnologies(list.getTechnologies());
				activityR.setTower(tower);
				activityRepository.save(activityR);

				response.setActivityId(list.getActivityId());
				response.setActivityName(list.getActivityName());
				response.setCategory(list.getCategory());
				response.setFacilitator(list.getFacilitator());
				response.setService(list.getService());
				response.setSkill(list.getSkillLevel());
				response.setTechnologies(list.getTechnologies());

				activityResponse.add(response);
			}
		}

		return activityResponse;
	}

	@Override
	public String deleteActivity(Integer activityId) throws ActivityException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ActivityGlobalChanges updateGlobalChanges(ActivityGlobalChanges activityGlobalChanges)
			throws TowerException {
		// TODO Auto-generated method stub
		return null;
	}

}
